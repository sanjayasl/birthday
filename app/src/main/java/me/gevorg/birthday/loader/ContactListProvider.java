package me.gevorg.birthday.loader;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.gevorg.birthday.BirthdayApp;
import me.gevorg.birthday.model.Contact;
import me.gevorg.birthday.util.BirthdayUtil;

/**
 * Provides contact list.
 *
 * @author Gevorg Harutyunyan.
 */
public class ContactListProvider {
    // Projection.
    private static String[] PROJECTION = new String[] {
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.LOOKUP_KEY,
            ContactsContract.Contacts.PHOTO_THUMBNAIL_URI,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Event.START_DATE
    };

    // Selection criteria.
    private static String SELECTION =
            ContactsContract.Data.MIMETYPE + "= ? AND " +
                    ContactsContract.CommonDataKinds.Event.TYPE + "=" +
                    ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY;

    // Selection arguments.
    private static String[] SELECTION_ARGS = new String[] {
            ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE
    };

    // Sorting order.
    private static String SORT_ORDER = ContactsContract.Contacts.DISPLAY_NAME;


    // Context.
    private Context context;

    /**
     * Constructor.
     *
     * @param context application context.
     */
    public ContactListProvider(Context context) {
        this.context = context;
    }

    /**
     * Returns list of filtered contacts.
     *
     * @return list of contacts.
     */
    public List<Contact> getContacts() {
        // Result.
        List<Contact> contacts = new ArrayList<>();

        // Must have read contact permission.
        if (PackageManager.PERMISSION_GRANTED !=
                ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS)) {
            return contacts;
        }

        // Get cursor.
        Cursor cursor = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                PROJECTION, SELECTION, SELECTION_ARGS, SORT_ORDER);

        // Found lookup keys.
        Set<String> lookupKeys = new HashSet<>();

        // List of wrong birthdays.
        List<String> wrongBirthdays = new ArrayList<>();

        // Move cursor.
        while (cursor.moveToNext()) {
            // Fetch lookup.
            int index = cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY);
            String lookupKey = cursor.getString(index);

            // We already have this key.
            if (lookupKeys.contains(lookupKey)) {
                continue;
            }

            // Fetch birthday.
            index = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE);
            String birthdayString = cursor.getString(index);
            Date birthday = BirthdayUtil.parseEventDateString(birthdayString);

            // Birthday identified.
            if (birthday != null) {
                Contact contact = new Contact(lookupKey, birthday);

                // Fetch contact id.
                index = cursor.getColumnIndex(ContactsContract.Contacts._ID);
                contact.setId(cursor.getInt(index));

                // Fetch name.
                index = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                contact.setName(cursor.getString(index));

                // Photo.
                index = cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI);
                contact.setPhoto(cursor.getString(index));

                // Add contact.
                contacts.add(contact);

                // We processed this key.
                lookupKeys.add(lookupKey);
            } else {
                wrongBirthdays.add(birthdayString);
            }
        }

        // Close cursor.
        cursor.close();

        // Track wrong birthdays.
        trackWrongBirthdays(wrongBirthdays);

        // Sort.
        Collections.sort(contacts);

        // Return result.
        return contacts;
    }

    /**
     * Tracking wrong birthdays.
     *
     * @param birthdayList birthday list.
     */
    private void trackWrongBirthdays(List<String> birthdayList) {
        BirthdayApp birthdayApp = (BirthdayApp) context.getApplicationContext();
        Tracker tracker = birthdayApp.getDefaultTracker();

        // If tracker is set send exception.
        if (tracker != null && !birthdayList.isEmpty()) {
            HitBuilders.ExceptionBuilder builder = new HitBuilders.ExceptionBuilder();
            tracker.send(builder.setDescription(birthdayList.toString()).build());

            // Trace.
            Log.w("WrongBirthdays", birthdayList.toString());
        }
    }
}
