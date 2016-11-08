package me.gevorg.birthday.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

import me.gevorg.birthday.R;
import me.gevorg.birthday.loader.ContactListProvider;
import me.gevorg.birthday.model.Contact;
import me.gevorg.birthday.util.BitmapUtil;
import me.gevorg.birthday.util.PrefUtil;
import me.gevorg.birthday.widget.WidgetProvider;

/**
 * Receiver for alarm.
 *
 * @author Gevor Harutyunyan
 */
public class AlarmReceiver extends BroadcastReceiver {
    /**
     * Receiving alarm intent.
     *
     * @param context context.
     * @param intent intent.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        // Contact list provider.
        ContactListProvider provider = new ContactListProvider(context);

        // Get contacts.
        List<Contact> contacts = provider.getContacts();

        // Today.
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getDefault());

        // Today's date string.
        String today = dateFormat.format(new Date());
        Set<String> lookupKeys = PrefUtil.getPrefLookupKeys(context, today);

        // Cancel all notifications if this is the first round.
        if (lookupKeys.isEmpty()) {
            // Notification manager.
            NotificationManager mgr = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);
            mgr.cancelAll();
        }

        // Get notification preferences.
        boolean notifyToday = PrefUtil.getPrefNotifyToday(context);
        boolean notifyTomorrow = PrefUtil.getPrefNotifyTomorrow(context);

        // Loop contacts.
        for (Contact contact : contacts) {
            checkBirthday(context, contact, lookupKeys, notifyToday, notifyTomorrow);
        }

        // Update lookup keys.
        if (!lookupKeys.isEmpty()) {
            PrefUtil.setPrefLookupKeys(context, today, lookupKeys);
        }

        // Update all widgets.
        WidgetProvider.updateAllWidgets(context);
    }

    /**
     * Check current entry and notify if needed.
     *
     * @param context context.
     * @param contact contact.
     * @param lookupKeys lookup keys.
     * @param notifyToday notify today pref.
     * @param notifyTomorrow notify tomorrow pref.
     * @return true if birthday.
     */
    private boolean checkBirthday(Context context, Contact contact, Set<String> lookupKeys,
                                  boolean notifyToday, boolean notifyTomorrow) {
        // Skip already processed ones.
        if (lookupKeys.contains(contact.getLookupKey())) {
            return true;
        }

        // Today or tomorrow.
        if (contact.getDays() < 2) {
            if ((contact.getDays() == 0 && notifyToday) ||
                (contact.getDays() == 1 && notifyTomorrow)) {
                setupNotification(context, contact);
                lookupKeys.add(contact.getLookupKey());
            }

            return true;
        }

        return false;
    }

    /**
     * Setup notification.
     *
     * @param context context.
     * @param contact contact.
     */
    private void setupNotification(Context context, Contact contact) {
        // Bitmap.
        Bitmap bitmap = null;

        // Extract photo.
        if (contact.getPhoto() == null) {
            bitmap = BitmapFactory.decodeResource(
                    context.getResources(), R.drawable.ic_placeholder);
        } else {
            bitmap = BitmapUtil.getContactBitmapFromURI(context, contact.getPhoto());
        }

        // Prepare fields.
        String title = context.getString(R.string.notification_title, contact.getName());
        String text = context.getString(contact.getDays() == 0 ?
                R.string.notification_today :
                R.string.notification_tomorrow, contact.getName());

        // Show it.
        showNotification(context, title, text, bitmap, contact);
    }

    /**
     * Show notification.
     *
     * @param context context.
     * @param title title.
     * @param text text.
     * @param photo photo.
     * @param contact contact.
     */
    private void showNotification(Context context, String title, String text, Bitmap photo,
                                  Contact contact) {
        // Notification manager.
        NotificationManager mgr = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Gets a content URI for the contact
        Uri uri = ContactsContract.Contacts.getLookupUri(contact.getId(), contact.getLookupKey());

        // Create intent.
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(uri);

        // Open activity intent.
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        // Build the Notification object using a Notification.Builder
        Notification note = new Notification.Builder(context)
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setSmallIcon(R.drawable.ic_notification)
                .setLargeIcon(photo)
                .setContentIntent(pi)
                .build();

        // Send the notification.
        mgr.notify(contact.getId(), note);
    }
}
