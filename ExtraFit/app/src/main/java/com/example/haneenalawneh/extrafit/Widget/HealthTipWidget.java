package com.example.haneenalawneh.extrafit.Widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import com.example.haneenalawneh.extrafit.R;

import java.util.Calendar;

/**
 * Implementation of App Widget functionality.
 */
public class HealthTipWidget extends AppWidgetProvider {
    private static PendingIntent service = null;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,

                                int appWidgetId) {

        final AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        final Calendar TIME = Calendar.getInstance();
        TIME.set(Calendar.MINUTE, 0);
        TIME.set(Calendar.SECOND, 0);
        TIME.set(Calendar.MILLISECOND, 0);
        CharSequence widgetText = context.getString(R.string.app_name);
        // Construct the RemoteViews object

        final Intent i = new Intent(context, MyService.class);

        if (service == null) {
            service = PendingIntent.getService(context, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
        }

        // Instruct the widget manager to update the widget
        manager.setRepeating(AlarmManager.RTC, TIME.getTime().getTime(), 1000 * 60, service);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        final AlarmManager m = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        m.cancel(service);
    }
}

