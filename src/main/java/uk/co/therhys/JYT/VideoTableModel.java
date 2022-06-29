package uk.co.therhys.JYT;

import uk.co.therhys.YT.Video;

import javax.swing.table.DefaultTableModel;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

public class VideoTableModel extends DefaultTableModel {
    public VideoTableModel() {
        super(new Object[][] {},
                new String[] {"Video" });
    }

    Class[] columnTypes = new Class[] { Video.class };

    public Class getColumnClass(int columnIndex) {
        return columnTypes[columnIndex];
    }

    public void sort() {
        Collections.sort(getDataVector(), new Comparator() {
            public int compare(Object o1, Object o2) {
                Video a = (Video) ((Vector) o1).get(0);
                Video b = (Video) ((Vector) o2).get(0);

                if(a.published == b.published){
                    return 0;
                }else if(a.published < b.published){
                    return 1;
                }else{
                    return -1;
                }
            }
        });

        fireTableDataChanged();
    }

    public void clear(){
        getDataVector().removeAllElements();
        fireTableDataChanged();
    }

    public void addRow(Video video){
        addRow(new Object[]{
                video
        });
    }
}
