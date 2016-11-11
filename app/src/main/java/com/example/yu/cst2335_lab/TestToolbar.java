package com.example.yu.cst2335_lab;

import android.app.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import static com.example.yu.cst2335_lab.R.id.newMessage;


public class TestToolbar extends AppCompatActivity {
    public static final String TOOLBAR_MESSAGE ="Toolbar";
    public static final String DIALOG_TITLE = "Do you want to go back?";
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toolbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Show SnackBar action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    //to create toolbar by inflating it from xml file
    public boolean onCreateOptionsMenu(Menu m){
        getMenuInflater().inflate(R.menu.toolbar_menu,m);
        return true;
    }
    //responds to one of the items being selected
    public boolean onOptionsItemSelected(MenuItem mi){
        int id = mi.getItemId();
        switch(id){
            case R.id.item1:
                Log.d(TOOLBAR_MESSAGE,"Item1 is selected");
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Snackbar.make(view, "You selected item 1", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                });

                break;
            case R.id.item2:
                Log.d(TOOLBAR_MESSAGE,"Item2 is selected");

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(DIALOG_TITLE);
                // Add the buttons
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        Intent resultIntent = new Intent(  );
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.cancel();
                    }
                });
                // Create the AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();


                break;
            case R.id.item3:
                Log.d(TOOLBAR_MESSAGE,"Item3 is selected");
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                // Get the layout inflater
                LayoutInflater inflater = this.getLayoutInflater();

                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                builder2.setView(inflater.inflate(R.layout.dialog, null))
                        // Add action buttons
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                // sign in the user ...
                                fab.setOnClickListener(new View.OnClickListener() {
                                    EditText newMessage;
                                    @Override
                                    public void onClick(View view) {
                                        newMessage = (EditText) findViewById(R.id.newMessage);
                                        Log.i("abc",newMessage.getText().toString());
                                        Snackbar.make(view, newMessage.getText().toString(), Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show();
                                    }
                                });
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();}
                        });
                // Create the AlertDialog
                builder2.create().show();

                break;
            case R.id.item4:
                Log.d(TOOLBAR_MESSAGE,"Item4 is selected");
                Toast toast = Toast.makeText(TestToolbar.this , "Version 1.0, by Yu Wang", Toast.LENGTH_SHORT);
                toast.show();
                break;
        }
        return true;
    }

}
