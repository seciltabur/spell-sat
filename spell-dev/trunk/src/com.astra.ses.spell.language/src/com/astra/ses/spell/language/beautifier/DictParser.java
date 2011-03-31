///////////////////////////////////////////////////////////////////////////////
//
// PACKAGE   : com.astra.ses.spell.language.beautifier
// 
// FILE      : DictParser.java
//
// DATE      : 2009-11-23
//
// Copyright (C) 2008, 2011 SES ENGINEERING, Luxembourg S.A.R.L.
//
// By using this software in any way, you are agreeing to be bound by
// the terms of this license.
//
// All rights reserved. This program and the accompanying materials
// are made available under the terms of the Eclipse Public License v1.0
// which accompanies this distribution, and is available at
// http://www.eclipse.org/legal/epl-v10.html
//
// NO WARRANTY
// EXCEPT AS EXPRESSLY SET FORTH IN THIS AGREEMENT, THE PROGRAM IS PROVIDED
// ON AN "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, EITHER
// EXPRESS OR IMPLIED INCLUDING, WITHOUT LIMITATION, ANY WARRANTIES OR
// CONDITIONS OF TITLE, NON-INFRINGEMENT, MERCHANTABILITY OR FITNESS FOR A
// PARTICULAR PURPOSE. Each Recipient is solely responsible for determining
// the appropriateness of using and distributing the Program and assumes all
// risks associated with its exercise of rights under this Agreement ,
// including but not limited to the risks and costs of program errors,
// compliance with applicable laws, damage to or loss of data, programs or
// equipment, and unavailability or interruption of operations.
//
// DISCLAIMER OF LIABILITY
// EXCEPT AS EXPRESSLY SET FORTH IN THIS AGREEMENT, NEITHER RECIPIENT NOR ANY
// CONTRIBUTORS SHALL HAVE ANY LIABILITY FOR ANY DIRECT, INDIRECT, INCIDENTAL,
// SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING WITHOUT LIMITATION
// LOST PROFITS), HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OR DISTRIBUTION OF THE PROGRAM OR THE
// EXERCISE OF ANY RIGHTS GRANTED HEREUNDER, EVEN IF ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGES.
//
// Contributors:
//    SES ENGINEERING - initial API and implementation and/or initial documentation
//
// PROJECT   : SPELL
//
// SUBPROJECT: SPELL Development Environment
//
///////////////////////////////////////////////////////////////////////////////
package com.astra.ses.spell.language.beautifier;

import java.util.ArrayList;

import com.astra.ses.spell.language.model.ast.Dict;
import com.astra.ses.spell.language.model.ast.List;
import com.astra.ses.spell.language.model.ast.Name;
import com.astra.ses.spell.language.model.ast.Num;
import com.astra.ses.spell.language.model.ast.Str;
import com.astra.ses.spell.language.model.ast.exprType;

public class DictParser extends MultilineParser
{
	public static void parse( Dict dict, ArrayList<Paragraph> pars, int indent, int maxLength )
	{
		Paragraph p = new Paragraph();
		ArrayList<String> lines = new ArrayList<String>();
		int totalLength = 0;
		for( int index=0; index<dict.keys.length; index++)
		{
			exprType element = dict.keys[index];
			if (element instanceof Num)
			{
				totalLength += addKeyValue( lines, ((Num)element).num, dict.values[index], indent, maxLength, (index<dict.keys.length-1) );
			}
			else if (element instanceof Str)
			{
				totalLength += addKeyValue( lines, ((Str)element).s, dict.values[index], indent, maxLength, (index<dict.keys.length-1) );
			}
			else if (element instanceof Name)
			{
				totalLength += addKeyValue( lines, ((Name)element).id, dict.values[index], indent, maxLength, (index<dict.keys.length-1) );
			}
			else
			{
				System.err.println("[DICT] Unable to parse element " + element);
			}
		}
		alignOrMerge( lines, p, indent, totalLength, maxLength );
		pars.add(p);
	}
	
	protected static int addKeyValue( ArrayList<String> lines, String key, exprType value,  int indent, int maxLength, boolean addComma )
	{
		ArrayList<String> sublines = new ArrayList<String>();
		parseValue( value, sublines, indent + key.length(), maxLength - key.length() );
		String line = key + ":" + sublines.get(0);
		int pairLength = line.length();
		if (sublines.size()>1)
		{
			lines.add(line);
			for(int idx=1; idx<sublines.size(); idx++)
			{
				line = sublines.get(idx);
				if (idx==sublines.size()-1) line += ", ";
				lines.add(line);
				pairLength += line.length();
			}
		}
		else
		{
			if (addComma) line += ", ";
			lines.add(line);
		}
		return pairLength;
	}

	protected static void parseValue( exprType expr, ArrayList<String> lines, int indent, int maxLength )
	{
		String result = "";
		if (expr instanceof Num)
		{
			result = ((Num)expr).num;
			lines.add(result);
		}
		else if (expr instanceof Str)
		{
			result = ((Str)expr).s;
			lines.add(result);
		}
		else if (expr instanceof Name)
		{
			result = ((Name)expr).id;
			lines.add(result);
		}
		else if (expr instanceof Dict)
		{
			ArrayList<Paragraph> sublist = new ArrayList<Paragraph>();
			parse( (Dict) expr, sublist, indent+2, maxLength );
			for(String subline : sublist.get(0).getLines())
			{
				lines.add(subline);
			}
		}
		else if (expr instanceof List)
		{
			ArrayList<Paragraph> sublist = new ArrayList<Paragraph>();
			ListParser.parse( (List) expr, sublist, indent+4, maxLength );
			for(String subline : sublist.get(0).getLines())
			{
				lines.add(subline);
			}
		}
		else
		{
			System.err.println("[DICTVAL] Unable to parse element " + expr);
			result = "<?>";
		}
	}

	protected static void alignOrMerge( ArrayList<String> lines, Paragraph p, int indent, int totalLength, int maxLength )
	{
		if (totalLength<maxLength)
		{
			mergeLines( lines, p , "{ ", " }" );
		}
		else
		{
			indentLines( lines, indent, p );
			p.addBefore("{ ");
			p.addAfter(" }");
			alignAt(p, ":", 0, indent);
			int pos = alignByCommas(p, indent);
			alignAt(p, "}", pos, indent);
		}
	}
}
