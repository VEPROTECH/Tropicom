package com.tropi.dvjl.tropicom.SqliteData;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Verbeck DEGBESSE on 01/10/2017.
 */

public class SessionManager {
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private final static String PREFS_NAME="app_prefs";
    private  final static int PRIVATE_MODE=0;
    private final static String IS_LOAGGED="islogged";
    private final static String TEL="tel";
    private final static String ID="id";
    private final static String NAME="name";
    private final static String NOM="nom";
    private final static String PRENOM="prenom";
    private final static String CODE="code";
    private final static String PAYS="pays";
    private final static String VILLE="ville";
    private final static String COMPTE="compte";
    private final static String PHOTO="photo";
    Context context;

    public SessionManager(Context context){
        this.context=context;
        prefs=context.getSharedPreferences(PREFS_NAME,PRIVATE_MODE);
        editor=prefs.edit();
    }

    public boolean isLogged(){
        return prefs.getBoolean(IS_LOAGGED,false);
    }

    public  String getTel() {
        return prefs.getString(TEL,null);
    }
    public  String getId() {
        return prefs.getString(ID,null);
    }
    public String getNAME() {
        return prefs.getString(NAME,null);
    }
    public String getCODE() {
        return prefs.getString(CODE,null);
    }
    public String getPays(){return  prefs.getString(PAYS,null);}
    public String getVille(){return  prefs.getString(VILLE,null);}
    public String getCompte(){return  prefs.getString(COMPTE,null);}
    public String getNom(){return  prefs.getString(NOM,null);}
    public String getPrenom(){return  prefs.getString(PRENOM,null);}

    public String getPhoto(){return  prefs.getString(PHOTO,null);}

    public void inserUser(String tel, String id, String name, String code,String pays,String ville,String compte,String photo,String nom,String prenom){
        editor.putBoolean(IS_LOAGGED,true);
        editor.putString(ID,id);
        editor.putString(TEL,tel);
        editor.putString(NAME,name);
        editor.putString(CODE,code);
        editor.putString(PAYS,pays);
        editor.putString(VILLE,ville);
        editor.putString(COMPTE,compte);
        editor.putString(PHOTO,photo);
        editor.putString(NOM,nom);
        editor.putString(PRENOM,prenom);
        editor.commit();
    }

    public void logout(){
        editor.clear().commit();
    }

}
