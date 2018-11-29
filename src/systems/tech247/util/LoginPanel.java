/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.util;

import java.awt.Component;
import org.openide.awt.StatusLineElementProvider;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Wilfred
 */
@ServiceProvider(service=StatusLineElementProvider.class, position = 1)
public class LoginPanel implements StatusLineElementProvider{

    @Override
    public Component getStatusLineElement() {
        return new LoginDetailsPanel();
    }
    
}
