package de.jonasrottmann.benutzungsoberflaechen.stockquotes.application.parts;

import java.util.Dictionary;
import java.util.Hashtable;

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
		
		Label labelHTTP = new Label(parent, SWT.NONE);
		labelHTTP.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		labelHTTP.setText("HTTP-Proxy:");
		
		Button checkboxUseProxy = new Button(parent, SWT.CHECK);
		checkboxUseProxy.setText(Messages.SettingsPart_apply);
		
		Label labelURL = new Label(parent, SWT.NONE);
		labelURL.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		labelURL.setText(Messages.SettingsPart_url);
		
		url = new Text(parent, SWT.BORDER);
		url.setText(Messages.SettingsPart_proxyurl);
		url.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label labelPort = new Label(parent, SWT.NONE);
		labelPort.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		labelPort.setText(Messages.SettingsPart_port);
		
		port = new Text(parent, SWT.BORDER);
		port.setText(Messages.SettingsPart_proxyport);
		port.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label labelUsername = new Label(parent, SWT.NONE);
		labelUsername.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		labelUsername.setText(Messages.SettingsPart_username);
		
		username = new Text(parent, SWT.BORDER);
		username.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label labelPassword = new Label(parent, SWT.NONE);
		labelPassword.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		labelPassword.setText(Messages.SettingsPart_password);
		
		password = new Text(parent, SWT.BORDER | SWT.PASSWORD);
		password.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Composite composite = new Composite(parent, SWT.NONE);
		GridData gd_composite = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 2, 1);
		gd_composite.verticalIndent = 30;
		composite.setLayoutData(gd_composite);
		composite.setLayout(new RowLayout(SWT.HORIZONTAL));
		
		Button buttonSubmit = new Button(composite, SWT.NONE);
		buttonSubmit.setText(Messages.SettingsPart_submit);
		buttonSubmit.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Dictionary<String, String> properties = null;
				if (checkboxUseProxy.getSelection()) {
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
