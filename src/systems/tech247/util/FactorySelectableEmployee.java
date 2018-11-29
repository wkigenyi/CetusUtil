/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.util;

import java.beans.IntrospectionException;
import java.util.List;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import systems.tech247.dbaccess.DataAccess;
import systems.tech247.hr.Employees;





/**
 *
 * @author Admin
 */
public class FactorySelectableEmployee extends ChildFactory<EmployeeSelectable>{
    
    String search = "";
    boolean select;
    public FactorySelectableEmployee(String search, boolean select){
        this.search = search;
        this.select = select;
    }
    @Override
    protected boolean createKeys(List<EmployeeSelectable> list) {
        List<Employees> elist = DataAccess.searchEmployees(search);
        for (Employees e: elist){
            list.add(new EmployeeSelectable(e, select));
            //StatusDisplayer.getDefault().setStatusText("Adding "+e.getEmpCode());
        }
        return true;
    }
    
    @Override
    protected Node createNodeForKey(EmployeeSelectable key){
        Node node = null;
        try{
            node = new NodeSelectableEmployee(key);
        }catch(IntrospectionException ex){
            
        }    
        return node;
    }
    
}
