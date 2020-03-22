package com.example.android.bakingapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.android.bakingapp.model.Ingredient;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in a service on a separate handler thread.
 */
public class UpdateIngredientsService extends IntentService {

    public static final String ACTION_UPDATE_INGREDIENTS = "com.example.android.bakingapp.widget";

    private String recipeTitle;
    private List<Ingredient> ingredientsList;



    public UpdateIngredientsService() {
        super ("UpdateIngedientsService");
    }

    /**
     * Starts this service to perform update ingredients action with the given parameters.
     * If the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionUpdateIngredients(Context context, Recipe recipe) {
        Intent intent = new Intent(context, UpdateIngredientsService.class);
        Bundle bundle = new Bundle();

        bundle.putString(Constants.NAME_KEY, recipe.getRecipeName());
        // Make ArrayList from List to avoid ClassCastException
        ArrayList<Ingredient> ingredientsArrayList = new ArrayList<>(recipe.getIngredients());
        bundle.putParcelableArrayList(Constants.RECIPE_INGREDIENTS_KEY,ingredientsArrayList);

        intent.putExtra(Constants.BUNDLE_KEY, bundle);
        intent.setAction(ACTION_UPDATE_INGREDIENTS);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_INGREDIENTS.equals(action)) {
                handleActionUpdateIngredients(intent);
            }
        }
    }

    private void handleActionUpdateIngredients(Intent intent) {
        Bundle bundle = intent.getBundleExtra(Constants.BUNDLE_KEY);
        recipeTitle = bundle.getString(Constants.NAME_KEY);
        ingredientsList = bundle.getParcelableArrayList(Constants.RECIPE_INGREDIENTS_KEY);

        // Instruct the widget manager to update the widget
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        ComponentName theWidget = new ComponentName(this, BakingAppWidgetProvider.class);
        int appWidgetIds[] = appWidgetManager.getAppWidgetIds(theWidget);
        BakingAppWidgetProvider.updateAppWidget (this, appWidgetManager, appWidgetIds, recipeTitle, ingredientsList);
    }
}