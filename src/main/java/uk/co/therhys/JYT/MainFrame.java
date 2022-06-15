package uk.co.therhys.JYT;

import uk.co.therhys.YT.Config;
import uk.co.therhys.YT.Video;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Date;

public class MainFrame extends JFrame {
    private Config conf;

    private DefaultTableModel tableModel;
    private JTable table;
    private JPanel mainPanel;

    private ActionListener playListener;

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
        playListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                int row = table.getSelectedRow();

                Video vid = (Video) tableModel.getValueAt(row, 3);

                Video.Stream stream = vid.getStream(conf);

                playStream(stream);
            }
        };
    }

    void setupToolbar(){
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);

        toolbar.add(newToolButton("Play", "playpause.png", playListener));

        mainPanel.add(toolbar, BorderLayout.NORTH);
    }

    void setupMenubar(){
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

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

        table = new JTable(new DefaultTableModel(new Object[]{"Title", "Channel", "Published", "Obj"}, 0));
        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        tableModel = (DefaultTableModel) table.getModel();

        table.getColumnModel().removeColumn(table.getColumnModel().getColumn(3));

        setupActions();
        setupToolbar();
        setupMenubar();
    }

    public MainFrame(Config conf){
        this.conf = conf;

        setIconImage(getIcon("JYT.png").getImage());

        setupUI();

        for(Video vid : conf.getVideos()){
            tableModel.addRow(new Object[]{vid.title, vid.channel.getName(), new Date((long)vid.published*1000), vid});
        }
    }
}
