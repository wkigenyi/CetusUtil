/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.view;

import java.lang.reflect.InvocationTargetException;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.nodes.Sheet.Set;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.windows.TopComponent;
import systems.tech247.dbaccess.DataAccess;
import systems.tech247.hr.EmployeeCategories;
import systems.tech247.util.BooleanPropertyEditor;
import systems.tech247.util.CapEditable;
import systems.tech247.util.CetusUTL;
import systems.tech247.util.NotifyUtil;

/**
 *
 * @author Wilfred
 */
public class NodeSetupCategory extends AbstractNode{
        
        private final InstanceContent instanceContent;
        CategorySelectable cat;
        
        public NodeSetupCategory(CategorySelectable emp){
            this(new InstanceContent(),emp);
        }
        
        private NodeSetupCategory (InstanceContent ic, CategorySelectable unit){
            super(Children.LEAF, new AbstractLookup(ic));
            this.cat = unit;
            instanceContent = ic;
            instanceContent.add(unit);
            if(CetusUTL.userRights.contains(231)){
                instanceContent.add(new CapEditable(){
                @Override
                public void edit() {
                    TopComponent tc = new CategoryEditorTopComponent(cat.getCat());
                    tc.open();
                    tc.requestActive();
                }
                
            });
            
            }
            
            setIconBaseWithExtension("systems/tech247/util/icons/EmpCategory.png");
            setDisplayName(unit.getCat().getCategoryName());
        }

    @Override
    protected Sheet createSheet() {
         
        Sheet sheet = Sheet.createDefault();
        Set set = Sheet.createPropertiesSet();
        final CategorySelectable cat = getLookup().lookup(CategorySelectable.class);

        Property number = new PropertySupport("number", String.class, "Employee Number", "Employee Number", true, false) {
            @Override
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return cat.getCat().getEmployeesCollection().size();
            }
            
            @Override
            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        
        Property categoryType = new PropertySupport("category", String.class, "Category Description", "Category Description", true, false) {
            @Override
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return cat.getCat().getCategoryDetails();
            }
            
            @Override
            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        try{
        final PropertySupport.Reflection test = new PropertySupport.Reflection(cat,Boolean.class,"selected");
        test.setPropertyEditorClass(BooleanPropertyEditor.class);
        Property selected = new PropertySupport("selected", Boolean.class, "Selected","Selected", true, true) {
            @Override
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return cat.getSelected();
                //To change body of generated methods, choose Tools | Templates.
            }
            
            @Override
            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                Boolean add = (Boolean)val;
                DataAccess.updateSelectedEmployee(cat.getCat(), add);
                test.setValue(val);
            }
        };
        set.put(selected);
                
        }catch(Exception ex){
            NotifyUtil.error("Error", "Error", ex, false);
        }
        
        set.put(categoryType);
        set.put(number);
        
        sheet.put(set);
        return sheet;
//To change body of generated methods, choose Tools | Templates.
    }
        
        


        
        
    
}
