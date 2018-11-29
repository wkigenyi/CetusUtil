/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.util;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import systems.tech247.dbaccess.DataAccess;
import systems.tech247.hr.HrsUsers;
import systems.tech247.hr.TblPeriods;



/**
 *
 * @author Admin
 */

public class LoginDetailsPanel extends JPanel implements LookupListener{
    
    
    Lookup.Result<HrsUsers> userResult=CetusUTL.getInstance().getLookup().lookupResult(HrsUsers.class);
    TblPeriods detail = DataAccess.getCurrentMonth();
    HrsUsers user;
    JPanel periodpanel = new JPanel(new BorderLayout(0, 1));
    
    JLabel usernameLabel = new JLabel("Login Name:");
    String workstation;
    
    
    JPanel loginpanel = new JPanel(new BorderLayout(0, 1));
    JPanel datePanel = new JPanel(new BorderLayout(0, 1));
    JPanel pcPanel = new JPanel(new BorderLayout(0, 1));
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    

    @Override
    public void resultChanged(LookupEvent le) {
        Lookup.Result<HrsUsers> results = (Lookup.Result<HrsUsers>)le.getSource();
        
        for(HrsUsers user: results.allInstances()){
            
            usernameLabel.setText(user.getUserName()+" ");
            
            try{
                InetAddress localMachine = InetAddress.getLocalHost();
                workstation= localMachine.getHostName();        
            }catch(UnknownHostException ex){
                workstation="Unknown Machine";
            }
        
        
            loginpanel.add(usernameLabel,BorderLayout.WEST);
            loginpanel.add(new JSeparator(SwingConstants.VERTICAL),BorderLayout.EAST);
            datePanel.add(new JLabel(sdf.format(new Date())+" "),BorderLayout.WEST);
            datePanel.add(new JSeparator(SwingConstants.VERTICAL),BorderLayout.EAST);
            pcPanel.add(new JLabel(workstation+" "),BorderLayout.WEST);
            pcPanel.add(new JSeparator(SwingConstants.VERTICAL),BorderLayout.EAST);
            
            this.add(loginpanel);
            
            this.add(pcPanel);
            
            this.add(datePanel);
            
            
            
            
            
            
            
            
            periodpanel.add(new JLabel("Current Period: "+detail.getPeriodMonth()+" "+detail.getPeriodYear()+" "),BorderLayout.WEST);
            periodpanel.add(new JSeparator(SwingConstants.VERTICAL),BorderLayout.EAST);
            this.add(periodpanel);            
        }
    }
    
    public LoginDetailsPanel(){
        setLayout(new FlowLayout());
        userResult.addLookupListener(this);
        resultChanged(new LookupEvent(userResult));
    }

    
    
    
}
