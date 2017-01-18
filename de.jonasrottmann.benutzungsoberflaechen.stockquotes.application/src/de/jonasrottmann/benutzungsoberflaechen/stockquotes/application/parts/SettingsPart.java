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
	private Text txtName;
	private Text text;
	private Text text_1;
	private Text text_2;
		
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
		
		txtName = new Text(parent, SWT.BORDER);
		txtName.setText("proxy.hs-karlsruhe.de");
		txtName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblPort = new Label(parent, SWT.NONE);
		lblPort.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPort.setText("Proxyport:");
		
		text = new Text(parent, SWT.BORDER);
		text.setText("8888");
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblBenutzername = new Label(parent, SWT.NONE);
		lblBenutzername.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblBenutzername.setText("Benutzername:");
		
		text_1 = new Text(parent, SWT.BORDER);
		text_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblPasswort = new Label(parent, SWT.NONE);
		lblPasswort.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPasswort.setText("Passwort:");
		
		text_2 = new Text(parent, SWT.BORDER | SWT.PASSWORD);
		text_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
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
				List<String> userSelection = new ArrayList<>();
				
				// wenn "HTTP-Proxy verwenden" angeklickt ist, übernehmen wir die Proxy Einstellungen
				if (btnVerwenden.getSelection()) {
					// Getting all properties, which have been set by the user
					userSelection.add(txtName.getText());
					userSelection.add(text.getText());
					userSelection.add(text_1.getText());
					userSelection.add(text_2.getText());
				}
				
				Dictionary<String, Object> properties = new Hashtable<String, Object>();
				properties.put("settings", userSelection);
				
				eventBroker.post("kish/settings/updated", properties);
				
				// hide the window
				MTrimmedWindow window = (MTrimmedWindow)modelService.find("benutzungsoberflaechen.trimmedwindow.proxy-einstellungen", app);
				window.setVisible(false);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
	}
}
