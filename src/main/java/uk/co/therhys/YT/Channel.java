package uk.co.therhys.YT;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.List;

public class Channel {
    public String id;
    public String name;
    public int subCount;
    public int videoCount;

    private static class ChannelResp {
        Video[] latestVideos;
        String author;

        public ChannelResp(){}
    }

    public Channel(){}

    public Channel(String id){
        this.id = id;
    }

    public String getName(){
        if(name != null){
            return name;
        }

        return "Unknown";
    }

    public void getVideos(Config conf, VidListener listener){
        String vids = Net.filterUnicode(Net.getInstance().get(conf.instance + "/api/v1/channels/" + id));

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        ChannelResp root = (ChannelResp) gson.fromJson(vids, ChannelResp.class);

        name = root.author;

        for(int i=0 ; i<root.latestVideos.length ; i++){
            root.latestVideos[i].channel = this;
        }

        for(int i=0 ; i<root.latestVideos.length ; i++){
            listener.getVideo(root.latestVideos[i]);
        }
        listener.vidFetchCompleted();
    }
}
