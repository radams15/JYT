package uk.co.therhys.YT;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import uk.co.therhys.JYT.OS;

import java.io.File;
import java.util.HashMap;

public class Video {
    public String title;
    public String videoId;
    public long published;
    public transient Channel channel;
    public Thumbnail[] videoThumbnails;

    public static class Stream {
        public String resolution;
        public String url;
    }

    public static class Thumbnail{
        public String quality;
        public String url;
        public int width;
        public int height;
    }

    private static class StreamResp {
        Stream[] formatStreams;
    }

    public Thumbnail getThumbnail(){
        for(int i=0 ; i<videoThumbnails.length ; i++){
            Thumbnail thumb = videoThumbnails[i];

            if(thumb.width <= 240){
                return thumb;
            }
        }

        return videoThumbnails[0];
    }

    public File getThumbFile() {
        Thumbnail thumbnail = getThumbnail();
        if (thumbnail != null) {
            String uid = videoId + "_thumb." + FileUtils.getExtension(thumbnail.url);

            File imgFile;
            switch (OS.getOS()) {
                case OS.OSX:
                    imgFile = new File("/Users/"+OS.getUsername()+"/Library/JReddit/cache/" + uid);
                    break;

                case OS.LINUX:
                    imgFile = new File("/home/"+OS.getUsername()+"/.cache/JReddit/" + uid);
                    break;

                default:
                    imgFile = new File("/JReddit/cache/" + uid);
                    break;
            }

            if (!imgFile.getParentFile().exists()) {
                boolean success = imgFile.getParentFile().mkdirs();

                if (!success) {
                    System.err.println("Could not create cache dir: " + imgFile.getParentFile().toString());
                } else {
                    System.out.println("Created: " + imgFile.getParentFile().toString());
                }
            }

            if (!imgFile.exists()) {
                boolean fail = Net.getInstance().download(thumbnail.url, new HashMap(), imgFile.toString());

                if (fail) {
                    System.err.println("Failed to download image: '" + thumbnail + "'");
                }
            }

            return imgFile;
        } else{
            return null;
        }
    }


    public Stream getStream(Config conf){
        String data = Net.filterUnicode(Net.getInstance().get(conf.instance + "/api/v1/videos/" + videoId));

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        StreamResp resp = (StreamResp) gson.fromJson(data, StreamResp.class);

        for(int i=0 ; i<resp.formatStreams.length ; i++){
            Stream stream = resp.formatStreams[i];

            int res = Integer.parseInt(stream.resolution.substring(0, stream.resolution.indexOf("p")));
            if(res == conf.quality){
                return stream;
            }
        }

        for(int i=0 ; i<resp.formatStreams.length ; i++){
            Stream stream = resp.formatStreams[i];

            int res = Integer.parseInt(stream.resolution.substring(0, stream.resolution.indexOf("p")));
            if(res >= conf.quality){
                return stream;
            }
        }

        return null;
    }
}
