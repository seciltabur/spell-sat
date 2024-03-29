// ################################################################################
// FILE       : SPELLshellCif.C
// DATE       : Mar 18, 2011
// PROJECT    : SPELL
// DESCRIPTION: CIF implementation for shell
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

// FILES TO INCLUDE ////////////////////////////////////////////////////////
// System includes ---------------------------------------------------------
// Project includes --------------------------------------------------------
#include "SPELL_UTIL/SPELLbase.H"
#include "SPELL_UTIL/SPELLlog.H"
#include "SPELL_UTIL/SPELLutils.H"
#include "SPELL_UTIL/SPELLerror.H"
#include "SPELL_UTIL/SPELLpythonHelper.H"
#include "SPELL_WRP/SPELLconstants.H"
#include "SPELL_CFG/SPELLconfiguration.H"
#include "SPELL_EXC/SPELLcommand.H"
#include "SPELL_EXC/SPELLexecutor.H"
#include "SPELL_EXC/SPELLexecutorStatus.H"
#include "SPELL_CIF/SPELLcif.H"
// Local includes ----------------------------------------------------------
#include "SPELLshellCif.H"
#include <iomanip>



// DEFINES /////////////////////////////////////////////////////////////////
#define ITEM_COLUMN_WIDTH 25

//=============================================================================
// CONSTRUCTOR: SPELLshellCif::SPELLshellCif
//=============================================================================
SPELLshellCif::SPELLshellCif()
    : SPELLcif()
{
}

//=============================================================================
// DESTRUCTOR: SPELLshellCif::~SPELLshellCif
//=============================================================================
SPELLshellCif::~SPELLshellCif()
{
}

//=============================================================================
// METHOD: SPELLshellCif::setup
//=============================================================================
void SPELLshellCif::setup( const std::string& procId, const std::string& ctxName, int ctxPort, const std::string& timeId)
{
    SPELLcif::setup(procId, ctxName, ctxPort, timeId);
}

//=============================================================================
// METHOD: SPELLshellCif::cleanup
//=============================================================================
void SPELLshellCif::cleanup( bool force )
{
}

//=============================================================================
// METHOD: SPELLshellCif::canClose
//=============================================================================
void SPELLshellCif::canClose() {};

//=============================================================================
// METHOD: SPELLshellCif::resetClose
//=============================================================================
void SPELLshellCif::resetClose() {};

//=============================================================================
// METHOD: SPELLshellCif::waitClose
//=============================================================================
void SPELLshellCif::waitClose() {};

//=============================================================================
// METHOD: SPELLshellCif::getArguments
//=============================================================================
std::string SPELLshellCif::getArguments()
{
    return "";
}

//=============================================================================
// METHOD: SPELLshellCif::getCondition
//=============================================================================
std::string SPELLshellCif::getCondition()
{
    return "";
}

//=============================================================================
// METHOD: SPELLshellCif::isAutomatic
//=============================================================================
bool SPELLshellCif::isAutomatic()
{
    return false;
}

//=============================================================================
// METHOD: SPELLshellCif::notifyStatus
//=============================================================================
void SPELLshellCif::notifyStatus( const SPELLstatusInfo& st )
{
    std::cout << "STATUS: " << StatusToString(st.status) + " (" + st.condition + ")" << std::endl;
}

//=============================================================================
// METHOD: SPELLshellCif::notifyError
//=============================================================================
void SPELLshellCif::notifyError( const std::string& error, const std::string& reason, bool fatal )
{
    std::string fatalStr = "(Fatal:no)";
    if (fatal)
    {
        fatalStr = "(Fatal:yes)";
    }
    std::cout << "[ERROR] " << error + ": " + reason + " " + fatalStr << std::endl;
}

