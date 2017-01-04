package com.junqua.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class CarAdapter extends CursorAdapter implements View.OnClickListener{

    private Context context;

    public CarAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_cars, parent, false);

        this.context = context;
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView name = (TextView)view.findViewById(R.id.list_item_cars_name);
        name.setText(cursor.getString(cursor.getColumnIndex(MyContract.CarsVariables.CAR_NAME)));

        ImageButton delete = (ImageButton)view.findViewById(R.id.list_item_cars_delete);
        delete.setOnClickListener(this);
        delete.setTag(cursor.getInt(cursor.getColumnIndex(MyContract.CarsVariables.CAR_ID)));
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.list_item_cars_delete:

                context.getContentResolver().delete(
                        Uri.withAppendedPath(MyContract.CarsVariables.CARS_URI, String.valueOf((int)view.getTag())),
                        null,
                        null
                );
                break;
            default:
                break;

        }
        ((MainActivity)context).getSupportLoaderManager().restartLoader(0, null, (MainActivity)context);
    }
}