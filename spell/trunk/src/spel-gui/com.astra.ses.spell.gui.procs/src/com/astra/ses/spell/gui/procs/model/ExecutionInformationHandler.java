////////////////////////////////////////////////////////////////////////////////
//
// PACKAGE   : com.astra.ses.spell.gui.procs.model
// 
// FILE      : ExecutionInformationHandler.java
//
// DATE      : 2010-07-30
//
// Copyright (C) 2008, 2012 SES ENGINEERING, Luxembourg S.A.R.L.
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
////////////////////////////////////////////////////////////////////////////////
package com.astra.ses.spell.gui.procs.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import com.astra.ses.spell.gui.core.interfaces.IExecutorInfo;
import com.astra.ses.spell.gui.core.interfaces.IProcedureClient;
import com.astra.ses.spell.gui.core.model.notification.DisplayData;
import com.astra.ses.spell.gui.core.model.notification.ErrorData;
import com.astra.ses.spell.gui.core.model.notification.UserActionNotification.UserActionStatus;
import com.astra.ses.spell.gui.core.model.server.ExecutorConfig;
import com.astra.ses.spell.gui.core.model.types.ClientMode;
import com.astra.ses.spell.gui.core.model.types.ExecutorStatus;
import com.astra.ses.spell.gui.core.model.types.Level;
import com.astra.ses.spell.gui.core.model.types.Severity;
import com.astra.ses.spell.gui.core.utils.Logger;
import com.astra.ses.spell.gui.procs.interfaces.model.priv.IExecutionInformationHandler;

/*******************************************************************************
 * 
 * RuntimeModel holds the procedure execution trace
 * 
 ******************************************************************************/
class ExecutionInformationHandler implements IExecutionInformationHandler
{

	/** Display messages */
	private Collection<DisplayData> m_displayMessages;
	/** Error messages */
	private ErrorData m_error;
	/** Executor status */
	private ExecutorStatus m_status;
	/** Holds the current stage identifier if any */
	private String m_stageId;
	/** Holds the current stage name if any */
	private String m_stageTitle;
	/** User action name */
	private String m_userAction;
	/** Current user action status */
	private UserActionStatus m_userActionStatus;
	/** User action severity */
	private Severity m_userActionSeverity;
	/** Step over execution mode */
	private StepOverMode m_soMode;
	/** Previous so mode */
	private StepOverMode m_soPrevious;
	/** By step execution mode */
	private boolean m_byStep;
	/** Holds the show lib state */
	private boolean m_showLib;
	/** TC confirmation flag */
	private boolean m_tcConfirmation;
	/** Holds the execution delay in secs */
	private int m_execDelay;
	/** Mode of this client for this executor */
	private ClientMode m_mode;
	/** Holds the list of monitoring clients, if any */
	private IProcedureClient[] m_monitoringClients;
	/** Holds the execution condition */
	private String m_condition;
	/** Holds the controlling client if any */
	private IProcedureClient m_controllingClient;
	/** True if the procedure is started in visible mode */
	private boolean m_visible;
	/** True if the procedure is started in automatic mode */
	private boolean m_automatic;
	/** True if the procedure is started in blocking mode */
	private boolean m_blocking;
	/** Waiting input flag */
	private boolean m_waitingInput;
	/** Executor lost flag */
	private boolean m_executorLost;
	/** Parent procedure */
	private String m_parent;

	/***************************************************************************
	 * Constructor
	 **************************************************************************/
	public ExecutionInformationHandler(ClientMode mode)
	{
		m_displayMessages = new Vector<DisplayData>();
		m_error = null;
		m_status = ExecutorStatus.UNKNOWN;
		m_stageId = null;
		m_stageTitle = null;
		m_userAction = null;
		m_userActionStatus = UserActionStatus.DISMISSED;
		m_userActionSeverity = Severity.INFO;
		m_soMode = null;
		m_soPrevious = null;
		m_byStep = false;
		m_showLib = false;
		m_tcConfirmation = false;
		m_execDelay = 0;
		m_condition = null;
		m_controllingClient = null;
		m_monitoringClients = null;
		m_mode = mode;
		m_visible = true;
		m_automatic = true;
		m_blocking = true;
		m_waitingInput = false;
		m_executorLost = false;
		m_parent = "";
	}

	@Override
	public ClientMode getClientMode()
	{
		return m_mode;
	}

	@Override
	public String getCondition()
	{
		return m_condition;
	}

	@Override
	public IProcedureClient getControllingClient()
	{
		return m_controllingClient;
	}