//=============================================================================
// METHOD: SPELLshellCif::notify
//=============================================================================
void SPELLshellCif::notify( ItemNotification notification )
{
    std::size_t pos = notification.name.find(",,");
    std::cout << "--------------------------------------------------" << std::endl;
    if ( pos == std::string::npos )
    {
        std::cout << "         "
                  << std::setw(ITEM_COLUMN_WIDTH) << std::left
                  << notification.name
                  << std::setw(ITEM_COLUMN_WIDTH) << std::left
                  << notification.value
                  << std::setw(ITEM_COLUMN_WIDTH) << std::left
                  << notification.status
                  << notification.comment << std::endl;
    }
    else
    {
        std::vector<std::string> names = tokenize(notification.name, ",,");
        std::vector<std::string> values = tokenize(notification.value, ",,");
        std::vector<std::string> status = tokenize(notification.status, ",,");
        std::vector<std::string> comments = tokenize(notification.comment, ",,");
        for( unsigned int idx = 0; idx < names.size(); idx++)
        {
            std::cout << "         "
                      << std::setw(ITEM_COLUMN_WIDTH) << std::left
                      << names[idx]
                      << std::setw(ITEM_COLUMN_WIDTH) << std::left
                      << values[idx]
                      << std::setw(ITEM_COLUMN_WIDTH) << std::left
                      << status[idx]
                      << comments[idx]  << std::endl;
        }
    }
    std::cout << "--------------------------------------------------" << std::endl;
}

//=============================================================================
// METHOD: SPELLshellCif::write
//=============================================================================
void SPELLshellCif::write( const std::string& msg, unsigned int scope )
{
    std::cout << msg << std::endl;
}

//=============================================================================
// METHOD: SPELLshellCif::warning
//=============================================================================
void SPELLshellCif::warning( const std::string& msg, unsigned int scope )
{
    std::cout << "[!] " << msg << std::endl;
}

//=============================================================================
// METHOD: SPELLshellCif::error
//=============================================================================
void SPELLshellCif::error( const std::string& msg, unsigned int scope )
{
    std::cout << "[ERROR] " << msg << std::endl;
}

//=============================================================================
// METHOD: SPELLshellCif::log
//=============================================================================
void SPELLshellCif::log( const std::string& msg )
{
    // Nothing to do
}

//=============================================================================
// METHOD: SPELLshellCif::prompt
//=============================================================================
std::string SPELLshellCif::prompt( const std::string& message, PromptOptions options, unsigned int type, unsigned int scope )
{
    std::string answer = "A";
    bool keepTrying = true;
	std::list<std::string> expected;
	unsigned int count = 0;
    while(keepTrying)
    {
        std::cout << "[?] " << message << std::endl;
        if (options.size()>0)
        {
			std::cout << "    Options: ";
			PromptOptions::const_iterator it;
			count = 1;
			expected.clear();
			for( it = options.begin(); it != options.end(); it++)
			{
				std::string key = "";
	            if ( (*it).find(":") == std::string::npos )
	            {
	                // Put an internal key in this case
	                key = ISTR(count);
	            }
	            else
	            {
	            	std::vector<std::string> tokens = tokenize( (*it), ":" );
	            	key = tokens[0];
	            	trim(key);
	            }
				expected.push_back(key);
				std::cout << (*it);
				if (count<options.size()-1) std::cout << ", ";
				count++;
			}
        }
        std::cout << std::endl << ">> ";
        char answ[512];
        std::cin.getline(answ, '\n');
        std::cout << std::endl;
        answer = answ;
        trim(answer);
        if (options.size()>0)
        {
			std::list<std::string>::const_iterator eit;
			for( eit = expected.begin(); eit != expected.end(); eit++)
			{
				if (*eit == answer )
				{
					keepTrying = false;
				}
			}
			if (keepTrying)
			{
				std::cout << "[ERROR] Expected one of the following: ";
				count = 0;
				for( eit = expected.begin(); eit != expected.end(); eit++)
				{
					std::cout << (*eit);
					if (count<expected.size()-1) std::cout << ", ";
					count++;
				}
				std::cout << std::endl;
			}
        }
        else
        {
        	keepTrying = false;
        }
    }
    return answer;
}



