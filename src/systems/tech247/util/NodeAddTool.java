/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.util;

import javax.swing.Action;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;


/**
 *
 * @author Admin
 */
public class NodeAddTool extends AbstractNode{
    
    AddTool item;
    public NodeAddTool(AddTool item){
        super(Children.LEAF);
        this.item = item;
        setDisplayName("Add New");
        setIconBaseWithExtension("systems/tech247/util/icons/AddNew.png");
    }

    @Override
    public Action getPreferredAction() {
        return item.getAction();
    }
    
    
    
}
