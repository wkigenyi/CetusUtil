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
        id = "systems.tech247.util.Delete"
)
@ActionRegistration(
        iconBase = "systems/tech247/util/icons/delete.png",
        displayName = "#CTL_Delete"
)
@ActionReference(path = "Toolbars/CetusEdit", position = 620)
@Messages("CTL_Delete=Delete")
public final class Delete implements ActionListener {

    private final CapDeletable context;

    public Delete(CapDeletable context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        context.delete();
    }
}
