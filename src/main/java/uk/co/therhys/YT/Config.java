package uk.co.therhys.YT;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Config {
    public int quality;
    public String instance;
    public Channel[] subscriptions;
    public transient String saveFile;

    public static Config fromFile(String configFile){
        String config = FileUtils.readFile(new File(configFile));

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        Config conf = (Config) gson.fromJson(config, Config.class);
        conf.saveFile = configFile;

        return conf;
    }

    public Config(){}

    public void getVideos(boolean useThreading, VidListener listener) {
        ArrayList out = new ArrayList();


        if(useThreading){
            VidGetThread[] threads = new VidGetThread[subscriptions.length];

            for (int i=0 ; i<threads.length ; i++) {
                threads[i] = new VidGetThread(this, (Channel) subscriptions[i], listener);
                threads[i].start();
            }

            for (int i=0 ; i<threads.length ; i++) {
                try {
                    threads[i].join();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }else {
            for (int i=0 ; i<subscriptions.length ; i++) {
                ((Channel) subscriptions[i]).getVideos(this, listener);
            }
        }
    }

    public void search(String query, int page, VidListener listener){
        try {
            query = URLEncoder.encode(query, "UTF-8");
        }catch (Exception e){
            e.printStackTrace();
        }

        String url = instance + "/api/v1/search?q=" + query + "&type=video&page=" + String.valueOf(page);

        Result res = Net.getInstance().get(url);

        if(res.hadError){
            res.error.printStackTrace();
            return;
        }

        JSONArray array = res.toJsonArray();

        for(int i=0 ; i<array.length() ; i++){
            try {
                JSONObject vidObj = array.getJSONObject(i);

                Channel channel = new Channel();
                channel.id = vidObj.getString("authorId");
                channel.name = vidObj.getString("author");

                Video vid = new Video();
                vid.channel = channel;
                vid.title = vidObj.getString("title");
                vid.videoId = vidObj.getString("videoId");
                vid.published = vidObj.getInt("published");

                listener.getVideo(vid);

                //vid.videoThumbnails = vidObj.getJSONArray("videoThumbnails"); // TODO fix this

                listener.vidFetchCompleted();
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    public void getVideos(VidListener listener){
        getVideos(true, listener);
    }

    public void save(){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        String out = gson.toJson(this);

        FileUtils.writeFile(new File(saveFile), out);
    }
}
