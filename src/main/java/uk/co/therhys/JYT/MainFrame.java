package uk.co.therhys.JYT;

import uk.co.therhys.YT.Config;
import uk.co.therhys.YT.Video;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.List;

import apple.dts.samplecode.osxadapter.OSXAdapter;

public class MainFrame extends JFrame {
    private Config conf;

    private VideoTable table;
    private JPanel mainPanel;

    private ActionListener playListener;
    private ActionListener preferencesListener;
    private ActionListener quitListener;
    private ActionListener aboutListener;

    private final boolean isOsx = (System.getProperty("os.name").toLowerCase().startsWith("mac os x"));

    private static ImageIcon getIcon(String img){
        ImageIcon icon;
        try{
            URL url = ClassLoader.getSystemClassLoader().getResource(img);
            icon = new ImageIcon(url);
        }catch(Exception e){
            icon = new ImageIcon("src/main/resources/"+img);
        }

        return icon;
    }

    private static JButton newToolButton(String title, String img, ActionListener listener){
        JButton btn = new UnifiedToolBtn(title);

        btn.setIcon(getIcon(img));

        btn.addActionListener(listener);

        return btn;
    }

    private void playStream(Video.Stream stream){
        new VideoPlayer(stream).start();
    }

    private void setupActions(){

        final MainFrame frameRef = this;

        playListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                int row = table.getSelectedRow();

                Video vid = (Video) table.getVideo(row);

                Video.Stream stream = vid.getStream(conf);

                playStream(stream);
            }
        };

        preferencesListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SettingsDialog settingsDlg = new SettingsDialog(frameRef, conf);
                settingsDlg.setVisible(true);

                settingsDlg.save();
            }
        };

        aboutListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                JDialog dialog = new JDialog();
                dialog.setSize(300, 150);

                dialog.add(new JLabel("JReddit - by Rhys Adams"));
                dialog.setVisible(true);
            }
        };

        quitListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("Quit");
                frameRef.dispose();
            }
        };

        if(isOsx){
            OSXAdapter.setQuitHandler(this, quitListener);
            OSXAdapter.setAboutHandler(this, aboutListener);
            OSXAdapter.setPreferencesHandler(this, preferencesListener);
        }
    }

    void setupToolbar(){
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);

        toolbar.add(newToolButton("Play", "play.png", playListener));
        toolbar.add(newToolButton("Preferences", "preferences.png", preferencesListener));

        mainPanel.add(toolbar, BorderLayout.NORTH);
    }

    void setupMenubar(){
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        if(!isOsx) {
            JMenu fileMenu = new JMenu("File");
            menuBar.add(fileMenu);
            fileMenu.add("Quit").addActionListener(quitListener);

            JMenu editMenu = new JMenu("Edit");
            menuBar.add(editMenu);

            editMenu.add("Settings").addActionListener(preferencesListener);
        }

        JMenu videoMenu = new JMenu("Video");
        menuBar.add(videoMenu);

        videoMenu.add("Play").addActionListener(playListener);
    }

    private void setupUI(){
        setSize(800, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        mainPanel = new UnifiedToolbarPanel(new BorderLayout());
        setContentPane(mainPanel);

        getRootPane().putClientProperty("apple.awt.brushMetalLook", Boolean.TRUE);

        BorderLayout layout = new BorderLayout();
        mainPanel.setLayout(layout);

        table = new VideoTable();
        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        setupActions();
        setupToolbar();
        setupMenubar();
    }

    public MainFrame(Config conf){
        this.conf = conf;

        Image appIcon = getIcon("JYT.png").getImage();
        setIconImage(appIcon);
        if(isOsx){
            OSXAdapter.setApplicationIcon(this, appIcon);
        }

        setupUI();

        List videos = conf.getVideos();
        for(int i=0 ; i<videos.size() ; i++){
            Video vid = (Video) videos.get(i);

            table.addVideo(vid);
        }
    }
}
