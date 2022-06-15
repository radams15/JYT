package uk.co.therhys.JYT;

import uk.co.therhys.YT.Config;
import uk.co.therhys.YT.Video;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

public class MainFrame extends JFrame {
    private Config conf;

    private DefaultTableModel tableModel;
    private JTable table;

    private ActionListener playListener;

    private static JButton newToolButton(String title, String img, ActionListener listener){
        JButton btn = new JButton(title);

        ImageIcon icon;
        try{
            URL url = ClassLoader.getSystemClassLoader().getResource(img);
            icon = new ImageIcon(url);
        }catch(Exception e){
            icon = new ImageIcon("src/main/resources/"+img);
        }

        btn.setIcon(icon);

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

        toolbar.add(newToolButton("Play", "playpause.png", playListener));

        getContentPane().add(toolbar, BorderLayout.NORTH);
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

        BorderLayout layout = new BorderLayout();
        getContentPane().setLayout(layout);

        table = new JTable(new DefaultTableModel(new Object[]{"Title", "Channel", "Published", "Obj"}, 0));
        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        tableModel = (DefaultTableModel) table.getModel();

        table.getColumnModel().removeColumn(table.getColumnModel().getColumn(3));

        setupActions();
        setupToolbar();
        setupMenubar();
    }

    public MainFrame(Config conf){
        this.conf = conf;

        setupUI();

        for(Video vid : conf.getVideos()){
            tableModel.addRow(new Object[]{vid.title, vid.channel.getName(), new Date((long)vid.published*1000), vid});
        }
    }
}
