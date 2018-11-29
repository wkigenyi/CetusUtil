/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.util;


import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import org.openide.awt.StatusDisplayer;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.lookup.Lookups;
import systems.tech247.dbaccess.DataAccess;
import systems.tech247.hr.Employees;



/**
 *
 * @author Admin
 */
public class NodeSelectableEmployee extends AbstractNode {
  
    public NodeSelectableEmployee(EmployeeSelectable bean) throws IntrospectionException {
        super(Children.LEAF,Lookups.singleton(bean));
        setDisplayName(bean.getEmployee().getSurName());
        setIconBaseWithExtension("systems/tech247/util/icons/person.png");
    }
    
    @Override
    protected Sheet createSheet(){
        Sheet s = super.createSheet();
        Sheet.Set basic = Sheet.createPropertiesSet();
        basic.setDisplayName("Info");
        final EmployeeSelectable bean = getLookup().lookup(EmployeeSelectable.class);
        
            final PropertySupport.Reflection test;
            Property isSelected;
            
        try {
            test = new PropertySupport.Reflection(bean, Boolean.class, "selected");
            test.setPropertyEditorClass(BooleanPropertyEditor.class);
            //basic.put(test);
            
            isSelected = new PropertySupport(
                    "isSelected", 
                    Boolean.class, 
                    "Select", 
                    "Check the box to select this employee", true, true) {
                @Override
                public Boolean getValue() throws IllegalAccessException, InvocationTargetException {
                    return bean.getSelected();
//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
                
                @Override
                public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                    
                               
                        
                        
                        Employees employee = bean.getEmployee();
                        //HrsGroups grp = DataAccess.entityManager.find(HrsGroups.class, groupID);
                        
                        boolean assign = (Boolean)val;
                        //Save the Policy
                        
                        
                        //DataAccess.savePolicy(policyID,grp, assign);
                        DataAccess.updateSelectedEmployee(employee, assign);
                        test.setValue(val);
                        
                    
                }
            };
            basic.put(isSelected);
            
            Property surname = new PropertySupport(
                    "surname", 
                    String.class, "Surname", "Employee Surname", true, false) {
                @Override
                public Object getValue() throws IllegalAccessException, InvocationTargetException {
                    return bean.getSurname();
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
                
                @Override
                public void setValue(Object t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            };
            
            basic.put(surname);
            
            Property fname = new PropertySupport(
                    "fname", 
                    String.class, "Other Names", "Employee Other Names", true, false) {
                @Override
                public Object getValue() throws IllegalAccessException, InvocationTargetException {
                    return bean.getOtherNames();
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
                
                @Override
                public void setValue(Object t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            };
            
            basic.put(fname);
            
            Property department = new PropertySupport(
                    "department", 
                    String.class, "Department", "Employee Department", true, false) {
                @Override
                public Object getValue() throws IllegalAccessException, InvocationTargetException {
                    return bean.getDepartment();
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
                
                @Override
                public void setValue(Object t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            };
            
            basic.put(department);
            
            
            Property category = new PropertySupport(
                    "category", 
                    String.class, "Category", "Employee Category", true, false) {
                @Override
                public Object getValue() throws IllegalAccessException, InvocationTargetException {
                    return bean.getCategory();
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
                
                @Override
                public void setValue(Object t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            };
            
            basic.put(category);
            
        } catch (NoSuchMethodException ex) {
            StatusDisplayer.getDefault().setStatusText("We have a problem");
        }
        
            
        
            
            
            
            
            
        
        
        s.put(basic);
        
        
        return s;
    }    
    
    
}
    
    
    
    

