package com.tropi.dvjl.tropicom.SqliteData;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.tropi.dvjl.tropicom.MyObject.Annonce;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Verbeck DEGBESSE on 19/05/2018.
 */

public class CrudSqlite {

    private static final String TABLE_ANNONCE="Annonces";
    private static final String COL_ID="ID";
    private static final int NUM_COL_ID = 0;
    private static final String COL_NOM="NOM";
    private static final int NUM_COL_NOM = 1;

    private static final String COL_PRENOM = "PRENOM";
    private static final int NUM_COL_PRENOM = 2;

    private static final String COL_DAY = "DAY";
    private static final int NUM_COL_DAY = 3;

    private static final String COL_HEURE = "HOURE";
    private static final int NUM_COL_HEURE = 4;

    private static final String COL_LIBELLE = "LIBELLE";
    private static final int NUM_COL_LIBELLE = 5;

    private static final String COL_TEL = "TEL";
    private static final int NUM_COL_TEL = 6;

    private static final String COL_QUALITE = "QUALITE";
    private static final int NUM_COL_QUALITE = 7;

    private static final String COL_CONDITIONNEMENT = "CONDITIONNEMENT";
    private static final int NUM_COL_CONDITIONNEMENT = 8;

    private static final String COL_PAYS = "PAYS";
    private static final int NUM_COL_PAYS = 9;

    private static final String COL_CITY = "CITY";
    private static final int NUM_COL_CITY = 10;

    private static final String COL_PRODUIT = "PRODUIT";
    private static final int NUM_COL_PRODUIT = 11;

    private static final String COL_QTE = "QUANTITE";
    private static final int NUM_COL_QTE = 12;

    private static final String COL_PU = "PRIX_UNITAIRE";
    private static final int NUM_COL_PU= 13;

    private static final String COL_PV = "PRIX_VENTE";
    private static final int NUM_COL_PV = 14;

    private static final String COL_VISIBLE = "VISIBLE";
    private static final int NUM_COL_VISIBLE= 15;

    private static final String COL_UNITE = "UNITE";
    private static final int NUM_COL_UNITE= 16;

    private static final String COL_DESCRIPTION = "DESCRIPTION";
    private static final int NUM_COL_DESCRIPTION= 17;

    private static final int VERSION_BDD=1;
    private static final String NOM_BDD="tropicomv2.db";
    Context context;

    //Tres important
    private SQLiteDatabase bdd;
    public ConnectSqlite connectSqlite;

    public CrudSqlite(Context context) {
        this.context = context;
        connectSqlite= new ConnectSqlite(context,NOM_BDD, null, VERSION_BDD);
        bdd = connectSqlite.getWritableDatabase();
    }


    public long insertionAnnonce(Annonce annonce){
        ContentValues values= new ContentValues();
        values.put(COL_NOM, annonce.getNom());
        values.put(COL_PRENOM, annonce.getPrenom());
        values.put(COL_DAY, annonce.getDayAnnonce());
        values.put(COL_HEURE, annonce.getHeuAnnonce());
        values.put(COL_LIBELLE, annonce.getLibelle());
        values.put(COL_PAYS, annonce.getPays());
        values.put(COL_CITY, annonce.getCity());
        values.put(COL_PRODUIT, annonce.getProduit());
        values.put(COL_QTE, annonce.getQte());
        values.put(COL_PU, annonce.getPrix_unitaire());
        values.put(COL_PV, annonce.getPrix_vente());
        if(annonce.getQualite() == null){
            values.put(COL_QUALITE, "");
        }else{
            values.put(COL_QUALITE, annonce.getQualite());
        }
        values.put(COL_TEL, annonce.getTel());
        values.put(COL_CONDITIONNEMENT, annonce.getConditionnement());
        values.put(COL_VISIBLE, "true");
        values.put(COL_UNITE, annonce.getUnite());
        values.put(COL_DESCRIPTION, annonce.getDescription());

        return bdd.insert(TABLE_ANNONCE,null,values);
    }

    public List<Annonce> getAnonceList(){
        List<Annonce> annonceList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_ANNONCE;
        Cursor cursor = bdd.rawQuery(query, null);

        if (cursor.getCount()== 0) {
            Toast.makeText(context,"aucune donnée trouvée",Toast.LENGTH_SHORT).show();
            return null;
        }else{
            while(cursor.moveToNext()){
                Annonce annonce = new Annonce();
                // On lui affecte ttes les infos
                annonce.setId(cursor.getInt(NUM_COL_ID));
                annonce.setNom(cursor.getString(NUM_COL_NOM));
                annonce.setPrenom(cursor.getString(NUM_COL_PRENOM));
                annonce.setDayAnnonce(cursor.getString(NUM_COL_DAY));
                annonce.setHeuAnnonce(cursor.getString(NUM_COL_HEURE));
                annonce.setCity(cursor.getString(NUM_COL_CITY));
                annonce.setPays(cursor.getString(NUM_COL_PAYS));
                annonce.setConditionnement(cursor.getString(NUM_COL_CONDITIONNEMENT));
                annonce.setQte(Integer.parseInt(cursor.getString(NUM_COL_QTE)));
                annonce.setQualite(cursor.getString(NUM_COL_QUALITE));
                annonce.setTel(cursor.getString(NUM_COL_TEL));
                annonce.setLibelle(cursor.getString(NUM_COL_LIBELLE));
                annonce.setPrix_unitaire(cursor.getString(NUM_COL_PU));
                annonce.setPrix_vente(cursor.getString(NUM_COL_PV));
                annonce.setProduit(cursor.getString(NUM_COL_PRODUIT));
                annonce.setVisible(cursor.getString(NUM_COL_VISIBLE));
                annonce.setUnite(cursor.getString(NUM_COL_UNITE));
                annonce.setDescription(cursor.getString(NUM_COL_DESCRIPTION));
                annonceList.add(annonce);
            }
            cursor.close();
            return annonceList;
        }
    }


    public void close(){
        bdd.close();
    }

    public SQLiteDatabase getBDD(){
        return bdd;
    }
}
