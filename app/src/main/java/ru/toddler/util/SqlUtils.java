package ru.toddler.util;

import android.text.TextUtils;

public class SqlUtils {

    public static final String AND = " AND ";
    public static final String OR = " OR ";
    public static final String IN = " IN ";
    public static final String EQUAL = " = ";
    public static final String PARAM = "=? ";
    public static final String TRUE = " 1 ";
    public static final String FALSE = " 0 ";
    public static final String COMMA = ",";
    public static final String IN_DELIMITER = COMMA;
    public static final String ORDER_ASC = " ASC ";
    public static final String ORDER_DESC = " DESC ";

    public static final char SQL_STR_WRAP_SYMBOL = 39; // '

    private SqlUtils() {
    }

    public static String param(String column) {
        return column + PARAM;
    }

    public static String isTrue(String column) {
        return column + EQUAL + TRUE;
    }

    public static String isFalse(String column) {
        return column + EQUAL + FALSE;
    }

    public static String param(String column, Object value) {
        return column + EQUAL + String.valueOf(value);
    }

    public static String param(String column, String value) {
        return column + EQUAL + wrapString(value);
    }

    public static String param(String column, int value) {
        return column + EQUAL + value;
    }

    public static String param(String column, long value) {
        return column + EQUAL + value;
    }

    public static String param(String column, boolean value) {
        return column + EQUAL + (value ? TRUE:FALSE);
    }

    public static String in(String column, String values) {
        return column + IN + "(" + values + ")";
    }

    public static String in(String column, Object[] array) {
        return in(column, ConvertUtils.join(array, IN_DELIMITER));
    }

    public static String in(String column, Iterable<?> iterable) {
        return in(column, ConvertUtils.join(iterable, IN_DELIMITER));
    }

    public static String in(String column, long[] array) {
        return in(column, ConvertUtils.join(array, IN_DELIMITER));
    }

    public static String in(String column, int[] array) {
        return in(column, ConvertUtils.join(array, IN_DELIMITER));
    }

    public static <T extends CharSequence> String inStrings(String column, T[] array) {
        StringBuilder sb = new StringBuilder();
        boolean firstTime = true;
        for (T token: array) {
            if (firstTime) {
                firstTime = false;
            } else {
                sb.append(IN_DELIMITER);
            }
            wrapString(sb, token);
        }
        return in(column, sb.toString());
    }

    public static <T extends CharSequence> String inStrings(String column, Iterable<T> iterable) {
        StringBuilder sb = new StringBuilder();
        boolean firstTime = true;
        for (T token: iterable) {
            if (firstTime) {
                firstTime = false;
            } else {
                sb.append(IN_DELIMITER);
            }
            wrapString(sb, token);
        }
        return in(column, sb.toString());
    }

    private static void wrapString(StringBuilder sb, CharSequence charSequence) {
        sb.append(SQL_STR_WRAP_SYMBOL).append(charSequence).append(SQL_STR_WRAP_SYMBOL);
    }

    private static String wrapString(String string) {
        String resultString = NpeUtils.getNonNull(string);
        int length = resultString.length();
        if (length > 2
                && resultString.charAt(0) == resultString.charAt(length-1)
                && resultString.charAt(0) == SQL_STR_WRAP_SYMBOL) {
            return resultString;
        }
        return SQL_STR_WRAP_SYMBOL + resultString + SQL_STR_WRAP_SYMBOL;
    }

    public static String and(String... expression) {
        return join(AND, expression);
    }

    public static String or(String... expression) {
        return join(OR, expression);
    }

    private static String join(String delimiter, String[] expressions) {
        if (NpeUtils.isEmpty(expressions)) {
            return delimiter;
        } else {
            return TextUtils.join(delimiter, expressions);
        }
    }

    public static String orderJoin(String... orderExp) {
        return join(COMMA, orderExp);
    }

    public static String orderAsc(String... columns) {
        return join(COMMA, columns) + ORDER_ASC;
    }

    public static String orderDesc(String... columns) {
        return join(COMMA, columns) + ORDER_DESC;
    }
}
