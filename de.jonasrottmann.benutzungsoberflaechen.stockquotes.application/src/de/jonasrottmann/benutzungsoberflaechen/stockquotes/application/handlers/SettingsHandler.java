package de.jonasrottmann.benutzungsoberflaechen.stockquotes.application.handlers;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimmedWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;

public class SettingsHandler {
	@Execute
	public void execute(IEclipseContext context, MApplication app, EModelService modelService) {
		MTrimmedWindow window = (MTrimmedWindow) modelService.find("de.jonasrottmann.benutzungsoberflaechen.stockquotes.application.trimmedwindow.settings", app);
		window.setVisible(true);
	}		
}