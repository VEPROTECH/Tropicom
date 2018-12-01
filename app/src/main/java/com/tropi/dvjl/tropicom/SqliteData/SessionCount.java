package com.tropi.dvjl.tropicom.SqliteData;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Verbeck DEGBESSE on 01/10/2017.
 */

public class SessionCount {
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private final static String PREFS_NAME="app_prefs";
    private  final static int PRIVATE_MODE=0;
    private final static String IS_LOAGGED="islogged";
    private final static String ANNONCE_NBRE="nbre";
    private final static String DEMANDE_NBRE="nbredemande";
    Context context;

    public SessionCount(Context context){
        this.context=context;
        prefs=context.getSharedPreferences(PREFS_NAME,PRIVATE_MODE);
        editor=prefs.edit();
    }

    public  String getAnnonceNbre() {
        return prefs.getString(ANNONCE_NBRE,null);
    }

    public  String getDemandeNbre() {
        return prefs.getString(DEMANDE_NBRE,null);
    }


    public void inserNbreDemande(String nbre){
        editor.putString(DEMANDE_NBRE,nbre);

        editor.commit();
    }

    public void inserNbre(String nbre){

        editor.putString(ANNONCE_NBRE,nbre);

        editor.commit();
    }

}
