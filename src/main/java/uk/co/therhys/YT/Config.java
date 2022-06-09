package uk.co.therhys.YT;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Config {
    int quality;
    String instance;
    public String[] subs;
    public transient String saveFile;

    public ArrayList<Channel> subscriptions;

    public static Config fromFile(String configFile){
        String config = FileUtils.readFile(new File(configFile));

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        Config conf = gson.fromJson(config, Config.class);
        conf.refreshSubs();
        conf.saveFile = configFile;

        return conf;
    }

    public Config(){}

    public void refreshSubs(){
        if(subscriptions == null) {
            subscriptions = new ArrayList<Channel>();

            for (String sub : subs) {
                subscriptions.add(new Channel(sub));
            }
        }
    }

    public List<Video> getVideos(boolean useThreading) {
        ArrayList<Video> out = new ArrayList<Video>();


        if(useThreading){
            VidGetThread[] threads = new VidGetThread[subscriptions.size()];

            for (int i=0 ; i<threads.length ; i++) {
                threads[i] = new VidGetThread(this, subscriptions.get(i));
                threads[i].start();
            }

            for (int i=0 ; i<threads.length ; i++) {
                try {
                    threads[i].join();

                    out.addAll(threads[i].vids);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }else {
            for (Channel channel : subscriptions) {
                out.addAll(
                        channel.getVideos(this)
                );
            }
        }



        Collections.sort(out, new Comparator<Video>() {
            public int compare(Video v1, Video v2) {
                if (v1.published == v2.published) {
                    return 0;
                } else if (v1.published < v2.published) {
                    return 1;
                }
                return -1;
            }
        });

        return out;
    }

    public List<Video> getVideos(){
        return getVideos(true);
    }

    public void save(){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        String out = gson.toJson(this);

        FileUtils.writeFile(new File(saveFile), out);
    }
}
