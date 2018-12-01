package com.tropi.dvjl.tropicom.Annonceur;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.hbb20.CountryCodePicker;
import com.tropi.dvjl.tropicom.R;
import com.tropi.dvjl.tropicom.SqliteData.SessionManager;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PublierAnnonce extends AppCompatActivity {


    MaterialBetterSpinner conditionnement;
    Toolbar toolbar;
    Button btnsuivant;
    EditText phone,libelle,qualite,city,description,autr;
    CountryCodePicker pays;
    SessionManager sessionManager;
    @Override
    public void onStart() {
        getCoditionnement();
        super.onStart();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publier_annonce);
        toolbar=(Toolbar)findViewById(R.id.matoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Publier une annonce");
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_back));
        conditionnement=(MaterialBetterSpinner) findViewById(R.id.condionnement);
        phone= (EditText) findViewById(R.id.phone);
        libelle= (EditText) findViewById(R.id.libelle);
        qualite= (EditText) findViewById(R.id.qualite);
        city= (EditText) findViewById(R.id.city);
        pays= (CountryCodePicker) findViewById(R.id.pays);
        btnsuivant=(Button)findViewById(R.id.btnsuivant);
        description= (EditText) findViewById(R.id.description);
        autr=findViewById(R.id.autr);
        sessionManager=new SessionManager(getApplicationContext());

        phone.setText(sessionManager.getTel());
        city.setText(sessionManager.getVille());





        btnsuivant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tel=phone.getText().toString().trim();
                String conditi=conditionnement.getText().toString().trim();
                if(Objects.equals(conditi,"Autres..."))
                {
                    conditi=autr.getText().toString();
                }
                String lib=libelle.getText().toString().trim();
                String qal=qualite.getText().toString().trim();
                String cit=city.getText().toString().trim();
                String des=description.getText().toString().trim();

                if (!tel.isEmpty()){

                    if(!lib.isEmpty()){
                        if(!cit.isEmpty()){
                            if(!conditi.isEmpty())
                            {
                                if(!des.isEmpty()){
                                if(qal.isEmpty()){
                                    Intent intent=new Intent(PublierAnnonce.this,Annonce2.class);
                                    intent.putExtra("phone",tel);
                                    intent.putExtra("conditionnement",conditi);
                                    intent.putExtra("libelle",lib);
                                    intent.putExtra("city",cit);
                                    intent.putExtra("des",des);
                                    intent.putExtra("pays",pays.getSelectedCountryName());
                                    startActivity(intent);
                                }else{
                                    Intent intent=new Intent(PublierAnnonce.this,Annonce2.class);
                                    intent.putExtra("phone",tel);
                                    intent.putExtra("conditionnement",conditi);
                                    intent.putExtra("libelle",lib);
                                    intent.putExtra("city",cit);
                                    intent.putExtra("des",des);
                                    intent.putExtra("pays",pays.getSelectedCountryName());
                                    intent.putExtra("qalite",qal);
                                    startActivity(intent);
                                }
                                }else{
                                    description.setError("Donnez une description du produit");
                                }

                            }else{
                                conditionnement.setError("Champ vide");
                                autr.setError("Champ vide");
                            }

                        }else{
                            city.setError("Champ vide");
                        }
                    }else{
                        libelle.setError("Champ vide");
                    }
                }else{
                    phone.setError("Champ vide");
                }

            }
        });

    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    public void getCoditionnement(){
        String[] condlist=new String[]{"Sac de jute", "Sac pp", "Autres..."};
        final List<String> listadap=new ArrayList<String>();
        for(int j=0; j<condlist.length;j++){
            listadap.add(condlist[j]);
            ArrayAdapter<String> adp=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,listadap);
            adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            conditionnement.setAdapter(adp);
        }

        conditionnement.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==2)
                {
                    autr.setVisibility(View.VISIBLE);
                }else{
                    autr.setVisibility(View.GONE);
                }
            }
        });
    }


}
