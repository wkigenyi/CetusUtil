/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.util;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import systems.tech247.hr.Employees;


/**
 *
 * @author WKigenyi
 */
public class EMP implements Transferable {

   

    /**
     * @return the EM_DATA
     */
    public static DataFlavor getEM_DATA() {
        return EM_DATA;
    }

    /**
     * @param aEM_DATA the EM_DATA to set
     */
    public static void setEM_DATA(DataFlavor aEM_DATA) {
        EM_DATA = aEM_DATA;
    }
    
    private Employees em;
    private String fName;
    private String birthDay;
    private String lName;
    private String eAddress;
    private String employeeId;
    private Boolean validEmail;
    private String contractEnd;
    
    
    private static DataFlavor EM_DATA = new DataFlavor(EMP.class, "EMPTR");
    
    public EMP(Employees em){
        this.em=em;
    }
    
    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[]{getEM_DATA()};
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the em
     */
    public Employees getEm() {
        return em;
    }

    /**
     * @param ou the em to set
     */
    public void setEm(Employees em) {
        this.em=em;
    }

    /**
     * @return the fName
     */
    public String getfName() {
        return em.getOtherNames();
    }

    /**
     * @param fName the fName to set
     */
    public void setfName(String fName) {
        this.fName = fName;
    }

    /**
     * @return the lName
     */
    public String getlName() {
        return em.getSurName();
    }

    /**
     * @param lName the lName to set
     */
    public void setlName(String lName) {
        this.lName = lName;
    }

    /**
     * @return the eAddress
     */
    public String geteAddress() {
        return em.getEMail();
    }

    /**
     * @param eAddress the eAddress to set
     */
    public void seteAddress(String eAddress) {
        this.eAddress = eAddress;
    }

    /**
     * @return the employeeID
     */
    public String getemployeeId() {
        
        
        return em.getEmpCode();
    }

    /**
     * @param employeeId the userId to set
     */
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * @return the validEmail
     */
    public Boolean getValidEmail() {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        Pattern p = Pattern.compile(ePattern);
        Matcher m = p.matcher(geteAddress());
        return m.matches();
    }

    /**
     * @param validEmail the validEmail to set
     */
    public void setValidEmail(Boolean validEmail) {
        this.validEmail = validEmail;
    }

    /**
     * @return the department
     */
    public String getDepartment() {
        return em.getOrganizationUnitID().getOrganizationUnitName();
    }

    /**
     * @return the employeeId
     */
    public String getEmployeeId() {
        return employeeId;
    }

    /**
     * @return the contractEnd
     */
    public String getContractEnd() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        return formatter.format(em.getEmploymentValidThro());
    }

    /**
     * @param contractEnd the contractEnd to set
     */
    public void setContractEnd(String contractEnd) {
        this.contractEnd = contractEnd;
    }

    /**
     * @return the birthDay
     */
    public String getBirthDay() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        return formatter.format(em.getDateOfBirth());
    }

    /**
     * @param birthDay the birthDay to set
     */
    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    
    

    

    
    

   
    
}
