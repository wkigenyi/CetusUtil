/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.view;

import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.windows.TopComponent;
import systems.tech247.dbaccess.DataAccess;
import systems.tech247.hr.CSSSCategories;
import systems.tech247.hr.EmployeeCategories;
import systems.tech247.util.AddTool;
import systems.tech247.util.NodeAddTool;
import systems.tech247.util.NotifyUtil;



/**
 *
 * @author Wilfred
 */
public class FactoryCategories extends ChildFactory<Object>{

    
    
    CSSSCategories c;
    boolean add=false;
    String sql;
    public FactoryCategories(CSSSCategories c){
        this(null, false);
    }
    
    public FactoryCategories(){
        CSSSCategories cat = DataAccess.searchDefaultCategories().get(0);
        this.c = cat;
        
    }
    
    
    public FactoryCategories(CSSSCategories c, boolean add){
        this.add = add;
        this.c = c;
    }
    
    public FactoryCategories(String sql){
        
        this.sql = sql;
    }
    
    
    
    @Override
    protected boolean createKeys(List<Object> list) {
        if(add){
            list.add(new AddTool(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    TopComponent tc = new CategoryEditorTopComponent();
                    tc.open();
                    tc.requestActive();
                }
            }));
            
        }
        //Populate the list of child entries
        if( c!=null){
            list.addAll(c.getEmployeeCategoriesCollection());
        }else{
            
            list.add(DataAccess.searchCategories(sql));
            NotifyUtil.info("Where are?", list.size()+" categories", false);
        }
        //return true since we are set
        return true;
    }
    
    @Override
    protected Node createNodeForKey(Object key){
        Node node = null;
        if(key instanceof EmployeeCategories){
            node = new NodeSetupCategory(new CategorySelectable((EmployeeCategories)key, false));
        }else if(key instanceof AddTool){
            node = new NodeAddTool((AddTool)key);
        }
        
        return node;
    }
    
    
    
}
