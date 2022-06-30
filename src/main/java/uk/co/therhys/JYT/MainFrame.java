package uk.co.therhys.JYT;

import com.sun.rowset.internal.Row;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import uk.co.therhys.YT.Channel;
import uk.co.therhys.YT.Config;
import uk.co.therhys.YT.VidListener;
import uk.co.therhys.YT.Video;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import org.eclipse.swt.graphics.Image;

import apple.dts.samplecode.osxadapter.OSXAdapter;

public class MainFrame implements VidListener {
    private Config conf;

    private VideoTable table;
    private VideoTableModel tableModel;
    private JPanel mainPanel;

    private Display display;
    private Shell shell;

    private LoadingDlg loadingDlg = null;

    private ActionListener goChannelListener;
    private ActionListener searchListener;
    private ActionListener playListener;
    private ActionListener preferencesListener;
    private ActionListener quitListener;
    private ActionListener aboutListener;

    private final boolean isOsx = (System.getProperty("os.name").toLowerCase().startsWith("mac os x"));

    private Image getIcon(String img){
        Image icon;
        try{
            URL url = ClassLoader.getSystemClassLoader().getResource(img);
            icon = new Image(display, url.openStream());
        }catch(Exception e){
            icon = new Image(display,"src/main/resources/"+img);
        }

        return icon;
    }

    private void newToolButton(ToolBar bar, String title, String img, final ActionListener listener){
        ToolItem btn = new ToolItem(bar, SWT.PUSH);
        btn.setText(title);

        btn.setImage(getIcon(img));


        btn.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent selectionEvent) {
                listener.actionPerformed(new ActionEvent(shell, 0, "Proxy"));
            }

            public void widgetDefaultSelected(SelectionEvent selectionEvent) {

            }
        });
    }

    private void playStream(Video.Stream stream){
        new VideoPlayer(stream).start();
    }

    private void showLoading(){
        loadingDlg = new LoadingDlg();
        //loadingDlg.setModal(true);

        display.asyncExec(new Runnable() {
            public void run() {
                loadingDlg.setVisible(true);
                loadingDlg.requestFocus();
            }
        });
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

                    display.asyncExec(new Runnable() {
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

                    display.asyncExec(new Runnable() {
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
                /*SettingsDialog settingsDlg = new SettingsDialog(frameRef, conf);
                settingsDlg.setVisible(true);

                settingsDlg.save();*/
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
                shell.dispose();
            }
        };

        if(isOsx){
            OSXAdapter.setQuitHandler(this, quitListener);
            OSXAdapter.setAboutHandler(this, aboutListener);
            OSXAdapter.setPreferencesHandler(this, preferencesListener);
        }
    }

    void setupToolbar(){
        ToolBar toolbar = null;//shell.getToolBar();
        if(toolbar == null){
            toolbar = new ToolBar(shell, SWT.HORIZONTAL);
        }

        toolbar.setSize(300, 70);
        toolbar.setLocation(0, 0);

        newToolButton(toolbar, "Play", "play.png", playListener);
        newToolButton(toolbar,"Preferences", "preferences.png", preferencesListener);
        newToolButton(toolbar,"Search", "search.png", searchListener);
        newToolButton(toolbar,"Channels", "go.png", goChannelListener);
    }

    void setupMenubar(){
        JMenuBar menuBar = new JMenuBar();
        //setJMenuBar(menuBar);

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
        shell.setSize(800, 600);
        shell.setLayout(new GridLayout(1, true));
        //setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        //setLocationRelativeTo(null);

        setupActions();
        setupToolbar();
        setupMenubar();

        mainPanel = new JPanel(new BorderLayout());

        SWT_AWT.embeddedFrameClass = "sun.lwawt.macosx.CViewEmbeddedFrame";
        Composite comp = new Composite(shell, SWT.EMBEDDED);
        comp.setBounds(5,5,300,300);
        Frame frame = SWT_AWT.new_Frame(comp);

        BorderLayout layout = new BorderLayout();
        mainPanel.setLayout(layout);

        table = new VideoTable();
        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        tableModel = (VideoTableModel) table.getModel();

        frame.add(mainPanel);
    }

    public MainFrame(Config conf){
        this.conf = conf;

        display = new Display();
        shell = new Shell(display);

        /*Image appIcon = getIcon("JYT.png");
        //setIconImage(appIcon);
        if(isOsx){
            OSXAdapter.setApplicationIcon(this, appIcon);
        }*/

        setupUI();

        shell.pack();
        shell.open();

        //showLoading();

        final MainFrame frameRef = this;

        /*display.asyncExec(new Runnable() {
            public void run() {
                frameRef.conf.getVideos(frameRef);
            }
        });*/

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }

    public void vidFetchCompleted(){
        tableModel.sort();

        hideLoading();
        System.out.println("Done");
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
