// Autogenerated AST node
package com.astra.ses.spell.language.model.ast;
import com.astra.ses.spell.language.model.SimpleNode;

public class TryFinally extends stmtType {
    public stmtType[] body;
    public suiteType finalbody;

    public TryFinally(stmtType[] body, suiteType finalbody) {
        this.body = body;
        this.finalbody = finalbody;
    }

    public TryFinally(stmtType[] body, suiteType finalbody, SimpleNode
    parent) {
        this(body, finalbody);
        this.beginLine = parent.beginLine;
        this.beginColumn = parent.beginColumn;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("TryFinally[");
        sb.append("body=");
        sb.append(dumpThis(this.body));
        sb.append(", ");
        sb.append("finalbody=");
        sb.append(dumpThis(this.finalbody));
        sb.append("]");
        return sb.toString();
    }

    public Object accept(VisitorIF visitor) throws Exception {
        return visitor.visitTryFinally(this);
    }

    public void traverse(VisitorIF visitor) throws Exception {
        if (body != null) {
            for (int i = 0; i < body.length; i++) {
                if (body[i] != null)
                    body[i].accept(visitor);
            }
        }
        if (finalbody != null)
            finalbody.accept(visitor);
    }

}
