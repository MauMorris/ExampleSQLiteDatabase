package com.example.mauriciogodinez.basedatos1;

import android.provider.BaseColumns;

public class StudentContract {

    public static final class StudentEntry implements BaseColumns{
        public static final String TABLE_NAME = "student_table";
        public static final String COLUMN_NAME = "NAME";
        public static final String COLUMN_LASTNAME = "LASTNAME";
        public static final String COLUMN_MARKS = "MARKS";
    }
}