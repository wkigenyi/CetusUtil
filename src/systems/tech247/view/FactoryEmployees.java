/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.view;

import systems.tech247.util.QueryEmployee;
import java.util.List;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import systems.tech247.hr.Employees;

import systems.tech247.api.ReloadableQueryCapability;

/**
 *
 * @author Wilfred
 */
public class FactoryEmployees extends ChildFactory<Employees>{

    QueryEmployee query;
    
    public FactoryEmployees(QueryEmployee query){
        this.query = query;
    }
    
    @Override
    protected boolean createKeys(List<Employees> list) {
        //get this ability from the look
        ReloadableQueryCapability r = query.getLookup().lookup(ReloadableQueryCapability.class);
        //Use the ability
        if(r != null){
            try{
                r.reload();
            }catch(Exception ex){
                
            }
        }
        //Populate the list of child entries
        list.addAll(query.getList());
        
        //return true since we are set
        return true;
    }
    
    @Override
    protected Node createNodeForKey(Employees key){
        Node node = new EmployeeNode(key);
       
        
        return node;
    }
    
    private class EmployeeNode extends AbstractNode{
        
        private final InstanceContent instanceContent;
        
        public EmployeeNode(Employees emp){
            this(new InstanceContent(),emp);
        }
        
        private EmployeeNode (InstanceContent ic, Employees emp){
            super(Children.LEAF, new AbstractLookup(ic));
            instanceContent = ic;
            instanceContent.add(emp);
            setIconBaseWithExtension("systems/tech247/util/icons/person.png");
            setDisplayName(emp.getSurName()+" "+emp.getOtherNames());
        }
    
}
    
}
