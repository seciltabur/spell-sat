/*
 * Created on Jun 8, 2006
 * @author Fabio
 */
package org.python.pydev.parser.visitors.scope;

import java.util.Iterator;
import java.util.List;

import org.python.pydev.parser.jython.ast.Attribute;
import org.python.pydev.parser.jython.ast.ClassDef;
import org.python.pydev.parser.jython.ast.FunctionDef;
import org.python.pydev.parser.jython.ast.Name;

public class OutlineIterator implements Iterator<ASTEntry> {


    private ASTEntry next = null;
    private Iterator<ASTEntry> nodesIt;

    public OutlineIterator(List<ASTEntry> nodes) {
        this.nodesIt = nodes.iterator();
        setNext();
    }

    private void setNext() {
        while(nodesIt.hasNext()){
            ASTEntry entry = nodesIt.next();
            
            if(entry.node instanceof ClassDef || entry.node instanceof FunctionDef || 
                    entry.node instanceof Attribute || entry.node instanceof Name ){
                
                next = entry;
                return;
            }
        }
        next = null;
    }

    public boolean hasNext() {
        return next != null;
    }

    public ASTEntry next() {
        ASTEntry n = next;
        setNext();
        return n;
    }

    public void remove() {
        throw new RuntimeException("Not Impl");
    }

}
