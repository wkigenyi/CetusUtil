/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.util;

/**
 *
 * @author Wilfred
 */
public class Marital {
    
    private Short code;
    private String description;
    
    public Marital (Short code){
        this.code = code;
        
    }

    /**
     * @return the code
     */
    public Short getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(Short code) {
        this.code = code;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        switch(code){
            case 1:
                return "Single";
            case 2:
                return "Married";
            case 3:
                return "Divorced";
            case 4:
                return "Separated";    
            default:
                return "Not Specified";
        }
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    @Override
    public String toString(){
        switch(code){
            case 1:
                return "Single";
            case 2:
                return "Married";
            case 3:
                return "Divorced";
            case 4:
                return "Separated";    
            default:
                return "Not Specified";
        }
    }
}
