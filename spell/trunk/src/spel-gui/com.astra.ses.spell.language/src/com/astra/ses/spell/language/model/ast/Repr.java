// Autogenerated AST node
package com.astra.ses.spell.language.model.ast;

import com.astra.ses.spell.language.model.SimpleNode;

public class Repr extends exprType
{
	public exprType	value;

	public Repr(exprType value)
	{
		this.value = value;
	}

	public Repr(exprType value, SimpleNode parent)
	{
		this(value);
		this.beginLine = parent.beginLine;
		this.beginColumn = parent.beginColumn;
	}

	public String toString()
	{
		StringBuffer sb = new StringBuffer("Repr[");
		sb.append("value=");
		sb.append(dumpThis(this.value));
		sb.append("]");
		return sb.toString();
	}

	public Object accept(VisitorIF visitor) throws Exception
	{
		return visitor.visitRepr(this);
	}

	public void traverse(VisitorIF visitor) throws Exception
	{
		if (value != null) value.accept(visitor);
	}

}