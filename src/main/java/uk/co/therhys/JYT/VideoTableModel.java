package uk.co.therhys.JYT;

import uk.co.therhys.YT.Video;

import javax.swing.table.DefaultTableModel;

public class VideoTableModel extends DefaultTableModel {
    public VideoTableModel() {
        super(new Object[][] {},
                new String[] {"Video" });
    }

    Class[] columnTypes = new Class[] { Video.class };

    public Class getColumnClass(int columnIndex) {
        return columnTypes[columnIndex];
    }

    public void addRow(Video video){
        addRow(new Object[]{
                video
        });
    }
}
