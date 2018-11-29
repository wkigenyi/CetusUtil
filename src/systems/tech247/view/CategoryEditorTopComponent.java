/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.view;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.math.BigDecimal;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.netbeans.api.settings.ConvertAsProperties;
import org.netbeans.spi.actions.AbstractSavable;
import org.openide.awt.StatusDisplayer;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.InstanceContent;
import systems.tech247.dbaccess.DataAccess;
import systems.tech247.hr.EmployeeCategories;


/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//systems.tech247.pdr//PositionEditor//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "EmployeePersonalInfoEditorTopComponent",
        iconBase="systems/tech247/util/icons/EmpCategory.png", 
        persistenceType = TopComponent.PERSISTENCE_NEVER
)



@Messages({
    
    "CTL_CategoryEditorTopComponent=New Category",
    "HINT_CategoryEditorTopComponent=Category Editor"
})
public final class CategoryEditorTopComponent extends TopComponent{
    
    EmployeeCategories emp;
    
    DataAccess da = new DataAccess();
    InstanceContent ic = new InstanceContent();
    
    
    //Updatables
    String pname = "";
    String pdetails = "";
    BigDecimal defaultRenu  = BigDecimal.ZERO;
    BigDecimal casualDaily = BigDecimal.ZERO;
    boolean showInOrganogram = false;
    String positionDetails = "";
    short catLevel = 0;
    
    String defa = "";
    String casual ="";
    
    
    
    EntityManager entityManager = DataAccess.getEntityManager();
    
    EmployeeCategories updatable;
    
    public CategoryEditorTopComponent(){
        this(null);
    }

