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
    public Channel[] subscriptions;
    public transient String saveFile;

    public static Config fromFile(String configFile){
        String config = FileUtils.readFile(new File(configFile));

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        Config conf = (Config) gson.fromJson(config, Config.class);
        //conf.refreshSubs();
        conf.saveFile = configFile;

        return conf;
    }

    public Config(){}

    /*public void refreshSubs(){
        if(subscriptions == null) {
            subscriptions = new ArrayList();

            for (int i=0 ; i<subs.length ; i++) {
                subscriptions.add(new Channel(subs[i]));
            }
        }
    }*/

    public List getVideos(boolean useThreading) {
        ArrayList out = new ArrayList();


        if(useThreading){
            VidGetThread[] threads = new VidGetThread[subscriptions.length];

            for (int i=0 ; i<threads.length ; i++) {
                threads[i] = new VidGetThread(this, (Channel) subscriptions[i]);
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
            for (int i=0 ; i<subscriptions.length ; i++) {
                out.addAll(
                        ((Channel) subscriptions[i]).getVideos(this)
                );
            }
        }

        Collections.sort(out, new Comparator() {
            public int compare(Object o1, Object o2) {
                Video v1 = (Video) o1;
                Video v2 = (Video) o2;
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

    public List getVideos(){
        return getVideos(true);
    }

    public void save(){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        String out = gson.toJson(this);

        FileUtils.writeFile(new File(saveFile), out);
    }
}
