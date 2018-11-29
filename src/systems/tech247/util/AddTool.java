/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.util;

import javax.swing.Action;

/**
 *
 * @author Admin
 */
public class AddTool {
    private Action action;
    public AddTool(Action action){
        this.action = action;
    }

    /**
     * @return the action
     */
    public Action getAction() {
        return action;
    }
    
}
