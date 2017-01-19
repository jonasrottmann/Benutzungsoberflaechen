package de.jonasrottmann.benutzungsoberflaechen.stockquotes.application.parts;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "de.jonasrottmann.benutzungsoberflaechen.stockquotes.application.parts.messages";
	public static String StockQuotesPart_highStock;
	public static String StockQuotesPart_lowStock;
	public static String StockQuotesPart_date;
	public static String StockQuotesPart_endStock;
	public static String StockQuotesPart_endDate;
	public static String StockQuotesPart_dataSource;
	public static String StockQuotesPart_shortName;
	public static String StockQuotesPart_company;
	public static String StockQuotesPart_stock;
	
	public static String SettingsPart_url;

	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
