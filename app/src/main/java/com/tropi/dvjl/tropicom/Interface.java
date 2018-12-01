package com.tropi.dvjl.tropicom;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.tropi.dvjl.tropicom.Annonceur.AnnoncePublier;
import com.tropi.dvjl.tropicom.Annonceur.ProfilVendeur;
import com.tropi.dvjl.tropicom.Demandes.Demande;

public class Interface extends AppCompatActivity {

    FrameLayout content;
    Toolbar toolbar;


    final Fragment fragment1 = new AnnoncePublier();
    final Fragment fragment2 = new Demande();
    final Fragment fragment3 = new ProfilVendeur();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragment1;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fm.beginTransaction().hide(active).show(fragment1).commit();
                    active = fragment1;
                    getSupportActionBar().setTitle("Tropicom");

//                    AnnoncePublier annoncePublier=new AnnoncePublier();
//                    android.support.v4.app.FragmentManager manager=getSupportFragmentManager();
//                    manager.beginTransaction().replace(R.id.content,annoncePublier).commit();
                    return true;
                case R.id.demande:
                    fm.beginTransaction().hide(active).show(fragment2).commit();
                    active = fragment2;
                    getSupportActionBar().setTitle("Demandes");

//                    Demande demande=new Demande();
//                    android.support.v4.app.FragmentManager manager3=getSupportFragmentManager();
//                    manager3.beginTransaction().replace(R.id.content,demande).commit();
                    return true;

                case R.id.navigation_notifications:
                    fm.beginTransaction().hide(active).show(fragment3).commit();
                    active = fragment3;
                    getSupportActionBar().setTitle("Mon Tropicom");

//                    ProfilVendeur profilVendeur=new ProfilVendeur();
//                    android.support.v4.app.FragmentManager manager4=getSupportFragmentManager();
//                    manager4.beginTransaction().replace(R.id.content,profilVendeur).commit();
                    return true;
            }
            return false;
        }

    };


    BottomNavigationView navigation;
    private int mSelectedItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interface);
        content= (FrameLayout) findViewById(R.id.content);


        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);




        if(getIntent().hasExtra("PROFIL"))
        {
            fm.beginTransaction().add(R.id.content, fragment3, "3").commit();
            fm.beginTransaction().add(R.id.content, fragment2, "2").hide(fragment2).commit();
            fm.beginTransaction().add(R.id.content,fragment1, "1").hide(fragment1).commit();

//            ProfilVendeur profilVendeur=new ProfilVendeur();
//            android.support.v4.app.FragmentManager manager4=getSupportFragmentManager();
//            manager4.beginTransaction().replace(R.id.content,profilVendeur).commit();
        }else{
            fm.beginTransaction().add(R.id.content, fragment3, "3").hide(fragment3).commit();
            fm.beginTransaction().add(R.id.content, fragment2, "2").hide(fragment2).commit();
            fm.beginTransaction().add(R.id.content,fragment1, "1").commit();

//            AnnoncePublier annoncePublier=new AnnoncePublier();
//            android.support.v4.app.FragmentManager manager=getSupportFragmentManager();
//            manager.beginTransaction().replace(R.id.content,annoncePublier).commit();

        }


    }


    @Override
    public void onBackPressed() {
      if(navigation.getSelectedItemId() == R.id.navigation_home)
      {
          AlertDialog alertDialog=new AlertDialog.Builder(Interface.this).create();
          alertDialog.setMessage("Voulez-vous quitter l'application ? ");
          alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Oui", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialogInterface, int i) {
                  finish();
              }
          });

          alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "NON", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialogInterface, int i) {
                  alertDialog.dismiss();
              }
          });

          alertDialog.show();

      }
      else
      {
          navigation.setSelectedItemId(R.id.navigation_home);
      }


    }
}
