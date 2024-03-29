/*
 * Created on Mar 27, 2006
 */
package org.python.pydev.editor.actions;


import java.util.ResourceBundle;

import org.eclipse.ui.texteditor.IUpdate;
import org.eclipse.ui.texteditor.ResourceAction;
import org.python.pydev.editor.PyEdit;

/**
 * Reference: IncrementalFindAction extends ResourceAction implements IUpdate
 * 
 * @author Fabio
 */
public class OfflineAction extends ResourceAction implements IUpdate {

    private OfflineActionTarget fTarget;
    private PyEdit edit;

    public OfflineAction(ResourceBundle bundle, String prefix, PyEdit edit) {
        super(bundle, prefix);
        this.edit = edit;
        update();
    }


    /*
     * @see IAction#run()
     */
    public void run() {
        if (fTarget == null)
            return;

        fTarget.beginSession();
    }

    /*
     * @see IUpdate#update()
     */
    public void update() {
        fTarget= (OfflineActionTarget) edit.getAdapter(OfflineActionTarget.class);
        setEnabled(fTarget != null);
    }


}
