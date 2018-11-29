/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.util;

import javax.swing.Action;
import org.openide.nodes.AbstractNode;
import org.openide.util.lookup.AbstractLookup;


/**
 *
 * @author Admin
 */
public class NodeSetupItem extends AbstractNode{
    
    SetupItem item;
    public NodeSetupItem(SetupItem item){
        super(item.getKids(),new AbstractLookup(item.ic));
        this.item = item;
        setDisplayName(item.getDescription());
        try{
        setIconBaseWithExtension(item.getIconPath());
        }catch(NullPointerException ex){
            setIconBaseWithExtension("systems/tech247/util/icons/settings.png");
        }
    }

    @Override
    public Action getPreferredAction() {
        return item.getDefaultAction();
    }
    
    
    
}
