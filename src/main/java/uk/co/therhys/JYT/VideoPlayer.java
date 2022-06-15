package uk.co.therhys.JYT;

import uk.co.therhys.YT.Video;

public class VideoPlayer extends Thread {
    private Video.Stream stream;

    public VideoPlayer(Video.Stream stream){
        super();

        this.stream = stream;
    }

    public void run() {
        try {
            Runtime rt = Runtime.getRuntime();
            Process pr = rt.exec(new String[]{"bash", "-c", "/Applications/JYT.app/Contents/Resources/bin/ffplay \""+stream.url+"\" &"});
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