    public CategoryEditorTopComponent(EmployeeCategories e) {
        initComponents();
        setName(Bundle.CTL_CategoryEditorTopComponent());
        setToolTipText(Bundle.HINT_CategoryEditorTopComponent());
        
        emp = e;
        
        if(null!=e){
            updatable = entityManager.find(EmployeeCategories.class, e.getCategoryID());
        }
        
        
        //Fill in the employee details
        if(null!=e){
            fillCategory(e);
        }
        
        
        
        jtCatLevel.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                try{
                    catLevel = new Short(jtCatLevel.getText());
                    updatable.setCategoryLevel(catLevel);
                    modify();
                }catch(Exception ex){
                    
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                try{
                    catLevel = new Short(jtCatLevel.getText());
                    updatable.setCategoryLevel(catLevel);
                    modify();
                }catch(Exception ex){
                    
                } //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void keyReleased(KeyEvent e) {
                try{
                    catLevel = new Short(jtCatLevel.getText());
                    updatable.setCategoryLevel(catLevel);
                    modify();
                }catch(Exception ex){
                    
                }//To change body of generated methods, choose Tools | Templates.
            }
        });
        
        
        
        
        
        
        jtCatName.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                
                if(pname!=jtCatName.getText()){
                        pname = jtCatName.getText();
                        try{
                            updatable.setCategoryName(pname);
                        }catch(Exception ex){
                            
                        }
                        modify();
                    }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                
                
                
                   if(pname!=jtCatName.getText()){
                        pname = jtCatName.getText();
                        try{
                            updatable.setCategoryName(pname);
                        }catch(Exception ex){
                            
                        }
                        modify();
                    }
                    
                
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                
                
                
                    if(pname!=jtCatName.getText()){
                        pname = jtCatName.getText();
                        try{
                            updatable.setCategoryName(pname);
                        }catch(Exception ex){
                            
                        }
                        modify();
                    }
                    
               
                    
                
            }
        });
        
        jtaCategoryDetails.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                
                
                    if(positionDetails!= jtaCategoryDetails.getText()){
                        positionDetails = jtaCategoryDetails.getText();
                        
                        try{
                            updatable.setCategoryDetails(positionDetails);
                        }catch(Exception ex){
                            
                        }
                        modify();
                    }
                    
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(positionDetails!= jtaCategoryDetails.getText()){
                        positionDetails = jtaCategoryDetails.getText();
                        
                        try{
                            updatable.setCategoryDetails(positionDetails);
                        }catch(Exception ex){
                            
                        }
                        modify();
                    }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(positionDetails!= jtaCategoryDetails.getText()){
                        positionDetails = jtaCategoryDetails.getText();
                        
                        try{
                            updatable.setCategoryDetails(positionDetails);
                        }catch(Exception ex){
                            
                        }
                        modify();
                    }
            }
        });
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
       
                
        
        
        

    }
    
    private class CategorySavable extends AbstractSavable{
        
        CategorySavable(){
            register();
        }

        @Override
        protected String findDisplayName() {
            if(null==emp){
                return "New Category";
            }else{
                return emp.getCategoryName();
            }
        }
        
        CategoryEditorTopComponent tc(){
            return CategoryEditorTopComponent.this;
        }

        @Override
        protected void handleSave() throws IOException {
            
            tc().ic.remove(this);
            unregister();
            //New Employee
            if(null==emp){
                
                        String insertSQL = "INSERT INTO [dbo].[EmployeeCategories]\n" +
"           ([CategoryName]\n" +
"           ,[CategoryDetails]\n" +
"           ,[CategoryLevel])\n" +
"     VALUES\n" +
"           (?,?,?)";
            Query query = entityManager.createNativeQuery(insertSQL);
            query.setParameter(1, pname);
            query.setParameter(2, positionDetails);
            
            query.setParameter(3, catLevel);

            entityManager.getTransaction().begin();
            query.executeUpdate();
            entityManager.getTransaction().commit();
            }else{
                //Creating a new Employee
                entityManager.getTransaction().begin();
                entityManager.getTransaction().commit();
                
                    
                
            }
        }

        @Override
        public boolean equals(Object o) {
            if(o instanceof CategorySavable){
                CategorySavable e = (CategorySavable)o;
                return tc() == e.tc();
            }
            return false;        }

        @Override
        public int hashCode() {
            return tc().hashCode();
        }
        
    }
    
    public void modify(){
        
        
    
            if(pname.length()<1){
                StatusDisplayer.getDefault().setStatusText("Proper Category Name is Required");
            }else{
                if(getLookup().lookup(CategorySavable.class)==null){
                            ic.add(new CategorySavable());
                }
            }
        
        
            
        
    }
    
    void fillCategory(EmployeeCategories e){
        try{
                setName(e.getCategoryName());
                
                
                
                pname = e.getCategoryName();
                jtCatName.setText(pname );
                
                
              
                catLevel = e.getCategoryLevel();
                jtCatLevel.setText(e.getCategoryLevel()+"");
                
                
                
                
                
                
                positionDetails = e.getCategoryDetails();
                jtaCategoryDetails.setText(positionDetails);
                
                
                
                
                
                
                
                
                
        }catch(Exception ex){
            
        }
                
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jtCatName = new javax.swing.JTextField();
        jtCatLevel = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtaCategoryDetails = new javax.swing.JTextArea();

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(CategoryEditorTopComponent.class, "CategoryEditorTopComponent.jLabel1.text")); // NOI18N

        jtCatName.setText(org.openide.util.NbBundle.getMessage(CategoryEditorTopComponent.class, "CategoryEditorTopComponent.jtCatName.text")); // NOI18N

        jtCatLevel.setText(org.openide.util.NbBundle.getMessage(CategoryEditorTopComponent.class, "CategoryEditorTopComponent.jtCatLevel.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(CategoryEditorTopComponent.class, "CategoryEditorTopComponent.jLabel3.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel6, org.openide.util.NbBundle.getMessage(CategoryEditorTopComponent.class, "CategoryEditorTopComponent.jLabel6.text")); // NOI18N

        jtaCategoryDetails.setColumns(20);
        jtaCategoryDetails.setRows(5);
        jScrollPane1.setViewportView(jtaCategoryDetails);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 398, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(103, 103, 103)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jtCatName, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
                            .addComponent(jtCatLevel))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jtCatName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jtCatLevel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jtCatLevel;
    private javax.swing.JTextField jtCatName;
    private javax.swing.JTextArea jtaCategoryDetails;
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
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

    
}
