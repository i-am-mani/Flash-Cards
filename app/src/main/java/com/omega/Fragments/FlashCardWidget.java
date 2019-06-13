package com.omega.Fragments;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.os.AsyncTask;
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

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, FlashCards flashCards) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.flash_card_widget);
        views.setTextViewText(R.id.appwidget_text, flashCards.getTitle());


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
}