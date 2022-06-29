package uk.co.therhys.JYT;

import uk.co.therhys.YT.Video;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class VideoTableRenderer implements TableCellRenderer {
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {

        VideoPanel panel = new VideoPanel((Video) value);

        panel.setSelected(isSelected);

        return panel;
    }
}