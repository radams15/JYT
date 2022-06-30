package uk.co.therhys.JYT;

import javax.swing.*;
import java.awt.*;

public class LoadingDlg extends JDialog {
    private JProgressBar bar;

    public LoadingDlg(){
        setLayout(new GridLayout(-1, 1));

        add(new JLabel("Working..."));

        bar = new JProgressBar(0, 100);
        add(bar);

        setSize(200, 80);

        bar.setValue(1);
    }

    public void setProgress(float progress){
        bar.setValue((int) (progress*100.0f));
    }
}
