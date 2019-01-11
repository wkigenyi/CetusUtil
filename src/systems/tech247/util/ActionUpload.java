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
        id = "systems.tech247.util.ActionUpload"
)
@ActionRegistration(
        iconBase = "systems/tech247/util/icons/upload.png",
        displayName = "#CTL_Upload"
)
@ActionReference(path = "Toolbars/CetusPLR", position = 620)
@Messages("CTL_Upload=Upload")
public final class ActionUpload implements ActionListener {

    private final CapUploadable context;

    public ActionUpload(CapUploadable context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        context.upload();
    }
}
