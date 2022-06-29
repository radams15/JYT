package uk.co.therhys.JYT;

import uk.co.therhys.YT.Config;

import javax.swing.*;
import java.awt.*;

public class SettingsDialog extends JDialog {
    private final Config conf;

    private JComboBox qualityChooser;
    private JTextField instanceUrl;

    private JPanel initApiPanel(){
        JPanel card2 = new ClearPanel();

        JPanel instancePanel = new ClearPanel();
        instanceUrl = new JTextField(conf.instance);
        instancePanel.add(new JLabel("Instance URL: "));
        instancePanel.add(instanceUrl);
        card2.add(instancePanel);

        return card2;
    }

    private JPanel initPlaybackPanel(){
        JPanel card1 = new ClearPanel();

        JPanel qualityBox = new ClearPanel();
        qualityChooser = new JComboBox(
                new String[]{
                        "1080",
                        "720",
                        "480",
                        "360",
                        "144"
                }
        );
        qualityChooser.setSelectedItem(String.valueOf(conf.quality));
        qualityBox.add(new JLabel("Playback Quality: "));
        qualityBox.add(qualityChooser);
        card1.add(qualityBox);

        return card1;
    }

    public void save(){
        conf.quality = Integer.valueOf((String) qualityChooser.getSelectedItem()).intValue();
        conf.instance = instanceUrl.getText();
    }

    private void initUI(){
        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Playback", initPlaybackPanel());
        tabbedPane.addTab("API", initApiPanel());

        getContentPane().add(tabbedPane, BorderLayout.CENTER);
    }
    public SettingsDialog(JFrame parent, Config conf){
        super(parent, "Settings");
        setModal(true);
        this.conf = conf;

        setSize(600, 400);

        initUI();
    }
}
