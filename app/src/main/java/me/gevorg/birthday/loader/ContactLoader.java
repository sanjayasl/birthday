package me.gevorg.birthday.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

import me.gevorg.birthday.model.Contact;

/**
 * Contact loader.
 *
 * @author Gevorg Harutyunyan.
 */
public class ContactLoader extends AsyncTaskLoader<List<Contact>> {
    /**
     * Constructor matching parent.
     *
     * @param context context.
     */
    public ContactLoader(Context context) {
        super(context);
    }

    /**
     * Start loading.
     */
    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * Loading contact list.
     *
     * @return contact list.
     */
    @Override
    public List<Contact> loadInBackground() {
        // Contact list provider.
        ContactListProvider provider = new ContactListProvider(getContext());
        return provider.getContacts();
    }
}
