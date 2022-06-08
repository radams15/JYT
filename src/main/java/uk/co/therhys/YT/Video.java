package uk.co.therhys.YT;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Video {
    public String title;
    public String videoId;
    public long published;
    public transient Channel channel;

    public static class Stream {
        public String resolution;
        public String url;
    }

    private static class StreamResp {
        Stream[] formatStreams;
    }

    public Stream getStream(Config conf){
        String data = new Net().get(conf.instance + "/api/v1/videos/" + videoId);

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        StreamResp resp = gson.fromJson(data, StreamResp.class);

        for(Stream stream : resp.formatStreams){
            int res = Integer.parseInt(stream.resolution.substring(0, stream.resolution.indexOf("p")));
            if(res == conf.quality){
                return stream;
            }
        }

        for(Stream stream : resp.formatStreams){
            int res = Integer.parseInt(stream.resolution.substring(0, stream.resolution.indexOf("p")));
            if(res >= conf.quality){
                return stream;
            }
        }

        return null;
    }
}
