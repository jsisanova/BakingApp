package com.example.android.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;

import com.example.android.bakingapp.MainActivity;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Ingredient;

import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
// We communicate with app via broadcast messages, this makes widgets a broadcast receiver (so we need to register widget provider in manifest using a receiver tag)
// AppWidgetProvider extends BroadcastReceiver. So our widget class is a receiver class.
public class BakingAppWidgetProvider extends AppWidgetProvider {

    public static List<Ingredient> mIngredientsList;
    public static String mRecipeName;

    // set updateAppWidget to handle clicks and launch MainActivity
    // AppWidgetManager gives info about all existing widgets on home screen; it's also access to forcing an update on all these widgets
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetIds[], String recipeName, List<Ingredient> ingredientList) {

        mIngredientsList = ingredientList;
        mRecipeName = recipeName;


        // There may be multiple widgets active, so update all of them
        // user can have multiple instances of the same widgets on home screen
        for (int appWidgetId : appWidgetIds) {

            // Construct the RemoteViews object
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget_provider);
            // Call the RemoteViewsService
            // Set the IngredientsListService intent to act as the adapter for the ListView
            Intent intent = new Intent(context, IngredientsListService.class);
            views.setRemoteAdapter(R.id.appwidget_ingredientsList, intent);
            views.setTextViewText(R.id.appwidget_title, recipeName);


            // Create an Intent to launch MainActivity when clicked (from recipe title and also from list items)
            Intent openMyAppIntent = new Intent(context, MainActivity.class);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, openMyAppIntent, 0);
//            // RemoteViews can be linked to PendingIntent that will launch once that view is clicked
//            // Widgets allow click handlers to only launch pending intents
            views.setOnClickPendingIntent(R.id.appwidget_title, pendingIntent);

            // Template to handle the click listener for each list item
            // Source: https://www.sitepoint.com/killer-way-to-show-a-list-of-items-in-android-collection-widget/
            PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
                    .addNextIntentWithParentStack(openMyAppIntent)
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.appwidget_ingredientsList, clickPendingIntentTemplate);


            // Instruct the widget manager to update the widget
//            appWidgetManager.updateAppWidget(component, views);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }


    // This is called to update the App Widget at intervals defined by the updatePeriodMillis attribute + when widget is created
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        super.onUpdate (context, appWidgetManager, appWidgetIds);
    }


    // This is called every time an App Widget is deleted from the App Widget host.
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // Perform any action when one or more AppWidget instances have been deleted
    }

    // This is called when an instance the App Widget is created for the first time.
    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    // This is called when the last instance of your App Widget is deleted from the App Widget host.
    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}


