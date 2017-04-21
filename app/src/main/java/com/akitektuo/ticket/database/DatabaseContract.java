package com.akitektuo.ticket.database;

import android.provider.BaseColumns;

/**
 * Created by AoD Akitektuo on 21-Apr-17 at 17:28.
 */

public class DatabaseContract {

    public static final int CURSOR_ERROR = 0;
    public static final int CURSOR_TYPE = 1;
    public static final int CURSOR_TEXT = 2;
    public static final int CURSOR_DAY = 3;
    public static final int CURSOR_MONTH = 4;
    public static final int CURSOR_YEAR = 5;
    public static final int CURSOR_HOUR = 6;
    public static final int CURSOR_MINUTE = 7;

    abstract class MessageContractEntry implements BaseColumns {
        static final String TABLE_NAME = "massage";
        static final String COLUMN_ERROR = "error";
        static final String COLUMN_TYPE = "type";
        static final String COLUMN_TEXT = "text";
        static final String COLUMN_DAY = "day";
        static final String COLUMN_MONTH = "month";
        static final String COLUMN_YEAR = "year";
        static final String COLUMN_HOUR = "hour";
        static final String COLUMN_MINUTE = "minute";
    }

}
