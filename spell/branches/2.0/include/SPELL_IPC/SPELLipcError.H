// ################################################################################
// FILE       : SPELLipcError.H
// DATE       : Mar 17, 2011
// PROJECT    : SPELL
// DESCRIPTION: IPC error defintions
// --------------------------------------------------------------------------------
//
//  Copyright (C) 2008, 2011 SES ENGINEERING, Luxembourg S.A.R.L.
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

#ifndef __SPELL_IPCERROR_H__
#define __SPELL_IPCERROR_H__

// FILES TO INCLUDE ////////////////////////////////////////////////////////
// System includes ---------------------------------------------------------
// Local includes ----------------------------------------------------------
#include "SPELL_IPC/SPELLipc.H"
// Project includes --------------------------------------------------------
#include "SPELL_UTIL/SPELLerror.H"

/** \addtogroup SPELL_IPC */
/*@{*/


// FORWARD REFERENCES //////////////////////////////////////////////////////
// TYPES ///////////////////////////////////////////////////////////////////
// DEFINES /////////////////////////////////////////////////////////////////
/** Unknown message type error code */
#define IPC_ERROR_UNKNOWN_MSG_TYPE 10

//////////////////////////////////////////////////////////////////////////////
/**
 ** \brief Error raised by IPC classes
 **
 ** \see SPELLcoreException
 **
 *////////////////////////////////////////////////////////////////////////////
class SPELLipcError : public SPELLcoreException
{
public:
	//////////////////////////////////////////////////////////////////////
	/** Constructor.
	 *
	 * \param error IN: error description
	 * \param code IN: error code
	**////////////////////////////////////////////////////////////////////
    SPELLipcError( std::string error, int code )
        : SPELLcoreException( error, "", code ) {
        ;
    };
};

/*@}*/
#endif

