package com.tropi.dvjl.tropicom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.facebook.accountkit.AccountKit;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.hbb20.CountryCodePicker;
import com.tropi.dvjl.tropicom.MyRequest.MyRequest;
import com.tropi.dvjl.tropicom.SqliteData.SessionManager;

public class Social_connect extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    EditText phone,societe,ville;
    MyRequest request;
    RequestQueue queue;
    Handler handler;
    LinearLayout linea;
    CountryCodePicker country;
    RadioGroup group;
    RadioButton rad1,rad2;
    Toolbar toolbar;
    Button inscrit,retour;
    String compte;
    SessionManager sessionManager;
    GoogleApiClient googleApiClient;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_connect);

        Window window=this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        toolbar=(Toolbar)findViewById(R.id.matoolbar);
        setSupportActionBar(toolbar);

        phone=findViewById(R.id.phone);
        retour=findViewById(R.id.retour);
        group=findViewById(R.id.group);
        societe=findViewById(R.id.societe);
        ville=findViewById(R.id.ville);
        inscrit=findViewById(R.id.inscrit);
        inscrit.setOnClickListener(regi);
        linea=findViewById(R.id.linea);
        country=findViewById(R.id.pays);
        rad1=findViewById(R.id.rad1);
        rad2=findViewById(R.id.rad2);

        GoogleSignInOptions signInOptions= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient=new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions).build();


        sessionManager=new SessionManager(getApplicationContext());
        linea.setVisibility(View.GONE);
        compte=rad1.getText().toString();

        queue= VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        request=new MyRequest(getApplicationContext(),queue);
        handler=new Handler();
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if(radioGroup.getCheckedRadioButtonId()==R.id.rad1)
                {
                    linea.setVisibility(View.GONE);
                    compte=rad1.getText().toString();
                }else{
                    linea.setVisibility(View.VISIBLE);
                    compte=rad2.getText().toString();
                }
            }
        });


        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logOut();
                AccountKit.logOut();

                Intent i = getIntent();
                if(i.hasExtra("VALUE")){
                    Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            Intent intent=new Intent(Social_connect.this,TypeConnexion.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }

                Intent intent=new Intent(Social_connect.this,TypeConnexion.class);
                startActivity(intent);
                finish();
            }
        });


    }




    public View.OnClickListener regi=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            regist();
        }
    };

    ProgressDialog prg;
    public void loading(){
        prg=new ProgressDialog(Social_connect.this);
        prg.setMessage("Veuillez patienter...");
//        prg.setCancelable(false);
        prg.show();
    }

    public void hideDialogue(){
        if(prg !=null){
            prg.dismiss();
            prg=null;
        }
    }

    public void regist()
    {
        final String pays=country.getSelectedCountryName(); //le pays ici
        final String nm=getIntent().getStringExtra("nom");
        final String pnom=getIntent().getStringExtra("prenom");
        final String tof=getIntent().getStringExtra("photoUrl");
        final String lien=getIntent().getStringExtra("lien");
        final String vil=ville.getText().toString();
        final String tel=""; // country.getSelectedCountryCodeWithPlus()+""+phone.getText().toString();
        final String soc=societe.getText().toString();


        if(rad1.isChecked())
        {
            if(!vil.isEmpty())
            {
                loading();
                request.registerSocial(tel, nm, pnom, pays, vil, lien, tof, compte, "", new MyRequest.LoginCallback() {
                    @Override
                    public void onSuccess(String tel,String password, String nom,String prenom,String code,String ville,String pays,String compte,String photo)
                    {
                        hideDialogue();
                        sessionManager.inserUser(tel,code,nom+" "+prenom,code,pays,ville,compte,photo,nom,prenom);
                        Intent intent=new Intent(Social_connect.this,Interface.class);
                        startActivity(intent);
                        finish();

                    }

                    @Override
                    public void onError(String message) {
                        hideDialogue();
                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();

                    }
                });

            }else{
                hideDialogue();
                Toast.makeText(getApplicationContext(),"Champ(s) vide(s) !",Toast.LENGTH_LONG).show();
            }
        }else{
            if(rad2.isChecked()){
                if(!vil.isEmpty() && !soc.isEmpty())
                {
                    loading();
                    request.registerSocial(tel, nm, pnom, pays, vil, lien, tof, compte, soc, new MyRequest.LoginCallback() {
                        @Override
                        public void onSuccess(String tel,String password, String nom,String prenom,String code,String ville,String pays,String compte,String photo) {
                            hideDialogue();
                            sessionManager.inserUser(tel,code,nom+" "+prenom,code,pays,ville,compte,photo,nom,prenom);
                            Intent intent=new Intent(Social_connect.this,Interface.class);
                            startActivity(intent);
                            finish();
                        }


                        @Override
                        public void onError(String message) {
                            hideDialogue();
                            Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                        }
                    });

                }else{
                    hideDialogue();
                    Toast.makeText(getApplicationContext(),"Champ(s) vide(s) !",Toast.LENGTH_LONG).show();
                }
            }

        }


    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
