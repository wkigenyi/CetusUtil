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
        id = "systems.tech247.util.Email"
)
@ActionRegistration(
        iconBase = "systems/tech247/util/icons/email.png",
        displayName = "#CTL_Email"
)
@ActionReference(path = "Toolbars/CetusDeliver", position = 620)
@Messages("CTL_Email=Email")
public final class Email implements ActionListener {

    private final CapEmail context;

    public Email(CapEmail context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        context.email();
    }
}
