package org.python.pydev.outline;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.python.pydev.core.bundle.ImageCache;
import org.python.pydev.parser.jython.SimpleNode;
import org.python.pydev.parser.jython.ast.Attribute;
import org.python.pydev.parser.jython.ast.ClassDef;
import org.python.pydev.parser.jython.ast.FunctionDef;
import org.python.pydev.parser.jython.ast.Name;
import org.python.pydev.parser.jython.ast.NameTok;
import org.python.pydev.parser.visitors.NodeUtils;
import org.python.pydev.ui.UIConstants;

/**
 * Action that will hide the 'magic' members in the outline, i.e. members that both start and end with '__'.
 * 
 * Such members are defined as 'magic' in PEP 8
 * 
 * @author laurent.dore
 */
public class OutlineHideMagicObjectsAction extends AbstractOutlineFilterAction {

    private static final String PREF_HIDE_MAGICOBJECTS = "org.python.pydev.OUTLINE_HIDE_MAGICOBJECTS";

    public OutlineHideMagicObjectsAction(PyOutlinePage page, ImageCache imageCache) {
        super("Hide Magic Objects", page, imageCache, PREF_HIDE_MAGICOBJECTS, UIConstants.MAGIC_OBJECT_ICON);
    }

    /**
     * @return the filter used to hide members that start and end with "___"
     */
    @Override
    protected ViewerFilter createFilter() {
        return new ViewerFilter() {

            @Override
            public boolean select(Viewer viewer, Object parentElement, Object element) {
                if (element instanceof ParsedItem) {
                    ParsedItem item = (ParsedItem) element;

                    SimpleNode token = item.getAstThis().node;

                    String name = null;
                    if (token instanceof ClassDef) {
                        ClassDef classDefToken = (ClassDef) token;
                        name = NodeUtils.getNameFromNameTok((NameTok) (classDefToken).name);
                    }
                    else if (token instanceof FunctionDef) {
                        FunctionDef functionDefToken = (FunctionDef) token;
                        name = NodeUtils.getNameFromNameTok((NameTok) (functionDefToken).name);
                    }
                    else if (token instanceof Attribute) {
                        Attribute attributeToken = (Attribute) token;
                        name = NodeUtils.getNameFromNameTok((NameTok) (attributeToken).attr);
                    }
                    else if (token instanceof Name) {
                        Name nameToken = (Name) token;
                        name = nameToken.id;
                    }
                    else if (token instanceof NameTok) {
                        NameTok nameTokToken = (NameTok) token;
                        name = NodeUtils.getNameFromNameTok(nameTokToken);
                    }
                    
                    if (name != null) {
                        return !(name.startsWith("__") && (name.endsWith("__")));
                    }
                }
                return true;
            }

        };
    }
}
