/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.view;

import systems.tech247.hr.EmployeeCategories;

/**
 *
 * @author Wilfred
 */
public class CategorySelectable {
    
    private EmployeeCategories cat;
    private Boolean selected;

    public CategorySelectable(EmployeeCategories cat, Boolean selected) {
        this.cat = cat;
        this.selected = selected;
    }

    /**
     * @return the cat
     */
    public EmployeeCategories getCat() {
        return cat;
    }

    /**
     * @param cat the cat to set
     */
    public void setCat(EmployeeCategories cat) {
        this.cat = cat;
    }

    /**
     * @return the selected
     */
    public Boolean getSelected() {
        return selected;
    }

    /**
     * @param selected the selected to set
     */
    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
    
    
    
}
