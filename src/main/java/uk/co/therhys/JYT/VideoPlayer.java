package uk.co.therhys.JYT;

import uk.co.therhys.YT.Video;

import java.io.InputStream;
import java.io.OutputStream;

public class VideoPlayer extends Thread {
    private Video.Stream stream;

    private String getFfplayBin(){
        if(OS.getOS() == OS.OSX) {
            System.setProperty( "jdk.lang.Process.launchMechanism", "FORK" );

            if (OS.versionAbove("10.6")) { // Use ffmpeg from macports
                return "/opt/local/bin/ffplay";
            }else{ // Under snow leopard - use ppc binary
                return "/Applications/JYT.app/Contents/Resources/bin/ffplay";
            }
        }else {
            return "/usr/local/bin/ffplay"; // Use linux ffmpeg binary
        }
    }

    public VideoPlayer(Video.Stream stream){
        super();

        this.stream = stream;
    }

    public void run() {
        try {
            String cmd = getFfplayBin() + " \""+stream.url+"\"";

            System.out.println(cmd);

            Runtime rt = Runtime.getRuntime();
            Process pr = rt.exec(new String[]{"/bin/sh", "-c", cmd});

            InputStream out = pr.getErrorStream();

            byte[] buf = new byte[1024]; // When buffer fills the program clears so keep clearing the buffer.
            while(out.read(buf) != -1){
                //System.out.println(new String(buf));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
