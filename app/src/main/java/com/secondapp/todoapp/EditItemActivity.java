package com.secondapp.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class EditItemActivity extends ActionBarActivity {
    private EditText etEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.todo_icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setTitle("Edit Item");

        setContentView(R.layout.activity_edit_item);
        String textClickedItem = getIntent().getStringExtra("text_clicked_item");
        //Toast.makeText(getApplicationContext(), textClickedItem, Toast.LENGTH_SHORT).show();
        etEditText = (EditText)findViewById(R.id.etEditText);
        etEditText.setText(textClickedItem);
        etEditText.setSelection(textClickedItem.length());
        etEditText.setFocusable(true);
        etEditText.requestFocus();
        //Be sure the user's cursor in the text field is at the end of the current text value and is in focus by default.
    }

    public void onSave(View view) {
        Intent data = new Intent();
        // Pass relevant data back as a result
        int positionClickedItem = getIntent().getIntExtra("position_clicked_item", 0);
       // Toast.makeText(getApplicationContext(), String.valueOf(positionClickedItem), Toast.LENGTH_SHORT).show();
        data.putExtra("edited_clicked_item", etEditText.getText().toString()); //etName.getText().toString());
        data.putExtra("position_clicked_item", positionClickedItem);
        // Activity finished ok, return the data
        setResult(RESULT_OK, data); // set result code and bundle data for response
        finish(); // closes the activity, pass data to parent
    }

    public void onCancel(View view){
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
