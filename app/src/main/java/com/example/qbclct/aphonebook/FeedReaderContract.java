package com.example.qbclct.aphonebook;

import android.provider.BaseColumns;

/**
 * Created by QBCLCT on 27/7/16.
 */
public class FeedReaderContract {
    public FeedReaderContract() {
    }

    /* Inner class that defines the table contents */
    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_PHONE = "phone";
        public static final String COLUMN_NAME_IMG_PATH = "imgPath";
        public static final String TEXT_TYPE = " TEXT";
        public static final String COMMA_SEP = ",";
        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                        FeedEntry._ID + " INTEGER PRIMARY KEY," +
                        FeedEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                        FeedEntry.COLUMN_NAME_PHONE + TEXT_TYPE + COMMA_SEP +
                        FeedEntry.COLUMN_NAME_IMG_PATH + TEXT_TYPE + " )";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;
    }

}
