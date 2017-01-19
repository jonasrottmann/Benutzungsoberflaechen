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
	public static String StockQuotesPart_index;
	public static String StockQuotesPart_filter;
	
	public static String SettingsPart_url;
	public static String SettingsPart_port;
	public static String SettingsPart_username;
	public static String SettingsPart_password;
	public static String SettingsPart_apply;
	public static String SettingsPart_proxyurl;
	public static String SettingsPart_proxyport;
	public static String SettingsPart_submit;
	
	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
