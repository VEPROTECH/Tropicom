package com.tropi.dvjl.tropicom.Demandes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.android.volley.RequestQueue;
import com.hbb20.CountryCodePicker;
import com.tropi.dvjl.tropicom.MyRequest.MyRequest;
import com.tropi.dvjl.tropicom.R;
import com.tropi.dvjl.tropicom.SqliteData.SessionManager;
import com.tropi.dvjl.tropicom.VolleySingleton;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class addDemande extends AppCompatActivity {
    MaterialBetterSpinner spinner_type;
    private Bitmap bitmap1;
    Toolbar toolbar;
    Button btnsuivant;
    EditText phone,libelle,city,description,autr,qte;
    RadioGroup group;
    CountryCodePicker pays;
    SessionManager sessionManager;

    MyRequest request;
    private RequestQueue queue;
    Handler handler;

    @Override
    protected void onStart() {
        super.onStart();
        saveTypeProduit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_demande);
        handler=new Handler();

        toolbar=(Toolbar)findViewById(R.id.matoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Demande de produit");
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_back));

        queue= VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        request=new MyRequest(getApplicationContext(),queue);
        sessionManager=new SessionManager(getApplicationContext());

        group= (RadioGroup) findViewById(R.id.group);
        qte=(EditText)findViewById(R.id.qte);
        phone= (EditText) findViewById(R.id.phone);
        libelle= (EditText) findViewById(R.id.libelle);
        city= (EditText) findViewById(R.id.city);
        spinner_type=findViewById(R.id.spinner_type);
        pays= (CountryCodePicker) findViewById(R.id.pays);
        btnsuivant=(Button)findViewById(R.id.btnsuivant);
        description= (EditText) findViewById(R.id.description);
         autr=findViewById(R.id.autr);

        phone.setText(sessionManager.getTel());
        city.setText(sessionManager.getVille());

        btnsuivant.setOnClickListener(add);




    }



    public View.OnClickListener add=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String tel=phone.getText().toString().trim();
            String produit=spinner_type.getText().toString().trim();
            if(Objects.equals(produit,"Autres..."))
            {
                produit=autr.getText().toString();
            }
            String lib=libelle.getText().toString().trim();
            String cit=city.getText().toString().trim();
            String des=description.getText().toString().trim();
            String pay=pays.getSelectedCountryName();
            final String qt=qte.getText().toString();

            String unit = null;
            if(group.getCheckedRadioButtonId() == R.id.ra1){
                unit="Kg";
            }else{
                if(group.getCheckedRadioButtonId() == R.id.ra2){
                    unit="Tonne";
                }

            }

            if (!tel.isEmpty()){
                if(!lib.isEmpty()){
                    if(!cit.isEmpty()){
                        if(!produit.isEmpty())
                        {
                            if(!qt.isEmpty())
                            {
                                if(!des.isEmpty()){

                                    loading();
                                    String finalProduit = produit;
                                    String finalUnit = unit;
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            String code=sessionManager.getCODE();

                                            request.saveDemande(code, finalProduit,lib, pay,  cit, des, tel,qt, finalUnit, new MyRequest.ChangeCallBack()
                                            {
                                                @Override
                                                public void onSuccess(String message)
                                                {
                                                    hideDialogue();
                                                    Intent intent=new Intent(addDemande.this, Demande_img.class);
                                                    intent.putExtra("id",message);
                                                    startActivity(intent);
                                                    finish();
                                                }

                                                @Override
                                                public void onError(String msg)
                                                {
                                                    hideDialogue();
                                                }
                                            });

                                        }
                                    },2000);



                                }else{
                                    description.setError("Donnez une description du produit");
                                }
                            }else{
                                qte.setError("Champ vide !");
                            }


                        }else{
                            spinner_type.setError("Champ vide");
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
    };



    ProgressDialog prg;
    public void loading(){
        prg=new ProgressDialog(addDemande.this);
        prg.setMessage("Patienter un instant...");
//        prg.setCancelable(false);
        prg.show();
    }

    public void hideDialogue(){
        if(prg !=null){
            prg.dismiss();
            prg=null;
        }
    }

    public void saveTypeProduit(){
        String [] listProduits=new String[]{"Noix de cadjou",
                "Soja",
                "Riz",
                "Maïs",
                "Noix de karité",
                "Mil",
                "Sorgho",
                "Gemgenbre",
                "Cacao",
                "Autres..."

        };
        final List<String> listadap=new ArrayList<String>();
        for(int j=0; j<listProduits.length;j++){
            listadap.add(listProduits[j]);
            ArrayAdapter<String> adp=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,listadap);
            adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_type.setAdapter(adp);

            spinner_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if(i==9)
                    {
                        autr.setVisibility(View.VISIBLE);
                    }else{
                        autr.setVisibility(View.GONE);
                    }
                }
            });


        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }


}
