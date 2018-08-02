package misat11.za.utils;

import misat11.za.Main;

public class I18n {
	public static String _(String key) {
		return _(key, true);
	}

	public static String _(String key, boolean prefix) {
		String value = "";
		if (prefix) {
			value += Main.getConfigurator().config.getString("message_prefix") + " ";
		}
		if (Main.getConfigurator().config.isSet("message_" + key)) {
			value += Main.getConfigurator().config.getString("message_" + key);
		} else {
			value += key;
		}
		return value;
	}
}
