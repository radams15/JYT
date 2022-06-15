package uk.co.therhys.JYT;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class UnifiedToolbarPanel extends JPanel {

    public static final Color OS_X_UNIFIED_TOOLBAR_FOCUSED_BOTTOM_COLOR =
            new Color(64, 64, 64);
    public static final Color OS_X_UNIFIED_TOOLBAR_UNFOCUSED_BORDER_COLOR =
            new Color(135, 135, 135);

    public UnifiedToolbarPanel(LayoutManager manager) {
        super(manager);
        // make the component transparent
        setOpaque(false);
        // create an empty border around the panel
        // note the border below is created using JGoodies Forms
        //setBorder(Borders.createEmptyBorder("3dlu, 3dlu, 1dlu, 3dlu"));
    }

    public Border getBorder() {
        Window window = SwingUtilities.getWindowAncestor(this);
        return window != null && window.isFocused()
                ? BorderFactory.createMatteBorder(0,0,1,0,
                OS_X_UNIFIED_TOOLBAR_FOCUSED_BOTTOM_COLOR)
                : BorderFactory.createMatteBorder(0,0,1,0,
                OS_X_UNIFIED_TOOLBAR_UNFOCUSED_BORDER_COLOR);
    }
}