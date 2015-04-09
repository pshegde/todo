package com.secondapp.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class TodoActivity extends ActionBarActivity {
    private ArrayList<String> items;
    private ArrayAdapter<String> aTodoItemsAdapter; //translate string/class/model to a view
    private ListView lvItems;
    private EditText etNewItem;
    private final int REQUEST_CODE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getSupportActionBar().setIcon(R.drawable.todo_icon);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.todo_icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setTitle("Simple Todo");

//        LinearLayout myTitleBar = (LinearLayout) findViewById(R.id.title_bar_layout);
//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setCustomView(R.layout.title_bar);

        setContentView(R.layout.activity_todo);
        //populateItems();
        readItems();
        lvItems = (ListView)findViewById(R.id.lvItems);
        etNewItem = (EditText) findViewById(R.id.etNewItem);
        aTodoItemsAdapter = new ArrayAdapter<String>(getBaseContext(),android.R.layout.simple_list_item_1,items);
        lvItems.setAdapter(aTodoItemsAdapter);
        //add to adapter automatically adds to view
        //aTodoItemsAdapter.add("Item 3");
        setupListViewListener();
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View item, int position, long id) {
                items.remove(position);
                aTodoItemsAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(TodoActivity.this, EditItemActivity.class);
                i.putExtra("text_clicked_item", items.get(position).toString());
                i.putExtra("position_clicked_item", position);
                //startActivity(i); // brings up the second activity
                startActivityForResult(i, REQUEST_CODE);
            }

        });
    }

    private void populateItems() {
        items = new ArrayList<String>();
        items.add("First item");
        items.add("Second item");
    }

    public void addAddedItems(View view) {
        String itemText = etNewItem.getText().toString();
        aTodoItemsAdapter.add(itemText);
        etNewItem.setText("");
        writeItems();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
            String editedClickedItem = data.getExtras().getString("edited_clicked_item");
            int position = data.getExtras().getInt("position_clicked_item", 0);
            // Toast the name to display temporarily on screen
           // Toast.makeText(this, editedClickedItem, Toast.LENGTH_SHORT).show();

            //write to item in array and notify adapter
            items.set(position,editedClickedItem);
            aTodoItemsAdapter.notifyDataSetChanged();
            writeItems();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_todo, menu);
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

    private void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir,"todo.txt");
        try {
            items = new ArrayList<>(FileUtils.readLines(todoFile));
        } catch(IOException e) {
            items = new ArrayList<>();
        }
    }

    private void writeItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir,"todo.txt");
        try{
            FileUtils.writeLines(todoFile, items);
        }catch(IOException e){
            e.printStackTrace();
        }

    }

}
