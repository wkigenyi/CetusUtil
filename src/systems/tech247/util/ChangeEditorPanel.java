/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.util;

import javax.swing.JPanel;

/**
 *
 * @author Wilfred
 */
public class ChangeEditorPanel {
    private JPanel panel;
    private String info;
    public ChangeEditorPanel(String info,JPanel panel){
        this.info = info;
        this.panel = panel;
        
    }

    /**
     * @return the panel
     */
    public JPanel getPanel() {
        return panel;
    }

    /**
     * @param panel the panel to set
     */
    public void setPanel(JPanel panel) {
        this.panel = panel;
    }

    /**
     * @return the info
     */
    public String getInfo() {
        return info;
    }

    /**
     * @param info the info to set
     */
    public void setInfo(String info) {
        this.info = info;
    }
    @Override
    public String toString(){
        return info;
    }
    
}