	@Override
	public ErrorData getError()
	{
		return m_error;
	}

	@Override
	public boolean isExecutorLost()
	{
		return m_executorLost;
	}

	@Override
	public IProcedureClient[] getMonitoringClients()
	{
		return m_monitoringClients;
	}

	@Override
	public int getExecutionDelay()
	{
		return m_execDelay;
	}

	@Override
	public String getParent()
	{
		return m_parent;
	}

	@Override
	public ExecutorStatus getStatus()
	{
		return m_status;
	}

	@Override
	public DisplayData[] getDisplayMessages()
	{
		return m_displayMessages.toArray(new DisplayData[0]);
	}

	@Override
	public String getStageId()
	{
		return m_stageId;
	}

	@Override
	public String getStageTitle()
	{
		return m_stageTitle;
	}

	@Override
	public String getUserAction()
	{
		return m_userAction;
	}

	@Override
	public Severity getUserActionSeverity()
	{
		return m_userActionSeverity;
	}

	@Override
	public UserActionStatus getUserActionStatus()
	{
		return m_userActionStatus;
	}

	@Override
	public boolean isAutomatic()
	{
		return m_automatic;
	}

	@Override
	public boolean isBlocking()
	{
		return m_blocking;
	}

	@Override
	public boolean isShowLib()
	{
		return m_showLib;
	}

	@Override
	public boolean isStepByStep()
	{
		return m_byStep;
	}

	@Override
	public void setStepOverMode(StepOverMode mode)
	{
		m_soPrevious = m_soMode;
		m_soMode = mode;
	}

	@Override
	public void setExecutorLost()
	{
		m_executorLost = true;
	}

	@Override
	public void resetStepOverMode()
	{
		if (m_soPrevious != null)
		{
			switch (m_soMode)
			{
			case STEP_INTO_ONCE:
			case STEP_OVER_ONCE:
				m_soMode = m_soPrevious;
				break;
			default:
				break;
			}
		}
	}

	@Override
	public StepOverMode getStepOverMode()
	{
		return m_soMode;
	}

	@Override
	public boolean isVisible()
	{
		return m_visible;
	}

	@Override
	public boolean isWaitingInput()
	{
		return m_waitingInput;
	}

	@Override
	public void displayMessage(DisplayData data)
	{
		m_displayMessages.add(data);
	}

	@Override
	public void setExecutorStatus(ExecutorStatus status)
	{
		Logger.debug("Execution information new status: " + status, Level.PROC, this);
		m_status = status;
	}

	@Override
	public void setStage(String id, String title)
	{
		m_stageId = id;
		m_stageTitle = title;
	}

	@Override
	public void setUserActionStatus(UserActionStatus status, Severity severity)
	{
		m_userActionStatus = status;
		m_userActionSeverity = severity;
	}

	@Override
	public void setError(ErrorData error)
	{
		m_error = error;
	}

	@Override
	public void setStepByStep(boolean value)
	{
		m_byStep = value;
	}

	@Override
	public void setShowLib(boolean value)
	{
		m_showLib = value;
	}

	@Override
	public boolean isForceTcConfirmation()
	{
		return m_tcConfirmation;
	}

	@Override
	public void setForceTcConfirmation(boolean value)
	{
		m_tcConfirmation = value;
	}

	@Override
	public void setExecutionDelay(int msec)
	{
		m_execDelay = msec;
	}

	@Override
	public void setAutomatic(boolean automatic)
	{
		m_automatic = automatic;
	}

	@Override
	public void setBlocking(boolean blocking)
	{
		m_blocking = blocking;
	}

	@Override
	public void setCondition(String condition)
	{
		m_condition = condition;
	}

	@Override
	public void setClientMode(ClientMode mode)
	{
		m_mode = mode;
	}

	@Override
	public void setControllingClient(IProcedureClient client)
	{
		m_controllingClient = client;
	}

	@Override
	public void setMonitoringClients(IProcedureClient[] clients)
	{
		if (clients == null)
		{
			m_monitoringClients = new IProcedureClient[0];
		}
		else
		{
			m_monitoringClients = Arrays.copyOf(clients, clients.length);
		}
	}

