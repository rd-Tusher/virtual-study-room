package com.virtualStudyRoom.components;

import javax.swing.*;
import java.awt.*;

import me.friwi.jcefmaven.CefAppBuilder;
import org.cef.CefApp;
import org.cef.CefClient;
import org.cef.CefSettings.LogSeverity;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.browser.CefMessageRouter;
import org.cef.callback.CefQueryCallback;
import org.cef.handler.CefDisplayHandlerAdapter;
import org.cef.handler.CefLoadHandlerAdapter;
import org.cef.handler.CefMessageRouterHandlerAdapter;
import org.cef.network.CefRequest.TransitionType;

public class JcefEngine {

    private static CefApp cefApp;
    private static CefBrowser browser;

    public static void initialize() {
        if (cefApp != null) return;

        try {
            CefAppBuilder builder = new CefAppBuilder();
            builder.addJcefArgs(
                "--enable-media-stream",
                "--use-fake-ui-for-media-stream",
                "--autoplay-policy=no-user-gesture-required"
            );
            builder.getCefSettings().windowless_rendering_enabled = true;
            builder.getCefSettings().root_cache_path=System.getProperty("user.home") + "/.virtualStudyRoom/cache_" + System.currentTimeMillis();
            cefApp = builder.build();
            System.out.println("JCEF initialized successfully");
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }

    public JcefEngine(String url) {
        if (cefApp == null) throw new IllegalStateException("Call initialize() first!");

        CefClient client = cefApp.createClient();

        CefMessageRouter router = CefMessageRouter.create();

        router.addHandler(new CefMessageRouterHandlerAdapter() {
            @Override
            public boolean onQuery(CefBrowser browser, CefFrame frame, long queryId,  String request, boolean persistent,  CefQueryCallback callback) {
                // System.out.println("Received from JS: " + request);
                SessionWebSocketClient.getInstance().sendWebRTCSignal(request);
                // callback.success("Java received: " + request);
                return true; 
            }
        }, true);

        client.addMessageRouter(router);
 
        client.addDisplayHandler(new CefDisplayHandlerAdapter() {
            @Override
            public boolean onConsoleMessage(CefBrowser browser, LogSeverity logSeverity,  String message, String source, int line) {
                // System.out.println("JS Console: " + message + " (Source: " + source + " Line: " + line + ")");
                // System.out.println( message);
                return false;
            }
        });

        client.addLoadHandler(new CefLoadHandlerAdapter() {
            @Override
            public void onLoadStart(CefBrowser browser, CefFrame frame, TransitionType transitionType) {
                System.out.println("Load started: " + frame.getURL());
            }

            @Override
            public void onLoadEnd(CefBrowser browser, CefFrame frame, int httpStatusCode) {

                if (frame.isMain()) {
                    System.out.println("Load ended: " + frame.getURL() + " status=" + httpStatusCode);
                    System.out.println("Browser url : " + browser.getURL());
                }
            }
        });


        browser = client.createBrowser(url, true, false);

        JFrame frame = new JFrame("Standalone JCEF Browser");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(browser.getUIComponent(), BorderLayout.CENTER);
        frame.setSize(0, 0);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
 

    public Component getUiComponent() {
        return browser.getUIComponent();
    }

    public CefBrowser getBrowser() {
        return browser;
    }

    public static void shutdown() {
        if (cefApp != null) {
            cefApp.dispose();
            cefApp = null;
        }
    }

    public void sendSignalToJS(String message) {
        if (browser == null) {
            return;
        }
        String escaped = message
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "");
        browser.executeJavaScript("handleSignal(\"" + escaped + "\");", browser.getURL(), 0);
    }

    public static void initRTC(String userID, String roomID){
        browser.executeJavaScript("initRTC(\"" + userID + "\", \"" + roomID + "\");", browser.getURL(),  0);
    }
} 