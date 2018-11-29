/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.dbaccess;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.netbeans.api.options.OptionsDisplayer;

import org.openide.awt.StatusDisplayer;
import org.openide.util.NbPreferences;
import systems.tech247.hr.Awards;
import systems.tech247.hr.Banks;
import systems.tech247.hr.CSSSCategories;
import systems.tech247.hr.Checkinout;
import systems.tech247.hr.CompanyAssetIssue;
import systems.tech247.hr.CompanyAssets;
import systems.tech247.hr.CompanyDetails;
import systems.tech247.hr.Contacts;
import systems.tech247.hr.Contracts;
import systems.tech247.hr.Countries;
import systems.tech247.hr.Ctypes;
import systems.tech247.hr.Currencies;
import systems.tech247.hr.Edu;
import systems.tech247.hr.EmployeeBankAccounts;
import systems.tech247.hr.EmployeeCategories;

import systems.tech247.hr.Employees;
import systems.tech247.hr.Employment;
import systems.tech247.hr.Family;
import systems.tech247.hr.HrsAuditTrail;
import systems.tech247.hr.HrsGroupPolicies;
import systems.tech247.hr.HrsGroups;
import systems.tech247.hr.HrsHRModules;
import systems.tech247.hr.HrsLoginHistory;
import systems.tech247.hr.HrsPasswordHistory;
import systems.tech247.hr.HrsPolicies;
import systems.tech247.hr.HrsPolicyCategories;
import systems.tech247.hr.HrsSecurityOptions;
import systems.tech247.hr.HrsUsers;
import systems.tech247.hr.JDCategories;
import systems.tech247.hr.JobPositionJDs;
import systems.tech247.hr.JobPositions;
import systems.tech247.hr.Kin;
import systems.tech247.hr.Locations;
import systems.tech247.hr.LvwLeave;
import systems.tech247.hr.LvwLeaveApplication;
import systems.tech247.hr.Nationalities;
import systems.tech247.hr.OrganizationUnitTypes;
import systems.tech247.hr.OrganizationUnits;
import systems.tech247.hr.PdContractTypes;
import systems.tech247.hr.PrlMonthlyLocalTaxTable;
import systems.tech247.hr.PrlMonthlyLocalTaxTableTiers;
import systems.tech247.hr.PrlMonthlyPAYETable;
import systems.tech247.hr.PrlMonthlyPAYETableTiers;
import systems.tech247.hr.Prof;
import systems.tech247.hr.PtmHolidays;
import systems.tech247.hr.PtmOutstationVisits;
import systems.tech247.hr.PtmShiftSchedule;
import systems.tech247.hr.PtmShifts;
import systems.tech247.hr.Ref;
import systems.tech247.hr.Religions;
import systems.tech247.hr.TblCostCenter;
import systems.tech247.hr.TblEmployeePayrollCode;
import systems.tech247.hr.TblEmployeePeriodDetails;
import systems.tech247.hr.TblEmployeeTransactions;
import systems.tech247.hr.TblEmployeeType;
import systems.tech247.hr.TblNSSF;
import systems.tech247.hr.TblPaypoint;
import systems.tech247.hr.TblPayroll;
import systems.tech247.hr.TblPayrollCode;
import systems.tech247.hr.TblPayrollCodeGroups;
import systems.tech247.hr.TblPeriodTransactions;
import systems.tech247.hr.TblPeriods;
import systems.tech247.hr.Tribes;
import systems.tech247.hr.Userinfo;
import systems.tech247.hr.Visa;
import systems.tech247.util.CetusUTL;
import systems.tech247.util.Month;
import systems.tech247.util.NotifyUtil;

/**
 *
 * @author Admin
 */
public class DataAccess implements CurrentPeriodProvider {
    static Map properties = new HashMap();
    

    
    //Calculate the above payroll amounts
    
    static double totalDeductions;

    public double getTotalDeductions(Employees e, TblPeriods p) {
        return getStatutoryDeductions(e, p)+getOtherDeductions(e, p);
    }
    
    

    public static Double getAbsentism(Employees e,TblPeriods p) {
        return 0.0;
    }

    public static Double getLHT() {
        return 0.0;
    }
    
    

    public Double getStatutoryDeductions(Employees e, TblPeriods p) {
        Double deduction = new Double("0.0");
        
        
        //Add Paye
        deduction = deduction+getPaye(e,p);
        //Add NSSF
        deduction = deduction+getEmployeeNssf(e, p);
        //Add LHT
        deduction = deduction+getLHT();
        
        
             
                
        
        return deduction;
    }
    
    

    public static Double getBasicPay(Employees e) {
        return e.getBasicPay().doubleValue();
    }

    public static Double getEmployeeNssf(Employees e,TblPeriods p) {
        Double nssf = getNSSF().getGrossPercentage()*getGrossPay(e, p)/100;
        return nssf;
    }

    public static Double getEmployerNssf(Employees e,TblPeriods p) {
        Double employerNSSF = getEmployeeNssf(e, p)*getNSSF().getEmployerFactor();
        return employerNSSF;
    }

    public static Double getGrossPay(Employees e,TblPeriods p) {
        List<TblEmployeeTransactions> transactions = DataAccess.getEmployeeTransactions(e.getEmployeeID(),p.getPeriodYear(), covertMonthsToInt(p.getPeriodMonth()));
        
        Double gross = new Double("0.0");
        for(TblEmployeeTransactions t: transactions){
            if(t.getPayrollCodeID().getPayment()){
                gross= gross+(t.getAmount().multiply(t.getCurrencyID().getConversionRate())).doubleValue();
            }
        }
        
        //Add Basic
        gross = gross+(e.getBasicPay().multiply(e.getCurrencyID().getConversionRate())).doubleValue();
        //Housing
        gross = gross+ (e.getHouseAllowance().multiply(e.getCurrencyID().getConversionRate())).doubleValue();
        
        BigDecimal bigGross = new BigDecimal(gross).setScale(4, RoundingMode.HALF_EVEN);
        
        BigDecimal taxablePay = bigGross;

        
        
        
        
        
        return taxablePay.doubleValue();
    }

    public static Double getHousing(Employees e) {
        return e.getHouseAllowance().doubleValue();
    }

    public Double getNetPay(Employees e, TblPeriods p) {
        return getGrossPay(e, p)-getStatutoryDeductions(e, p)-getOtherDeductions(e, p);
    }

    public static Double getOtherDeductions(Employees e,TblPeriods p) {
        List<TblEmployeeTransactions> transactions = DataAccess.getEmployeeTransactions(e.getEmployeeID(),p.getPeriodYear(), covertMonthsToInt(p.getPeriodMonth()));
        
        Double deduction = new Double("0.0");
        for(TblEmployeeTransactions t: transactions){
            if(t.getPayrollCodeID().getDeduction()){
                deduction= deduction+(t.getAmount().multiply(t.getCurrencyID().getConversionRate())).doubleValue();
            }
        }
        
        
        return deduction;
    }

    public  Double getPaye(Employees e, TblPeriods p) {
        return getPaye(getGrossPay(e, p)).doubleValue();
    }
    
    
    static ArrayList<Employees> selectedEmployees = new ArrayList<>();
    
    
    public static EntityManager entityManager= getEntityManager();
    
   
    
    public static EntityManager getEntityManager(){
            String connectionUrl = "jdbc:sqlserver://"+
                NbPreferences.forModule(DatabaseSettingsPanel.class).get("DBServerName", "")+":"+
                NbPreferences.forModule(DatabaseSettingsPanel.class).get("DBPort", "")+";databaseName="+
                NbPreferences.forModule(DatabaseSettingsPanel.class).get("DBName", "")+";user="+
                NbPreferences.forModule(DatabaseSettingsPanel.class).get("DBLogin", "")+";password="+
                NbPreferences.forModule(DatabaseSettingsPanel.class).get("DBPassword", "");
        
                Connection con =  null;
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection(connectionUrl);
            Properties p=con.getClientInfo();
           
        }catch(Exception ex){
            OptionsDisplayer.getDefault().open();
          
        }
                      
