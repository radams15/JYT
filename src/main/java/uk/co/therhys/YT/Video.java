package uk.co.therhys.YT;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import uk.co.therhys.JYT.OS;

import java.io.File;
import java.util.HashMap;

public class Video {
    public static class Stream {
        public int resolution;
        public String url;
    }

    public static class Thumbnail{
        public String quality;
        public String url;
        public int width;
        public int height;
    }

    public String title;
    public String videoId;
    public long published;
    public Channel channel;
    public JSONArray thumbs;
    public Thumbnail[] videoThumbnails;

    public Thumbnail[] getThumbnails(){
        Video.Thumbnail[] videoThumbnails = new Video.Thumbnail[thumbs.length()];

        try {
            for (int x = 0; x < thumbs.length(); x++) {
                JSONObject thumb = thumbs.getJSONObject(x);

                videoThumbnails[x] = new Video.Thumbnail();
                videoThumbnails[x].height = thumb.getInt("height");
                videoThumbnails[x].width = thumb.getInt("width");
                videoThumbnails[x].url = thumb.getString("url");
                videoThumbnails[x].quality = thumb.getString("quality");
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        return videoThumbnails;
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

    public Stream[] getStreams(Config conf){
        Result ret = Net.getInstance().get(conf.instance + "/api/v1/videos/" + videoId);

        try {
            JSONObject root = ret.toJson();
            JSONArray formatStreams = root.getJSONArray("formatStreams");

            Stream[] out = new Stream[formatStreams.length()];

            for (int i = 0; i < formatStreams.length(); i++) {
                JSONObject stream = formatStreams.getJSONObject(i);

                String resolutionStr = stream.getString("resolution");

                int res = Integer.parseInt(resolutionStr.substring(0, resolutionStr.indexOf("p")));

                out[i] = new Stream();
                out[i].resolution = res;
                out[i].url = stream.getString("url");
            }

            return out;
        }catch (JSONException e){
            e.printStackTrace();
        }

        return new Stream[0];
    }

    public Stream getStream(Config conf) {
        Stream[] streams = getStreams(conf);

        for (int i = 0; i < streams.length; i++) {
            Stream stream = streams[i];

            if (stream.resolution == conf.quality) {
                return stream;
            }
        }

        for (int i = 0; i < streams.length; i++) {
            Stream stream = streams[i];

            if (stream.resolution >= conf.quality) {
                return stream;
            }
        }

        return null;
    }
}
