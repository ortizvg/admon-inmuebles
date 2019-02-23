package mx.com.admoninmuebles.constant;

import java.util.Locale;

public final class LocaleConst {
	
	private LocaleConst() throws IllegalAccessException {
		throw new IllegalAccessException();
	}
	
	public static final String LOCALE_ES = "es";
	public static final String LOCALE_EN = "en";
	
	public static final Locale LOCALE_MX = new Locale("es", "MX");

}
