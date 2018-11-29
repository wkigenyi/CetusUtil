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
public class Gender {
    
    private Short code;
    private String description;
    
    public Gender (Short code){
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
                return "Male";
            case 2:
                return "Female";    
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
                return "Male";
            case 2:
                return "Female";    
            default:
                return "Not Specified";
        }
    }
}
