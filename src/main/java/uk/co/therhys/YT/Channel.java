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

    public List<Video> getVideos(Config conf){
        String vids = new Net().get(conf.instance + "/api/v1/channels/" + id);

        vids = vids.replaceAll("[^\\x20-\\x7e]", "");

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        ChannelResp root = gson.fromJson(vids, ChannelResp.class);

        name = root.author;

        for(Video vid : root.latestVideos){
            vid.channel = this;
        }

        return Arrays.asList(root.latestVideos);
    }
}
