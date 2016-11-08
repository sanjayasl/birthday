package me.gevorg.birthday.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;

import me.gevorg.birthday.R;
import me.gevorg.birthday.activity.BirthdayActivity;


/**
 * Provider for birthday widget.
 *
 * @author Gevorg Harutyunyan.
 */
public class WidgetProvider extends AppWidgetProvider {
    /**
     * Update on widget.
     *
     * @param context context.
     * @param appWidgetManager manager.
     * @param appWidgetIds ids.
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            // Update widget.
            updateWidget(context, appWidgetManager, appWidgetId);
        }

        // Calling super for unknown reason.
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        // App widget.
        updateWidget(context, appWidgetManager, appWidgetId);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    /**
     * Set list data configuration.
     *
     * @param context context.
     * @param remoteViews remote views.
     * @param appWidgetId widget id.
     */
    private static void setListData(Context context, RemoteViews remoteViews, int appWidgetId) {
        // RemoteViews Service needed to provide adapter for ListView.
        Intent svcIntent = new Intent(context, WidgetService.class);

        // Setting a unique Uri to the intent, don't know its purpose to me right now.
        svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));

        // Put widget id.
        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

        // Setting adapter to list view of the widget.
        remoteViews.setRemoteAdapter(R.id.listViewWidget, svcIntent);

        // Setting an empty view in case of no data.
        remoteViews.setEmptyView(R.id.listViewWidget, R.id.emptyWidget);
    }

    /**
     * Update widget.
     *
     * @param context context.
     * @param mgr app widget manager.
     * @param appWidgetId widget id.
     */
    private static void updateWidget(Context context, AppWidgetManager mgr, int appWidgetId) {
        // Create remote view.
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.widget_birthday);

        // Set list data.
        setListData(context, remoteViews, appWidgetId);

        // Open activity intent.
        Intent activityIntent = new Intent(context, BirthdayActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, activityIntent, 0);

        // Set onclick listener.
        remoteViews.setPendingIntentTemplate(R.id.listViewWidget, pi);
        remoteViews.setOnClickPendingIntent(R.id.emptyWidget, pi);

        // Update widget.
        mgr.updateAppWidget(appWidgetId, remoteViews);
    }

    /**
     * Updating all widgets.
     *
     * @param context context.
     */
    public static void updateAllWidgets(Context context) {
        AppWidgetManager mgr = AppWidgetManager.getInstance(context);
        ComponentName name = new ComponentName(context, WidgetProvider.class);
        int[] appWidgetIds = mgr.getAppWidgetIds(name);

        // Date update request.
        mgr.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.listViewWidget);
    }
}
