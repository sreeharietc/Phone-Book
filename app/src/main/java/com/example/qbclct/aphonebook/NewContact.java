package com.example.qbclct.aphonebook;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by QBCLCT on 22/7/16.
 */
public class NewContact implements Parcelable {
    String name;
    String phone;
    String imgPath;
    int id;

    protected NewContact(Parcel in) {
        name = in.readString();
        phone = in.readString();
        imgPath = in.readString();
        id = in.readInt();
    }

    public static final Creator<NewContact> CREATOR = new Creator<NewContact>() {
        @Override
        public NewContact createFromParcel(Parcel in) {
            return new NewContact(in);
        }

        @Override
        public NewContact[] newArray(int size) {
            return new NewContact[size];
        }
    };

    public NewContact() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(imgPath);
        dest.writeInt(id);
    }
}
