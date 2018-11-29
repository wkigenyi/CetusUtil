/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.dbaccess;

import systems.tech247.hr.TblPeriods;



/**
 *
 * @author Admin
 */
public interface CurrentPeriodProvider {
    
    TblPeriods getCurrentPeriod();
    
    
}
