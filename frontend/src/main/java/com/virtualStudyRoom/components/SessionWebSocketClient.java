package com.virtualStudyRoom.components;

import com.virtualStudyRoom.components.BackendToFrontend.User;
import com.virtualStudyRoom.components.Whiteboard.CanvasResizeDTO;
import com.virtualStudyRoom.utils.ResponseModel.FileUploadNotification;

import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;

import java.awt.Color;
import java.awt.Point;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.SwingUtilities;

public class SessionWebSocketClient {

    private static SessionWebSocketClient instance;
    private static final Map<String, List<User>> sessionUserCache =
            new ConcurrentHashMap<>();

    private String sessionID;
    private boolean connected = false;
    protected String userID;
    protected String name;
    protected StompSession stompSession;

    private SessionPanel sPanel;

    private Whiteboard whiteboard;


    public SessionWebSocketClient(String websocketUrl, String sessionID,String name, String userID) {
        this.sessionID = sessionID;
        this.userID = userID;
        this.name = name;

        if (websocketUrl == null) {
            System.out.println("The required websocket is null.");
            return;
        }

        WebSocketStompClient stompClient = new WebSocketStompClient(new StandardWebSocketClient());

        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        System.out.println("Connecting to WebSocket: " + websocketUrl);

        stompClient.connectAsync(websocketUrl, new StompSessionHandlerAdapter() {

            @Override
            public void afterConnected(@NonNull StompSession session,@NonNull StompHeaders headers) {

                connected = true;
                SessionWebSocketClient.this.stompSession = session;

                System.out.println("STOMP Connected. SessionId = " + session.getSessionId());

                session.subscribe("/topic/session/" + sessionID, new SessionEventHandler());
                session.subscribe("/topic/session/" + sessionID + "/whiteboard", new WhiteboardEventHandler());
                session.subscribe("/topic/session/" + sessionID + "/whiteboard/resize", new WhiteboardHeightHandler());
                session.subscribe("/topic/session/" + sessionID + "/auto-scroll", new ScrollHandler());
                session.subscribe("/topic/session/" + sessionID + "/webrtc", new WebRTCEventHandler());
                session.subscribe("/topic/session/" + sessionID + "/notification", new FileUploadHandler());
                session.subscribe("/topic/session/" + sessionID + "/info-to-late-user", new LateUserInfoHandler());

                Set<User> users = FrontendToBackend.joinSession(sessionID, name, userID);
                for (User user : users) {
                    User newUser = new User(user.getUserID(), user.getName());
                    sessionUserCache
                        .computeIfAbsent(sessionID, k -> new CopyOnWriteArrayList<>())
                        .add(newUser);
                }
            }

            @Override
            public void handleTransportError(@NonNull StompSession session,@NonNull Throwable exception) {
                System.err.println("Transport error");
                exception.printStackTrace();
            }
        })
        .exceptionally(ex -> {
            System.err.println("STOMP connection failed");
            ex.printStackTrace();
            return null;
        });

    }

    public boolean isConnected() {
        return connected;
    }

    private class SessionEventHandler implements StompFrameHandler {

        @Override
        @NonNull
        public Type getPayloadType(@NonNull StompHeaders headers) {
            return SessionEvent.class;
        }

        @Override
        public void handleFrame(@NonNull StompHeaders headers,@Nullable Object payload) {
            SessionEvent event = (SessionEvent) payload;
            if (event == null) {
                return;
            }

            if (userID != null & userID.equals(event.userID)) {
                return;
            }

            if (event != null) {
                switch (event.event) {
                    case "USER_JOINED" -> {
                        handleUserJoined(event);
                        System.out.println("User joined: " + event.name);
                    }
                    case "USER_LEFT" -> {
                        handleUserLeft(event);
                        System.out.println("User left: " + event.name);
                    }
                    default -> System.out.println("Unknown event: " + event.event);
                }
            }
        }
    }

    private void handleUserJoined(SessionEvent event) {
        if (event.userID == null) return;

        User user = new User(event.userID, event.name);

        sessionUserCache.computeIfAbsent(sessionID, k -> new CopyOnWriteArrayList<>())
                .add(user);
    }

    private void handleUserLeft(SessionEvent event) {
        if (event.userID == null) return;

        List<User> users = sessionUserCache.get(sessionID);
        if (users != null) {
            users.removeIf(u -> u.getUserID().equals(event.userID));
        }
    }

    public static List<User> getUsers(String sessionID) {
        return sessionUserCache.getOrDefault(sessionID, List.of());
    }


    public void sendStroke(StrokeDTO stroke) {
        if (!connected || stompSession == null || stroke == null) return;

        stroke.senderID = userID;
        stompSession.send("/app/session/" + sessionID + "/whiteboard", stroke);
    }
    
    // stompSession.send("/topic/session/" + sessionID + "/whiteboard", stroke);
    private class WhiteboardEventHandler implements StompFrameHandler {

        @Override
        @NonNull
        public Type getPayloadType(@NonNull StompHeaders headers) {
            return StrokeDTO.class;
        }

        @Override
        public void handleFrame( @NonNull StompHeaders headers, @Nullable Object payload) {

            if (!(payload instanceof StrokeDTO strokeDTO)) {
                return;
            }

            if (userID != null && userID.equals(strokeDTO.senderID)) {
                return;
            }

            Whiteboard.Stroke stroke = new Whiteboard.Stroke( new Color(strokeDTO.colorRGB), strokeDTO.strokeSize );

            for (StrokeDTO.PointDTO p : strokeDTO.points) {
                stroke.points.add(new Point(p.x, p.y));
            }

            if (whiteboard != null) {
                SwingUtilities.invokeLater(() ->
                        whiteboard.addRemoteStroke(stroke)
                );
            }
        }
    }

