///////////////////////////////////////////////////////////////////////////////
//
// PACKAGE   : com.astra.ses.spell.gui.model.nav.content
// 
// FILE      : CategoryNode.java
//
// DATE      : 2008-11-21 08:55
//
// Copyright (C) 2008, 2010 SES ENGINEERING, Luxembourg S.A.R.L.
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
// SUBPROJECT: SPELL GUI Client
//
///////////////////////////////////////////////////////////////////////////////
package com.astra.ses.spell.gui.model.nav.content;

import java.util.ArrayList;
import java.util.Collection;

/******************************************************************************
 * A category may contain procedures or other categories
 * @author jpizar
 *****************************************************************************/
public class CategoryNode extends BaseProcedureSystemElement {

	/** contained children */
	private ArrayList<BaseProcedureSystemElement> m_children;
	
	/**************************************************************************
	 * Constructor
	 * @param name
	 * @param parent
	 *************************************************************************/
	public CategoryNode(String name)
	{
		super(name);
		m_children = new ArrayList<BaseProcedureSystemElement>();
	}
	
	@Override
	public boolean isLeaf() {
		return false;
	}
	
	/**************************************************************************
	 * Add a children to this category
	 *************************************************************************/
	public void addChildren(BaseProcedureSystemElement element)
	{
		m_children.add(element);
		element.setParent(this);
	}
	
	/**************************************************************************
	 * Get children
	 * @return
	 *************************************************************************/
	public Collection<BaseProcedureSystemElement> getChildren() {
		return m_children;
	}
	
	/**************************************************************************
	 * Check if this category contains a child with the given name
	 * @param name
	 * @return the child if this category contains a child with the name
	 * 			otherwise null is returned
	 *************************************************************************/
	public BaseProcedureSystemElement getChild(String name)
	{
		for (BaseProcedureSystemElement child : getChildren())
		{
			if (child.getName().equals(name))
			{
				return child;
			}
		}
		return null;
	}
	
	/**************************************************************************
	 * Remove all of its children
	 *************************************************************************/
	public void clear()
	{
		m_children.clear();
	}
}