	/***************************************************************************
	 * Add a client to the list of current monitoring clients
	 **************************************************************************/
	@Override
	public void addMonitoringClient(IProcedureClient client)
	{
		if (m_monitoringClients == null)
		{
			m_monitoringClients = new IProcedureClient[1];
			m_monitoringClients[0] = client;
		}
		else
		{
			for (IProcedureClient clt : m_monitoringClients)
			{
				if (clt.getKey().equals(client.getKey()))
					return;
			}
			List<IProcedureClient> aux = new LinkedList<IProcedureClient>();
			aux.addAll(Arrays.asList(m_monitoringClients));
			aux.add(client);
			m_monitoringClients = aux.toArray(new IProcedureClient[0]);
		}
	}

	/***************************************************************************
	 * Remove a client from the list of current monitoring clients
	 **************************************************************************/
	@Override
	public void removeMonitoringClient(IProcedureClient client)
	{
		if (m_monitoringClients == null)
		{
			return;
		}
		else
		{
			List<IProcedureClient> aux = new LinkedList<IProcedureClient>();
			aux.addAll(Arrays.asList(m_monitoringClients));
			IProcedureClient toDelete = null;
			for (IProcedureClient clt : aux)
			{
				if (clt.getKey().equals(client.getKey()))
				{
					toDelete = clt;
					break;
				}
			}
			if (toDelete != null)
			{
				aux.remove(toDelete);
				m_monitoringClients = aux.toArray(new IProcedureClient[0]);
			}
		}
	}

	@Override
	public void setParent(String parentId)
	{
		m_parent = parentId;
	}

	@Override
	public void setVisible(boolean visible)
	{
		m_visible = visible;
	}

	@Override
	public void setWaitingInput(boolean waiting)
	{
		Logger.debug("Procedure waiting input: " + waiting, Level.PROC, this);
		m_waitingInput = waiting;
	}

	@Override
	public void visit(IExecutorInfo info)
	{
		info.setAutomatic(m_automatic);
		info.setBlocking(m_blocking);
		info.setCondition(m_condition);
		info.setControllingClient(m_controllingClient);
		info.setError(m_error);
		info.setMode(m_mode);
		info.setMonitoringClients(m_monitoringClients);
		info.setStage(m_stageId, m_stageTitle);
		info.setStatus(m_status);
		info.setUserAction(m_userAction);
		info.setUserActionEnabled(m_userActionStatus.equals(UserActionStatus.ENABLED));
		info.setUserActionSeverity(m_userActionSeverity);
		info.setVisible(m_visible);
		info.setParent(m_parent);
	}

	@Override
	public void visit(ExecutorConfig config)
	{
		config.setBrowsableLib(m_showLib);
		config.setExecDelay(m_execDelay);
		config.setRunInto(m_soMode.equals(StepOverMode.STEP_INTO_ALWAYS));
		config.setStepByStep(m_byStep);
		config.setTcConfirmation(m_tcConfirmation);
	}

	@Override
	public void copyFrom(IExecutorInfo info)
	{
		setAutomatic(info.getAutomatic());
		setBlocking(info.getBlocking());
		setCondition(info.getCondition());
		setControllingClient(info.getControllingClient());
		if (getError() == null)
		{
			setError(info.getError());
		}
		else
		{
			if (info.getError() != null)
				setError(info.getError());
		}
		setMonitoringClients(info.getMonitoringClients());
		setStage(info.getStageId(), info.getStageTitle());
		setExecutorStatus(info.getStatus());
		setParent(info.getParent());
	}

	@Override
	public void copyFrom(ExecutorConfig config)
	{
		setShowLib(config.getBrowsableLib());
		setExecutionDelay(config.getExecDelay());
		if (config.getRunInto())
		{
			setStepOverMode(StepOverMode.STEP_INTO_ALWAYS);
		}
		else
		{
			setStepOverMode(StepOverMode.STEP_OVER_ALWAYS);
		}
		setStepByStep(config.getStepByStep());
		setForceTcConfirmation(config.getTcConfirmation());
	}

	@Override
	/***************************************************************************
	 * NOTE: ClientMode keeps the same value
	 **************************************************************************/
	public void reset()
	{
		m_displayMessages.clear();
		m_error = null;
		m_status = ExecutorStatus.UNKNOWN;
		m_stageId = null;
		m_stageTitle = null;
		m_userAction = null;
		m_userActionStatus = UserActionStatus.DISMISSED;
		m_userActionSeverity = Severity.INFO;
		m_showLib = false;
		m_tcConfirmation = false;
		m_condition = null;
		m_controllingClient = null;
		m_monitoringClients = null;
		m_visible = true;
		m_automatic = true;
		m_blocking = true;
		m_waitingInput = false;
		m_parent = "";
	}
}
