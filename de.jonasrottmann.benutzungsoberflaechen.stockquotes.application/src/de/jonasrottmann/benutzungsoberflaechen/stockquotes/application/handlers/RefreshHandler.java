package de.jonasrottmann.benutzungsoberflaechen.stockquotes.application.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MContribution;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.widgets.Shell;

import de.jonasrottmann.benutzungsoberflaechen.stockquotes.application.parts.StockQuotesPart;

public class RefreshHandler {
	@Execute
	public void execute(IEclipseContext context, @Named(IServiceConstants.ACTIVE_SHELL) Shell shell, @Named(IServiceConstants.ACTIVE_PART) final MContribution contribution) {
		System.out.println("RefreshHandler:execute()");
		StockQuotesPart activePart = (StockQuotesPart) contribution.getObject();
		if (activePart != null) {
			activePart.refreshData();
		}
	}
}
