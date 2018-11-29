/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.util;

import java.beans.PropertyEditorSupport;

/**
 *
 * @author Admin
 */
public class SelectStatusEditor extends PropertyEditorSupport {
    
    
    @Override
    public void setAsText(String text){
        setValue(text);
    }
}