    public void sendCanvasResize(int newHeight) {
        if (!connected || stompSession == null) return;

        CanvasResizeDTO dto = new CanvasResizeDTO();
        dto.canvasHeight = newHeight;
        dto.senderID = userID;
 
        // stompSession.send("/topic/session/" + sessionID + "/whiteboard/resize",dto);
        stompSession.send("/app/session/" + sessionID + "/whiteboard/resize",dto);
    }

    private class WhiteboardHeightHandler implements StompFrameHandler {

        @Override
        @NonNull
        public Type getPayloadType(@NonNull StompHeaders headers) {
            return CanvasResizeDTO.class;
        }

        @Override
        public void handleFrame(@NonNull StompHeaders headers, @Nullable Object payload) {

            if(!(payload instanceof CanvasResizeDTO resizeDTO)){
                return;
            }

            if (userID != null && userID.equals(resizeDTO.senderID)) {
                return;
            }

            System.out.println("Received canvas resize from user: " + resizeDTO.senderID + " new height: " + resizeDTO.canvasHeight);

            SwingUtilities.invokeLater(() -> {
                whiteboard.growCanvas(resizeDTO.canvasHeight);
            });
        }
    }


    public void sendScroll(ScrollMessage scroll){
        if (!connected || stompSession == null) return;

        scroll.senderID = userID;
        // stompSession.send("/topic/session/" + sessionID + "/auto-scroll",scroll);
        stompSession.send("/app/session/" + sessionID + "/remote-scroll",scroll);
    }

    private class ScrollHandler implements StompFrameHandler {

        @Override
        @NonNull
        public Type getPayloadType(@NonNull StompHeaders headers) {
            return ScrollMessage.class;
        }

        @Override
        public void handleFrame(@NonNull StompHeaders headers, @Nullable Object payload) {

            if (!(payload instanceof ScrollMessage scrollMessage)) {
                return;
            }

            if (userID != null && userID.equals(scrollMessage.senderID)) {
                return;
            }

            System.out.println("Received scroll from user: " + scrollMessage.senderID+ " percent: "+ scrollMessage.verticalPercent);

            if (scrollMessage != null) {
                SwingUtilities.invokeLater(() -> {
                    sPanel.applyRemoteScroll(scrollMessage.verticalPercent);
                });
            }
        }
    }

    public void sendWebRTCSignal(String json) {
        if (!connected || stompSession == null || json == null) return;
        stompSession.send("/topic/session/" + sessionID + "/webrtc", json);
    }

    private class WebRTCEventHandler   implements StompFrameHandler {

        @Override
        @NonNull
        public Type getPayloadType(@NonNull StompHeaders headers) {
            return String.class;
        }

        @Override
        public void handleFrame(@NonNull StompHeaders headers,@Nullable Object payload) {
            String json = (String) payload;
            SessionPanel.getJcefEngine().sendSignalToJS(json);
        }
    }

    private class FileUploadHandler implements StompFrameHandler {
        @Override
        @NonNull
        public Type getPayloadType (@NonNull StompHeaders headers) {
            return FileUploadNotification.class;
        }

 
        @Override
        public void handleFrame(@NonNull StompHeaders headers, @Nullable Object payload) {

            if (!(payload instanceof FileUploadNotification fileUploadNotification)) {
                return;
            }

            if (userID != null && userID.equals(fileUploadNotification.userID)) {
                return;
            }

            if (fileUploadNotification != null) {
                SwingUtilities.invokeLater(() -> {
                    WaitingRoom.getSesionPanel().showFileNotification(fileUploadNotification);
                });
            }
        }
    }

    private class LateUserInfoHandler implements StompFrameHandler {

        @Override
        @NonNull
        public Type getPayloadType(@NonNull StompHeaders headers) {
            System.out.println("Entered getPayloadType for late user info");
            return LateUserInfo.class;  // now deserialization will work
        }

        @Override
        public void handleFrame(@NonNull StompHeaders headers, @Nullable Object payload) {
            System.out.println("Entered handleFrame for late user info");

            if (!(payload instanceof LateUserInfo lateUserInfo)) {
                System.out.println("Payload is not of type LateUserInfo");
                return;
            }

            System.out.println("LateUserInfo received for userID: " + lateUserInfo.getUserID());
            System.out.println("user id : " + userID);
            if (userID != null && userID.equals(lateUserInfo.getUserID())) {

                SwingUtilities.invokeLater(() -> {
                    System.out.println("new height : " + lateUserInfo.getCanvasHeight());
                    whiteboard.growCanvas(lateUserInfo.getCanvasHeight());
                    sPanel.applyRemoteScroll( lateUserInfo.getVerticalPercentage());
                });
            }

        }
    }


    public String getSessionID(){
        return sessionID;
    }

    public static void setInstance(SessionWebSocketClient client) {
        instance = client;
    }
    
    public static SessionWebSocketClient getInstance() {
        return instance;
    }

    public void setWhiteboard(Whiteboard whiteboard){
        this.whiteboard = whiteboard;
    }

    public void setSessionPanel(SessionPanel sPanel){
        this.sPanel = sPanel;
    }

    private static class SessionEvent {
        public String event;
        public String userID;
        public String name;
    }
}