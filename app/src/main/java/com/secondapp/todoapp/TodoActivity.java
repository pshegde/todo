package com.secondapp.todoapp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class TodoActivity extends ActionBarActivity {
    private List<TodoItem> items; //stores priority and item name
    private TodoAdapter aTodoItemsAdapter; //translate string/class/model to a view
    private ListView lvItems;
    private EditText etNewItem;
    private final int REQUEST_CODE_EDIT = 20;
    private TodoItemDatabase todoItemDatabase;
    private boolean click=true;
    private Button btnAdd;
    private Dialog dialog ;
    private Spinner spnSetPriority;
    private Button btnDialogAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getSupportActionBar().setIcon(R.drawable.todo_icon);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.todo_icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setTitle("Simple Todo");
        todoItemDatabase = new TodoItemDatabase(getBaseContext());

//        LinearLayout myTitleBar = (LinearLayout) findViewById(R.id.title_bar_layout);
//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setCustomView(R.layout.title_bar);

        setContentView(R.layout.activity_todo);
        //populateItems();
        readItems();
        lvItems = (ListView)findViewById(R.id.lvItems);
        etNewItem = (EditText) findViewById(R.id.etNewItem);

        aTodoItemsAdapter = new TodoAdapter(getBaseContext(), items);
        //new ArrayAdapter<String>(getBaseContext(),android.R.layout.simple_list_item_1,array);
        lvItems.setAdapter(aTodoItemsAdapter);
        //add to adapter automatically adds to view
        //aTodoItemsAdapter.add("Item 3");
        setupListViewListener();

        dialog = new Dialog(this,android.R.style.Theme_Translucent_NoTitleBar);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        final LinearLayout mainLayout = new LinearLayout(this);
        LayoutInflater li = LayoutInflater.from(this);
        final View dialogView = li.inflate(R.layout.popup_add_item, null);

        spnSetPriority = (Spinner) dialogView.findViewById(R.id.spnSetPriority);
        String[] arraySpinner = new String[] {
                "3", "2", "1"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        spnSetPriority.setAdapter(adapter);

        btnDialogAdd = (Button)dialogView.findViewById(R.id.btnDialogAdd);
        btnDialogAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemText = etNewItem.getText().toString();
                int priority = Integer.valueOf(spnSetPriority.getSelectedItem().toString());
                TodoItem newItem = new TodoItem(itemText,priority);
                addAddedItems(newItem);
                dialog.dismiss();
            }
        });
        btnAdd.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.setContentView(dialogView);
                dialog.show();
            }
        });
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View item, int position, long id) {
                todoItemDatabase.deleteTodoItem(items.get(position));
                items.remove(position);
                aTodoItemsAdapter.notifyDataSetChanged();
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(TodoActivity.this, EditItemActivity.class);
                i.putExtra("text_clicked_item", items.get(position).getBody().toString());
                i.putExtra("position_clicked_item", position);
                //startActivity(i); // brings up the second activity
                startActivityForResult(i, REQUEST_CODE_EDIT);
            }
        });
    }

    private void populateItems() {
        items = new ArrayList<>();
        items.add(new TodoItem("First item",1));

    }

    public void addAddedItems(TodoItem newItem) {
        aTodoItemsAdapter.add(newItem);
        etNewItem.setText("");
        todoItemDatabase.addTodoItem(newItem);
       // writeItems();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_EDIT) {
            // Extract name value from result extras
            String editedClickedItem = data.getExtras().getString("edited_clicked_item");
            int position = data.getExtras().getInt("position_clicked_item", 0);
            // Toast the name to display temporarily on screen
           // Toast.makeText(this, editedClickedItem, Toast.LENGTH_SHORT).show();

            //write to item in array and notify adapter
            TodoItem item =items.get(position);
            item.setBody(editedClickedItem);
            aTodoItemsAdapter.notifyDataSetChanged();
            todoItemDatabase.updateTodoItem(item);
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
        //get items from sqlite and add to items map
//        File filesDir = getFilesDir();
//        File todoFile = new File(filesDir,"todo.txt");
        items = todoItemDatabase.getAllTodoItems();

//        try {
//            items = new HashMap<>(FileUtils.readLines(todoFile));
//        } catch(IOException e) {
//            items = new ArrayList<>();
//        }
    }

//    private void writeItems() {
//        File filesDir = getFilesDir();
//        File todoFile = new File(filesDir,"todo.txt");
//        try{
//            FileUtils.writeLines(todoFile, items);
//        }catch(IOException e){
//            e.printStackTrace();
//        }
//
//        for (TodoItem item:items){
//            todoItemDatabase.addTodoItem(item);
//        }
//    }

}
