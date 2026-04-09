// package com.virtualStudyRoom.components;

// import java.awt.BorderLayout;

// import javax.swing.JPanel;

// public class BrowserWindow extends JPanel{
//     private static JcefEngine jcefEngine;

//     public BrowserWindow(String url){
//         setLayout(new BorderLayout());
//         if (jcefEngine == null) {
//             jcefEngine = new JcefEngine(url);
//         }
//         add(jcefEngine.getUiComponent(), BorderLayout.CENTER);
//     }

//     public static JcefEngine getJcefEngine(){
//         return jcefEngine;
//     }

 
// }