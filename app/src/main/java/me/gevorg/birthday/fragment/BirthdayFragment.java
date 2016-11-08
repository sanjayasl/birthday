package me.gevorg.birthday.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.ContactsContract;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.melnykov.fab.FloatingActionButton;


import java.util.ArrayList;
import java.util.List;

import me.gevorg.birthday.R;
import me.gevorg.birthday.adapter.ContactAdapter;
import me.gevorg.birthday.loader.ContactLoader;
import me.gevorg.birthday.model.Contact;

/**
 * Birthday fragment.
 *
 * @author Gevorg Harutyunyan
 */
public class BirthdayFragment extends ListFragment implements AdapterView.OnItemLongClickListener,
        LoaderManager.LoaderCallbacks<List<Contact>> {
    // READ_CONTACTS_REQUEST code.
    private static final int READ_CONTACTS_REQUEST = 1;

    // Contact adapter.
    private ContactAdapter adapter;

    /**
     * View created already.
     *
     * @param inflater inflater.
     * @param container container.
     * @param savedInstanceState saved instance.
     * @return view.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_birthday, container, false);
    }

    /**
     * View already created.
     *
     * @param view view.
     * @param savedInstanceState state.
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        getListView().setOnItemLongClickListener(this);
    }

    /**
     * Toggle edit contact.
     *
     * @param parent parent.
     * @param view view.
     * @param position position.
     * @param id id.
     * @return true if done.
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        // Get contact.
        Contact contact = (Contact) parent.getItemAtPosition(position);

        // Gets a content URI for the contact
        Uri uri = ContactsContract.Contacts.getLookupUri(id, contact.getLookupKey());

        // Create intent.
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(uri);

        // Start activity.
        startActivity(intent);

        return true;
    }

    /**
     * List click listener.
     *
     * @param l list.
     * @param v view.
     * @param position position.
     * @param id id.
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Get contact.
        Contact contact = (Contact) l.getItemAtPosition(position);

        // Gets a content URI for the contact
        Uri uri = ContactsContract.Contacts.getLookupUri(id, contact.getLookupKey());

        // Create intent.
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(uri);

        // Start activity.
        startActivity(intent);
    }

    /**
     * Attaching floating action button.
     */
    private void attachFab() {
        // Attach fab.
        FloatingActionButton fab = (FloatingActionButton) getView().findViewById(R.id.fab);
        fab.attachToListView(getListView());
        fab.setOnClickListener(new View.OnClickListener() {
            /**
             * On Fab click.
             *
             * @param v view.
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_INSERT);
                intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                startActivity(intent);
            }
        });
    }

    /**
     * Activity created already.
     *
     * @param savedInstanceState saved instance state.
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Attach fab.
        attachFab();

        // Set adapter.
        adapter = new ContactAdapter(getContext(), new ArrayList<Contact>());
        setListAdapter(adapter);

        // Needed for image issue.
        getListView().setRecyclerListener(adapter);

        // Check READ_CONTACTS permission.
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS) ==
                PackageManager.PERMISSION_GRANTED) {
            // Initializes the loader
            getLoaderManager().initLoader(0, null, this);
        } else {
            requestPermissions(new String[]{ Manifest.permission.READ_CONTACTS},
                    READ_CONTACTS_REQUEST);
        }
    }

    /**
     * On request permission result.
     *
     * @param requestCode request code.
     * @param permissions permissions.
     * @param grantResults grantResults.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == READ_CONTACTS_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Initializes the loader
                getLoaderManager().initLoader(0, null, this);
            }
        }
    }

    /**
     * Creates loader.
     *
     * @param id id.
     * @param args arguments.
     * @return loader.
     */
    @Override
    public Loader<List<Contact>> onCreateLoader(int id, Bundle args) {
        // Hide empty view.
        getListView().getEmptyView().setVisibility(View.GONE);

        // Create loader.
        return new ContactLoader(getContext());
    }

    /**
     * Loading done.
     *
     * @param loader loader.
     * @param data data.
     */
    @Override
    public void onLoadFinished(Loader<List<Contact>> loader, List<Contact> data) {
        adapter.setContacts(data);
        adapter.notifyDataSetChanged();
    }

    /**
     * Clear adapter.
     *
     * @param loader loader.
     */
    @Override
    public void onLoaderReset(Loader<List<Contact>> loader) {
        adapter.setContacts(new ArrayList<Contact>());
        adapter.notifyDataSetChanged();
    }
}
