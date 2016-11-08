package me.gevorg.birthday.adapter;

import android.content.Context;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import me.gevorg.birthday.R;
import me.gevorg.birthday.model.Contact;
import me.gevorg.birthday.util.BirthdayUtil;

/**
 * Contact birthday adapter.
 *
 * @author Gevorg Harutyunyan.
 */
public class ContactAdapter extends BaseAdapter implements ListView.RecyclerListener {
    // List of contacts.
    private List<Contact> contacts;

    // Layout inflater.
    private LayoutInflater inflater;

    // Context.
    private Context context;

    /**
     * Constructor.
     *
     * @param context context.
     * @param contacts contacts.
     */
    public ContactAdapter(Context context, List<Contact> contacts) {
        this.context = context;
        this.contacts = contacts;
        this.inflater = LayoutInflater.from(context);
    }

    /**
     * Set contacts.
     *
     * @param contacts contacts.
     */
    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    /**
     * Get item count.
     *
     * @return item count.
     */
    @Override
    public int getCount() {
        return contacts.size();
    }

    /**
     * Get item at position.
     *
     * @param pos position.
     * @return contact.
     */
    @Override
    public Contact getItem(int pos) {
        return contacts.get(pos);
    }

    /**
     * Get item id.
     *
     * @param pos position.
     * @return contact id.
     */
    @Override
    public long getItemId(int pos) {
        return contacts.get(pos).getId();
    }

    /**
     * Get view.
     *
     * @param pos position.
     * @param convertView convert view.
     * @param viewGroup view group.
     * @return view.
     */
    @Override
    public View getView(int pos, View convertView, ViewGroup viewGroup) {
        // Item view.
        RelativeLayout view;

        // Create new view or use existing.
        if (convertView == null) {
            view = (RelativeLayout) inflater.inflate(R.layout.contact_birthday, viewGroup, false);
        } else {
            view = (RelativeLayout) convertView;
        }

        // Assign views.
        TextView nameView = (TextView) view.findViewById(R.id.name);
        TextView birthday = (TextView) view.findViewById(R.id.birthday);
        TextView ageView = (TextView) view.findViewById(R.id.age);
        TextView daysView = (TextView) view.findViewById(R.id.days);
        ImageView photoView = (ImageView) view.findViewById(R.id.photo);

        // Get contact.
        Contact contact = getItem(pos);

        // Set name.
        nameView.setText(contact.getName());

        // Set photo.
        if (contact.getPhoto() != null) {
            // Load and transform.
            Picasso.with(context).
                    load(contact.getPhoto()).
                    transform(new RoundedCorners()).
                    into(photoView);
        }

        // Set birthday and check if it is today.
        // Birthday display format.
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());

        // Find fields to populate in inflated template.
        birthday.setText(sdf.format(contact.getBirthday()));

        // Set age.
        ageView.setText(String.valueOf(contact.getAge()));

        // Set days.
        int days = contact.getDays();
        daysView.setText(BirthdayUtil.getDaysText(context, days));

        // If birthday is today or tomorrow.
        if (days < 2) {
            daysView.setTypeface(Typeface.DEFAULT_BOLD);
        }

        return view;
    }

    /**
     * When view is cleaned.
     *
     * @param view view.
     */
    @Override
    public void onMovedToScrapHeap(View view) {
        // Release strong reference when a view is recycled.
        final ImageView imageView = (ImageView) view.findViewById(R.id.photo);
        imageView.setImageResource(R.drawable.ic_placeholder);

        // Cancel image load request.
        Picasso.with(view.getContext()).cancelRequest(imageView);

        // Reset typeface.
        TextView daysView = (TextView) view.findViewById(R.id.days);
        daysView.setTypeface(Typeface.DEFAULT);
    }
}
