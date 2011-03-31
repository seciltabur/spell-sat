// Autogenerated AST node
package com.astra.ses.spell.language.model.ast;
import com.astra.ses.spell.language.model.SimpleNode;

public class BoolOp extends exprType implements boolopType {
    public int op;
    public exprType[] values;

    public BoolOp(int op, exprType[] values) {
        this.op = op;
        this.values = values;
    }

    public BoolOp(int op, exprType[] values, SimpleNode parent) {
        this(op, values);
        this.beginLine = parent.beginLine;
        this.beginColumn = parent.beginColumn;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("BoolOp[");
        sb.append("op=");
        sb.append(dumpThis(this.op, boolopType.boolopTypeNames));
        sb.append(", ");
        sb.append("values=");
        sb.append(dumpThis(this.values));
        sb.append("]");
        return sb.toString();
    }

    public Object accept(VisitorIF visitor) throws Exception {
        return visitor.visitBoolOp(this);
    }

    public void traverse(VisitorIF visitor) throws Exception {
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                if (values[i] != null)
                    values[i].accept(visitor);
            }
        }
    }

}
