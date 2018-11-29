/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.dbaccess;

import java.beans.PropertyEditorSupport;

/**
 *
 * @author WKigenyi
 */
public class BooleanEditor extends PropertyEditorSupport {
    

@Override
public void setAsText(String s) {
    
        setValue (s);
    
}
    
}
