package uk.co.therhys.YT;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URLEncoder;

public class Config {
    public int quality;
    public String instance;
    public Channel[] subscriptions;
    public String saveFile;

    public static Config fromFile(String configFile){
        String config = FileUtils.readFile(new File(configFile));

        try {
            JSONObject confData = new JSONObject(config);

            Config conf = new Config();
            conf.saveFile = configFile;
            conf.quality = confData.getInt("quality");
            conf.instance = confData.getString("instance");
            JSONArray subs = confData.getJSONArray("subscriptions");

            conf.subscriptions = new Channel[subs.length()];
            for(int i=0 ; i<subs.length() ; i++){
                JSONObject sub = subs.getJSONObject(i);

                conf.subscriptions[i] = new Channel();
                conf.subscriptions[i].id = sub.getString("id");
                conf.subscriptions[i].name = sub.getString("name");
            }

            return conf;
        }catch (JSONException e){
            e.printStackTrace();
        }

        return null;
    }

    public Config(){}

    public void getVideos(boolean useThreading, VidListener listener) {
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

        String url = instance + "/api/v1/search?q=" + query + "&type=video&page=" + page;

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

                vid.thumbs = vidObj.getJSONArray("videoThumbnails");

                listener.getVideo(vid);

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

        try{
            JSONObject out = new JSONObject();

            out.put("quality", quality);
            out.put("instance", instance);

            JSONArray subs = new JSONArray();

            for(int i=0 ; i<subscriptions.length ; i++){
                JSONObject sub = new JSONObject();
                sub.put("id", subscriptions[i].id);
                sub.put("name", subscriptions[i].name);

                subs.put(sub);
            }

            out.put("subscriptions", subs);

            String outStr = out.toString(1);

            FileUtils.writeFile(new File(saveFile), outStr);

        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
