/*
 * Created on Apr 12, 2005
 *
 * @author Fabio Zadrozny
 */
package org.python.pydev.editor.correctionassist.heuristics;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.python.pydev.core.IPythonNature;
import org.python.pydev.core.bundle.ImageCache;
import org.python.pydev.core.docutils.PySelection;
import org.python.pydev.editor.PyEdit;
import org.python.pydev.editor.actions.PyAction;
import org.python.pydev.editor.correctionassist.FixCompletionProposal;
import org.python.pydev.ui.UIConstants;

/**
 * @author Fabio Zadrozny
 */
public class AssistImport implements IAssistProps {

    /**
     * @see org.python.pydev.editor.correctionassist.heuristics.IAssistProps#getProps(org.python.pydev.core.docutils.PySelection, org.python.pydev.core.bundle.ImageCache)
     */
    public List<ICompletionProposal> getProps(PySelection ps, ImageCache imageCache, File f, IPythonNature nature, PyEdit edit, int offsetReceived) throws BadLocationException {
        ArrayList<ICompletionProposal> l = new ArrayList<ICompletionProposal>();
        String sel = PyAction.getLineWithoutComments(ps).trim();

        int i = sel.indexOf("import");
        if(ps.getStartLineIndex() != ps.getEndLineIndex())
            return l;
        
        
        String delimiter = PyAction.getDelimiter(ps.getDoc());
        
        int lineToMoveImport = ps.getLineAvailableForImport();
        
        int offset = ps.getDoc().getLineOffset(lineToMoveImport);
        
        
        if(i >= 0){
            l.add(new FixCompletionProposal(sel+delimiter, offset, 0, ps.getStartLine().getOffset(), imageCache.get(UIConstants.ASSIST_MOVE_IMPORT),
                    "Move import to global scope", null, null, ps.getStartLineIndex()+1));
        }
        return l;
    }


    /**
     * @see org.python.pydev.editor.correctionassist.heuristics.IAssistProps#isValid(org.python.pydev.core.docutils.PySelection)
     */
    public boolean isValid(PySelection ps, String sel, PyEdit edit, int offset) {
        return sel.indexOf("import ") != -1;
    }

}
