package com.example.artbook

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

public  object  DbInfo{
    const val DATABASE_VERSION = 1
    const val DATABASE_NAME = "app.db"
}

public  object ArtTableSchema: BaseColumns  {
        // Table contents are grouped together in an anonymous object.
        const val TABLE_NAME = "art"
        const val COLUMN_NAME_BITMAP = "bitmap"
        const val COLUMN_NAME_ARTNAME = "artname"
        const val COLUMN_NAME_ARTISTNAME= "artistname"
        const val COLUMN_NAME_YEAR  = "year"

    }
public  const val SQL_CREATE_ENTRIES =
    "CREATE TABLE ${ArtTableSchema.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY, " +
            "${ArtTableSchema.COLUMN_NAME_ARTNAME} TEXT, " +
            "${ArtTableSchema.COLUMN_NAME_ARTISTNAME} TEXT, " +
            "${ArtTableSchema.COLUMN_NAME_YEAR} INTEGER, " +
            "${ArtTableSchema.COLUMN_NAME_BITMAP} BLOB" +
            ")"

public  const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${ArtTableSchema.TABLE_NAME}"

public  class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DbInfo.DATABASE_NAME, null, DbInfo.DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }
    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }
    fun deleteTable(db: SQLiteDatabase){
        db.execSQL(SQL_DELETE_ENTRIES)
    }
    fun insertData(db: SQLiteDatabase , model:ArtModel){

    }

}
