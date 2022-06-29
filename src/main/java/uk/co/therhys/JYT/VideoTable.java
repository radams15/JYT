package uk.co.therhys.JYT;

import uk.co.therhys.YT.Video;

import javax.swing.*;
import javax.swing.table.TableRowSorter;

public class VideoTable extends JTable {
    private VideoTableModel model;

    public VideoTable(){
        super(new VideoTableModel());
        model = (VideoTableModel) getModel();

        getColumnModel().getColumn(0).setCellRenderer(new VideoTableRenderer());
        setRowHeight(60);
    }

    public Video getVideo(int row){
        return (Video) model.getValueAt(row, 0);
    }

    public void addVideo(Video video){
        model.addRow(video);
    }
}
