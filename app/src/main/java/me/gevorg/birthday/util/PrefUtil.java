package me.gevorg.birthday.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashSet;
import java.util.Set;

/**
 * Utility methods for preferences.
 *
 * @author Gevorg Harutyunyan.
 */
public class PrefUtil {
    // Lookup keys preference.
    private static final String PREF_LOOKUP_KEYS = "pref_lookup_keys";

    // Today's notification preference.
    private static final String PREF_NOTIFY_TODAY = "pref_notify_today";

    // Tomorrow's notification preference.
    private static final String PREF_NOTIFY_TOMORROW = "pref_notify_tomorrow";

    /**
     * Today's notification preference.
     *
     * @param context context.
     * @return true if notification preference is on.
     */
    public static boolean getPrefNotifyToday(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getBoolean(PREF_NOTIFY_TODAY, true);
    }

    /**
     * Tomorrow's notification preference.
     *
     * @param context context.
     * @return true if notification preference is on.
     */
    public static boolean getPrefNotifyTomorrow(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getBoolean(PREF_NOTIFY_TOMORROW, true);
    }

    /**
     * Saves pref for lookup keys.
     *
     * @param context context.
     * @param date date string.
     * @param lookupKeys lookup key set.
     */
    public static void setPrefLookupKeys(Context context, String date, Set<String> lookupKeys) {
        SharedPreferences sharedPref = context.getSharedPreferences(date, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.putStringSet(PREF_LOOKUP_KEYS, lookupKeys);
        editor.apply();
    }

    /**
     * Returns string set with lookup keys.
     *
     * @param context context.
     * @param date date.
     * @return lookup key set.
     */
    public static Set<String> getPrefLookupKeys(Context context, String date) {
        SharedPreferences sharedPref = context.getSharedPreferences(date, Context.MODE_PRIVATE);
        return sharedPref.getStringSet(PREF_LOOKUP_KEYS, new HashSet<String>());
    }
}
