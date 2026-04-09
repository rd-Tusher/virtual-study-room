
let pc = null;
let localStream = null;
let audioContext;
let userId = null;
let roomId = null;

async function initRTC(uID, rID) {
    try {
        if (!userId) userId = uID;
        if (!roomId) roomId = rID;

        await createPeerConnection();
        await createOffer();

        console.log("RTC initialized | room:", roomId, "user:", userId);
    } catch (err) {
        console.error("Init error:", err);
    }
}

// 🎤 MICROPHONE + FILTERS
async function startMicrophone() {
    try {
        const rawStream = await navigator.mediaDevices.getUserMedia({
            audio: {
                echoCancellation: true,
                noiseSuppression: true,
                autoGainControl: true,
                channelCount: 1
            }
        });

        console.log("Microphone captured");

        audioContext = new AudioContext();

        if (audioContext.state === "suspended") {
            await audioContext.resume();
        }

        const source = audioContext.createMediaStreamSource(rawStream);

        // 🎯 HIGH PASS FILTER
        const highpass = audioContext.createBiquadFilter();
        highpass.type = "highpass";
        highpass.frequency.value = 100;

        // 🎯 COMPRESSOR
        const compressor = audioContext.createDynamicsCompressor();
        compressor.threshold.value = -40;
        compressor.knee.value = 30;
        compressor.ratio.value = 3;
        compressor.attack.value = 0.05;
        compressor.release.value = 0.4;

        // 🎯 GAIN (SAFE LEVEL)
        const gainNode = audioContext.createGain();
        gainNode.gain.value = 0.6;

        // 🎯 DESTINATION (for WebRTC)
        const destination = audioContext.createMediaStreamDestination();

        // 🔗 PIPELINE
        source.connect(highpass);
        highpass.connect(compressor);
        compressor.connect(gainNode);
        gainNode.connect(destination);

        // 🔇 NOISE GATE
        applyNoiseGate(audioContext, compressor, gainNode);

        // ✅ FINAL STREAM
        localStream = destination.stream;

        console.log("Filtered audio ready");

    } catch (err) {
        console.error("Microphone error:", err);
    }
}

// 🔇 NOISE GATE
function applyNoiseGate(audioContext, source, gainNode) {
    const analyser = audioContext.createAnalyser();
    source.connect(analyser);

    const data = new Uint8Array(analyser.fftSize);

    function loop() {
        analyser.getByteTimeDomainData(data);

        let sum = 0;
        for (let i = 0; i < data.length; i++) {
            sum += Math.abs(data[i] - 128);
        }

        let volume = sum / data.length;

        if (volume < 4) {
            gainNode.gain.value = 0;
        } else {
            gainNode.gain.value = 0.8;
        }

        requestAnimationFrame(loop);
    }

    loop();
}


function applyNoiseGate(audioContext, nodeToAnalyze, gainNode) {
    const analyser = audioContext.createAnalyser();
    nodeToAnalyze.connect(analyser);

    analyser.fftSize = 512;
    const data = new Uint8Array(analyser.fftSize);

    function loop() {
        analyser.getByteTimeDomainData(data);

        let sum = 0;
        for (let i = 0; i < data.length; i++) {
            let v = (data[i] - 128) / 128;
            sum += v * v;
        }

        let rms = Math.sqrt(sum / data.length);

        // 🔥 smoother gating
        if (rms < 0.02) {
            gainNode.gain.value = 0;
        } else {
            gainNode.gain.value = 0.7;
        }

        requestAnimationFrame(loop);
    }

    loop();
}

// 🔗 PEER CONNECTION
async function createPeerConnection() {
    pc = new RTCPeerConnection({
        iceServers: [
            { urls: "stun:stun.l.google.com:19302" }
        ]
    });

    pc.onconnectionstatechange = () => {
        console.log("Connection state:", pc.connectionState);
    };

    pc.oniceconnectionstatechange = () => {
        console.log("ICE state:", pc.iceConnectionState);
    };

    // 📡 ICE
    pc.onicecandidate = event => {
        if (event.candidate) {
            sendToJava({
                type: "candidate",
                sender: userId,
                roomId: roomId,
                data: event.candidate
            });
        }
    };

    // 🔊 REMOTE AUDIO
    pc.ontrack = event => {
        console.log("Remote audio track received");

        const audio = document.createElement("audio");
        audio.srcObject = event.streams[0];
        audio.autoplay = true;
        audio.controls = true;
        audio.muted = false;
        audio.volume = 1.0;

        document.body.appendChild(audio);

        // 🔥 JCEF autoplay fix
        setTimeout(() => {
            audio.play().then(() => {
                console.log("Audio playing");
            }).catch(err => {
                console.error("Playback failed:", err);
            });
        }, 500);
    };

    // 🎤 START MIC
    await startMicrophone();

    // ➕ ADD TRACKS
    localStream.getTracks().forEach(track => {
        pc.addTrack(track, localStream);
    });
}

// 📤 OFFER
async function createOffer() {
    if (!pc) await createPeerConnection();

    const offer = await pc.createOffer();
    await pc.setLocalDescription(offer);

    sendToJava({
        type: "offer",
        sender: userId,
        roomId: roomId,
        data: offer
    });

    console.log("Offer sent");
}

// 📥 SIGNAL HANDLER
async function handleSignal(message) {
    if (typeof message === "string") {
        message = JSON.parse(message);
    }

    if (message.sender === userId) return;

    switch (message.type) {

        case "offer":
            console.log("Received OFFER");

            if (!pc) await createPeerConnection();

            await pc.setRemoteDescription(new RTCSessionDescription(message.data));

            const answer = await pc.createAnswer();
            await pc.setLocalDescription(answer);

            sendToJava({
                type: "answer",
                sender: userId,
                roomId: roomId,
                data: answer
            });

            console.log("Answer sent");
            break;

        case "answer":
            console.log("Received ANSWER");
            await pc.setRemoteDescription(new RTCSessionDescription(message.data));
            break;

        case "candidate":
            console.log("Received ICE candidate");
            await pc.addIceCandidate(new RTCIceCandidate(message.data));
            break;

        default:
            console.log("Unknown message:", message.type);
    }
}

// 🔁 JAVA BRIDGE
function sendToJava(message) {
    cefQuery({
        request: JSON.stringify(message),
        onSuccess: res => console.log("Sent:", res),
        onFailure: (code, msg) => console.error("Send error:", msg)
    });
}

// 🔊 LOCAL TEST
function playLocalAudio() {
    if (!localStream) return;

    const audio = document.createElement("audio");
    audio.srcObject = localStream;
    audio.autoplay = true;
    audio.controls = true;
    audio.muted = false;

    document.body.appendChild(audio);

    audio.play().catch(e => console.log("Autoplay blocked", e));
}

console.log("WebRTC loaded successfully");