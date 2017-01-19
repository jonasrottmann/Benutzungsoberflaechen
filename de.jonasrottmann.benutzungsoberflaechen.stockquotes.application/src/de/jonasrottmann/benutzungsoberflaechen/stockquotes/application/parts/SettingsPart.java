package de.jonasrottmann.benutzungsoberflaechen.stockquotes.application.parts;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimmedWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class SettingsPart {
	@Inject
	private IEventBroker eventBroker;
	
	// Views
	private Text url;
	private Text port;
	private Text username;
	private Text password;
		
	@PostConstruct
	public void createComposite(Composite parent, MApplication app, EModelService modelService) {
		parent.setLayout(new GridLayout(2, false));
		
		Label lblNewLabel = new Label(parent, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("HTTP-Proxy:");
		
		Button btnVerwenden = new Button(parent, SWT.CHECK);
		btnVerwenden.setText("verwenden");
		
		Label lblNewLabel_1 = new Label(parent, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_1.setText("Proxyname:");
		
		url = new Text(parent, SWT.BORDER);
		url.setText("proxy.hs-karlsruhe.de");
		url.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblPort = new Label(parent, SWT.NONE);
		lblPort.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPort.setText("Proxyport:");
		
		port = new Text(parent, SWT.BORDER);
		port.setText("8888");
		port.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblBenutzername = new Label(parent, SWT.NONE);
		lblBenutzername.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblBenutzername.setText("Benutzername:");
		
		username = new Text(parent, SWT.BORDER);
		username.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblPasswort = new Label(parent, SWT.NONE);
		lblPasswort.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPasswort.setText("Passwort:");
		
		password = new Text(parent, SWT.BORDER | SWT.PASSWORD);
		password.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Composite composite = new Composite(parent, SWT.NONE);
		GridData gd_composite = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 2, 1);
		gd_composite.verticalIndent = 30;
		composite.setLayoutData(gd_composite);
		composite.setLayout(new RowLayout(SWT.HORIZONTAL));
		
		Button btnbernehmen = new Button(composite, SWT.NONE);
		btnbernehmen.setText("Übernehmen");
		btnbernehmen.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Dictionary<String, String> properties = null;
				if (btnVerwenden.getSelection()) {
					properties = new Hashtable<>();
					properties.put("url", url.getText());
					properties.put("port", port.getText());
					properties.put("username", username.getText());
					properties.put("password", password.getText());
				}
				
				eventBroker.post("de/jonasrottmann/benutzungsoberflaechen/stockquotes/application/settings/updated", properties);
				
				MTrimmedWindow window = (MTrimmedWindow) modelService.find("de.jonasrottmann.benutzungsoberflaechen.stockquotes.application.trimmedwindow.settings", app);
				window.setVisible(false);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});
	}
}
