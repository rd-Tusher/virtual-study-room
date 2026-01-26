package com.virtualStudyRoom.components;

import com.virtualStudyRoom.components.BackendToFrontend.User;
import com.virtualStudyRoom.components.Whiteboard.CanvasResizeDTO;

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

    private Whiteboard whiteboard;


    public SessionWebSocketClient(String websocketUrl, String sessionID,String name, String userID) {
        this.sessionID = sessionID;
        this.userID = userID;
        this.name = name;

        WebSocketStompClient stompClient =
                new WebSocketStompClient(new StandardWebSocketClient());

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

                System.out.println("Subscribed to /topic/session/" + sessionID);

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
        stompSession.send("/topic/session/" + sessionID + "/whiteboard", stroke);
    }

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

        stompSession.send("/topic/session/" + sessionID + "/whiteboard/resize",dto);
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

    private static class SessionEvent {
        public String event;
        public String userID;
        public String name;
    }

    
}