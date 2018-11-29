/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.view;

import java.awt.BorderLayout;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.OutlineView;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;


/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//systems.tech247.pdr//Categories//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "CategoriesTopComponent",
        iconBase = "systems/tech247/util/icons/EmpCategory.png",
        persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "explorer", openAtStartup = false)
@ActionID(category = "PDR", id = "systems.tech247.pdr.CategoriesTopComponent")
@ActionReference(path = "Menu/PDR" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_CategoriesAction",
        preferredID = "CategoriesTopComponent"
)
@Messages({
    "CTL_CategoriesAction=Employee Categories",
    "CTL_CategoriesTopComponent=Employee Categories",
    "HINT_CategoriesTopComponent= "
})
public final class CategoriesTopComponent extends TopComponent implements ExplorerManager.Provider {
    
    ExplorerManager em = new ExplorerManager();
    
    String sqlString = "";
    String searchString;
    QueryCategory query = new QueryCategory();
    
    public CategoriesTopComponent(){
        this("");
    }
    
    
    public CategoriesTopComponent(String purpose) {
        initComponents();
        setName(Bundle.CTL_CategoriesTopComponent());
        setToolTipText(Bundle.HINT_CategoriesTopComponent());
        OutlineView ov = new OutlineView("Employee Categories");
        ov.getOutline().setRootVisible(false);
        ov.addPropertyColumn("number", "Employee Number");
        ov.addPropertyColumn("category", "Description");
        if("multi".equals(purpose)){
            ov.addPropertyColumn("selected", "Select Category");
        }
        sqlString ="SELECT r from EmployeeCategories r";
        query.setSqlString(sqlString);
        loadItems();
        viewPanel.setLayout(new BorderLayout());
        viewPanel.add(ov);
        associateLookup(ExplorerUtils.createLookup(em, getActionMap()));
        

        
        

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        viewPanel = new javax.swing.JPanel();

        viewPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout viewPanelLayout = new javax.swing.GroupLayout(viewPanel);
        viewPanel.setLayout(viewPanelLayout);
        viewPanelLayout.setHorizontalGroup(
            viewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 402, Short.MAX_VALUE)
        );
        viewPanelLayout.setVerticalGroup(
            viewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 282, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(viewPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(viewPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel viewPanel;
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
       
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return em;
    }
    
    void loadItems(){
        em.setRootContext(new AbstractNode(Children.create(new FactoryCategories(), true)));
    }
    
    
  }