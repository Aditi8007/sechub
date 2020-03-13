// SPDX-License-Identifier: MIT
package com.daimler.sechub.developertools.admin.ui.action.scheduler;

import java.awt.event.ActionEvent;

import com.daimler.sechub.developertools.admin.ui.UIContext;
import com.daimler.sechub.developertools.admin.ui.action.AbstractUIAction;

public class DisableSchedulerJobProcessingAction extends AbstractUIAction {
	private static final long serialVersionUID = 1L;

	public DisableSchedulerJobProcessingAction(UIContext context) {
		super("Disable scheduler job processing",context);
	}

	@Override
	public void execute(ActionEvent e) {
	    if (!confirm("Do you really want to disable the processing of all jobs in the queue?")) {
	        return;
	    }
	    
		String infoMessage = getContext().getAdministration().disableSchedulerJobProcessing();
		outputAsTextOnSuccess(infoMessage);
	}

}