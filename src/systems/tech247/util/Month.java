/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.util;

/**
 *
 * @author WKigenyi
 */
public class Month {
    private int yr;
    private int month;
    
    public  Month(int yr, int month){
        this.yr=yr;
        this.month = month;
    }

    /**
     * @return the yr
     */
    public int getYr() {
        return yr;
    }

    /**
     * @param yr the yr to set
     */
    public void setYr(int yr) {
        this.yr = yr;
    }

    /**
     * @return the month
     */
    public int getMonth() {
        return month;
    }

    /**
     * @param month the month to set
     */
    public void setMonth(int month) {
        this.month = month;
    }
    
}
