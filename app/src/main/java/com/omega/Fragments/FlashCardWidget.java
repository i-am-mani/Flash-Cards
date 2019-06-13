package com.omega.Fragments;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.RemoteViews;

import com.omega.Database.FlashCardDatabase;
import com.omega.Database.FlashCards;
import com.omega.R;

import java.util.List;
import java.util.Random;

/**
 * Implementation of App Widget functionality.
 */
public class FlashCardWidget extends AppWidgetProvider {

    private List<FlashCards> dataSet;
    private static boolean isWidgetUp = false;
    private static boolean isSolutionVisible = false;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, FlashCards flashCards) {
        isSolutionVisible = false;
        isWidgetUp = true;

        CharSequence widgetText = context.getString(R.string.widget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.flash_card_widget);
        views.setTextViewText(R.id.widget_title, flashCards.getTitle());
        views.setTextViewText(R.id.widget_solution, flashCards.getContent());
        views.setViewVisibility(R.id.widget_solution, View.GONE);

        // Setup update button to send an update request as a pending intent.

        Intent intentUpdate = new Intent(context, FlashCardWidget.class);


        // The intent action must be an app widget update.

        intentUpdate.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);


        // Include the widget ID to be updated as an intent extra.

        int[] idArray = new int[]{appWidgetId};

        intentUpdate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, idArray);


        // Wrap it all in a pending intent to send a broadcast.

        // Use the app widget ID as the request code (third argument) so that

        // each intent is unique.

        PendingIntent pendingUpdate = PendingIntent.getBroadcast(context,
                appWidgetId, intentUpdate, PendingIntent.FLAG_UPDATE_CURRENT);

        // Assign the pending intent to the button onClick handler

        views.setOnClickPendingIntent(R.id.widget_layout, pendingUpdate);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        new AsyncTask<Context, Void, List<FlashCards>>() {
            @Override
            protected List<FlashCards> doInBackground(Context... context) {


                List<FlashCards> list = null;
                FlashCardDatabase db = FlashCardDatabase.getDatabase(context[0]);
                list = db.flashCardDao().getAllFlashCardsAsList();
                return list;
            }

            @Override
            protected void onPostExecute(List<FlashCards> flashCards) {
                super.onPostExecute(flashCards);

                if (flashCards != null) {

                    final Random random = new Random();
                    for (int appWidgetId : appWidgetIds) {
                        updateAppWidget(context, appWidgetManager, appWidgetId, flashCards.get(random.nextInt(flashCards.size())));
                    }

                }

            }

        }.execute(context);


    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created


    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (isWidgetUp && !isSolutionVisible) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.flash_card_widget);
            views.setViewVisibility(R.id.widget_solution, View.VISIBLE);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            appWidgetManager.updateAppWidget(intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS), views);
            isSolutionVisible = true;
        } else {
            super.onReceive(context, intent);
        }
    }
}