package uk.co.therhys.JYT;

import uk.co.therhys.YT.Config;

import javax.swing.*;

public class JYT {
    private static boolean tryLookAndFeel(String className){
        try {
            UIManager.setLookAndFeel(className);

            return false;
        }catch(Exception e){
            e.printStackTrace();

            return true;
        }
    }

    public static void main(String[] args){
        String os = System.getProperty("os.name");

        if(os.equals("Linux")){
            tryLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
            System.out.println("Using gtk theme");
        }else if(! tryLookAndFeel(UIManager.getSystemLookAndFeelClassName())){
            System.out.println("Using system theme");
        }else if(! tryLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName())){
            System.out.println("Using cross platform theme");
        }else{
            System.out.println("Cannot get theme!");
        }

        try{
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            System.setProperty("com.apple.mrj.application.apple.menu.about.name", "JMD");
        } catch(Exception e){
            e.printStackTrace();
        }

        String savePath = "yt_saves.json.small";

        if(os.equals("Linux")){
            savePath = "/home/rhys/.config/" + savePath;
        }else if(os.equals("Mac OS X")){
            savePath = "/Users/rhys/Library/" + savePath;
        }else{
            savePath = ".\\" + savePath;
        }

        Config conf = Config.fromFile(savePath);

        MainFrame frame = new MainFrame(conf);
        frame.setVisible(true);

        conf.save();
    }
}
