package com.example.toshiba.sqlitev1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


 public class Sqlite extends SQLiteOpenHelper {

    String table_usuario="CREATE TABLE usuario(id_usuario INTEGER PRIMARY KEY AUTOINCREMENT , campo1 TEXT ,campo2 TEXT)";

    public Sqlite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "BDusuario", null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(table_usuario);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS usuario");
        onCreate(db);
    }
}
