package uk.co.therhys.JYT;

import uk.co.therhys.YT.Video;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Date;

public class VideoPanel extends JPanel {
    private Video video;

    private void initUI(){
        GridLayout layout = new GridLayout(-1, 2);
        setLayout(layout);

        //File thumbFile = video.getThumbFile();

        SizedLbl titleLbl = new SizedLbl(video.title, 20);
        SizedLbl authorLbl = new SizedLbl(video.channel.name, 15);
        SizedLbl dateLbl = new SizedLbl(new Date((long)video.published*1000).toString(), 15);
        //JLabel thumbnailImg = OsxUiFactory.getInstance().getImageViewer(thumbFile);

        add(titleLbl);
        //add(thumbnailImg);
        add(authorLbl);
        add(dateLbl);
    }

    public void setSelected(boolean selected){
        Color colour;

        if(selected){
            colour = UIManager.getColor("Table.selectionBackground");
        }else{
            colour = UIManager.getColor("Table.background");
        }

        setBackground(colour);
    }

    public VideoPanel(Video video){
        super();

        this.video = video;

        initUI();
    }
}
