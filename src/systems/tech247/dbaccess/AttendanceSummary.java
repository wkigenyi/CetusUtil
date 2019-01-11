/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.dbaccess;

import java.util.Date;

/**
 *
 * @author Wilfred
 */
public class AttendanceSummary {
    
    private int employeeID;
    private String empCode;
    private String surName;
    private String dept;
    private String position;
    private String category;
    private int daysinPeriods;
    private int absentDays;
    private double basicPay;
    private String otherName;
    private Date joined;

    public AttendanceSummary(int employeeID, Date joined, String empCode, String surName, String otherName, String dept, String position, String category, int daysinPeriods, int absentDays, double basicPay) {
        this.employeeID = employeeID;
        this.empCode = empCode;
        this.surName = surName;
        this.dept = dept;
        this.position = position;
        this.category = category;
        this.daysinPeriods = daysinPeriods;
        this.absentDays = absentDays;
        this.basicPay = basicPay;
        this.otherName = otherName;
        this.joined = joined;
    }

    /**
     * @return the employeeID
     */
    public int getEmployeeID() {
        return employeeID;
    }

    /**
     * @param employeeID the employeeID to set
     */
    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    /**
     * @return the empCode
     */
    public String getEmpCode() {
        return empCode;
    }

    /**
     * @param empCode the empCode to set
     */
    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }

    /**
     * @return the surName
     */
    public String getSurName() {
        return surName;
    }

    /**
     * @param surName the surName to set
     */
    public void setSurName(String surName) {
        this.surName = surName;
    }

    /**
     * @return the dept
     */
    public String getDept() {
        return dept;
    }

    /**
     * @param dept the dept to set
     */
    public void setDept(String dept) {
        this.dept = dept;
    }

    /**
     * @return the position
     */
    public String getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(String position) {
        this.position = position;
    }

    /**
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * @return the daysinPeriods
     */
    public int getDaysinPeriods() {
        return daysinPeriods;
    }

    /**
     * @param daysinPeriods the daysinPeriods to set
     */
    public void setDaysinPeriods(int daysinPeriods) {
        this.daysinPeriods = daysinPeriods;
    }

    /**
     * @return the absentDays
     */
    public int getAbsentDays() {
        return absentDays;
    }

    /**
     * @param absentDays the absentDays to set
     */
    public void setAbsentDays(int absentDays) {
        this.absentDays = absentDays;
    }

    /**
     * @return the basicPay
     */
    public double getBasicPay() {
        return basicPay;
    }

    /**
     * @param basicPay the basicPay to set
     */
    public void setBasicPay(double basicPay) {
        this.basicPay = basicPay;
    }

    /**
     * @return the otherName
     */
    public String getOtherName() {
        return otherName;
    }

    /**
     * @param otherName the otherName to set
     */
    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }

    /**
     * @return the joined
     */
    public Date getJoined() {
        return joined;
    }

    /**
     * @param joined the joined to set
     */
    public void setJoined(Date joined) {
        this.joined = joined;
    }
    
    
    
    
}
