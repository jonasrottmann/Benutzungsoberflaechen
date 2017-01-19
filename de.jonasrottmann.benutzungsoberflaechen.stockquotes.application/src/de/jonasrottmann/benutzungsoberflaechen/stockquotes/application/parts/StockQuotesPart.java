package de.jonasrottmann.benutzungsoberflaechen.stockquotes.application.parts;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import de.hska.iwii.stockquotes.net.IStockQuotes;
import de.hska.iwii.stockquotes.net.StockData;
import de.hska.iwii.stockquotes.net.StockData.CurrentPriceChange;
import de.hska.iwii.stockquotes.net.simulation.StockQuotesSimulation;
import de.hska.iwii.stockquotes.net.yahoo.StockQuotesYahoo;
import de.jonasrottmann.benutzungsoberflaechen.stockquotes.application.contentProviders.StockDataContentProvider;

public class StockQuotesPart implements EventHandler {

	// Constants
	private static String DATA_SOURCE_SIMULATION = "Simulation";
	private static String DATA_SOURCE_YAHOO = "Yahoo";

	private ProxySettings settings;
	
	// Views
	private TableViewer tableViewer;
	private Text text;
	private Combo combo;
	private Combo combo_1;
	private Table table;
	private ProgressBar progressBar;
	
	// StockData
	private IStockQuotes stockQuotesSource;
	private List<StockData> stockDataModel = new ArrayList<>();
	@Inject
	private StockQuotesSimulation simulationSource;
	@Inject
	private StockQuotesYahoo yahooSource;
	
