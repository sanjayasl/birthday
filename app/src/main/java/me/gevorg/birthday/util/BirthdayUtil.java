package me.gevorg.birthday.util;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import me.gevorg.birthday.R;

/**
 * Utility methods for birthday.
 *
 * @author Gevorg Harutyunyan.
 */
public class BirthdayUtil {
    /**
     * Used for format cases.
     */
    private static class FormatCase {
        private String format;
        private Locale locale;
        private boolean setYear1700;

        /**
         * Getter for format.
         *
         * @return format.
         */
        String getFormat() {
            return format;
        }

        /**
         * Getter for locale.
         *
         * @return locale.
         */
        Locale getLocale() {
            return locale;
        }

        /**
         * Getter for set year 1700.
         *
         * @return set year 1700.
         */
        boolean isSetYear1700() {
            return setYear1700;
        }

        /**
         * Full constructor.
         *
         * @param format format.
         * @param setYear1700 set year 1700.
         * @param locale locale.
         */
        FormatCase(String format, boolean setYear1700, Locale locale) {
            this.format = format;
            this.setYear1700 = setYear1700;
            this.locale = locale;
        }

        /**
         * Simple version.
         *
         * @param format format.
         * @param setYear1700 set year 1700.
         */
        FormatCase(String format, boolean setYear1700) {
            this(format, setYear1700, Locale.ENGLISH);
        }
    }

    // Format cases.
    private static FormatCase[] formatCases = new FormatCase[] {
            new FormatCase("dd.MM.yyyy", false),
            new FormatCase("yyyy.MM.dd", false),
            new FormatCase("dd/MM/yyyy", false),
            new FormatCase("dd/MM", true),
            new FormatCase("dd MMM yyyy", false),
            new FormatCase("dd MMM yyyy", false, new Locale("ru")),
            new FormatCase("dd MMM yyyy", false, new Locale("pl")),
            new FormatCase("dd-MMM-yyyy", false)
    };

    /**
     * Try to parse input with SimpleDateFormat
     *
     * @param input input.
     * @param format      SimpleDateFormat
     * @param setYear1700 When true the age will be not displayed in brackets
     * @return Date object if successful, otherwise null
     */
    private static Date parseStringWithSimpleDateFormat(String input, String format,
                                                        boolean setYear1700) {
        return parseStringWithSimpleDateFormat(input, format, setYear1700, Locale.ENGLISH);
    }

    /**
     * Try to parse input with SimpleDateFormat
     *
     * @param input input.
     * @param format      SimpleDateFormat
     * @param setYear1700 When true the age will be not displayed in brackets
     * @param locale locale.
     * @return Date object if successful, otherwise null
     */
    private static Date parseStringWithSimpleDateFormat(String input, String format,
                                                        boolean setYear1700, Locale locale) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, locale);
        dateFormat.setTimeZone(TimeZone.getDefault());

        try {
            Date parsedDate = dateFormat.parse(input);

            /*
             * Because no year is defined in address book, set year to 1700
             *
             * When year < 1800, the age will be not displayed in brackets
             */
            if (setYear1700) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(parsedDate);
                cal.set(Calendar.YEAR, 1700);
            }

            return parsedDate;
        } catch (ParseException ignored) {
            return null;
        }
    }

    /**
     * The date format in the contact events is not standardized! This method will try to parse it
     * trying different date formats.
     * <p/>
     * See also: http://dmfs.org/carddav/?date_format
     *
     * @param eventDateString event date string.
     * @return eventDate as Date object
     */
    public static Date parseEventDateString(String eventDateString) {
        if (eventDateString != null) {
            Date eventDate = null;

            /*
             * MMM d, yyyy
             */
            eventDate = parseStringWithSimpleDateFormat(eventDateString, "MMM d, yyyy", false);

            /*
             * yyyy-MM-dd
             */
            if (eventDate == null) {
                eventDate = parseStringWithSimpleDateFormat(eventDateString, "yyyy-MM-dd", false);
            }

            /*
             * --MM-dd
             *
             * Note: Most used format without year!
             */
            if (eventDate == null) {
                eventDate = parseStringWithSimpleDateFormat(eventDateString, "--MM-dd", true);
            }

            /*
             * yyyyMMdd
             *
             * Note: HTC Desire
             */
            if (eventDate == null) {
                if (eventDateString.length() == 8) {
                    eventDate = parseStringWithSimpleDateFormat(eventDateString, "yyyyMMdd", false);
                }
            }

            /*
             * Unix timestamp
             *
             * Note: Some Motorola devices
             */
            if (eventDate == null) {
                try {
                    eventDate = new Date(Long.parseLong(eventDateString));
                } catch (NumberFormatException ignored) {}
            }

            // Loop regular cases.
            if (eventDate == null) {
                for (FormatCase formatCase : formatCases) {
                    // Attempt to parse.
                    eventDate = parseStringWithSimpleDateFormat(eventDateString,
                            formatCase.getFormat(), formatCase.isSetYear1700(),
                            formatCase.getLocale());

                    // Found format.
                    if (eventDate != null) break;
                }
            }

            /* Return */
            if (eventDate != null) {
                return eventDate;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Returns days left text.
     *
     * @param context context.
     * @param days days.
     * @return days left text.
     */
    public static String getDaysText(Context context, int days) {
        String daysLeft;

        if (days == 0) {
            daysLeft = context.getString(R.string.today);
        } else if (days == 1) {
            daysLeft = context.getString(R.string.tomorrow);
        } else {
            // Special case for russian translation.
            int lastDigit = days % 10;
            int preLastDigit = days % 100 / 10;

            if (preLastDigit == 1 || lastDigit == 0 ||  lastDigit >= 5) {
                daysLeft = context.getString(R.string.days_left, days);
            } else if (lastDigit == 1) {
                daysLeft = context.getString(R.string.days_left_ru2, days);
            } else {
                daysLeft = context.getString(R.string.days_left_ru, days);
            }
        }

        return daysLeft;
    }
}
