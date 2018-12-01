package com.tropi.dvjl.tropicom.SqliteData;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Verbeck DEGBESSE on 19/05/2018.
 */

public class ConnectSqlite extends SQLiteOpenHelper {

    private static final String TABLE_ANNONCE="Annonces";
    private static final String COL_ID="ID";
    private static final String COL_NOM="NOM";
    private static final String COL_PRENOM = "PRENOM";
    private static final String COL_DAY = "DAY";
    private static final String COL_HEURE = "HOURE";
    private static final String COL_LIBELLE = "LIBELLE";
    private static final String COL_TEL = "TEL";
    private static final String COL_QUALITE = "QUALITE";
    private static final String COL_CONDITIONNEMENT = "CONDITIONNEMENT";
    private static final String COL_PAYS = "PAYS";
    private static final String COL_CITY = "CITY";
    private static final String COL_PRODUIT = "PRODUIT";
    private static final String COL_QTE = "QUANTITE";
    private static final String COL_PU = "PRIX_UNITAIRE";
    private static final String COL_PV = "PRIX_VENTE";
    private static final String COL_VISIBLE = "VISIBLE";
    private static final String COL_UNITE = "UNITE";
    private static final String COL_DESCRIPTION = "DESCRIPTION";

    private static final String CREATE_BDD = "CREATE TABLE " + TABLE_ANNONCE + "("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_NOM + " TEXT NOT NULL, "
            + COL_PRENOM + " TEXT NOT NULL,"
            + COL_DAY + " DATE NOT NULL,"
            + COL_HEURE + " TEXT NOT NULL,"
            + COL_LIBELLE + " TEXT NOT NULL,"
            + COL_TEL + " TEXT NOT NULL,"
            + COL_QUALITE + " TEXT NULL,"
            + COL_CONDITIONNEMENT + " TEXT NOT NULL,"
            + COL_PAYS + " TEXT NOT NULL,"
            + COL_CITY + " TEXT NOT NULL,"
            + COL_PRODUIT + " TEXT NOT NULL,"
            + COL_QTE + " INTEGER NOT NULL,"
            + COL_PU + " TEXT NOT NULL,"
            + COL_PV + " TEXT NOT NULL,"
            + COL_UNITE + " TEXT NOT NULL,"
            + COL_DESCRIPTION + " TEXT NOT NULL,"
            + COL_VISIBLE + " TEXT NULL" +");";




    public ConnectSqlite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_BDD);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE" + TABLE_ANNONCE + ";");
        onCreate(sqLiteDatabase);
    }
}
