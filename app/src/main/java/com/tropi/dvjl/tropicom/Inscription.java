package com.tropi.dvjl.tropicom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.hbb20.CountryCodePicker;
import com.tropi.dvjl.tropicom.MyRequest.MyRequest;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Inscription extends AppCompatActivity {
MaterialBetterSpinner compte;
    Toolbar toolbar;
    Button inscrit;

    EditText phone,nom,prenom,societe,ville,passe1,passe2;
    MyRequest request;
    RequestQueue queue;
    Handler handler;
    LinearLayout linea;
    CountryCodePicker country;
    TextView sec;

    @Override
    protected void onStart() {
        saveTypeCompte();
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        toolbar=(Toolbar)findViewById(R.id.matoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setTitle("Inscription");
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_back));

        compte= (MaterialBetterSpinner) findViewById(R.id.compte);
        phone=findViewById(R.id.phone);
        nom=findViewById(R.id.nom);
        prenom=findViewById(R.id.prenom);
        societe=findViewById(R.id.societe);
        ville=findViewById(R.id.ville);
        passe1=findViewById(R.id.passe1);
        passe2=findViewById(R.id.passe2);
        inscrit=findViewById(R.id.inscrit);
        inscrit.setOnClickListener(regi);
        linea=findViewById(R.id.linea);
        country=findViewById(R.id.pays);
        sec=findViewById(R.id.sec);

        AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
            @Override
            public void onSuccess(Account account) {
                phone.setText(account.getPhoneNumber().toString());
                Log.e("ER","cool" );
            }

            @Override
            public void onError(AccountKitError accountKitError) {
                Log.e("ER",accountKitError.toString() );
            }
        });

        linea.setVisibility(View.GONE);
        queue= VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        request=new MyRequest(getApplicationContext(),queue);
        handler=new Handler();

sec.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        AccountKit.logOut();
        
        Intent i=new Intent(Inscription.this,login.class);
                startActivity(i);
        finish();
    }
});
    }
    ProgressDialog prg;
    public void loading(){
        prg=new ProgressDialog(Inscription.this);
        prg.setMessage("Enregistrement en cours...");
//        prg.setCancelable(false);
        prg.show();
    }

    public void hideDialogue(){
        if(prg !=null){
            prg.dismiss();
            prg=null;
        }
    }

    public void saveTypeCompte(){
        String [] listCompte=new String[]{"Acheteur","Vendeur" };
        final List<String> listadap=new ArrayList<String>();
        for(int j=0; j<listCompte.length;j++){
            listadap.add(listCompte[j]);
            ArrayAdapter<String> adp=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,listadap);
            adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            compte.setAdapter(adp);

            compte.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if(i == 0)
                    {
                        linea.setVisibility(View.GONE);
                    }else{
                        linea.setVisibility(View.VISIBLE);
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

    public View.OnClickListener regi=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            register();
        }
    };

    public void register()
    {
        final String pays=country.getSelectedCountryName(); //le pays ici
        final String nm=nom.getText().toString();
        final String pnom=prenom.getText().toString();
        final String vil=ville.getText().toString();
        final String pax1=passe1.getText().toString();
        String pax2=passe2.getText().toString();
        final String tel=phone.getText().toString();
        final String cmp=compte.getText().toString();
        if(!cmp.isEmpty()){
            if(!nm.isEmpty()){
                if(!pnom.isEmpty())
                {
                    if(!vil.isEmpty())
                    {
                        if(!pax1.isEmpty())
                        {
                            if(!pax2.isEmpty()){

                                if(pax1.equals(pax2))
                                {
                                if(!cmp.equals("Vendeur")){
                                    loading();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            request.register(tel, nm, pnom, pays, vil, pax1, cmp, "", new MyRequest.RegisterCallBack() {
                                                @Override
                                                public void onSuccess(String message) {
                                                    hideDialogue();
                                                    Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                                                    Intent intent=new Intent(Inscription.this,login.class);
                                                    startActivity(intent);
                                                    finish();
                                                }

                                                @Override
                                                public void inputError(Map<String, String> errors) {
                                                    hideDialogue();
                                                }

                                                @Override
                                                public void onError(String message) {
                                                    hideDialogue();
                                                    Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }
                                    },2000);



                                }else{


                                    final String soci=societe.getText().toString();
                                    if(!soci.isEmpty())
                                    {
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                loading();
                                                request.register(tel, nm, pnom, pays, vil, pax1, cmp, soci, new MyRequest.RegisterCallBack() {
                                                    @Override
                                                    public void onSuccess(String message) {
                                                        hideDialogue();
                                                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                                                        Intent intent=new Intent(Inscription.this,login.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }

                                                    @Override
                                                    public void inputError(Map<String, String> errors) {
                                                        hideDialogue();
                                                    }

                                                    @Override
                                                    public void onError(String message) {
                                                        hideDialogue();
                                                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                            }
                                        },2000);

                                    }else{
                                        societe.setError("Champ vide !");
                                    }
                                }

                                }else {
                                    Toast.makeText(getApplicationContext(),"Les deux mot de passe ne sont pas les mêmes !",Toast.LENGTH_LONG).show();

                                }
                            }else{
                                passe2.setError("Champ vide !");
                            }

                        }else{
                            passe1.setError("Champ vide !");
                        }
                    }else{
                        ville.setError("Champ vide !");
                    }

                }else{
                    prenom.setError("Entrer votre prénom !");
                }
            }else{
                nom.setError("Entrer votre nom");
            }
        }else{
            compte.setError("Sélectionnez le type de compte !");
        }
    }


}
