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
        id = "systems.tech247.util.Preview"
)
@ActionRegistration(
        iconBase = "systems/tech247/util/icons/preview.png",
        displayName = "#CTL_Preview"
)
@ActionReference(path = "Toolbars/CetusDeliver", position = 610)
@Messages("CTL_Preview=Preview")
public final class Preview implements ActionListener {

    private final CapPreview context;

    public Preview(CapPreview context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        context.preview();
    }
}
