package uk.co.therhys.JYT;

import javax.swing.*;

public class UnifiedToolBtn extends JButton {
    public UnifiedToolBtn(String title){
        super(title);

        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);

        setVerticalTextPosition(SwingConstants.BOTTOM);
        setHorizontalTextPosition(SwingConstants.CENTER);
    }
}
