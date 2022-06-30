package uk.co.therhys.YT;

public class VidGetThread extends Thread {
    private Channel channel;
    private Config conf;
    private VidListener listener;

    public void run(){
        channel.getVideos(conf, listener, false);
    }

    public VidGetThread(Config conf, Channel channel, VidListener listener){
        this.conf = conf;
        this.channel = channel;
        this.listener = listener;
    }
}
