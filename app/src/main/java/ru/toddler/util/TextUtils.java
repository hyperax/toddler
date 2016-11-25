package ru.toddler.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


public class TextUtils {

    public static final String LINE_SEPARATOR = SystemUtils.getSystemProperty("line.separator");

    public static final String EMPTY_STRING = "";
    public static final String SPACE = " ";

    private TextUtils() {
    }

    /**
     * <p>Wraps a single line of text, identifying words by <code>' '</code>.</p>
     * <p>
     * <p>Leading spaces on a new line are stripped.
     * Trailing spaces are not stripped.</p>
     * <p>
     * <table border="1" summary="Wrap Results">
     * <tr>
     * <th>input</th>
     * <th>wrapLenght</th>
     * <th>newLineString</th>
     * <th>wrapLongWords</th>
     * <th>result</th>
     * </tr>
     * <tr>
     * <td>null</td>
     * <td>*</td>
     * <td>*</td>
     * <td>true/false</td>
     * <td>null</td>
     * </tr>
     * <tr>
     * <td>""</td>
     * <td>*</td>
     * <td>*</td>
     * <td>true/false</td>
     * <td>""</td>
     * </tr>
     * <tr>
     * <td>"Here is one line of text that is going to be wrapped after 20 columns."</td>
     * <td>20</td>
     * <td>"\n"</td>
     * <td>true/false</td>
     * <td>"Here is one line of\ntext that is going\nto be wrapped after\n20 columns."</td>
     * </tr>
     * <tr>
     * <td>"Here is one line of text that is going to be wrapped after 20 columns."</td>
     * <td>20</td>
     * <td>"&lt;br /&gt;"</td>
     * <td>true/false</td>
     * <td>"Here is one line of&lt;br /&gt;text that is going&lt;br /&gt;to be wrapped after&lt;br /&gt;20 columns."</td>
     * </tr>
     * <tr>
     * <td>"Here is one line of text that is going to be wrapped after 20 columns."</td>
     * <td>20</td>
     * <td>null</td>
     * <td>true/false</td>
     * <td>"Here is one line of" + systemNewLine + "text that is going" + systemNewLine + "to be wrapped after" + systemNewLine + "20 columns."</td>
     * </tr>
     * <tr>
     * <td>"Click here to jump to the commons website - http://commons.apache.org"</td>
     * <td>20</td>
     * <td>"\n"</td>
     * <td>false</td>
     * <td>"Click here to jump\nto the commons\nwebsite -\nhttp://commons.apache.org"</td>
     * </tr>
     * <tr>
     * <td>"Click here to jump to the commons website - http://commons.apache.org"</td>
     * <td>20</td>
     * <td>"\n"</td>
     * <td>true</td>
     * <td>"Click here to jump\nto the commons\nwebsite -\nhttp://commons.apach\ne.org"</td>
     * </tr>
     * </table>
     *
     * @param str           the String to be word wrapped, may be null
     * @param wrapLength    the column to wrap the words at, less than 1 is treated as 1
     * @param newLineStr    the string to insert for a new line,
     *                      <code>null</code> uses the system property line separator
     * @param wrapLongWords true if long words (such as URLs) should be wrapped
     * @return a line with newlines inserted, <code>null</code> if null input
     */
    @NonNull
    public static String wrap(final String str, int wrapLength, String newLineStr, final boolean wrapLongWords) {
        if (str == null) {
            return EMPTY_STRING;
        }
        if (newLineStr == null) {
            newLineStr = LINE_SEPARATOR;
        }
        if (wrapLength < 1) {
            wrapLength = 1;
        }
        final int inputLineLength = str.length();
        int offset = 0;
        final StringBuilder wrappedLine = new StringBuilder(inputLineLength + 32);

        while (inputLineLength - offset > wrapLength) {
            if (str.charAt(offset) == ' ') {
                offset++;
                continue;
            }
            int spaceToWrapAt = str.lastIndexOf(' ', wrapLength + offset);

            if (spaceToWrapAt >= offset) {
                // normal case
                wrappedLine.append(str.substring(offset, spaceToWrapAt));
                wrappedLine.append(newLineStr);
                offset = spaceToWrapAt + 1;

            } else {
                // really long word or URL
                if (wrapLongWords) {
                    // wrap really long word one line at a time
                    wrappedLine.append(str.substring(offset, wrapLength + offset));
                    wrappedLine.append(newLineStr);
                    offset += wrapLength;
                } else {
                    // do not wrap really long word, just extend beyond limit
                    spaceToWrapAt = str.indexOf(' ', wrapLength + offset);
                    if (spaceToWrapAt >= 0) {
                        wrappedLine.append(str.substring(offset, spaceToWrapAt));
                        wrappedLine.append(newLineStr);
                        offset = spaceToWrapAt + 1;
                    } else {
                        wrappedLine.append(str.substring(offset));
                        offset = inputLineLength;
                    }
                }
            }
        }

        // Whatever is left in line is short enough to just pass through
        wrappedLine.append(str.substring(offset));

        return wrappedLine.toString();
    }

    public static String extractNumber(String str) {
        return NpeUtils.getNonNull(str).trim().replaceAll("[^\\d]", "");
    }

    public static String capitalize(@Nullable String str) {
        String capString = NpeUtils.EMPTY_STRING;
        if (!NpeUtils.isEmpty(str)) {
            capString = str.substring(0, 1).toUpperCase();
            if (str.length() > 1) {
                capString += str.substring(1);
            }
        }
        return capString;
    }

    public static String trim(@Nullable String message, int length) {
        if (length <= 0) {
            return NpeUtils.EMPTY_STRING;
        }
        String safeMessage = NpeUtils.getNonNull(message);
        return safeMessage.substring(0, Math.min(safeMessage.length(), length));
    }
}
