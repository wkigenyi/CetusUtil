/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.util;

import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author Wilfred
 */
public class NodeRefreshProvider implements Lookup.Provider {
    static NodeRefreshProvider instance;
    public static InstanceContent content = new InstanceContent();
    Lookup lookup= new AbstractLookup(content);

    @Override
    public Lookup getLookup() {
        return lookup;
    }

    public static NodeRefreshProvider getInstance() {
        if(null==instance){
            instance= new NodeRefreshProvider();
        }
        return instance;
    }
    
    
}
