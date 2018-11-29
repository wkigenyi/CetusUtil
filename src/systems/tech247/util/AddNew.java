/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "CetusEdit",
        id = "systems.tech247.util.AddNew"
)
@ActionRegistration(
        iconBase = "systems/tech247/util/icons/AddNew.png",
        displayName = "#CTL_AddNew"
)
@ActionReference(path = "Toolbars/CetusEdit", position = 600)
@Messages("CTL_AddNew=Add New")
public final class AddNew implements ActionListener {

    private final CapCreatable context;

    public AddNew(CapCreatable context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        context.create();
    }
}
