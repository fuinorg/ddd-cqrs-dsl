package org.fuin.dsl.cqrs.validation;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Self-contained validity check for Spring Boot cron expressions, with no dependency on Spring. It
 * mirrors the syntax accepted by {@code org.springframework.scheduling.support.CronExpression}: either
 * one of the supported macros, or exactly six whitespace-separated fields
 * <em>second minute hour day-of-month month day-of-week</em>. This is a structural/range check meant
 * to catch mistakes at model time; it does not reproduce every edge case of the Spring parser.
 * <p>
 * IMPORTANT: an identical copy of this class lives in the IntelliJ plugin
 * ({@code org.fuin.dsl.cqrs.intellij.SpringCronExpression}). Keep the two in sync.
 */
public final class SpringCronExpression {

    private SpringCronExpression() {
    }

    private static final List<String> MACROS = Arrays.asList(
            "@yearly", "@annually", "@monthly", "@weekly", "@daily", "@midnight", "@hourly");

    private static final String[] MONTHS = {
            "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC" };

    private static final String[] DAYS = {
            "SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT" };

    private enum Kind {
        NORMAL, DAY_OF_MONTH, DAY_OF_WEEK
    }

    /**
     * Returns whether the given text is a valid Spring cron expression.
     *
     * @param expression expression to check (may be null).
     *
     * @return true if valid.
     */
    public static boolean isValid(final String expression) {
        if (expression == null) {
            return false;
        }
        final String expr = expression.trim();
        if (expr.isEmpty()) {
            return false;
        }
        if (expr.charAt(0) == '@') {
            return MACROS.contains(expr.toLowerCase(Locale.ROOT));
        }
        final String[] fields = expr.split("\\s+");
        if (fields.length != 6) {
            return false;
        }
        return field(fields[0], 0, 59, null, Kind.NORMAL) // seconds
                && field(fields[1], 0, 59, null, Kind.NORMAL) // minutes
                && field(fields[2], 0, 23, null, Kind.NORMAL) // hours
                && field(fields[3], 1, 31, null, Kind.DAY_OF_MONTH) // day of month
                && field(fields[4], 1, 12, MONTHS, Kind.NORMAL) // month
                && field(fields[5], 0, 7, DAYS, Kind.DAY_OF_WEEK); // day of week
    }

    private static boolean field(final String field, final int min, final int max, final String[] names,
            final Kind kind) {
        if (field.isEmpty()) {
            return false;
        }
        if (field.equals("?")) {
            return kind != Kind.NORMAL; // '?' only for day-of-month / day-of-week
        }
        for (final String item : field.split(",", -1)) {
            if (!item(item, min, max, names, kind)) {
                return false;
            }
        }
        return true;
    }

    private static boolean item(final String item, final int min, final int max, final String[] names,
            final Kind kind) {
        if (item.isEmpty()) {
            return false;
        }
        if (item.equals("*")) {
            return true;
        }
        final int slash = item.indexOf('/');
        if (slash >= 0) {
            final String base = item.substring(0, slash);
            final String step = item.substring(slash + 1);
            if (!positiveInt(step)) {
                return false;
            }
            return base.equals("*") || rangeOrValue(base, min, max, names);
        }
        if (kind == Kind.DAY_OF_MONTH) {
            if (item.equals("L") || item.equals("LW")) {
                return true;
            }
            if (item.startsWith("L-")) {
                return intInRange(item.substring(2), 1, max);
            }
            if (item.endsWith("W")) {
                return intInRange(item.substring(0, item.length() - 1), 1, max);
            }
        }
        if (kind == Kind.DAY_OF_WEEK) {
            if (item.equals("L")) {
                return true;
            }
            final int hash = item.indexOf('#');
            if (hash >= 0) {
                return value(item.substring(0, hash), min, max, names)
                        && intInRange(item.substring(hash + 1), 1, 5);
            }
            if (item.endsWith("L")) {
                return value(item.substring(0, item.length() - 1), min, max, names);
            }
        }
        return rangeOrValue(item, min, max, names);
    }

    private static boolean rangeOrValue(final String text, final int min, final int max, final String[] names) {
        final int dash = text.indexOf('-');
        if (dash > 0) {
            return value(text.substring(0, dash), min, max, names)
                    && value(text.substring(dash + 1), min, max, names);
        }
        return value(text, min, max, names);
    }

    private static boolean value(final String text, final int min, final int max, final String[] names) {
        if (text.isEmpty()) {
            return false;
        }
        if (names != null && nameIndex(text, names) >= 0) {
            return true;
        }
        return intInRange(text, min, max);
    }

    private static boolean intInRange(final String text, final int min, final int max) {
        if (!isInt(text)) {
            return false;
        }
        final int value = Integer.parseInt(text);
        return value >= min && value <= max;
    }

    private static boolean isInt(final String text) {
        if (text.isEmpty()) {
            return false;
        }
        for (int i = 0; i < text.length(); i++) {
            final char ch = text.charAt(i);
            if (ch < '0' || ch > '9') {
                return false;
            }
        }
        return true;
    }

    private static boolean positiveInt(final String text) {
        return isInt(text) && Integer.parseInt(text) > 0;
    }

    private static int nameIndex(final String text, final String[] names) {
        for (int i = 0; i < names.length; i++) {
            if (names[i].equalsIgnoreCase(text)) {
                return i;
            }
        }
        return -1;
    }
}
