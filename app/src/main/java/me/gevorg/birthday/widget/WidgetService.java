package me.gevorg.birthday.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Service for widget.
 *
 * @author Gevorg Harutyunyan.
 */
public class WidgetService extends RemoteViewsService {
    /**
     *  So pretty simple just defining the Adapter of the list view
     *  here Adapter is ListProvider.
     */
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListProvider(this.getApplicationContext(), intent);
    }
}
