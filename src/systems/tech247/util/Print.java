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
        category = "CetusDeliver",
        id = "systems.tech247.util.Print"
)
@ActionRegistration(
        iconBase = "systems/tech247/util/icons/print.png",
        displayName = "#CTL_Print"
)
@ActionReference(path = "Toolbars/CetusDeliver", position = 600)
@Messages("CTL_Print=Print")
public final class Print implements ActionListener {

    private final CapPrint context;

    public Print(CapPrint context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        context.print();
    }
}
