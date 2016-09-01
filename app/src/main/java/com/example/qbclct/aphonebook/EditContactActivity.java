package com.example.qbclct.aphonebook;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EditContactActivity extends AppCompatActivity {

    public static final String EDITED = "edited";
    public static final String EDIT = "edit";
    public static final String EDIT1 = "edit1";
    String contactArray;
    int index;
    JSONArray jsonContactArray;
    JSONObject jsonContact;
    TextView name;
    TextView phone;
    ImageView contactIcon;
    FeedReaderDbHelper mDbHelper;
    NewContact contact;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
//        Bundle extras = intent.getExtras();
//        contactArray = extras.getString(PeopleActivity.edit);
        contact = intent.getParcelableExtra(PeopleActivity.index);
//        try {
//            jsonContactArray = new JSONArray(contactArray);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        try {
//            jsonContact = (JSONObject) jsonContactArray.get(index);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        NewContact contact= (NewContact) PeopleActivity.mainListView.getItemAtPosition(index);

        name = (TextView) findViewById(R.id.textView3);
        phone = (TextView) findViewById(R.id.textView4);
        contactIcon = (ImageView) findViewById(R.id.imageView3);
        name.setText(contact.getName());
        phone.setText(contact.getPhone());

        String path = contact.getImgPath();
        int orientation = ImageRotate.getExifRotation(path);
        Matrix matrix = new Matrix();
        matrix.postRotate(90);

        Bitmap bm = null;
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(contact.getImgPath(), options);
                // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 200, 200);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        bm = BitmapFactory.decodeFile(contact.getImgPath(), options);

        if(orientation == 90){
            bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);}
        contactIcon.setImageBitmap(bm);

    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.edit_contact_menu, menu);
        MenuItem deleteButton = menu.findItem(R.id.delete);
        MenuItem editButton = menu.findItem(R.id.edit);
        return true;
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.delete: {
//                jsonContactArray.remove(index);
                Intent intent = new Intent(EditContactActivity.this, PeopleActivity.class);
//                intent.putExtra(EDITED,jsonContactArray.toString());
                FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(EditContactActivity.this);
                mDbHelper.delete(contact.getId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                Toast.makeText(EditContactActivity.this, "Contact deleted", Toast.LENGTH_SHORT).show();
                return true;
            }
            case R.id.edit: {
                Intent intent = new Intent(EditContactActivity.this, NewContactActivity.class);
                intent.putExtra(EDIT, contact);
//                Bundle extras = new Bundle();
//                extras.putString(EDIT,contactArray);
//                extras.putInt(EDIT1,index);
//                intent.putExtras(extras);
                startActivity(intent);
                return true;
            }
            default:{
                return super.onOptionsItemSelected(item);
            }
        }
    }
//    @TargetApi(Build.VERSION_CODES.KITKAT)
//    public void deleteItem(JSONArray jsonContactArray, int index){
//        jsonContactArray.remove(index);
//        Intent intent = new Intent(EditContactActivity.this, PeopleActivity.class);
//        intent.putExtra(EDITED,jsonContactArray.toString());
//        startActivity(intent);
//        Toast.makeText(EditContactActivity.this, "Contact deleted", Toast.LENGTH_SHORT).show();
//    }
}
