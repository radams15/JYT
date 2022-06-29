package uk.co.therhys.JYT;

import javax.swing.*;

public class SizedLbl extends JTextArea {
    public SizedLbl(String text, float fontSize){
        super(text);

        setFont(getFont().deriveFont((float) fontSize));

        setLineWrap(true);
        setWrapStyleWord(true);
        setEditable(false);
        setFocusable(false);

        //setBackground(UIManager.getColor("window"));
        setOpaque(false);
        setBorder(UIManager.getBorder("Label.border"));
    }

    public SizedLbl(String text){
        this(text, 15);
    }
}
