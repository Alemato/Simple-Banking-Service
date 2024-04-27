package it.univaq.sose.simplebankingsoapservice.util;

import java.util.Map;

public final class JAASUtils {

    private JAASUtils() {
    }

    public static String getString(Map<String, ?> options, String key) {
        Object val = options.get(key);
        if (val instanceof String) {
            val = ((String) val).trim();
        }
        return (String) val;
    }
}
