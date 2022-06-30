package uk.co.therhys.YT;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Channel {
    public String id;
    public String name;

    public Channel(){}

    public String getName(){
        if(name != null){
            return name;
        }

        return "Unknown";
    }



    public void getVideos(Config conf, VidListener listener){
        getVideos(conf, listener, true);
    }

    public void getVideos(Config conf, VidListener listener, boolean notifyOnDone){
        Result res = Net.getInstance().get(conf.instance + "/api/v1/channels/" + id);

        try {
            JSONObject root = res.toJson();

            name = root.getString("author");

            JSONArray latestVideos = root.getJSONArray("latestVideos");

            for (int i = 0; i < latestVideos.length(); i++) {
                JSONObject vidObj = latestVideos.getJSONObject(i);

                Video vid = new Video();
                vid.channel = this;

                vid.title = vidObj.getString("title");
                vid.videoId = vidObj.getString("videoId");
                vid.published = vidObj.getLong("published");

                vid.thumbs = vidObj.getJSONArray("videoThumbnails");

                listener.getVideo(vid);
            }

            if(notifyOnDone) {
                listener.vidFetchCompleted();
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