	// Listener
	private SelectionListener comboboxSelectionListener = new SelectionListener() {
		@Override
		public void widgetSelected(SelectionEvent e) {				
			if (stockQuotesSource != null) {
				stockQuotesSource.reset();
			}
			stockDataModel.clear();
			tableViewer.refresh();
		}
		
		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			
		}
	};
	@Inject
	private IEventBroker broker;
	
	@Inject
	private MDirtyable dirty;

	@PostConstruct
	public void createComposite(Composite parent) {
		broker.subscribe("de/hska/iwii/*", this);
		broker.subscribe("de/jonasrottmann/benutzungsoberflaechen/*", this);
		
		parent.setLayout(new GridLayout(1, false));

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Composite composite_1 = new Composite(composite, SWT.NONE);
		composite_1.setLayout(new GridLayout(4, false));
		
		Label label = new Label(composite_1, SWT.NONE);
		label.setAlignment(SWT.RIGHT);
		label.setText("Index:");
		
		combo = new Combo(composite_1, SWT.READ_ONLY);
		combo.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		combo.setItems(IStockQuotes.ALL_STOCK_INDEXES);
		combo.select(0);
		combo.addSelectionListener(comboboxSelectionListener);
		
		Label label_1 = new Label(composite_1, SWT.NONE);
		label_1.setAlignment(SWT.RIGHT);
		label_1.setText("Filter:");
		
		text = new Text(composite_1, SWT.BORDER);
		
		Composite composite_2 = new Composite(composite, SWT.NONE);
		composite_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		RowLayout rl_composite_2 = new RowLayout(SWT.HORIZONTAL);
		rl_composite_2.center = true;
		composite_2.setLayout(rl_composite_2);
		
		Label label_2 = new Label(composite_2, SWT.NONE);
		label_2.setAlignment(SWT.RIGHT);
		label_2.setText(Messages.SamplePart_dataSource);
		
		combo_1 = new Combo(composite_2, SWT.READ_ONLY);
		combo_1.setItems(new String[] {DATA_SOURCE_SIMULATION, DATA_SOURCE_YAHOO});
		combo_1.select(0);
		combo_1.addSelectionListener(comboboxSelectionListener);
		
		Composite composite_3 = new Composite(parent, SWT.NONE);
		composite_3.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		TableColumnLayout tcl_composite_3 = new TableColumnLayout();
		composite_3.setLayout(tcl_composite_3);
		
		this.tableViewer = new TableViewer(composite_3, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		tableViewerColumn.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				StockData data = (StockData) element;
				return data.getCompanyShortName();
			}
		});
		TableColumn tblclmnNewColumn = tableViewerColumn.getColumn();
		tcl_composite_3.setColumnData(tblclmnNewColumn, new ColumnPixelData(30, true, true));
		tblclmnNewColumn.setText(Messages.SamplePart_shortName);
		
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewer, SWT.NONE);
		tableViewerColumn_1.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				StockData data = (StockData) element;
				return data.getCompanyName();
			}
		});
		TableColumn tblclmnNewColumn_1 = tableViewerColumn_1.getColumn();
		tcl_composite_3.setColumnData(tblclmnNewColumn_1, new ColumnPixelData(150, true, true));
		tblclmnNewColumn_1.setText(Messages.SamplePart_company);
		
		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(tableViewer, SWT.NONE);
		tableViewerColumn_2.setLabelProvider(new ColumnLabelProvider() {
			
			public String getText(Object element) {
//				final String format = "$###,###.###";
				NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.GERMANY);
				DecimalFormat df = (DecimalFormat) nf;
//				df.applyPattern(format);
				df.setCurrency(Currency.getInstance(Locale.US));

				String output = ""; 
				if (((StockData) element).getCurrentPrice() instanceof Double) {
					output = df.format(((StockData) element).getCurrentPrice());
				}
				return output;
			}

			public Color getBackground(Object element) {
				StockData data = (StockData) element;
				CurrentPriceChange change = data.getPriceChange();
				switch (change) {
				case UP:
					return Display.getDefault().getSystemColor(SWT.COLOR_GREEN);
				case DOWN:
					return Display.getDefault().getSystemColor(SWT.COLOR_RED);
				default:
					return super.getBackground(element);
				}
			}
		});
		TableColumn tblclmnNewColumn_2 = tableViewerColumn_2.getColumn();
		tcl_composite_3.setColumnData(tblclmnNewColumn_2, new ColumnPixelData(90, true, true));
		tblclmnNewColumn_2.setText(Messages.SamplePart_stock);
		
		TableViewerColumn tableViewerColumn_3 = new TableViewerColumn(tableViewer, SWT.NONE);
		tableViewerColumn_3.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				return String.valueOf(((StockData) element).getDayHighPrice());
			}
			
			@Override
			public Color getBackground(Object element) {
				StockData data = (StockData) element;
				switch (data.getPriceChange()) {
				case UP:
					return Display.getCurrent().getSystemColor(SWT.COLOR_GREEN);
				case DOWN:
					return Display.getCurrent().getSystemColor(SWT.COLOR_RED);
				default:
					return Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
				}

			}
		});
		TableColumn tblclmnTageshchststand = tableViewerColumn_3.getColumn();
		tcl_composite_3.setColumnData(tblclmnTageshchststand, new ColumnPixelData(90, true, true));
		tblclmnTageshchststand.setText(Messages.SamplePart_highStock);
		
		TableViewerColumn tableViewerColumn_4 = new TableViewerColumn(tableViewer, SWT.NONE);
		tableViewerColumn_4.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				return String.valueOf(((StockData) element).getDayLowPrice());
			}
		});
		TableColumn tblclmnNewColumn_3 = tableViewerColumn_4.getColumn();
		tcl_composite_3.setColumnData(tblclmnNewColumn_3, new ColumnPixelData(90, true, true));
		tblclmnNewColumn_3.setText(Messages.SamplePart_lowStock);
		
		TableViewerColumn tableViewerColumn_5 = new TableViewerColumn(tableViewer, SWT.NONE);
		tableViewerColumn_5.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				return String.valueOf(((StockData) element).getDate());
			}
		});
		TableColumn tblclmnNewColumn_4 = tableViewerColumn_5.getColumn();
		tcl_composite_3.setColumnData(tblclmnNewColumn_4, new ColumnPixelData(90, true, true));
		tblclmnNewColumn_4.setText(Messages.SamplePart_date);
		
		TableViewerColumn tableViewerColumn_6 = new TableViewerColumn(tableViewer, SWT.NONE);
		tableViewerColumn_6.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				return String.valueOf(((StockData) element).getLastClosePrice());
			}
		});
		TableColumn tblclmnNewColumn_5 = tableViewerColumn_6.getColumn();
		tcl_composite_3.setColumnData(tblclmnNewColumn_5, new ColumnPixelData(90, true, true));
		tblclmnNewColumn_5.setText(Messages.SamplePart_endStock);
		
		TableViewerColumn tableViewerColumn_7 = new TableViewerColumn(tableViewer, SWT.NONE);
		tableViewerColumn_7.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				return String.valueOf(((StockData) element).getLastTradeTime());
			}
		});
		TableColumn tblclmnNewColumn_6 = tableViewerColumn_7.getColumn();
		tcl_composite_3.setColumnData(tblclmnNewColumn_6, new ColumnPixelData(90, true, true));
		tblclmnNewColumn_6.setText(Messages.SamplePart_endDate);
		tableViewer.setContentProvider(new StockDataContentProvider());
		
		Composite composite_4 = new Composite(parent, SWT.NONE);
		composite_4.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		RowLayout rl_composite_4 = new RowLayout(SWT.HORIZONTAL);
		rl_composite_4.wrap = false;
		rl_composite_4.center = true;
		composite_4.setLayout(rl_composite_4);
		
		progressBar = new ProgressBar(composite_4, SWT.INDETERMINATE);
		progressBar.setVisible(false);
		
		Label lblStatus = new Label(composite_4, SWT.NONE);
		lblStatus.setText("Status");
	}

	@Focus
	public void setFocus() {
		tableViewer.getTable().setFocus();
	}

	@Persist
	public void save() {
		dirty.setDirty(false);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handleEvent(Event event) {
		System.out.println("StockQuotesPart:handleEvent()");

		switch (event.getTopic()) {
		case "de/hska/iwii/stockdata":
			stockDataModel = (List<StockData>) event.getProperty("stockdata");
			tableViewer.setInput(stockDataModel);
			progressBar.setVisible(false);
			break;
		case "de/jonasrottmann/benutzungsoberflaechen/stockquotes/application/settings/updated":
				settings = event.containsProperty("url") ? 
						new ProxySettings((String) event.getProperty("url"), 
								Integer.parseInt((String) event.getProperty("port")),
								(String) event.getProperty("username"), 
								(String) event.getProperty("password")) 
						: null;		
			break;
		}
	}

	public void refreshData() {
		System.out.println("StockQuotesPart:refreshData()");
		
		String selectedDataSourceKey = combo_1.getText();
		if (selectedDataSourceKey.equals(DATA_SOURCE_SIMULATION)) {
			stockQuotesSource = simulationSource;
		}
		if (selectedDataSourceKey.equals(DATA_SOURCE_YAHOO)) {
			stockQuotesSource = yahooSource;
			if (!(settings == null)) {
				stockQuotesSource.setProxy(settings.getUrl(), settings.getPort());
				stockQuotesSource.setProxyAuthentication(settings.getUsername(), settings.getPassword());
			}
		}
				
		stockQuotesSource.requestDataAsync(combo.getText());
		tableViewer.setInput(stockDataModel);
		progressBar.setVisible(true);
	}
	
	private static class ProxySettings {
		private final String url;
		private final int port;
		private final String username;
		private final String password;
		
		public ProxySettings(String url, int port, String username, String password) {
			super();
			this.url = url;
			this.port = port;
			this.username = username;
			this.password = password;
		}
		
		public String getUrl() {
			return url;
		}
		
		public int getPort() {
			return port;
		}
		
		public String getUsername() {
			return username;
		}
		
		public String getPassword() {
			return password;
		}
		
		@Override
		public String toString() {
			return String.format("{ url: %s; port: %s; username %s; password: %s }", this.url, this.port, this.username, this.password);
		}
	}
}