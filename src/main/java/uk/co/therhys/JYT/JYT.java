package uk.co.therhys.JYT;
import uk.co.therhys.YT.Config;

public class JYT {
    public static void main(String[] args){
        String os = System.getProperty("os.name");

        try{
            System.setProperty("com.apple.mrj.application.apple.menu.about.name", "JYT");
        } catch(Exception e){
            e.printStackTrace();
        }

        String savePath = "yt_saves.json.small";

        if(os.equals("Linux")){
            savePath = "/home/rhys/.config/" + savePath;
        }else if(os.equals("Mac OS X")){
            savePath = "/Users/rhys/Library/" + savePath;
        }else{
            savePath = ".\\" + savePath;
        }

        Config conf = Config.fromFile(savePath);

        new MainFrame(conf);

        conf.save();
    }
}
