/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.util;

import systems.tech247.hr.Employees;


/**
 *
 * @author Admin
 */
public class EmployeeSelectable {
    private Employees employee;
    private Boolean selected;
    private String surname;
    private String otherNames;
    
    public EmployeeSelectable(Employees employee, Boolean selected){
        this.employee = employee;
        this.selected = selected;
    }

    /**
     * @return the employee
     */
    public Employees getEmployee() {
        return employee;
    }

    /**
     * @param employee the employee to set
     */
    public void setEmployee(Employees employee) {
        this.employee = employee;
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

    /**
     * @return the department
     */
    public String getDepartment() {
        return employee.getOrganizationUnitID().getOrganizationUnitName();
    }

    /**
     * @return the category
     */
    public String getCategory() {
        return employee.getCategoryID().getCategoryName();
    }

    /**
     * @return the code
     */
    public String getCode() {
        return employee.getEmpCode();
    }

    /**
     * @return the surname
     */
    public String getSurname() {
        return employee.getSurName();
    }

    /**
     * @return the otherNames
     */
    public String getOtherNames() {
        return employee.getOtherNames();
    }
    
}
