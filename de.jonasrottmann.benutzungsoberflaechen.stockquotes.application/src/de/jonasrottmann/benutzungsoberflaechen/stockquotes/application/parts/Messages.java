package de.jonasrottmann.benutzungsoberflaechen.stockquotes.application.parts;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "de.jonasrottmann.benutzungsoberflaechen.stockquotes.application.parts.messages";
	public static String SamplePart_highStock;
	public static String SamplePart_lowStock;
	public static String SamplePart_date;
	public static String SamplePart_endStock;
	public static String SamplePart_endDate;
	public static String SamplePart_dataSource;
	public static String SamplePart_shortName;
	public static String SamplePart_company;
	public static String SamplePart_stock;

	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
