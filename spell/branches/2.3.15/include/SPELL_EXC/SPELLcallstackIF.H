// ################################################################################
// FILE       : SPELLcallstackIF.H
// DATE       : Mar 17, 2011
// PROJECT    : SPELL
// DESCRIPTION: Call stack interface.
// --------------------------------------------------------------------------------
//
//  Copyright (C) 2008, 2012 SES ENGINEERING, Luxembourg S.A.R.L.
//
//  This file is part of SPELL.
//
// SPELL is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// SPELL is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with SPELL. If not, see <http://www.gnu.org/licenses/>.
//
// ################################################################################

#ifndef __SPELL_CALLSTACK_IF_H__
#define __SPELL_CALLSTACK_IF_H__

// FILES TO INCLUDE ////////////////////////////////////////////////////////
// System includes ---------------------------------------------------------
// Local includes ----------------------------------------------------------
#include "SPELL_EXC/SPELLstepOverMode.H"
#include "SPELL_EXC/SPELLdispatchListener.H"
// Project includes --------------------------------------------------------
#include "SPELL_UTIL/SPELLbase.H"

/** \addtogroup SPELL_EXC */
/*@{*/


// FORWARD REFERENCES //////////////////////////////////////////////////////
// TYPES ///////////////////////////////////////////////////////////////////
// DEFINES /////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////////////
/**
 ** \brief Call stack interface
 **
 ** \par Description and usage:
 **
 ** 	Interface defining the mechanisms of the call stack manager. To be
 ** 	overriden by concrete implementations (see SPELLcallstack)
 **
**////////////////////////////////////////////////////////////////////////////
class SPELLcallstackIF : public SPELLdispatchListener
{
public: //--------------------------------------------------------------------

    // EXCEPTIONS ////////////////////////////////////////////////////////////
    // ENUMS /////////////////////////////////////////////////////////////////
    // TYPES /////////////////////////////////////////////////////////////////
    // LIFECYCLE /////////////////////////////////////////////////////////////

		//////////////////////////////////////////////////////////////////////
		/** Destructor.
		**////////////////////////////////////////////////////////////////////
		SPELLcallstackIF(): SPELLdispatchListener() {;};

		//////////////////////////////////////////////////////////////////////
		/** Destructor.
		**////////////////////////////////////////////////////////////////////
		virtual ~SPELLcallstackIF() {;};

    // STATIC ////////////////////////////////////////////////////////////////
    // METHODS ///////////////////////////////////////////////////////////////

		//////////////////////////////////////////////////////////////////////
		/** Reset the stack status.
		**////////////////////////////////////////////////////////////////////
		virtual void reset() = 0;

		//////////////////////////////////////////////////////////////////////
		/** Obtain the current callstack string.
		 *
		 * \returns The current callstack string.
		**////////////////////////////////////////////////////////////////////
		virtual const std::string& getStack() = 0;

		//////////////////////////////////////////////////////////////////////
		/** Obtain the current callstack string.
		 *
		 * \returns The current callstack string.
		**////////////////////////////////////////////////////////////////////
		virtual const std::string getFullStack() = 0;

		//////////////////////////////////////////////////////////////////////
		/** Obtain the current line number.
		 *
		 * \returns The current line number.
		**////////////////////////////////////////////////////////////////////
		virtual unsigned int getCurrentLine() = 0;

		//////////////////////////////////////////////////////////////////////
		/** Obtain the current code name string.
		 *
		 * \returns The current code name string.
		**////////////////////////////////////////////////////////////////////
		virtual const std::string& getCodeName() = 0;

		//////////////////////////////////////////////////////////////////////
		/** Set the step over mode.
		 *
		 * \param mode IN: the mode to be used.
		**////////////////////////////////////////////////////////////////////
		virtual void stepOver( const SPELLstepOverMode& mode ) = 0;

		//////////////////////////////////////////////////////////////////////
		/** Set the current stage.
		 *
		 * \param id IN: stage identifier
		 * \param title IN: stage title
		**////////////////////////////////////////////////////////////////////
		virtual void setStage( const std::string& id, const std::string& title ) = 0;

		//////////////////////////////////////////////////////////////////////
		/** Obtain the current stage.
		 *
		 * \returns The current stage identifier and title.
		**////////////////////////////////////////////////////////////////////
		virtual const std::string getStage() = 0;

		//////////////////////////////////////////////////////////////////////
		/** The current line is being skipped without executing it.
		**////////////////////////////////////////////////////////////////////
		virtual const void skipCurrentLine() = 0;

		//////////////////////////////////////////////////////////////////////
		/** Check if we are stepping over.
		 *
		 * \returns True if stepping over.
		**////////////////////////////////////////////////////////////////////
		virtual const bool isSteppingOver() = 0;

		//////////////////////////////////////////////////////////////////////
		/** Move stack view position to the given level
		 *
		 * \param level IN: level of the stack where to move the active node.
		**////////////////////////////////////////////////////////////////////
		virtual void moveToLevel( unsigned int level ) = 0;

	    //////////////////////////////////////////////////////////////////////
	    /** Enable or disable notifications
	    **////////////////////////////////////////////////////////////////////
	    virtual void enableNotifications( bool enable ) = 0;
};

/*@}*/
#endif
