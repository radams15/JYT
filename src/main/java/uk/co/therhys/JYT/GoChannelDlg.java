package uk.co.therhys.JYT;

import uk.co.therhys.YT.Channel;
import uk.co.therhys.YT.Config;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class GoChannelDlg extends JDialog {
    private JComboBox choice;
    private final Config conf;
    private boolean complete = false;

    public GoChannelDlg(Config conf){
        this.conf = conf;
        choice = new JComboBox();

        for(int i=0 ; i<conf.subscriptions.length ; i++){
            choice.addItem(conf.subscriptions[i].name);
        }

        JButton ok = new JButton("OK");

        final GoChannelDlg dlgRef = this;

        ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dlgRef.complete = true;
                dlgRef.setVisible(false);
            }
        });

        setLayout(new GridLayout(-1, 1));

        add(choice);
        add(ok);

        setSize(280, 120);
        setLocationRelativeTo(null);
    }

    Channel getSelected(){
        if(!complete) { return null; }

        return conf.subscriptions[choice.getSelectedIndex()];
    }
}
