package uk.co.therhys.JYT;

import javax.swing.*;

public class ImageViewer extends JLabel {
    public ImageViewer(String url){
        setText("<html> <img alt='Post image' src='"+url+"'> </html>");
    }
}
