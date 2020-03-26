package com.example.android.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Ingredient;

import java.util.List;


// Simply creates & returns in the constructor a RemoteViewsFactory object, which further handles the task of filling the widget with appropriate data
// RemoteViewsService – on top of RemoteViewsFactory, connects RemoteAdapter (RemoteViewsFactory) to be able to request RemoteViews
public class IngredientsListService extends RemoteViewsService {
    @Override
    public ListRemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext());
    }
}


// RemoteViewsFactory serves the purpose of an adapter in the widget’s context
class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private List<Ingredient> ingredientList;

    // Create constructor with context
    public ListRemoteViewsFactory(Context applicationContext) {
        this.mContext = applicationContext;
    }

    // Called when the appwidget is created for the first time
    @Override
    public void onCreate() { }

    // Called on start and when notifyAppWidgetViewDataChanged is called (= when RemoteViewsFactory is created & notified to update it's data)
    @Override
    public void onDataSetChanged() {
        ingredientList = BakingAppWidgetProvider.mIngredientsList;
    }


    @Override
    public void onDestroy() { }

    // Returns the number of list items
    @Override
    public int getCount() {
        if (ingredientList == null || ingredientList.size() == 0) {
            return -1;
        }
        return ingredientList.size();
    }

    /**
     * This method acts like the onBindViewHolder method in an Adapter
     * It returns a RemoteViews object at the specific position which in our case is the single list item
     *
     * @param position The current position of the item in the ListView to be displayed
     * @return The RemoteViews object to display for the provided postion
     */
    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_ingredient_list_item);
        views.setTextViewText(R.id.widget_single_ingredient_TV,
                String.valueOf(ingredientList.get(position).getIngredientsQuantity()) + " " +
                        ingredientList.get(position).getIngredientsMeasure() + " " +
                        ingredientList.get(position).getIngredientsName());

        // Template to handle the click listener for each list item
        // Source: https://www.sitepoint.com/killer-way-to-show-a-list-of-items-in-android-collection-widget/
        Intent fillInIntent = new Intent();
        views.setOnClickFillInIntent(R.id.widgetListItemContainer, fillInIntent);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    // Returns the number of types of views we have in ListView. In our case, we have same view types in each ListView item so we return 1 there
    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}