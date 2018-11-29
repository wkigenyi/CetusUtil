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
public class Year {
    int yearValue;
    
    public Year(int year){
        this.yearValue =  year;
    }
    
    public int getYear(){
        return this.yearValue;
    }
    
    public void setYear(int yearValue){
        this.yearValue = yearValue;
    }
    @Override
    public boolean equals(Object obj){
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Year)) {
            return false;
        }
        Year other = (Year) obj;
        return this.yearValue==(other.yearValue);
        
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + this.yearValue;
        return hash;
    }
    
}
