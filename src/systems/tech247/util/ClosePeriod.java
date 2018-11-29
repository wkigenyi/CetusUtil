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
        category = "CetusPLR",
        id = "systems.tech247.util.ClosePeriod"
)
@ActionRegistration(
        iconBase = "systems/tech247/util/icons/close.png",
        displayName = "#CTL_Close"
)
@ActionReference(path = "Toolbars/CetusPLR", position = 610)
@Messages("CTL_Close=Close Period")
public final class ClosePeriod implements ActionListener {

    private final CapClosePeriod context;

    public ClosePeriod(CapClosePeriod context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        context.closePeriod();
    }
}
