package com.example.qbclct.aphonebook;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class PeopleActivity extends AppCompatActivity {

    public static final String COLLECT = "collect";
    public static final String edit = "edit";
    public static final String index = "index";
    int position;
    long newRowId = 1;
    String stringContactArray;
    JSONArray jsonContactArray;
    ArrayList<NewContact> contactArrayList;
    NewContact contact;
    public static ListView mainListView;
    private ContactListAdapter listAdapter;
    ActionMode mActionMode;
    FeedReaderDbHelper mDbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        mainListView = (ListView) findViewById(R.id.listView);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        contactArrayList = new ArrayList<NewContact>();
        mDbHelper = new FeedReaderDbHelper(this);
        final Cursor cursor = mDbHelper.getAllPersons();
        if (cursor.moveToFirst())
        {
            do {
                try
                {
                    contact = new NewContact();
                    contact.setName(cursor.getString(cursor.getColumnIndex("name")));
                    contact.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
                    contact.setImgPath(cursor.getString(cursor.getColumnIndex("imgPath")));
                    contact.setId(cursor.getInt(cursor.getColumnIndex("_id")));
                    contactArrayList.add(contact);
                }
                catch (Exception e) {
//                    Log.e(MY_DEBUG_TAG, "Error " + e.toString());
                }

            } while (cursor.moveToNext());
        }
//        db.close();
//        String [] columns = new String[] {
//                FeedReaderContract.FeedEntry.COLUMN_NAME_ENTRY_ID,
//                FeedReaderContract.FeedEntry.COLUMN_NAME_NAME
//        };
//        int [] widgets = new int[] {
////                R.id.personID,
////                R.id.personName
//        };

//        Intent intent = getIntent();
//        if(intent.getExtras() != null && intent.getStringExtra(NewContactActivity.COLLECT) != null) {
//            stringContactArray = intent.getStringExtra(NewContactActivity.COLLECT);
//            try {
//                jsonContactArray = new JSONArray(stringContactArray);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        if(intent.getExtras() != null && intent.getStringExtra(EditContactActivity.EDITED) != null) {
//            stringContactArray = intent.getStringExtra(EditContactActivity.EDITED);
//            try {
//                jsonContactArray = new JSONArray(stringContactArray);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        if(stringContactArray == null){
//            SharedPreferences rSharedPref = this.getPreferences(Context.MODE_PRIVATE);
//            String defaultValue = "fgh";
//            stringContactArray =  rSharedPref.getString("key", defaultValue);
//
//            SQLiteDatabase db = mDbHelper.getReadableDatabase();
//            Cursor res = db.rawQuery( "SELECT * FROM " + FeedReaderContract.FeedEntry.TABLE_NAME + " WHERE " +
//                    FeedReaderContract.FeedEntry.COLUMN_NAME_ENTRY_ID + "=?", new String[] { Integer.toString((int) newRowId) } );
//
//            if(stringContactArray != null && stringContactArray != defaultValue) {
//                try {
//                    jsonContactArray = new JSONArray(stringContactArray);
//                    ;
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        if(jsonContactArray != null) {
//            contactArrayList = new Gson().fromJson(String.valueOf(jsonContactArray), new TypeToken<List<NewContact>>() {
//            }.getType());
//        }



        if(contactArrayList != null && contactArrayList.size() > 0){
            listAdapter = new ContactListAdapter(this, R.layout.person_contact, contactArrayList);
            mainListView.setAdapter(listAdapter);
        }
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PeopleActivity.this, EditContactActivity.class);
//                Bundle extras = new Bundle();
//                extras.putString(edit, jsonContactArray.toString());
//                extras.putInt(index, (jsonContactArray.length()-position-1));
//                intent.putExtras(extras);
//                Cursor itemCursor = (Cursor) PeopleActivity.this.mainListView.getItemAtPosition(position);
//                int personID = itemCursor.getInt(itemCursor.getColumnIndex(FeedReaderContract.FeedEntry._ID));
                contact = contactArrayList.get(contactArrayList.size()-position-1);
                intent.putExtra(index, contact);
                startActivity(intent);
            }
        });
        mainListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (mActionMode != null) {
                    return false;
                }
//                PeopleActivity.this.position = position;
                // Start the CAB using the ActionMode.Callback defined above
                PeopleActivity.this.position = position;
                view.setSelected(true);
                mActionMode = PeopleActivity.this.startSupportActionMode(mActionModeCallback);
                return true;
            }

        });
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
////        SharedPreferences wSharedPref = this.getPreferences(Context.MODE_PRIVATE);
////        SharedPreferences.Editor editor = wSharedPref.edit();
////        editor.putString("key" , stringContactArray);
////        editor.commit();
//
//        mDbHelper = new FeedReaderDbHelper(PeopleActivity.this);
//        SQLiteDatabase db = mDbHelper.getWritableDatabase();
//// Create a new map of values, where column names are the keys
//        ContentValues values = new ContentValues();
//        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_CONTACT, stringContactArray);
//// Insert the new row, returning the primary key value of the new row
//
//        newRowId = db.insert(
//                FeedReaderContract.FeedEntry.TABLE_NAME, null, values);
//    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_action, menu);
        MenuItem addButton = menu.findItem(R.id.add);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.add: {
                Intent intent = new Intent(PeopleActivity.this, NewContactActivity.class);
                if(jsonContactArray != null) {
                    intent.putExtra(COLLECT, jsonContactArray.toString());
                }
                startActivity(intent);
                return true;
            }
            default:{
                return super.onOptionsItemSelected(item);
            }
        }
    }

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.long_press_delete, menu);
            return true;
        }

        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.lDelete:
//                    jsonContactArray.remove(position);
                    NewContact dContact = contactArrayList.get(contactArrayList.size()-position-1);
                    contactArrayList.remove(contactArrayList.size()-position-1);
                    mDbHelper.delete(dContact.getId());
                    listAdapter.notifyDataSetChanged();
                    Toast.makeText(PeopleActivity.this, "Contact deleted", Toast.LENGTH_SHORT).show();
                    mode.finish(); // Action picked, so close the CAB
                    return true;
                default:
                    return false;
            }
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
        }
    };
}