            connectionUrl = "jdbc:sqlserver://"+
                NbPreferences.forModule(DatabaseSettingsPanel.class).get("DBServerName", "")+":"+
                NbPreferences.forModule(DatabaseSettingsPanel.class).get("DBPort", "")+";databaseName="+
                NbPreferences.forModule(DatabaseSettingsPanel.class).get("DBName", "");
                
             
            properties.put("javax.persistence.jdbc.url",connectionUrl);//Set the url
            properties.put("javax.persistence.jdbc.user", NbPreferences.forModule(DatabaseSettingsPanel.class).get("DBLogin", ""));//set the db user
            properties.put("javax.persistence.jdbc.password", NbPreferences.forModule(DatabaseSettingsPanel.class).get("DBPassword", ""));
            //properties.put("javax.persistence.jdbc.driver", "com.microsoft.sqlserver.jdbc.SQLServerDriver"); it is the same for live and demo
            //StatusDisplayer.getDefault().setStatusText("URL: "+ connectionUrl);
            
        
        //Subsitute the default properties with defined options
        try{
         entityManager = Persistence.createEntityManagerFactory("HRLibPU",properties).createEntityManager();
        
         return entityManager;
        }catch(Exception ex){
         //DialogDisplayer.getDefault().notify(new NotifyDescriptor.Message(ex));
         return null;
        }
    }
    
    public static double getDaysTaken(String sql){
        Query query = getEntityManager().createNativeQuery(sql);
        double days = 0;
        try{
            days = (Double)query.getSingleResult();
        }catch(Exception ex){
            ex.printStackTrace();
        }
       
        return days;
    }
    
    
    public static List<Employees> searchEmployees(String search){
        TypedQuery<Employees> query = getEntityManager().createQuery(search,Employees.class);
        return query.getResultList();
        
    }
    public static List<Contacts> searchEmployeeContacts(Employees emp){
        String sql = "SELECT * FROM Contacts WHERE EmployeeID = ?";
        Query query = entityManager.createNativeQuery(sql,Contacts.class);
        query.setParameter(1, emp.getEmployeeID());
        return query.getResultList();
    }
    
    public static List<Employees> searchBabyThisMonth(){
        Calendar cal =  Calendar.getInstance();
        cal.setTime(new Date());
        int month = cal.get(Calendar.MONTH)+1;
        Query query = getEntityManager().createNativeQuery("select * FROM Employees WHERE month(DateOfBirth)="+month+" AND isDisengaged=0",Employees.class);
        return query.getResultList();
        
    }
    
    public static List<Employees> searchEmployeesWithoutBDay(){

        Query query = getEntityManager().createNativeQuery("select * FROM Employees WHERE isDisengaged=0 AND DateOfBirth IS NULL",Employees.class);
        return query.getResultList();
        
    }
    
    
    
    public static List<CSSSCategories> searchDefaultCategories(){
        TypedQuery<CSSSCategories> query = getEntityManager().createQuery("SELECT c  FROM CSSSCategories c",CSSSCategories.class);
        return query.getResultList();
        
    }
    
    public static List<Employees> searchEmployeesByDepartment(int departmentID){
        Query query = getEntityManager().createNativeQuery("SELECT * FROM Employees WHERE OrganizationUnitID="+departmentID+" AND isDisengaged=0",Employees.class);
        return query.getResultList();
        
    }
    
    
    
    
    public static void runSql(String sql){
        entityManager.getTransaction().begin();
        Query query =  entityManager.createNativeQuery(sql);
        query.executeUpdate();
        entityManager.getTransaction().commit();
        //System.out.print(sql);
    }
    
    
    
    public static void updateSelectedEmployee(Employees e, Boolean add){
        if(add){
            if(!selectedEmployees.contains(e))
            selectedEmployees.add(e);
        }else{
            selectedEmployees.remove(e);
        }
        StatusDisplayer.getDefault().setStatusText(selectedEmployees.size()+" Employees Selected");
    }
    
    public static void updateSelectedEmployee(EmployeeCategories cat, Boolean add){
        for(Employees emp : cat.getEmployeesCollection()){
            if(add){
                if(!selectedEmployees.contains(emp))
                    selectedEmployees.add(emp);
            }else{
                selectedEmployees.remove(emp);
            }
        }
        
        StatusDisplayer.getDefault().setStatusText(selectedEmployees.size()+" Employees Selected");
    }
    
    public static List<Employees> getSelectedEmployees(){
        return selectedEmployees;
    }
    
    public static void resetSelectedEmployees(){
        selectedEmployees.removeAll(selectedEmployees);
    }
    
    public static void selectAllEmployees(){
        String sql = "SELECT e FROM Employees e WHERE e.isDisengaged = 0";
        selectedEmployees.addAll(searchEmployees(sql));
    }
    
    public static Employees getEmployeeByClockinID(String clockinID){
        //StatusDisplayer.getDefault().setStatusText(clockinID);
        TypedQuery<Employees> query = getEntityManager().createQuery("SELECT e FROM Employees e WHERE e.empCode = :iCardNo", Employees.class);
        query.setParameter("iCardNo", clockinID);
        try{
            return query.getResultList().get(0);
        }catch(Exception ex){
            System.out.println(clockinID+" is not in DB");
            return null;
        }    
    }
    
    public static List<Checkinout> getClockin(Employees e,Date startDate, Date endDate){
        Userinfo user = getClockinID(e);
        
        Query query = getEntityManager().createQuery("SELECT c FROM Checkinout c WHERE c.checkinoutPK.userid = :userid AND c.checkinoutPK.checktime>=:start AND c.checkinoutPK.checktime<=:end",Checkinout.class);
        query.setParameter("userid", user.getUserid());
        query.setParameter("start", startDate);
        query.setParameter("end", endDate);
        System.out.println(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(startDate));
        return query.getResultList();
        
    }
    
    public static List<Date> getCompDays(Employees e, Date startDate, Date endDate){
        String sql="SELECT DISTINCT ShiftDate FROM [vwPtmCompensationDays] WHERE EmployeeID = ? AND ShiftDate>=? and ShiftDate<= ?";
        Query query = getEntityManager().createNativeQuery(sql);
        query.setParameter(1, e.getEmployeeID());
        query.setParameter(2, startDate);
        query.setParameter(3, endDate);
        
        return query.getResultList();
        
    }
    
    public static List<Date> getAbssDays(Employees e, Date startDate, Date endDate){
        String sql="SELECT DISTINCT ShiftDate FROM [vwPtmNormalAttendance] WHERE EmployeeID = ? AND ShiftDate>=? and ShiftDate<= ? AND CHECKTIME IS NULL";
        Query query = getEntityManager().createNativeQuery(sql);
        query.setParameter(1, e.getEmployeeID());
        query.setParameter(2, startDate);
        query.setParameter(3, endDate);
        
        return query.getResultList();
        
    }
    
    
    
    
    
    
    
    public List<Contacts> searchContacts(Employees emp){
        String sql = "SELECT * FROM contacts r WHERE r.employeeid = "+emp.getEmployeeID()+ "";
        Query query = getEntityManager().createNativeQuery(sql,Contacts.class);
        return query.getResultList();
        
    }
    
    public List<Contracts> searchContracts(Employees emp){
        String sql = "SELECT * FROM contracts r WHERE r.employee_id = "+emp.getEmployeeID()+ "";
        Query query = getEntityManager().createNativeQuery(sql,Contracts.class);
        return query.getResultList();
        
    }
    
    public  static List<Contracts> searchExpiringContracts(){
        String sql = "SELECT * FROM Contracts WHERE CTo<=?";
        Query query = getEntityManager().createNativeQuery(sql,Contracts.class);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 1);
        query.setParameter(1, cal.getTime());
        return query.getResultList();
        
    }
    
    public  static List<Visa> searchExpiringVisas(){
        String sql = "SELECT * FROM Visa WHERE CTo<=?";
        Query query = getEntityManager().createNativeQuery(sql,Visa.class);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 1);
        query.setParameter(1, cal.getTime());
        return query.getResultList();
        
    }
    
    
    
    public List<CompanyAssetIssue> searchAssetsIssued(Employees emp){
        String sql = "SELECT * FROM CompanyAssetIssue r WHERE r.employee_id = "+emp.getEmployeeID()+ "";
        Query query = getEntityManager().createNativeQuery(sql,CompanyAssetIssue.class);
        return query.getResultList();
        
    }
    
    public List<Visa> searchVisa(Employees emp){
        String sql = "SELECT * FROM Visa r WHERE r.employee_id = "+emp.getEmployeeID()+ "";
        Query query = getEntityManager().createNativeQuery(sql,Visa.class);
        return query.getResultList();
        
    }
    
    public List<EmployeeBankAccounts> searchBankAccounts(Employees emp){
        String sql = "SELECT * FROM EmployeeBankAccounts r WHERE r.employeeid = "+emp.getEmployeeID()+ "";
        Query query = getEntityManager().createNativeQuery(sql,EmployeeBankAccounts.class);
        return query.getResultList();
        
    }
    
    public List<Awards> searchAwards(Employees emp){
        String sql = "SELECT * FROM Awards r WHERE r.employee_id = "+emp.getEmployeeID()+ "";
        Query query = getEntityManager().createNativeQuery(sql,Awards.class);
        return query.getResultList();
        
    }
    
    public List<Employment> searchEmployment(Employees emp){
        String sql = "SELECT * FROM Employment r WHERE r.employee_id = "+emp.getEmployeeID()+ "";
        Query query = getEntityManager().createNativeQuery(sql,Employment.class);
        return query.getResultList();
        
    }
    
    public List<Family> searchFamily(Employees emp){
        String sql = "SELECT * FROM family r WHERE r.employee_id = "+emp.getEmployeeID()+ "";
        Query query = getEntityManager().createNativeQuery(sql,Family.class);
        return query.getResultList();
        
    }
    
    public List<Kin> searchKin(Employees emp){
        String sql = "SELECT * FROM Kin r WHERE r.employee_id = "+emp.getEmployeeID()+ "";
        Query query = getEntityManager().createNativeQuery(sql,Kin.class);
        return query.getResultList();
        
    }
    
    public List<PtmOutstationVisits> searchOutSS(Employees emp){
        String sql = "SELECT * FROM Kin r WHERE r.EmployeeID = "+emp.getEmployeeID()+ "";
        Query query = getEntityManager().createNativeQuery(sql,Kin.class);
        return query.getResultList();
        
    }
    
    public List<Ref> searchReferees(Employees emp){
        String sql = "SELECT * FROM ref r WHERE r.employee_id = "+emp.getEmployeeID()+ "";
        Query query = getEntityManager().createNativeQuery(sql,Ref.class);
        return query.getResultList();
        
    }
    
    public List<Edu> searchEducation(Employees emp){
        String sql = "SELECT * FROM Edu r WHERE r.employee_id = "+emp.getEmployeeID()+ "";
        Query query = getEntityManager().createNativeQuery(sql,Edu.class);
        return query.getResultList();
        
    }
    
    public List<Prof> searchProf(Employees emp){
        String sql = "SELECT * FROM Prof r WHERE r.employee_id = "+emp.getEmployeeID()+ "";
        Query query = getEntityManager().createNativeQuery(sql,Prof.class);
        return query.getResultList();
        
    }
    

    
    
    
    
    public List<Currencies> searchCurrencies(String search){
        TypedQuery<Currencies> query = getEntityManager().createQuery(search,Currencies.class);
        return query.getResultList();
        
    }
    
    public static Currencies getBaseCurrency(){
        String sql = "SELECT c from Currencies c WHERE c.isBaseCurrency = 1";
        TypedQuery<Currencies> query = getEntityManager().createQuery(sql,Currencies.class);
        return query.getResultList().get(0);
        
    }
    
    public List<PdContractTypes> searchContractTypes(String search){
        TypedQuery<PdContractTypes> query = getEntityManager().createQuery(search,PdContractTypes.class);
        return query.getResultList();
        
    }
    
    public List<CompanyDetails> searchCompanyDetails(String search){
        TypedQuery<CompanyDetails> query = getEntityManager().createQuery(search,CompanyDetails.class);
        return query.getResultList();
        
    }
    
    public static CompanyDetails getDefaultCompany(){
        TypedQuery<CompanyDetails> query =  getEntityManager().createQuery("SELECT c FROM CompanyDetails c",CompanyDetails.class);
        return query.getSingleResult();
    }
    
    public List<TblPaypoint> searchPayPoint(){
        TypedQuery<TblPaypoint> query = getEntityManager().createQuery("SELECT p FROM TblPaypoint p",TblPaypoint.class);
        return query.getResultList();
        
    }
    
    
    
    public List<Countries> searchCountries(String search){
        
        TypedQuery<Countries> query = getEntityManager().createQuery(search,Countries.class);
        return query.getResultList();
        
    }
    
    public List<CompanyAssets> searchCompanyAssets(String search){
        Query query = getEntityManager().createNativeQuery(search,CompanyAssets.class);
        return query.getResultList();
    }
    
    public List<Tribes> searchTribes(String search){
        TypedQuery<Tribes> query = getEntityManager().createQuery(search,Tribes.class);
        return query.getResultList();
        
    }
    
    public List<JobPositions> searchJobPositions(String search){
        TypedQuery<JobPositions> query = getEntityManager().createQuery(search,JobPositions.class);
        return query.getResultList();
        
    }
    
    public List<JDCategories> searchJDCategories(String search){
        
        TypedQuery<JDCategories> query = getEntityManager().createQuery(search,JDCategories.class);
        return query.getResultList();
        
    }
    
    public static List<HrsPolicyCategories> getHRPolicyCategoriesByModuleID(int moduleID,int groupID){
        TypedQuery<HrsPolicyCategories> query = getEntityManager().createNamedQuery("HrsPolicyCategories.findByModuleID", HrsPolicyCategories.class);
        query.setParameter("moduleID", moduleID );
        
        return query.getResultList();
    }
    
    public static void updateLoginModule(int userID,int moduleID){
      String sqlString = 
                "UPDATE hrsLoginHistory " +
                "SET ModuleID = "+moduleID+"" +
                "WHERE LoginHistoryID = (SELECT TOP 1 LoginHistoryID FROM hrsLoginHistory WHERE UserID='"+userID+"' ORDER BY LoginHistoryID DESC )";
        
                
                    Query q = entityManager.createNativeQuery(sqlString);
                    entityManager.getTransaction().begin();
                    q.executeUpdate();
                    entityManager.getTransaction().commit();
    }
    
    
    
    
    
    
    public List<JobPositionJDs> searchJDsForJobAndCategory(JobPositions position,int categoryID){
        String sql = "SELECT * FROM JobPositionJDs r WHERE r.PositionID = "+position.getPositionID()+ " AND r.JDCategoryID =  "+categoryID+"";
        Query query = getEntityManager().createNativeQuery(sql,JobPositionJDs.class);
        return query.getResultList();
        
    }
    
    public List<Integer> searchJDCategoriesForJob(JobPositions position){
        String sql = "SELECT DISTINCT JDCategoryID FROM JobPositionJDs r WHERE r.PositionID = "+position.getPositionID()+ "";
        Query query = getEntityManager().createNativeQuery(sql);
        //StatusDisplayer.getDefault().setStatusText("SQL: "+ sql+ " Categories: "+ query.getResultList().size());
        return query.getResultList();
        
    }
    
    public List<JobPositionJDs> searchJDs(String search){
        
        TypedQuery<JobPositionJDs> query = getEntityManager().createQuery(search,JobPositionJDs.class);
        return query.getResultList();
        
    }
    
    public List<Banks> searchBanks(String search){
        TypedQuery<Banks> query = getEntityManager().createQuery(search,Banks.class);
        return query.getResultList();
        
    }
    
    public static List<OrganizationUnits> searchDepartments(String search){
        TypedQuery<OrganizationUnits> query = getEntityManager().createQuery(search,OrganizationUnits.class);
        
        return query.getResultList();
        
    }
    
    public static List<LvwLeave> searchLeave(String search){
        
        TypedQuery<LvwLeave> query = getEntityManager().createQuery(search,LvwLeave.class);
        
        
        
        return query.getResultList();
        
    }
    
    public static List<EmployeeCategories> searchCategories(String search){
        TypedQuery<EmployeeCategories> query = getEntityManager().createQuery("SELECT e FROM EmployeeCategories e",EmployeeCategories.class);
        //StatusDisplayer.getDefault().setStatusText(search);
        return query.getResultList();
        
    }
    
    
    
    public static List<JobPositions> searchPositions(String search){
        TypedQuery<JobPositions> query = getEntityManager().createQuery(search,JobPositions.class);
        return query.getResultList();
        
    }
    
    public static List<TblEmployeeType> searchEmployeeTypes(String search){
        TypedQuery<TblEmployeeType> query = getEntityManager().createQuery(search,TblEmployeeType.class);
        return query.getResultList();
        
    }
    
    public static List<LvwLeaveApplication> getEmployeeLeaveApplications(Employees emp){
        Query query = getEntityManager().createNativeQuery("SELECT * FROM LvwLeaveApplication WHERE EmployeeID = "+emp.getEmployeeID()+"",LvwLeaveApplication.class);
        return query.getResultList();
    }
    
    public static LvwLeave getLeaveByID(int ID){
        Query query =  getEntityManager().createNativeQuery("SELECT * FROM LvwLeave WHERE LeaveID ="+ID+"",LvwLeave.class);
        return (LvwLeave)query.getResultList().get(0);
        
    }
    
    
    
    
    public List<Religions> searchReligions(String search){
        TypedQuery<Religions> query = getEntityManager().createQuery(search,Religions.class);
        return query.getResultList();
        
    }
    
    public List<Ctypes> searchCtypes(String search){
        TypedQuery<Ctypes> query = getEntityManager().createQuery(search,Ctypes.class);
        return query.getResultList();
        
    }
    
    public List<OrganizationUnitTypes> searchOuTypes(String search){
        TypedQuery<OrganizationUnitTypes> query = getEntityManager().createQuery(search,OrganizationUnitTypes.class);
        return query.getResultList();
        
    }
    
    public List<Locations> searchLocations(String search){
        TypedQuery<Locations> query = getEntityManager().createQuery(search,Locations.class);
        return query.getResultList();
        
    }
    
    public List<Nationalities> searchNations(String search){
        TypedQuery<Nationalities> query = getEntityManager().createQuery(search,Nationalities.class);
        return query.getResultList();
        
    }
    
    public List<Integer> getTransYears(int empID){
        Query query = getEntityManager().createNativeQuery(
                "SELECT DISTINCT p.Period_Year y FROM tblPeriodTransactions p "+
                "WHERE Employee_ID = "+empID+" ORDER BY y DESC");
        
        
        List<Integer> resultList = query.getResultList();
        return resultList;
    }
    
    public List<Integer> getTransMonths(int empID, int yr){
       Query query = getEntityManager().createNativeQuery("SELECT DISTINCT p.Period_Month m FROM tblPeriodTransactions p\n" +
"WHERE Employee_ID = " + empID +
" AND p.Period_Year = " + yr +
" ORDER BY m DESC");
        
        
        List<Integer> resultList = query.getResultList();
        
        //StatusDisplayer.getDefault().setStatusText("Employee ID: " +empID +" Year: "+ yr+ "Results: "+resultList.size());
        
        return resultList;
    }
    
    public List<TblPeriodTransactions> getTransactions(int empID, int yr,int month){

        String s = "SELECT * FROM tblPeriodTransactions p\n" +
        "WHERE Employee_ID = " +empID +
        " AND p.Period_Year = " +yr+
        " AND p.Period_Month = " +month+
        " ORDER BY Group_Order";
        
        
        Query query = getEntityManager().createNativeQuery(s,TblPeriodTransactions.class);
        
        
        
        
        
        
        return  query.getResultList();
        
        
        
    }
    
    public  List<TblPeriods> getPayrollPeriodsByEmp(Employees emp){
       Query query = getEntityManager().createQuery("SELECT * FROM tblPeriods p WHERE p.periodYear IN ("+getTransYears(emp.getEmployeeID())+") ");
        
        
        List<TblPeriods> resultList = query.getResultList();
        
        //StatusDisplayer.getDefault().setStatusText("Employee ID: " +empID +" Year: "+ yr+ "Results: "+resultList.size());
        
        return resultList;
    }
    
    public static List<TblPeriods> getPayrollPeriods(){
       Query query = getEntityManager().createQuery("SELECT t FROM TblPeriods t ORDER BY t.openDate DESC",TblPeriods.class);
        
        
        List<TblPeriods> resultList = query.getResultList();
        
        //StatusDisplayer.getDefault().setStatusText("Employee ID: " +empID +" Year: "+ yr+ "Results: "+resultList.size());
        
        return resultList;
    }
    
    public List<Month> getPayrollPeriodsByEmp(int empID){
        List<Month> list = new ArrayList<>();
        for(int yr : getTransYears(empID)){
            for(int month : getTransMonths(empID, yr)){
                Month m = new Month(yr, month);
                list.add(m);
            }
        }
        
        return list;
    }
    
    public List<TblPayrollCode> searchPayrollCode(String sql){
        
        
        Query query = getEntityManager().createNativeQuery(sql,TblPayrollCode.class);
        
        return query.getResultList();
        
    }
    
    
    
    public PrlMonthlyPAYETable searchPayePeriods(int yr){
        
        
        TypedQuery<PrlMonthlyPAYETable> query = getEntityManager().createQuery("SELECT p FROM PrlMonthlyPAYETable p WHERE p.periodYear = '"+yr+"'",PrlMonthlyPAYETable.class);
        
        return query.getSingleResult();
        
    }
    
    
    
    
    
    public static List<PrlMonthlyPAYETableTiers> searchPayeTiers(int yr){
        
        TypedQuery<PrlMonthlyPAYETable> queryPeriods = getEntityManager().createQuery("SELECT p FROM PrlMonthlyPAYETable p WHERE p.periodYear = '"+yr+"'",PrlMonthlyPAYETable.class);
        
        
        Query query = getEntityManager().createQuery("SELECT p FROM PrlMonthlyPAYETableTiers p WHERE p.monthlyPAYETableID = :tableID");
        
       
        if(queryPeriods.getResultList().size()==1){
        
        query.setParameter("tableID", queryPeriods.getSingleResult());
       
        return query.getResultList();
        }else{
            return new ArrayList<>();
        }
        
    }
    
    public static List<PrlMonthlyLocalTaxTableTiers> searchLHTTiers(int yr){
        try{
        TypedQuery<PrlMonthlyLocalTaxTable> queryTable = getEntityManager().createQuery("SELECT p FROM PrlMonthlyLocalTaxTable p WHERE p.periodYear = :periodYear",PrlMonthlyLocalTaxTable.class);
        queryTable.setParameter("periodYear", yr);
        PrlMonthlyLocalTaxTable table = queryTable.getSingleResult();
        
        Query queryTiers = getEntityManager().createNativeQuery("SELECT * FROM PrlMonthlyLocalTaxTableTiers p WHERE p.monthlyLocalTaxTableID = ? ORDER BY TierLevel ASC",PrlMonthlyLocalTaxTableTiers.class);
        queryTiers.setParameter(1, table.getMonthlyLocalTaxTableID());
            return queryTiers.getResultList();
        }catch(Exception ex){
            
            return new ArrayList<>();
        }
        
    }
    
    public static int getLastestLHTTable(){
        String sql = "SELECT MAX(MonthlyLocalTaxTableID) FROM prlMonthlyLocalTaxTableTiers";
        Query query = entityManager.createNativeQuery(sql);
        try{
            return (int)query.getSingleResult();
        }catch(Exception ex){
            return 0;
        }
    }
    
    public static int getLastestPAYETable(){
        String sql = "SELECT MAX(MonthlyPAYETableID) FROM prlMonthlyPAYETableTiers";
        Query query = entityManager.createNativeQuery(sql);
        try{
            return (int)query.getSingleResult();
        }catch(Exception ex){
            return 0;
        }
    }
    
    
    
    
    public List<TblEmployeePeriodDetails> searchPayslips(Employees emp){
        
        
        Query query = getEntityManager().createNativeQuery(
                "SELECT * FROM TblEmployeePeriodDetails t "
                        + "AND t.Employee_ID = ?");
        query.setParameter(1, emp.getEmployeeID() );
        
        return query.getResultList();
        
    }
    
    
    
    public List<TblPayroll> searchPayroll(){
        
        
        Query query = getEntityManager().createQuery("SELECT p from TblPayroll p");
        
        return query.getResultList();
        
    }
    
    public static List<TblEmployeeTransactions> getEmployeeTransactions(int empID, int yr,int month){

        String s = "SELECT * FROM TblEmployeeTransactions p\n" +
        "WHERE p.Employee_ID = " +empID +
        " AND p.Period_Year = " +yr+
        " AND p.Period_Month = " +month+"";
        
        
        Query query = getEntityManager().createNativeQuery(s,TblEmployeeTransactions.class);
        
        
        
        
        
        
        return  query.getResultList();
        
        
        
    }
    
        public static List<TblEmployeePayrollCode> getEmployeeTransactionCodes(Employees emp){

        String s = "SELECT * FROM TblEmployeePayrollCode p\n" +
        "WHERE p.Employee_ID = " +emp.getEmployeeID()+"";
        
        
        Query query = getEntityManager().createNativeQuery(s,TblEmployeePayrollCode.class);
        
        
        
        
        
        
        return  query.getResultList();
        
        
        
    }
    
    
    
    
    
    public List<TblCostCenter> getCostCenters(){

        String s = "SELECT t FROM TblCostCenter t";
        
        
        Query query = getEntityManager().createQuery(s,TblCostCenter.class);
        
        
        
        
        
        
        return  query.getResultList();
        
        
        
    }
    
    public static TblNSSF getNSSF(){

        String s = "SELECT t FROM TblNSSF t";
        
        
        TypedQuery<TblNSSF> query = getEntityManager().createQuery(s,TblNSSF.class);
        
        
        
        
        
        
        return  query.getSingleResult();
        
        
        
    }
    
    public static HrsSecurityOptions getSecurityOptions(){
        TypedQuery<HrsSecurityOptions> query = getEntityManager().createNamedQuery("HrsSecurityOptions.findAll", HrsSecurityOptions.class);
        return query.getSingleResult();
    }
    
    public static Employees getEmployeeByID(int id){
       
        
        Employees e = getEntityManager().find(Employees.class, id);
        
        if(null==e){
         e= new Employees();
         e.setSurName("Set");
         e.setOtherNames("Not");
        }
        
        return e;
    }
    
    public static List<HrsPasswordHistory> getPasswordHistory(int userid){
        HrsUsers user = getEntityManager().find(HrsUsers.class, userid);
        TypedQuery<HrsPasswordHistory> query =  getEntityManager().createQuery("SELECT h FROM HrsPasswordHistory h WHERE h.userID = :userID ",HrsPasswordHistory.class);
        query.setParameter("userID", user);
        return query.getResultList();
    }
    

    
    public static void saveLogin(int userID,String workstation){
        Date now = new Date();
        String sql = "INSERT INTO [dbo].[hrsLoginHistory]\n" +
"           ([UserID]\n" +
"           ,[LogInTime]\n" +
"           ,[LogOffTime]\n" +
"           ,[IsOnLine]\n" +
"           ,[WorkStation]\n" +
"           ,[ModuleID])\n" +
"     VALUES\n" +
"           (?,?,?,?,?,?)";
        
        
            try{    
                Query q = entityManager.createNativeQuery(sql);
                entityManager.getTransaction().begin();    
                q.setParameter(1, userID);
                q.setParameter(2, now);
                q.setParameter(4, true);
                q.setParameter(5, workstation);
                q.setParameter(6, 0);
                int i = q.executeUpdate();
                entityManager.getTransaction().commit();
            }catch(Exception ex){
                NotifyUtil.error("Login Not Saved", "There is a Problem", ex, false);
            }    
                
    }
        
        
    
    
    public static void savePassword(HrsUsers user, String password){
    //Saving the user
    try{
    HrsUsers will = entityManager.find(HrsUsers.class, user.getUserID());
    will.setPassword(password);
    will.setMustChangePwd(false);
    
    entityManager.getTransaction().begin();
    entityManager.getTransaction().commit();
    //NotifyUtil.info("Password saved", "Password Saved", false);
    }catch(Exception ex){
        NotifyUtil.error("There was a problem", "Password was not saved", ex, false);
    }
    //Saving the Password History
    
    Date now = new Date();
    short passwordNo = (short)(getPasswordHistory(user.getUserID()).size()+1);
    String sqlString = "INSERT INTO hrsPasswordHistory" +
        "           (UserID" +
        "           ,Password" +
        "           ,DateSet" +
        "           ,PasswordNo)" +
        "     VALUES" +
        "           (?,?,?,?)";
                
    try{            
        Query q = entityManager.createNativeQuery(sqlString);
        q.setParameter(1, user.getUserID());
        q.setParameter(2, password);
        q.setParameter(3, now);
        q.setParameter(4, passwordNo);    
        entityManager.getTransaction().begin();
        q.executeUpdate();
        entityManager.getTransaction().commit();
        //NotifyUtil.info("Password History saved", "History Saved", false);
    }catch(Exception ex){
        NotifyUtil.error("There was a database error", "Password History was not saved", ex, false);
                
    }
    
}
    
    public List<HrsUsers> searchUsers(String search){
        TypedQuery<HrsUsers> query = entityManager.createQuery(search,HrsUsers.class);
        return query.getResultList();
        
    }
    
    public List<HrsGroups> searchGroups(String search){
        TypedQuery<HrsGroups> query = entityManager.createQuery(search,HrsGroups.class);
        return query.getResultList();
        
    }
    
    public static List<HrsLoginHistory> getLastLogin(int userID){
        
        String sqlString = "SELECT * FROM  HrsLoginHistory WHERE UserID = '"+userID+"'";
        Query query =  entityManager.createNativeQuery(sqlString, HrsLoginHistory.class);
        return query.getResultList();
    }
    
    public static Date getLastLoginDate(int userID){
        String sql="select MAX(LogInTime) FROM hrsLoginHistory WHERE UserID=?";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, userID);
        try{
            return (Date)query.getSingleResult();
        }catch(NullPointerException ex){
            return null;
        }
    }
    
    public static  List<HrsHRModules> getHRModules(int groupID){
        if(groupID == 0){
        TypedQuery<HrsHRModules> query = entityManager.createNamedQuery("HrsHRModules.findByEnabled", HrsHRModules.class);
        query.setParameter("enabled", true);
        
        return query.getResultList();
        }else{
             Query query = entityManager.createNativeQuery("SELECT * FROM HrsHRModules h WHERE h.enabled=1 AND h.moduleID IN (SELECT DISTINCT hhm.ModuleID FROM \n" +
"hrsGroupPolicies hgp \n" +
"INNER JOIN\n" +
"hrsPolicies hp ON hgp.PolicyID=hp.PolicyID \n" +
"INNER JOIN\n" +
"hrsPolicyCategories hpc ON hp.PolicyCategoryID=hpc.PolicyCategoryID \n" +
"INNER JOIN hrsHRModules hhm ON hpc.ModuleID = hhm.ModuleID\n" +
"WHERE hgp.GroupID="+groupID+" AND hgp.PolModify=1 AND hhm.Enabled=1)", HrsHRModules.class);
        
        
        return query.getResultList();
        }
    }
    
    public static HrsUsers getUser(String username,String password){
        
        
        
        
        Query query = entityManager.createNativeQuery(
                "SELECT * FROM HrsUsers WHERE accDisabled=0 AND password='"+password+"' AND username='"+username+"'",HrsUsers.class);
        
        
        List<HrsUsers> resultList = query.getResultList();
        if(resultList.size()==1){
            return resultList.get(0);
        }else{
            return null;
        }
    }
    
        public static HrsUsers getUserByUserName(String username){
        
        
        
        
        Query query = getEntityManager().createNativeQuery(
                "SELECT * FROM HrsUsers WHERE accDisabled=0 AND username='"+username+"'",HrsUsers.class);
        
        
        List<HrsUsers> resultList = query.getResultList();
        if(resultList.size()==1){
            return resultList.get(0);
        }else{
            return null;
        }
    }
    
    public static void saveAuditTrail(HrsUsers user,String detail){
        String machineName;
        try{
            machineName = InetAddress.getLocalHost().getHostName();
        }catch(UnknownHostException ex){
            machineName = "UNKNOWN MACHINE";
        }
        String sql="INSERT INTO [dbo].[hrsAuditTrail]\n" +
"           ([UserName]\n" +
"           ,[UserDetails]\n" +
"           ,[ModuleCode]\n" +
"           ,[ModuleName]\n" +
"           ,[WorkStation]\n" +
"           ,[ActionDate]\n" +
"           ,[ActionDesc]\n" +
"           ,[UserID]\n" +
"           ,[ModuleID]\n" +
"           ,[ActionID]\n" +
"           ,[GroupID])\n" +
"     VALUES\n" +
"           (?,?,?,?,?,?,?,?,?,?,?)";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(5, machineName);
        query.setParameter(6, new Date());
        query.setParameter(7, detail);
        query.setParameter(8, user.getUserID());
        entityManager.getTransaction().begin();
        query.executeUpdate();
        entityManager.getTransaction().commit();
    
    }    
    
    
    
    
    
    
    public static List<HrsPolicies> getHRPolicyByCategoryID(int categoryID,int groupID){
        TypedQuery<HrsPolicies> query = entityManager.createNamedQuery("HrsPolicies.findByPolicyCategoryID", HrsPolicies.class);
        query.setParameter("policyCategoryID", categoryID );
        List<HrsPolicies> list = query.getResultList();
        
        return list;
    }
    
    public static void savePolicy(HrsPolicies policyID, HrsGroups groupID,boolean polModify){
        
        
        //Does the entry exist?
        TypedQuery<HrsGroupPolicies> query = entityManager.createQuery("SELECT h FROM HrsGroupPolicies h WHERE h.policyID = :policyID AND h.groupID = :groupID",HrsGroupPolicies.class);
        query.setParameter("policyID", policyID);
        query.setParameter("groupID", groupID);
        try{
        HrsGroupPolicies p = query.getSingleResult();
        HrsGroupPolicies hgp = entityManager.find(HrsGroupPolicies.class, p.getGroupPolicyID());
            
            entityManager.getTransaction().begin();
            hgp.setPolModify(polModify);
            entityManager.getTransaction().commit();
            
            
            
            
            StatusDisplayer.getDefault().setStatusText("Policy Number: "+ hgp.getGroupPolicyID());
        }catch(NoResultException ex){
            //It is a new entry, create it
            entityManager.getTransaction().begin();
            String sqlString = "INSERT INTO [dbo].[hrsGroupPolicies]\n" +
"           ([PolicyID]\n" +
"           ,[GroupID]\n" +
"           ,[PolModify])\n" +
"     VALUES\n" +
"           ("+policyID.getPolicyID()+","+groupID.getGroupID()+",'"+polModify+"')";
            
            Query qry = DataAccess.entityManager.createNativeQuery(sqlString);

            qry.executeUpdate();
            entityManager.getTransaction().commit();
            
            StatusDisplayer.getDefault().setStatusText("It is a new function to this group");
        }
        
    }
    
    public static void savePayrollTransaction(TblEmployeeTransactions t){
        
        BigDecimal convertedamount = t.getAmount().multiply(t.getCurrencyID().getConversionRate());
        try{
        entityManager.getTransaction().begin();
        }catch(Exception ex){
            
        }
        String sql= "INSERT INTO [dbo].[tblPeriodTransactions]\n" +
"           ([Employee_ID]\n" +
"           ,[Currency_ID]\n" +
"           ,[PayrollCode_ID]\n" +
"           ,[Amount]\n" +
"           ,[Conversion_Rate]\n" +
"           ,[Converted_Amount]\n" +
"           ,[Message]\n" +
"           ,[Period_Month]\n" +
"           ,[Period_Year]\n" +
"           ,[PayPoint_ID]\n" +
"           ,[Bank_Transfer]\n" +
"           ,[Cash]\n" +
"           ,[Cheque])\n" +
"     VALUES\n" +
"           ("+t.getEmployeeID().getEmployeeID()+"\n" +
"           ,"+t.getCurrencyID().getCurrencyID()+"\n" +
"           ,"+t.getPayrollCodeID().getPayrollCodeID()+"\n" +
"           ,"+t.getAmount()+"\n" +
"           ,"+t.getCurrencyID().getConversionRate()+"\n" +
"           ,"+convertedamount+"\n" +
"           ,'No Message'" +

"           ,"+t.getPeriodMonth() +"\n" +
"           ,"+t.getPeriodYear()+"\n" +
"           ,"+1+"\n" +
"           ,"+0+"\n" +
"           ,"+0+","+0+")";
        
        Query query = entityManager.createNativeQuery(sql);
        query.executeUpdate();
        entityManager.getTransaction().commit();
        //StatusDisplayer.getDefault().setStatusText("Processed For: "+e.getSurName()+" "+ e.getOtherNames());
        
    }
    
   
    public static TblPayrollCode getPayrollCode(int id){
        TypedQuery<TblPayrollCode> query = entityManager.createQuery("SELECT t FROM TblPayrollCode t WHERE t.payrollCodeID = :payrollCodeID",TblPayrollCode.class);
        query.setParameter("payrollCodeID", id);
        return query.getSingleResult();
    }
    
    
        public static void postPayrollTransaction(Employees e,TblPayrollCode code,TblPeriods p){
        //Convert amount to Base Currency
        
        
            try{
                entityManager.getTransaction().begin();
            }catch(Exception ex){
            
            }
       
            String sql= "INSERT INTO [dbo].[tblPeriodTransactions]\n" +
"           ([Employee_ID]\n" +
"           ,[Currency_ID]\n" +
"           ,[PayrollCode_ID]\n" +
"           ,[Amount]\n" +
"           ,[Period_Month]\n" +
"           ,[Period_Year]\n" +
"           ,[PayPoint_ID]\n" +
"           ,[Bank_Transfer]\n" +
"           ,[Cash]\n" +
"           ,[Cheque])\n" +
"     VALUES\n" +
"           (?,?,?,dbo.prlFnComputePayrollAmount(?,?,?,?),?,?,?,?,?,?)";
            
        
            Query query = entityManager.createNativeQuery(sql);
            
            query.setParameter(1, e.getEmployeeID());
            query.setParameter(2, e.getCurrencyID().getCurrencyID());
            query.setParameter(3, code.getPayrollCodeID());
            query.setParameter(4, e.getEmployeeID());
            query.setParameter(5, code.getPayrollCodeID());
            query.setParameter(6, covertMonthsToInt(p.getPeriodMonth()));
            query.setParameter(7, p.getPeriodYear());
            query.setParameter(8, covertMonthsToInt(p.getPeriodMonth()));
            query.setParameter(9, p.getPeriodYear());
            query.setParameter(10, 1);
            query.setParameter(11, 1);
            query.setParameter(12, 0);
            query.setParameter(13, 0);
            
            
            
            query.executeUpdate();
            //StatusDisplayer.getDefault().setStatusText("Processed For: "+e.getSurName()+" "+ e.getOtherNames());
            entityManager.getTransaction().commit();
            
        
        
    }
    
    
    
    
    
    
    
    
    
    
    
        public static void postHousing(Employees e,TblPeriods p){
        //Convert amount to Base Currency
        try{
        BigDecimal converted = e.getHouseAllowance().multiply(e.getCurrencyID().getConversionRate());
        
        try{
        entityManager.getTransaction().begin();
        }catch(Exception ex){
            
        }
        String sql= "INSERT INTO [dbo].[tblPeriodTransactions]\n" +
"           ([Employee_ID]\n" +
"           ,[Currency_ID]\n" +
"           ,[PayrollCode_ID]\n" +
"           ,[Amount]\n" +
"           ,[Conversion_Rate]\n" +
"           ,[Converted_Amount]\n" +
"           ,[Message]\n" +
"           ,[Period_Month]\n" +
"           ,[Period_Year]\n" +
"           ,[PayPoint_ID]\n" +
"           ,[Bank_Transfer]\n" +
"           ,[Cash]\n" +
"           ,[Cheque])\n" +
"     VALUES\n" +
"           ("+e.getEmployeeID()+"\n" +
"           ,"+e.getCurrencyID().getCurrencyID()+"\n" +
"           ,"+3+"\n" +
"           ,"+e.getHouseAllowance()+"\n" +
"           ,"+e.getCurrencyID().getCurrencyID()+"\n" +
"           ,"+converted+"\n" +
"           ,'No Message'" +

"           ,"+covertMonthsToInt(p.getPeriodMonth()) +"\n" +
"           ,"+p.getPeriodYear()+"\n" +
"           ,"+1+"\n" +
"           ,"+0+"\n" +
"           ,"+0+","+0+")";
        
        Query query = entityManager.createNativeQuery(sql);
        query.executeUpdate();
        //StatusDisplayer.getDefault().setStatusText("Processed For: "+e.getSurName()+" "+ e.getOtherNames());
        entityManager.getTransaction().commit();
        }catch(Exception ex){
            
        }
        
    }
        public static void postGross(Employees e,TblPeriods p){

        
        Double gross = getGrossPay(e, p);
        
        
        try{
                entityManager.getTransaction().begin();
            }catch(Exception ex){
            
            }
        
            
        
        String sql= "INSERT INTO [dbo].[tblPeriodTransactions]\n" +
"           ([Employee_ID]\n" +
"           ,[Currency_ID]\n" +
"           ,[PayrollCode_ID]\n" +
"           ,[Amount]\n" +
"           ,[Conversion_Rate]\n" +
"           ,[Converted_Amount]\n" +
"           ,[Message]\n" +
"           ,[Period_Month]\n" +
"           ,[Period_Year]\n" +
"           ,[PayPoint_ID]\n" +
"           ,[Bank_Transfer]\n" +
"           ,[Cash]\n" +
"           ,[Cheque])\n" +
"     VALUES\n" +
"           ("+e.getEmployeeID()+"\n" +
"           ,"+e.getCurrencyID().getCurrencyID()+"\n" +
"           ,"+1+"\n" +
"           ,"+gross+"\n" +
"           ,"+e.getCurrencyID().getCurrencyID()+"\n" +
"           ,"+gross+"\n" +
"           ,'No Message'" +

"           ,"+covertMonthsToInt(p.getPeriodMonth()) +"\n" +
"           ,"+p.getPeriodYear()+"\n" +
"           ,"+1+"\n" +
"           ,"+0+"\n" +
"           ,"+0+","+0+")";
        
        Query query = entityManager.createNativeQuery(sql);
        query.executeUpdate();
        //StatusDisplayer.getDefault().setStatusText("Processed For: "+e.getSurName()+" "+ e.getOtherNames());
       

        
        
        
                    //Posting TAXABLE PAYE
        
        String sql3= "INSERT INTO [dbo].[tblPeriodTransactions]\n" +
"           ([Employee_ID]\n" +
"           ,[Currency_ID]\n" +
"           ,[PayrollCode_ID]\n" +
"           ,[Amount]\n" +
"           ,[Conversion_Rate]\n" +
"           ,[Converted_Amount]\n" +
"           ,[Message]\n" +
"           ,[Period_Month]\n" +
"           ,[Period_Year]\n" +
"           ,[PayPoint_ID]\n" +
"           ,[Bank_Transfer]\n" +
"           ,[Cash]\n" +
"           ,[Cheque])\n" +
"     VALUES\n" +
"           ("+e.getEmployeeID()+"\n" +
"           ,"+e.getCurrencyID().getCurrencyID()+"\n" +
"           ,"+5+"\n" +
"           ,"+gross+"\n" +
"           ,"+e.getCurrencyID().getCurrencyID()+"\n" +
"           ,"+gross+"\n" +
"           ,'No Message'" +

"           ,"+covertMonthsToInt(p.getPeriodMonth()) +"\n" +
"           ,"+p.getPeriodYear()+"\n" +
"           ,"+1+"\n" +
"           ,"+0+"\n" +
"           ,"+0+","+0+")";
        
        Query query3 = entityManager.createNativeQuery(sql3);
        query3.executeUpdate();
        //StatusDisplayer.getDefault().setStatusText("Processed For: "+e.getSurName()+" "+ e.getOtherNames());
        entityManager.getTransaction().commit();
        
                

        
        
        
        
        
        
        
            
        
        
    }
        public void postDeduction(Employees e,TblPeriods p){
        
            
        
            Double deductions = getTotalDeductions(e, p);
      
        
        
        
        
        
                
       
        
        
        
        try{
        
        
        entityManager.getTransaction().begin();
        
            
        
        String sql= "INSERT INTO [dbo].[tblPeriodTransactions]\n" +
"           ([Employee_ID]\n" +
"           ,[Currency_ID]\n" +
"           ,[PayrollCode_ID]\n" +
"           ,[Amount]\n" +
"           ,[Conversion_Rate]\n" +
"           ,[Converted_Amount]\n" +
"           ,[Message]\n" +
"           ,[Period_Month]\n" +
"           ,[Period_Year]\n" +
"           ,[PayPoint_ID]\n" +
"           ,[Bank_Transfer]\n" +
"           ,[Cash]\n" +
"           ,[Cheque])\n" +
"     VALUES\n" +
"           ("+e.getEmployeeID()+"\n" +
"           ,"+e.getCurrencyID().getCurrencyID()+"\n" +
"           ,"+11+"\n" +
"           ,"+deductions+"\n" +
"           ,"+e.getCurrencyID().getCurrencyID()+"\n" +
"           ,"+deductions+"\n" +
"           ,'No Message'" +

"           ,"+covertMonthsToInt(p.getPeriodMonth()) +"\n" +
"           ,"+p.getPeriodYear()+"\n" +
"           ,"+1+"\n" +
"           ,"+0+"\n" +
"           ,"+0+","+0+")";
        
        Query query = entityManager.createNativeQuery(sql);
        query.executeUpdate();
        //StatusDisplayer.getDefault().setStatusText("Processed For: "+e.getSurName()+" "+ e.getOtherNames());
        entityManager.getTransaction().commit();

        }catch(Exception ex){
            
        }
        
        
                    
        
                

        
        
        
        
        
        
        
            
        
        
    }
        
        public static void postOtherDeductions(Employees e,TblPeriods p){
        
         Double otherDeductions = getOtherDeductions(e, p);
            

        

        
        
        
        
                
       
        
        
        
        
        
        
        entityManager.getTransaction().begin();
        
            
        
        String sql= "INSERT INTO [dbo].[tblPeriodTransactions]\n" +
"           ([Employee_ID]\n" +
"           ,[Currency_ID]\n" +
"           ,[PayrollCode_ID]\n" +
"           ,[Amount]\n" +
"           ,[Conversion_Rate]\n" +
"           ,[Converted_Amount]\n" +
"           ,[Message]\n" +
"           ,[Period_Month]\n" +
"           ,[Period_Year]\n" +
"           ,[PayPoint_ID]\n" +
"           ,[Bank_Transfer]\n" +
"           ,[Cash]\n" +
"           ,[Cheque])\n" +
"     VALUES\n" +
"           ("+e.getEmployeeID()+"\n" +
"           ,"+e.getCurrencyID().getCurrencyID()+"\n" +
"           ,"+41+"\n" +
"           ,"+otherDeductions+"\n" +
"           ,"+e.getCurrencyID().getCurrencyID()+"\n" +
"           ,"+otherDeductions+"\n" +
"           ,'No Message'" +

"           ,"+covertMonthsToInt(p.getPeriodMonth()) +"\n" +
"           ,"+p.getPeriodYear()+"\n" +
"           ,"+1+"\n" +
"           ,"+0+"\n" +
"           ,"+0+","+0+")";
        
        Query query = entityManager.createNativeQuery(sql);
        query.executeUpdate();
        //StatusDisplayer.getDefault().setStatusText("Processed For: "+e.getSurName()+" "+ e.getOtherNames());
        entityManager.getTransaction().commit();

        
        
        
                    
        
                

        
        
        
        
        
        
        
            
        
        
    }
        
                public  void postStatutoryDeductions(Employees e,TblPeriods p){
        
            //Gross Pay, Taxable Pay, NSSF, PAYE
            

        


        
        
        Double deduction = getStatutoryDeductions(e, p);
        
        
        
        
        
        
        
        
        entityManager.getTransaction().begin();
        
            
        
        String sql= "INSERT INTO [dbo].[tblPeriodTransactions]\n" +
"           ([Employee_ID]\n" +
"           ,[Currency_ID]\n" +
"           ,[PayrollCode_ID]\n" +
"           ,[Amount]\n" +
"           ,[Conversion_Rate]\n" +
"           ,[Converted_Amount]\n" +
"           ,[Message]\n" +
"           ,[Period_Month]\n" +
"           ,[Period_Year]\n" +
"           ,[PayPoint_ID]\n" +
"           ,[Bank_Transfer]\n" +
"           ,[Cash]\n" +
"           ,[Cheque])\n" +
"     VALUES\n" +
"           ("+e.getEmployeeID()+"\n" +
"           ,"+e.getCurrencyID().getCurrencyID()+"\n" +
"           ,"+4+"\n" +
"           ,"+deduction+"\n" +
"           ,"+e.getCurrencyID().getCurrencyID()+"\n" +
"           ,"+deduction+"\n" +
"           ,'No Message'" +

"           ,"+covertMonthsToInt(p.getPeriodMonth()) +"\n" +
"           ,"+p.getPeriodYear()+"\n" +
"           ,"+1+"\n" +
"           ,"+0+"\n" +
"           ,"+0+","+0+")";
        
        Query query = entityManager.createNativeQuery(sql);
        query.executeUpdate();
        //StatusDisplayer.getDefault().setStatusText("Processed For: "+e.getSurName()+" "+ e.getOtherNames());
        entityManager.getTransaction().commit();

        
        
        
                    
        
                

        
        
        
        
        
        
        
            
        
        
    }
                
      public static List<TblPayrollCodeGroups> getPayrollCodeGroups(){
      TypedQuery<TblPayrollCodeGroups> query = entityManager.createQuery(
              "SELECT t FROM TblPayrollCodeGroups t",TblPayrollCodeGroups.class);
      return query.getResultList();
        
    }
        
        
        
        
        public static void deletePeriodTransactions(TblPeriods p){
            String sql = "DELETE FROM TblPeriodTransactions where Period_Year = "+p.getPeriodYear()+" AND Period_Month="+covertMonthsToInt(p.getPeriodMonth())+"";
            Query query = entityManager.createNativeQuery(sql);
            entityManager.getTransaction().begin();
            query.executeUpdate();
            entityManager.getTransaction().commit();
        }
        
        
        
        
        
        
        
        

        public static void postNSSF(Employees e, TblPeriods p){
            
            Double nssf5 = getEmployeeNssf(e, p);
            
            
            
                    
                    //Posting TAXABLE PAYE
        entityManager.getTransaction().begin();
        
        String sql4= "INSERT INTO [dbo].[tblPeriodTransactions]\n" +
"           ([Employee_ID]\n" +
"           ,[Currency_ID]\n" +
"           ,[PayrollCode_ID]\n" +
"           ,[Amount]\n" +
"           ,[Conversion_Rate]\n" +
"           ,[Converted_Amount]\n" +
"           ,[Message]\n" +
"           ,[Period_Month]\n" +
"           ,[Period_Year]\n" +
"           ,[PayPoint_ID]\n" +
"           ,[Bank_Transfer]\n" +
"           ,[Cash]\n" +
"           ,[Cheque])\n" +
"     VALUES\n" +
"           ("+e.getEmployeeID()+"\n" +
"           ,"+e.getCurrencyID().getCurrencyID()+"\n" +
"           ,"+9+"\n" +
"           ,"+nssf5+"\n" +
"           ,"+e.getCurrencyID().getCurrencyID()+"\n" +
"           ,"+nssf5+"\n" +
"           ,'No Message'" +

"           ,"+covertMonthsToInt(p.getPeriodMonth()) +"\n" +
"           ,"+p.getPeriodYear()+"\n" +
"           ,"+1+"\n" +
"           ,"+0+"\n" +
"           ,"+0+","+0+")";
        
        Query query4 = entityManager.createNativeQuery(sql4);
        query4.executeUpdate();
        //StatusDisplayer.getDefault().setStatusText("Processed For: "+e.getSurName()+" "+ e.getOtherNames());
        entityManager.getTransaction().commit();
            
            
            
        }
        
        public static void postNSSF10(Employees e, TblPeriods p){
            Double nssf10 = getEmployerNssf(e, p);
                                
                    //Posting Employeer NSSF
        entityManager.getTransaction().begin();
        
        String sql4= "INSERT INTO [dbo].[tblPeriodTransactions]\n" +
"           ([Employee_ID]\n" +
"           ,[Currency_ID]\n" +
"           ,[PayrollCode_ID]\n" +
"           ,[Amount]\n" +
"           ,[Conversion_Rate]\n" +
"           ,[Converted_Amount]\n" +
"           ,[Message]\n" +
"           ,[Period_Month]\n" +
"           ,[Period_Year]\n" +
"           ,[PayPoint_ID]\n" +
"           ,[Bank_Transfer]\n" +
"           ,[Cash]\n" +
"           ,[Cheque])\n" +
"     VALUES\n" +
"           ("+e.getEmployeeID()+"\n" +
"           ,"+e.getCurrencyID().getCurrencyID()+"\n" +
"           ,"+42+"\n" +
"           ,"+nssf10+"\n" +
"           ,"+e.getCurrencyID().getCurrencyID()+"\n" +
"           ,"+nssf10+"\n" +
"           ,'No Message'" +

"           ,"+covertMonthsToInt(p.getPeriodMonth()) +"\n" +
"           ,"+p.getPeriodYear()+"\n" +
"           ,"+1+"\n" +
"           ,"+0+"\n" +
"           ,"+0+","+0+")";
        
        Query query4 = DataAccess.entityManager.createNativeQuery(sql4);
        query4.executeUpdate();
        //StatusDisplayer.getDefault().setStatusText("Processed For: "+e.getSurName()+" "+ e.getOtherNames());
        entityManager.getTransaction().commit();
            
        }
        
        
        
        
        
        
        
        
        
        
        
        
        
        public void postNetPay(Employees e, TblPeriods p){
            Double netPay = getNetPay(e, p);
            
                    
                    //Posting TAXABLE PAYE
        entityManager.getTransaction().begin();
        
        String sql4= "INSERT INTO [dbo].[tblPeriodTransactions]\n" +
"           ([Employee_ID]\n" +
"           ,[Currency_ID]\n" +
"           ,[PayrollCode_ID]\n" +
"           ,[Amount]\n" +
"           ,[Conversion_Rate]\n" +
"           ,[Converted_Amount]\n" +
"           ,[Message]\n" +
"           ,[Period_Month]\n" +
"           ,[Period_Year]\n" +
"           ,[PayPoint_ID]\n" +
"           ,[Bank_Transfer]\n" +
"           ,[Cash]\n" +
"           ,[Cheque])\n" +
"     VALUES\n" +
"           ("+e.getEmployeeID()+"\n" +
"           ,"+e.getCurrencyID().getCurrencyID()+"\n" +
"           ,"+10+"\n" +
"           ,"+netPay+"\n" +
"           ,"+e.getCurrencyID().getCurrencyID()+"\n" +
"           ,"+netPay+"\n" +
"           ,'No Message'" +

"           ,"+covertMonthsToInt(p.getPeriodMonth()) +"\n" +
"           ,"+p.getPeriodYear()+"\n" +
"           ,"+1+"\n" +
"           ,"+0+"\n" +
"           ,"+0+","+0+")";
        
        Query query4 = DataAccess.entityManager.createNativeQuery(sql4);
        query4.executeUpdate();
        //StatusDisplayer.getDefault().setStatusText("Processed For: "+e.getSurName()+" "+ e.getOtherNames());
        entityManager.getTransaction().commit();
            
        }
        
        
        
        
        
        
        
        public  void postPAYE(Employees e, TblPeriods p){
            
            Double paye = getPaye(e, p);
            
            
            
            //Post PAYE
        entityManager.getTransaction().begin();
        
        String sql4= "INSERT INTO [dbo].[tblPeriodTransactions]\n" +
"           ([Employee_ID]\n" +
"           ,[Currency_ID]\n" +
"           ,[PayrollCode_ID]\n" +
"           ,[Amount]\n" +
"           ,[Conversion_Rate]\n" +
"           ,[Converted_Amount]\n" +
"           ,[Message]\n" +
"           ,[Period_Month]\n" +
"           ,[Period_Year]\n" +
"           ,[PayPoint_ID]\n" +
"           ,[Bank_Transfer]\n" +
"           ,[Cash]\n" +
"           ,[Cheque])\n" +
"     VALUES\n" +
"           ("+e.getEmployeeID()+"\n" +
"           ,"+e.getCurrencyID().getCurrencyID()+"\n" +
"           ,"+7+"\n" +
"           ,"+paye+"\n" +
"           ,"+e.getCurrencyID().getCurrencyID()+"\n" +
"           ,"+paye+"\n" +
"           ,'No Message'" +

"           ,"+covertMonthsToInt(p.getPeriodMonth()) +"\n" +
"           ,"+p.getPeriodYear()+"\n" +
"           ,"+1+"\n" +
"           ,"+0+"\n" +
"           ,"+0+","+0+")";
        
        Query query4 = entityManager.createNativeQuery(sql4);
        query4.executeUpdate();
        //StatusDisplayer.getDefault().setStatusText("Processed For: "+e.getSurName()+" "+ e.getOtherNames());
        entityManager.getTransaction().commit();
        
            
        }
        
            public static void saveEmployeeTransaction(Employees e, TblPayrollCode code,BigDecimal amount,Currencies currency,String message,TblPeriods period){
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        entityManager.getTransaction().begin();
        String sql= "INSERT INTO [dbo].[tblEmployeeTransactions]\n" +
"           ([Employee_ID]\n" +

"           ,[PayrollCode_ID]\n" +
"           ,[Amount]\n" +
"           ,[Currency_ID]\n" +
"           ,[Period_Month]\n" +
"           ,[Period_Year]\n" +

"           ,[Date_Transaction])\n" +
"     VALUES\n" +
"           ("+e.getEmployeeID()+"\n" +

"           ,"+code.getPayrollCodeID()+"\n" +
"           ,"+amount+"\n" +
"           ,"+currency.getCurrencyID()+"\n" +
"           ,"+covertMonthsToInt(period.getPeriodMonth())+"\n" +
"           ,"+period.getPeriodYear()+"\n" +

"           ,'"+sdf.format(new Date())+"')";
        
        Query query = entityManager.createNativeQuery(sql);
        query.executeUpdate();
        //StatusDisplayer.getDefault().setStatusText("Processed For: "+e.getSurName()+" "+ e.getOtherNames());
        entityManager.getTransaction().commit();
        
    }
        
    
    
    
    
    
    
    
    
    public static void saveEmployeePayrollCode(Employees e, TblPayrollCode code){
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        entityManager.getTransaction().begin();
        String sql= "INSERT INTO [dbo].[tblEmployeePayrollCode]\n" +
"           ([Currency_ID]\n" +
"           ,[Employee_ID]\n" +
"           ,[EmpCode]\n" +
"           ,[PayrollCode_ID]\n" +
"           ,[Amount]\n" +
"           ,[OBalance]\n" +
"           ,[Status]\n" +
"           ,[Active])\n" +
"     VALUES\n" +
"           (?,?,?,?,?,?,?,?)";
        
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, 1);
        query.setParameter(2, e.getEmployeeID());
        query.setParameter(4, code.getPayrollCodeID());
        query.setParameter(5, 0);
        query.setParameter(8, 1);
        query.executeUpdate();
        //StatusDisplayer.getDefault().setStatusText("Processed For: "+e.getSurName()+" "+ e.getOtherNames());
        entityManager.getTransaction().commit();
        
    }

    
    
    
    
    
    
    public static Boolean validatePolicy(HrsPolicies p, int groupID){
        Boolean result = Boolean.FALSE;
        
        try{
        HrsGroupPolicies policy = (HrsGroupPolicies) entityManager.createNativeQuery(
                "SELECT TOP 1 * FROM HrsGroupPolicies WHERE policyID = ? AND groupID = ?"
                , HrsGroupPolicies.class)
                .setParameter(1, p.getPolicyID())
                .setParameter(2, groupID)
                .getSingleResult();
        if(null!=policy){
            result = policy.getPolModify();
        }else{
            result = Boolean.FALSE;
        }
        }catch(NoResultException ex){
            result = Boolean.FALSE;
        }
        return result;
    }
    
    public static List<Integer> getAssignedFunctions(HrsGroups g){
        List<Integer> list = new ArrayList<>();
        
        TypedQuery<HrsGroupPolicies> query = entityManager.createQuery("SELECT h FROM HrsGroupPolicies h WHERE h.groupID = :id AND h.polModify = TRUE ",HrsGroupPolicies.class);
        query.setParameter("id", g);
        for (HrsGroupPolicies p : query.getResultList()){
            list.add(p.getPolicyID().getPolicyID());
        }
        return list;
    }
    
    public static List<HrsAuditTrail> searchAuditTrail(String sql){
        
        
        Query query = entityManager.createNativeQuery(sql,HrsAuditTrail.class);
        
        
        return query.getResultList();
    }
    
    public static int covertMonthsToInt(String month){
        
        switch (month) {
            case "January":
                return 1;
            case "February":
                return 2;
            case "March":
                return 3;
            case "April":
                return 4;
            case "May":
                return 5;
            case "June":
                return 6;
            case "July":
                return 7;
            case "August":
                return 8;
            case "September":
                return 9;
            case "October":
                return 10;
            case "November":
                return 11;
            case "December":
                return 12;
            default:
                return 0;
        }
    }
    
    public  BigDecimal getPaye(Double gross){
        TblPeriods currentMonth = getCurrentMonth();
        String sql= "select dbo.prlFnGetPAYE(?,?,?)";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, gross);
        query.setParameter(2, CetusUTL.covertMonthsToInt(currentMonth.getPeriodMonth()));
        query.setParameter(3, currentMonth.getPeriodYear());
        try{
            return (BigDecimal)query.getSingleResult();
        }catch(Exception ex){
            NotifyUtil.error("There was an error", "Show Error", ex, false);
            return BigDecimal.ZERO;
        }
    }
    
    @Override
    public TblPeriods getCurrentPeriod(){
        TblPeriods currentPeriod = new TblPeriods();
        
        List<TblPeriods> list = getPayrollPeriods();
        for(TblPeriods p: list){
            if(p.getStatus().equalsIgnoreCase("Open")){
                currentPeriod = p;
            }
        }
        return currentPeriod;
    }
    
    public static TblPeriods getCurrentMonth(){
        TblPeriods currentPeriod = new TblPeriods();
        
        List<TblPeriods> list = getPayrollPeriods();
        for(TblPeriods p: list){
            if(p.getStatus().equalsIgnoreCase("Open")){
                currentPeriod = p;
            }
        }
        return currentPeriod;
    }
    
    
   
    
    public static BigDecimal getPayrollAmount(Employees employeeID, int pcode,TblPeriods period){
        
        String sql= "SELECT dbo.prlFnGetPayrollAmount(?,?,?,?)";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, employeeID.getEmployeeID());
        query.setParameter(2, pcode);
        query.setParameter(3, CetusUTL.covertMonthsToInt(period.getPeriodMonth()));
        query.setParameter(4,period.getPeriodYear());
        try{
            BigDecimal amount = (BigDecimal)(query.getSingleResult());
            
            return amount;
          
        }catch(Exception ex){
            System.out.println("This is the exception: "+ex.getLocalizedMessage());
            return BigDecimal.ZERO;
        }
    }
    
    
    public static TblPeriods getNextPeriod(TblPeriods currentPeriod){
        int nextPeriodYear = currentPeriod.getPeriodYear();
        String nextMonth;
        if(currentPeriod.getPeriodMonth().equalsIgnoreCase("December")){
            nextPeriodYear = nextPeriodYear+1;
            nextMonth = "January";
            
            TblPeriods p = new TblPeriods();
            p.setPeriodYear(nextPeriodYear);
            p.setPeriodMonth(nextMonth);
            return p;
        }else{
            int month  = CetusUTL.covertMonthsToInt(currentPeriod.getPeriodMonth());
            String[] months = DateFormatSymbols.getInstance().getMonths();
            nextMonth = months[month];
            TblPeriods p = new TblPeriods();
            p.setPeriodYear(nextPeriodYear);
            p.setPeriodMonth(nextMonth);
            return p;
            
        }
    }
    private static Userinfo getClockinID(Employees e){
        String code = e.getEmpCode();
        String badge = code.replaceFirst("^0+(?!$)","");
        System.out.println(code+ " badge: "+badge );
        TypedQuery<Userinfo> query = entityManager.createNamedQuery("Userinfo.findByBadgenumber",Userinfo.class);
        query.setParameter("badgenumber", badge);
        try{
        return query.getSingleResult();
        }catch(Exception ex){
        return null;    
        }
        
    }
    
    
    public static List<Checkinout> getCheckinOut(Employees e, Date date){
        Userinfo user = getClockinID(e);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String sSelected = sdf.format(date);
        
        String sNextDay = sSelected;
        Calendar cSelected = Calendar.getInstance();
        Calendar nextDay = Calendar.getInstance();
        try{
        cSelected.setTime(sdf.parse(sdf.format(date)));
        nextDay.setTime(sdf.parse(sdf.format(date)));
        nextDay.add(Calendar.DAY_OF_MONTH, 1);
        sNextDay = sdf.format(nextDay.getTime());
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        
        try{
        
        String sql = "SELECT * FROM Checkinout WHERE userid ="+user.getUserid()+" AND checktime>='"+sSelected+"' AND checktime<'"+sNextDay+"'";
        Query query = entityManager.createNativeQuery(sql,Checkinout.class);
        //System.out.println(sql);
        return query.getResultList();
        }catch(NullPointerException ex){
            return new ArrayList<>();
        }
        
        
        
    }
    
    public static Date getCheckOut(Employees e,Date selectedDate){
        
        Userinfo user = getClockinID(e);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String sSelected = sdf.format(selectedDate);
        
        String sNextDay = sSelected;
        Calendar cSelected = Calendar.getInstance();
        Calendar nextDay = Calendar.getInstance();
        try{
        cSelected.setTime(sdf.parse(sdf.format(selectedDate)));
        nextDay.setTime(sdf.parse(sdf.format(selectedDate)));
        nextDay.add(Calendar.DAY_OF_MONTH, 1);
        sNextDay = sdf.format(nextDay.getTime());
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        
        try{
            
        String betterSQL = "SELECT TOP 1 CHECKTIME FROM CHECKINOUT WHERE CHECKTYPE='O' AND userid ="+user.getUserid()+" AND checktime>='"+sSelected+"' AND checktime<'"+sNextDay+"'  ORDER BY CHECKTIME DESC";    
        
        
        Query query = entityManager.createNativeQuery(betterSQL);
        //System.out.println(sql);
        return (Date)query.getSingleResult();
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
        
        
        
        
        
        
    }
    
    public static Date getCheckIn(Employees e,Date selectedDate){
        Userinfo user = getClockinID(e);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String sSelected = sdf.format(selectedDate);
        
        String sNextDay = sSelected;
        Calendar cSelected = Calendar.getInstance();
        Calendar nextDay = Calendar.getInstance();
        try{
        cSelected.setTime(sdf.parse(sdf.format(selectedDate)));
        nextDay.setTime(sdf.parse(sdf.format(selectedDate)));
        nextDay.add(Calendar.DAY_OF_MONTH, 1);
        sNextDay = sdf.format(nextDay.getTime());
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        
        try{
            
        String betterSQL = "SELECT TOP 1 CHECKTIME FROM CHECKINOUT WHERE CHECKTYPE='I' AND userid ="+user.getUserid()+" AND checktime>='"+sSelected+"' AND checktime<'"+sNextDay+"'  ORDER BY CHECKTIME ASC";    
        
        
        Query query = entityManager.createNativeQuery(betterSQL);
        //System.out.println(sql);
        return (Date)query.getSingleResult();
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
        
        
        
        
        
        
        
        
        
    }
    
    public static List<PtmShifts> getShifts(String sql){
        TypedQuery<PtmShifts> query = entityManager.createQuery(sql, PtmShifts.class);
        return query.getResultList();
    }
    
    public static List<PtmHolidays> getHolidays(){
        Calendar cal = Calendar.getInstance();
        Date today =  new Date();
        cal.setTime(today);
        
        
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        
        TypedQuery<PtmHolidays> query = entityManager.createQuery("SELECT p FROM PtmHolidays p WHERE p.dateOf >= :today", PtmHolidays.class);
        query.setParameter("today", cal.getTime());
        return query.getResultList();
    }
    
    public static List<PtmShiftSchedule> getShiftSchedule(String sql){
        
        Query query = entityManager.createNativeQuery(sql,PtmShiftSchedule.class);
        return query.getResultList();
    }
    
    public static PtmShifts getShiftByID(int ID){
        TypedQuery<PtmShifts> query = entityManager.createNamedQuery("PtmShifts.findByShiftID",PtmShifts.class);
        query.setParameter("shiftID", ID);
        try{
            return query.getResultList().get(0);
        }catch(Exception ex){
            
            return null;
        }
    }
    
    public static String getEmployeeShift(Date date,Employees e){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String sql="SELECT ShiftName FROM Ptmshifts WHERE ShiftID = (SELECT ShiftCode FROM PtmShiftSchedule WHERE EmployeeID = "+e.getEmployeeID()+" and ShiftDate ='"+sdf.format(date)+"')";
        
        Query query = entityManager.createNativeQuery(sql);
        
        try{
            return (String)query.getSingleResult();
        }catch(Exception ex){
            
            return "Unknown Shift";
        }
    }
    
      
}
