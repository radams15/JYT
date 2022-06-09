package uk.co.therhys.YT;

import java.util.List;

public class VidGetThread extends Thread {
    private Channel channel;
    private Config conf;

    public List<Video> vids;

    public void run(){
        vids = channel.getVideos(conf);
    }

    public VidGetThread(Config conf, Channel channel){
        this.conf = conf;
        this.channel = channel;
    }
}
