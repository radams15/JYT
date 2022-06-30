package uk.co.therhys.JYT;

import uk.co.therhys.YT.FileUtils;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;

public class OsxUiFactory {
    private static OsxUiFactory instance;

    public static final Color OS_X_UNIFIED_TOOLBAR_FOCUSED_BOTTOM_COLOR =
            new Color(64, 64, 64);
    public static final Color OS_X_UNIFIED_TOOLBAR_UNFOCUSED_BORDER_COLOR =
            new Color(135, 135, 135);

    public static final Color backgroundColour = UIManager.getColor("Panel.background");

    public static OsxUiFactory getInstance(){
        if(instance == null){
            instance = new OsxUiFactory();
        }

        return instance;
    }

    public JTextArea getSizedLabel(String text){
        return getSizedLabel(text, 15);
    }

    public JTextArea getSizedLabel(String text, float fontSize){
        JTextArea out = new JTextArea(text);

        out.setFont(out.getFont().deriveFont((float) fontSize));

        out.setLineWrap(true);
        out.setWrapStyleWord(true);
        out.setEditable(false);
        out.setFocusable(false);

        out.setOpaque(false);
        out.setBorder(UIManager.getBorder("Label.border"));

        return out;
    }

    public JLabel getImageViewer(String url){
        return new JLabel("<html> <img alt='Post image' src='"+url+"'> </html>");
    }

    public JLabel getImageViewer(File file, int width, int height){
        return new JLabel(
                new ImageIcon(
                        new ImageIcon(
                                FileUtils.readFileBytes(file)
                        ).getImage()
                                .getScaledInstance(width, height, Image.SCALE_FAST)
                )
        );
    }

    public JLabel getImageViewer(File file){
        return getImageViewer(file, 600, 800);
    }

    public JPanel getClearPanel(){
        JPanel out = new JPanel();

        out.setOpaque(false);

        return out;
    }

    public JButton getUnifiedToolBtn(String title){
        JButton out = new JButton(title);

        out.setOpaque(false);
        out.setContentAreaFilled(false);
        out.setBorderPainted(false);

        out.setVerticalTextPosition(SwingConstants.BOTTOM);
        out.setHorizontalTextPosition(SwingConstants.CENTER);

        return out;
    }

    public JPanel getUnifiedToolbarPanel(LayoutManager manager){
        JPanel out = new JPanel(manager){
            public Border getBorder() {
                Window window = SwingUtilities.getWindowAncestor(this);
                return window != null && window.isFocused()
                        ? BorderFactory.createMatteBorder(0,0,1,0,
                        OS_X_UNIFIED_TOOLBAR_FOCUSED_BOTTOM_COLOR)
                        : BorderFactory.createMatteBorder(0,0,1,0,
                        OS_X_UNIFIED_TOOLBAR_UNFOCUSED_BORDER_COLOR);
            }
        };

        out.setOpaque(false);

        return out;
    }
}
