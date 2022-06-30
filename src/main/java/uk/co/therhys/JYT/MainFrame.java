package uk.co.therhys.JYT;

import uk.co.therhys.YT.Channel;
import uk.co.therhys.YT.Config;
import uk.co.therhys.YT.VidListener;
import uk.co.therhys.YT.Video;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.List;

import apple.dts.samplecode.osxadapter.OSXAdapter;

public class MainFrame extends JFrame implements VidListener {
    private Config conf;

    private VideoTable table;
    private VideoTableModel tableModel;
    private JPanel mainPanel;

    private LoadingDlg loadingDlg = null;

    private ActionListener goChannelListener;
    private ActionListener searchListener;
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

    private void showLoading(){
        loadingDlg = new LoadingDlg();

        new Thread(new Runnable() {
            public void run() {
                loadingDlg.setModal(true);
                loadingDlg.setVisible(true);
            }
        }).start();
    }

    private void hideLoading(){
        if(loadingDlg != null){
            loadingDlg.setModal(false);
            loadingDlg.setVisible(false);

            loadingDlg.dispose();
            loadingDlg = null;
        }
    }

    private void setupActions(){

        final MainFrame frameRef = this;


        searchListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final String query = JOptionPane.showInputDialog("Query");

                if(query != null && !query.equals("")){
                    tableModel.clear();

                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            frameRef.conf.search(query, 1, frameRef);
                        }
                    });
                }
            }
        };

        goChannelListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GoChannelDlg dlg = new GoChannelDlg(conf);

                dlg.setModal(true);
                dlg.setVisible(true);

                final Channel channel = dlg.getSelected();

                if(channel != null) {
                    showLoading();
                    tableModel.clear();

                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            channel.getVideos(conf, frameRef);
                        }
                    });
                }
            }
        };

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
        toolbar.add(newToolButton("Search", "search.png", searchListener));
        toolbar.add(newToolButton("Channels", "go.png", goChannelListener));

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
        videoMenu.add("Search").addActionListener(searchListener);

        JMenu channelMenu = new JMenu("Channel");
        menuBar.add(channelMenu);

        channelMenu.add("Go").addActionListener(goChannelListener);
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

        tableModel = (VideoTableModel) table.getModel();

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

        setVisible(true);

        final MainFrame frameRef = this;

        showLoading();

        new Thread(new Runnable() {
            public void run() {
                frameRef.conf.getVideos(frameRef);
            }
        }).start();
    }

    public void vidFetchCompleted(){
        tableModel.sort();
        System.out.println("Done");

        hideLoading();
    }

    public void fetchProgress(float proportion) {
        if(loadingDlg != null){
            loadingDlg.setProgress(proportion);
        }
    }


    public void getVideo(Video video) {
        table.addVideo(video);
    }
}
