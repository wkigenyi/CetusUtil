/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.util;

import java.net.URL;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.openide.NotifyDescriptor;

/**
 *
 * @author Wilfred
 */
public enum MessageType {
    
    PLAIN   (NotifyDescriptor.PLAIN_MESSAGE,       null),
    INFO    (NotifyDescriptor.INFORMATION_MESSAGE, "infor.png"),
    QUESTION(NotifyDescriptor.QUESTION_MESSAGE,    "question.png"),
    ERROR   (NotifyDescriptor.ERROR_MESSAGE,       "error.png"),
    WARNING (NotifyDescriptor.WARNING_MESSAGE,     "warning.png");
    
    int notifyDescriptorType;
    Icon icon;
    
    private MessageType(int notifyDescriptorType, String resourceName){
        this.notifyDescriptorType = notifyDescriptorType;
        if(resourceName==null){
            icon = new ImageIcon();
        }else{
            icon = loadIcon(resourceName);
        }
    }
    
    private static Icon loadIcon(String resourceName) {
        URL resource = MessageType.class.getResource("images/"+resourceName);
        //System.out.println(resource);
        if (resource == null) {
            return new ImageIcon();
        }
        
        return new ImageIcon(resource);
    }
    
    

    public Icon getIcon() {
        return icon;
    }

    public int getNotifyDescriptorType() {
        return notifyDescriptorType;
    }
    
    
    
}
