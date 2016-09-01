package com.example.qbclct.aphonebook;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by QBCLCT on 22/7/16.
 */
public class ContactListAdapter extends BaseAdapter {

    Context context;
    int layout;
    ArrayList objectsList = new ArrayList();
    public static LayoutInflater inflater = null;

    public ContactListAdapter(Context context, int resource, ArrayList objects){
        super();
        this.context = context;
        layout = resource;
        objectsList = objects;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return objectsList.size();
    }

    @Override
    public Object getItem(int position) {
        return objectsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder myViewHolder;

        if(convertView == null ){
            convertView = inflater.inflate(R.layout.person_contact, parent, false);
            myViewHolder = new MyViewHolder(convertView);
            convertView.setTag(myViewHolder);
        }
        else{
            myViewHolder = (MyViewHolder) convertView.getTag();
        }
        NewContact contact = (NewContact) getItem(getCount()-position-1);
        myViewHolder.name.setText(contact.getName());
        myViewHolder.phone.setText(contact.getPhone());
        if(contact.getImgPath() != null){
            String path = contact.getImgPath();
            int orientation = ImageRotate.getExifRotation(path);
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
//            File imgFile = new File(path);
//            if(imgFile.exists())
            Bitmap bm = null;
            bm = Helper.decodeSampledBitmapFromResource(contact.getImgPath() ,50, 50);
            if(orientation == 90){
                bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);}
                myViewHolder.contactIcon.setImageBitmap(bm);

        }
        return convertView;
    }

    private class MyViewHolder{
        TextView name, phone;
        ImageView contactIcon;
        public MyViewHolder(View item){
            name = (TextView) item.findViewById(R.id.textView);
            phone = (TextView) item.findViewById(R.id.textView2);
            contactIcon = (ImageView) item.findViewById(R.id.imageView2);
        }
    }
}
