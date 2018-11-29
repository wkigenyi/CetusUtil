/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.util;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.openide.nodes.Children;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author Admin
 */
public class SetupItem {
    
    String description;
    Children kids;
    private String iconPath;
    private Action defaultAction;
    InstanceContent ic;
    
    
    public SetupItem(String description){
        this(description, Children.LEAF, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        },new InstanceContent());
        
    }
    
    public SetupItem(String description,Action action,String icon){
        this(description,Children.LEAF,icon);
        this.defaultAction = action;
    }
    
    
    
    
    public SetupItem(String description, Children kids){
        this(description, kids, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        },new InstanceContent());
    }
    
    public SetupItem(String description, Children kids,String icon){
        this(description, kids, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        },new InstanceContent());
        this.iconPath = icon;
    }
    
    public SetupItem(String description, Action defaultAction,InstanceContent ic){
        this(description, Children.LEAF, defaultAction,ic);
    }
    
    
    
    public SetupItem(String description, Action defaultAction){
        this(description, Children.LEAF, defaultAction,new InstanceContent());
    }

    public String getDescription() {
        return description;
    }

    public Children getKids() {
        return kids;
    }
    
    
    
    public SetupItem(String description, Children kids, Action defaultAction,InstanceContent ic){
        this.kids = kids;
        this.description = description;
        this.defaultAction = defaultAction;
        this.ic =ic;
    }
    
    public SetupItem(String description,Children kids,InstanceContent ic){
        this(description, kids, null, ic);
    }
    


    /**
     * @return the defaultAction
     */
    public Action getDefaultAction() {
        return defaultAction;
    }

    /**
     * @return the iconPath
     */
    public String getIconPath() {
        return iconPath;
    }
    
}
