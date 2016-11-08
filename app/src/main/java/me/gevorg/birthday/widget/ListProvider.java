package me.gevorg.birthday.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import me.gevorg.birthday.R;
import me.gevorg.birthday.loader.ContactListProvider;
import me.gevorg.birthday.model.Contact;
import me.gevorg.birthday.util.BirthdayUtil;
import me.gevorg.birthday.util.BitmapUtil;

/**
 * @author Gevorg Harutyunyan.
 */
public class ListProvider implements RemoteViewsService.RemoteViewsFactory  {
    // Contact list.
    private List<Contact> contactList = new ArrayList<>();

    // Context.
    private Context context = null;

    // Intent.
    private Intent intent = null;

    // Widget id.
    private int appWidgetId;

    // Contact list provider.
    private ContactListProvider provider;

    /**
     * Constructor.
     *
     * @param context context.
     */
    public ListProvider(Context context, Intent intent) {
        // Set context.
        this.context = context;

        // Set intent.
        this.intent = intent;

        // Get widget id.
        this.appWidgetId = intent.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

        // Init provider.
        provider = new ContactListProvider(context);

        // Set list.
        this.contactList = provider.getContacts();
    }

    /**
     * Item count.
     *
     * @return count of contacts.
     */
    @Override
    public int getCount() {
        return contactList.size();
    }

    /**
     * Returns contact id.
     *
     * @param position position.
     * @return contact id.
     */
    @Override
    public long getItemId(int position) {
        return contactList.get(position).getId();
    }

    /**
     * Returns view at position.
     *
     * @param position position.
     * @return view.
     */
    @Override
    public RemoteViews getViewAt(int position) {
        final RemoteViews remoteView = new RemoteViews(context.getPackageName(),
                R.layout.widget_item);

        // Some odd error with out of bound.
        position = position % contactList.size();

        // Get contact.
        Contact contact = contactList.get(position);

        // Set contact name.
        remoteView.setTextViewText(R.id.name, contact.getName());

        // Set photo.
        if (contact.getPhoto() != null) {
            Bitmap contactPhoto = BitmapUtil.getContactBitmapFromURI(context, contact.getPhoto());
            remoteView.setImageViewBitmap(R.id.photo, contactPhoto);
        } else {
            remoteView.setImageViewResource(R.id.photo, R.drawable.ic_placeholder);
        }

        // Set birthday and check if it is today.
        // Birthday display format.
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());

        // Find fields to populate in inflated template.
        remoteView.setTextViewText(R.id.birthday, sdf.format(contact.getBirthday()));

        // Set age.
        remoteView.setTextViewText(R.id.age, String.valueOf(contact.getAge()));

        // Set days.
        int days = contact.getDays();
        remoteView.setTextViewText(R.id.days, BirthdayUtil.getDaysText(context, days));

        // Set onclick listener.
        remoteView.setOnClickFillInIntent(R.id.widget_contact, intent);

        return remoteView;
    }

    /**
     * Count of view types..
     *
     * @return view type count.
     */
    @Override
    public int getViewTypeCount() {
        return 1;
    }

    /**
     * Has stable ids or not..
     *
     * @return false.
     */
    @Override
    public boolean hasStableIds() {
        return true;
    }

    /**
     * Ignored.
     */
    @Override
    public void onCreate() {
    }

    /**
     * Ignored.
     */
    @Override
    public void onDataSetChanged() {
        this.contactList = provider.getContacts();
    }

    /**
     * Ignored.
     */
    @Override
    public void onDestroy() {

    }

    /**
     * Ignored.
     *
     * @return remote views.
     */
    @Override
    public RemoteViews getLoadingView() {
        return null;
    }
}
