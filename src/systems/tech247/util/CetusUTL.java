/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.util;

import java.awt.Container;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import org.openide.explorer.ExplorerManager;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.windows.TopComponentGroup;

/**
 *
 * @author Wilfred
 */
public class CetusUTL implements Lookup.Provider {
    
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = 
    Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    static CetusUTL instance;

    public static CetusUTL getInstance() {
        if(instance==null){
            instance = new CetusUTL();
        }
        return instance;
    }
    
    
    public static List<Integer> userRights=new ArrayList();
    public static TopComponentGroup currentTCG;
    
    public static InstanceContent content = new InstanceContent();
    Lookup lookup = new AbstractLookup(content);
    public static ExplorerManager emSelectableEmployees = new ExplorerManager();
    
    int monthInt;
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
    
    public static String padRight(String s, int n) {
        while(s.length()<20){
            s = s +" ";
        }
        return s;
    }
    public static String padLeft(String s, int n) {
        while(s.length()<20){
            s = " "+s;
        }
        return s;
    }
    public static void loadSelectableEmployees(String sql,boolean select){
        emSelectableEmployees.setRootContext(new AbstractNode(Children.create(new FactorySelectableEmployee(sql,select), true)));
        
    }
    
    public static JSpinner addLabeledSpinner(Container c,
                                                
                                                SpinnerModel model) {

 
        JSpinner spinner = new JSpinner(model);
   
        c.add(spinner);
 
        return spinner;
    }

    @Override
    public Lookup getLookup() {
        return lookup;
    }
    
    public static boolean validateEmail(String email){
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.find();
    }

    
    
    
    
}
