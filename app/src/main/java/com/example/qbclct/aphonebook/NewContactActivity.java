package com.example.qbclct.aphonebook;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class NewContactActivity extends AppCompatActivity {

    public static final int SELECT_FILE = 2;
    public static final int REQUEST_CAMERA = 4;
    String path;
    EditText name;
    EditText phone;
    ImageView imageView;
    Button button;
    JSONObject jsonContact;
    JSONArray jsonContactArray;
    String stringContactArray;
    public static final String COLLECT = "collect";
    int index;
    Intent intent2;
    NewContact contact;
    FeedReaderDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        imageView = (ImageView) findViewById(R.id.imageView);
        name = (EditText) findViewById(R.id.editText);
        phone = (EditText) findViewById(R.id.editText2);
        button = (Button) findViewById(R.id.button3);

        intent2 = getIntent();
        if( intent2.getExtras()!= null ) {

            contact = intent2.getParcelableExtra(EditContactActivity.EDIT);
//            contact = (NewContact) PeopleActivity.mainListView.getItemAtPosition(index);

            name.setText(contact.getName());
            phone.setText(contact.getPhone());
            path = contact.getImgPath();
            Bitmap bm = null;
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(contact.getImgPath(), options);
            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, 200, 200);
            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            bm = BitmapFactory.decodeFile(contact.getImgPath(), options);
            imageView.setImageBitmap(bm);
        }
        else {
            imageView.setImageResource(R.drawable.no_phot_icon);
            path = "/storage/emulated/0/Download/images(2).jpeg";
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

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

    private void selectImage() {

        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(NewContactActivity.this);
        builder.setTitle("Add Photo");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
//                    startActivityForResult(intent, 1);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);

                }
                else if (options[item].equals("Choose from Gallery"))
                {
//                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    startActivityForResult(intent, 2);
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);//
                    startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                try {
                    onSelectFromGalleryResult(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            else if (requestCode == REQUEST_CAMERA){
                   onCaptureImageResult(data);
            }
        }
    }
    private void onSelectFromGalleryResult(Intent data) throws JSONException {
        Bitmap bm=null;
        Uri selectedImage = data.getData();
        path = getRealPathFromURI(this, selectedImage);
        int orientation = ImageRotate.getExifRotation(path);
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        // contact.setImgPath(path);
//        jsonContact.put("imgPath", path);
//        contact = new NewContact();
//        contact.setImgPath(path);

        if (data != null) {
            bm = Helper.decodeSampledBitmapFromResource(path ,50, 50);
        }
        if(orientation == 90){
            bm = Bitmap.createBitmap(bm, 0, 0,
                    bm.getWidth(), bm.getHeight(),
                    matrix, true);}
        ImageView selectedImg = (ImageView) findViewById(R.id.imageView);
        selectedImg.setImageBitmap(bm);
    }
    private void onCaptureImageResult(Intent data) {
//        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
//
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
//        File destination = new File(Environment.getExternalStorageDirectory(),
//                System.currentTimeMillis() + ".jpg");
//        FileOutputStream fo;
//        try {
//            destination.createNewFile();
//            fo = new FileOutputStream(destination);
//            fo.write(bytes.toByteArray());
//            fo.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        Bitmap thumbnail = null;
        Uri selectedImage = data.getData();
        path = getRealPathFromURI(this, selectedImage);
        int orientation = ImageRotate.getExifRotation(path);
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
//        Bitmap bitmapOrg = BitmapFactory.decodeFile(path);
//        rotateBitmap(bitmapOrg, 50, 50);
        if (data != null) {
            thumbnail = Helper.decodeSampledBitmapFromResource(path ,50, 50 );
        }
        if(orientation == 90){
            thumbnail = Bitmap.createBitmap(thumbnail, 0, 0,
                    thumbnail.getWidth(), thumbnail.getHeight(),
                    matrix, true);}
        ImageView selectedImg = (ImageView) findViewById(R.id.imageView);
        selectedImg.setImageBitmap(thumbnail);
        // contact.setImg(thumbnail);
    }

//    private Bitmap rotateBitmap(Bitmap bitmapOrg, int width, int height)
//    {
//        Matrix matrix = new Matrix();
//
//        matrix.postRotate(90);
//
//        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmapOrg,width,height,true);
//
//        Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap , 0, 0, scaledBitmap .getWidth(), scaledBitmap .getHeight(), matrix, true);
//        return  rotatedBitmap;
//    }

    private String getRealPathFromURI(Context context, Uri contentUri) {
       /* Cursor cursor = null;
        try {
            String[] proj = { MediaStore.MediaColumns.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }*/
        String[] filePath = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, filePath, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String imagePath = cursor.getString(column_index);
        cursor.close();
        return imagePath;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.new_contact_menu, menu);
        MenuItem done_button = (MenuItem) findViewById(R.id.done_button);
        MenuItem discard_item = (MenuItem) findViewById(R.id.discard_button);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.done_button: {
                name = (EditText) findViewById(R.id.editText);
                phone = (EditText) findViewById(R.id.editText2);
                FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(this);
// Create a new map of values, where column names are the keys
                if(intent2.getExtras() != null){
                    mDbHelper.update(contact.getId(), name.getText().toString(), phone.getText().toString(), path);
                }
                else {
                    SQLiteDatabase db = mDbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_NAME, name.getText().toString());
                    values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_PHONE, phone.getText().toString());
                    values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_IMG_PATH, path);
// Insert the new row, returning the primary key value of the new row
                    long newRowId;
                    newRowId = db.insert(
                            FeedReaderContract.FeedEntry.TABLE_NAME, null, values);
                    System.out.println(newRowId);
                }
//                Intent intent = getIntent();
//                if(intent.getExtras()!= null && intent.getStringExtra(PeopleActivity.COLLECT) != null){
//                    stringContactArray = intent.getStringExtra(PeopleActivity.COLLECT);
//                    try {
//                        jsonContactArray = new JSONArray(stringContactArray);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                Intent intent2 = getIntent();
//                Bundle extras = intent2.getExtras();
//
//
//                if(intent2.getExtras()!= null && extras.getString(EditContactActivity.EDIT) != null){
//                    stringContactArray = extras.getString(EditContactActivity.EDIT);
//                    index = extras.getInt(EditContactActivity.EDIT1);
//                    try {
//                        jsonContactArray = new JSONArray(stringContactArray);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                if(jsonContactArray == null) {
//                    jsonContactArray = new JSONArray();
//                }
//
//
//                name = (EditText) findViewById(R.id.editText);
//                phone = (EditText) findViewById(R.id.editText2);
//                jsonContact = new JSONObject();
//                try {
//                    jsonContact.put("name", name.getText().toString());
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                try {
//                    jsonContact.put("phone", phone.getText().toString());
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                try {
//                    jsonContact.put("imgPath", path);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                if(intent2.getExtras()!= null && extras.getString(EditContactActivity.EDIT) != null) {
//                    try {
//                        jsonContactArray.put(index,jsonContact);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    Toast.makeText(NewContactActivity.this, "Changes added.", Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    jsonContactArray.put(jsonContact);
//                }
                Intent intent1 = new Intent(NewContactActivity.this, PeopleActivity.class);

//                intent1.putExtra(COLLECT, jsonContactArray.toString());
//                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
                finish();
//                Toast.makeText(NewContactActivity.this, "New Contact added.", Toast.LENGTH_SHORT).show();

                return true;
            }
            case R.id.discard_button: {
                Intent intent2 = new Intent(NewContactActivity.this, PeopleActivity.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent2);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
