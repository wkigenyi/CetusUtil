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
        id = "systems.tech247.util.ActionDownload"
)
@ActionRegistration(
        iconBase = "systems/tech247/util/icons/download.png",
        displayName = "#CTL_ActionDownload"
)
@ActionReference(path = "Toolbars/CetusDeliver", position = 650)
@Messages("CTL_ActionDownload=Download")
public final class ActionDownload implements ActionListener {

    private final CapDownloadable context;

    public ActionDownload(CapDownloadable context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        context.download();
    }
}
