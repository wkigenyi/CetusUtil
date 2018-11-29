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
        id = "systems.tech247.util.Edit"
)
@ActionRegistration(
        iconBase = "systems/tech247/util/icons/edit.png",
        displayName = "#CTL_Edit"
)
@ActionReference(path = "Toolbars/CetusEdit", position = 610)
@Messages("CTL_Edit=Edit")
public final class Edit implements ActionListener {

    private final CapEditable context;

    public Edit(CapEditable context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        context.edit();
    }
}
