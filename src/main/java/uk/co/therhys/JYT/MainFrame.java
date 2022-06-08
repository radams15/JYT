package uk.co.therhys.JYT;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import uk.co.therhys.YT.Channel;
import uk.co.therhys.YT.Config;
import uk.co.therhys.YT.Video;

import java.net.URL;
import java.util.Date;

public class MainFrame {
    private Config conf;
    private Display display;
    private Shell shell;

    private Table table;

    private SelectionListener playListener;

    private ToolItem newToolButton(ToolBar bar, String title, String img, SelectionListener listener){
        ToolItem btn = new ToolItem(bar, SWT.PUSH);
        btn.setText(title);

        Image icon;
        try{
            URL url = ClassLoader.getSystemClassLoader().getResource(img);
            icon = new Image(display, url.openStream());
        }catch(Exception e){
            icon = new Image(display, "src/main/resources/"+img);
        }

        btn.setImage(icon);

        btn.addSelectionListener(listener);

        return btn;
    }

    private void setupActions(){
        playListener = new SelectionListener() {
            public void widgetSelected(SelectionEvent selectionEvent) {
                TableItem[] items = table.getSelection();
                if(items.length != 1){
                    return;
                }
                Video vid = (Video) items[0].getData();

                System.out.println(vid.title);
            }

            public void widgetDefaultSelected(SelectionEvent selectionEvent) {

            }
        };
    }

    void setupToolbar(){
        ToolBar toolbar = shell.getToolBar();
        if(toolbar == null){
            toolbar = new ToolBar(shell, SWT.HORIZONTAL);
        }
        toolbar.setSize(300, 70);
        toolbar.setLocation(0, 0);

        newToolButton(toolbar, "Play", "playpause.png", playListener);
    }

    void setupMenubar(){
        Menu menuBar = shell.getMenu();
        if(menuBar == null){
            menuBar = new Menu(shell, SWT.BAR);
        }

        MenuItem video = new MenuItem(menuBar, SWT.CASCADE);
        video.setText("Video");
        Menu videoMenu = new Menu(shell, SWT.DROP_DOWN);
        video.setMenu(videoMenu);

        MenuItem playMenu = new MenuItem(videoMenu, SWT.PUSH);
        playMenu.setText("Play\tCTRL+P");
        playMenu.setAccelerator(SWT.CTRL+'P');
        playMenu.addSelectionListener(playListener);

        shell.setMenuBar(menuBar);
    }

    private void setupUI(){
        shell.setSize(800, 600);
        shell.setLayout(new GridLayout(1, true));

        setupActions();
        setupToolbar();
        setupMenubar();

        table = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION | SWT.RESIZE | SWT.V_SCROLL | SWT.H_SCROLL);
        table.setLinesVisible(true);
        table.setHeaderVisible(true);

        String[] titles = new String[]{"Title", "Channel", "Published"};
        for(int i=0 ; i<titles.length ; i++){
            new TableColumn(table, SWT.NONE).setText(titles[i]);
        }
    }

    public MainFrame(Config conf){
        display = new Display();
        shell = new Shell(display);

        this.conf = conf;

        setupUI();

        for(Video vid : conf.subscriptions.get(0).getVideos(conf)){
            TableItem itm = new TableItem(table, SWT.NONE);
            itm.setText(0, vid.title);
            itm.setText(1, vid.channel.getName());
            itm.setText(2, new Date((long)vid.published*1000).toString());
            itm.setData(vid);
        }
        for(int i=0 ; i<3 ; i++){
            table.getColumn(i).pack();
        }


        shell.pack();
        shell.open();

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }
}
