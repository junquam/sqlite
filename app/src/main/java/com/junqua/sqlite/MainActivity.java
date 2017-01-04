package com.junqua.sqlite;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity implements  LoaderManager.LoaderCallbacks<Cursor> {

    CarAdapter mCarsAdapter;
    private static final int CARS_LOADER = 0;
    private ImageButton addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Cars");

        mCarsAdapter = new CarAdapter(this, null, 0);

        getSupportLoaderManager().initLoader(CARS_LOADER, null, this);

        setList();

        setAddButton();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        Uri carsUri = MyContract.CarsVariables.CARS_URI;

        String selection = null;
        String[] selectionArgs = null;

        return new CursorLoader(this,
                carsUri,
                null,
                selection,
                selectionArgs,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mCarsAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

        mCarsAdapter.swapCursor(null);
    }

    private void setList(){
        final ListView listView = (ListView) findViewById(R.id.listview_cars);
        listView.setAdapter(mCarsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3)
            {
                //get car id
                Cursor cur = mCarsAdapter.getCursor();
                cur.moveToPosition(position);
                final int carId = cur.getInt(cur.getColumnIndexOrThrow(MyContract.CarsVariables.CAR_ID));
                final String carName = cur.getString(cur.getColumnIndexOrThrow(MyContract.CarsVariables.CAR_NAME));

                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(v.getContext());
                View mView = layoutInflaterAndroid.inflate(R.layout.edit_car_dialog, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(v.getContext());
                alertDialogBuilderUserInput.setView(mView);

                final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.newCarName);
                userInputDialogEditText.setHint(carName);
                alertDialogBuilderUserInput
                        .setCancelable(false)
                        .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                editCar(userInputDialogEditText.getText().toString(), carId);
                            }
                        })

                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogBox, int id) {
                                        dialogBox.cancel();
                                    }
                                });

                AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                alertDialogAndroid.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                alertDialogAndroid.show();
            }
        });
    }

    private void editCar(String name, int id){
        ContentValues values = new ContentValues();
        values.put(MyContract.CarsVariables.CAR_NAME, name);

        getContentResolver().update(
            Uri.withAppendedPath(MyContract.CarsVariables.CARS_URI, String.valueOf(id)),
                values, null,null);
        getSupportLoaderManager().restartLoader(0, null, this);
    }

    private void setAddButton(){

        addButton = (ImageButton) findViewById(R.id.addCar);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(view.getContext());
                View mView = layoutInflaterAndroid.inflate(R.layout.add_car_dialog, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(view.getContext());
                alertDialogBuilderUserInput.setView(mView);

                final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.newCarName);
                alertDialogBuilderUserInput
                        .setCancelable(false)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                addCar(userInputDialogEditText.getText().toString());
                            }
                        })

                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogBox, int id) {
                                        dialogBox.cancel();
                                    }
                                });

                AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                alertDialogAndroid.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                alertDialogAndroid.show();
            }
        });
    }

    private void addCar(String name){

        ContentValues values = new ContentValues();
        values.put(MyContract.CarsVariables.CAR_NAME, name);
        getContentResolver().insert(MyContract.CarsVariables.CARS_URI, values);

        getSupportLoaderManager().restartLoader(0, null, this);
    }
}
