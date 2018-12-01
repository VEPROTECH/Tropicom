package com.tropi.dvjl.tropicom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.hbb20.CountryCodePicker;
import com.tropi.dvjl.tropicom.MyRequest.MyRequest;
import com.tropi.dvjl.tropicom.SqliteData.SessionManager;

public class login extends AppCompatActivity {
    Toolbar toolbar;
    Button connect;
    TextView ins,oubli;
    CountryCodePicker pick;
    TextInputEditText phone,passe;
    MyRequest request;
     RequestQueue queue;
    Handler handler;
    SessionManager sessionManager;
    private final  static  int APP_REQUEST_CODE =999;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        toolbar=(Toolbar)findViewById(R.id.matoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Connexion");
        toolbar.setTitleTextColor(Color.WHITE);
        handler=new Handler();
sessionManager=new SessionManager(getApplicationContext());
        pick=findViewById(R.id.pick);
        phone=findViewById(R.id.phone);
        passe=findViewById(R.id.passe);
        connect=(Button)findViewById(R.id.connect);
        ins= (TextView) findViewById(R.id.ins);
        oubli= (TextView) findViewById(R.id.oubli);

        queue= VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        request=new MyRequest(getApplicationContext(),queue);
        handler=new Handler();

        connect.setOnClickListener(connexion);
        ins.setOnClickListener(inscription);
        oubli.setOnClickListener(forgot);
    }


    public View.OnClickListener forgot=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i=new Intent(login.this,ForgotPasse.class);
            startActivity(i);
        }
    };

    public View.OnClickListener inscription=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent=new Intent(getApplicationContext(), AccountKitActivity.class);
            AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder=
                    new AccountKitConfiguration.AccountKitConfigurationBuilder(LoginType.PHONE,AccountKitActivity.ResponseType.TOKEN);

            intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,configurationBuilder.build());
            startActivityForResult(intent,APP_REQUEST_CODE );

//            Intent intent=new Intent(login.this,Inscription.class);
//            startActivity(intent);
        }
    };
    public View.OnClickListener connexion=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            login();
//            Intent intent=new Intent(login.this,Interface.class);
//            startActivity(intent);
//            finish();
        }
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if(requestCode == APP_REQUEST_CODE ){
                AccountKitLoginResult result=data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
                if(result.getError() != null){
                    Toast.makeText(getApplicationContext(),""+result.getError().getErrorType().getMessage(),Toast.LENGTH_SHORT).show();
                    return;
                }else if(result.wasCancelled()){
                    Toast.makeText(getApplicationContext(),"Cancel",Toast.LENGTH_SHORT).show();
                    return;
                }else{

//                    if(result.getAccessToken() != null){
//                        Toast.makeText(getApplicationContext(),"Success %s"+result.getAccessToken().getAccountId(),Toast.LENGTH_LONG).show();
//                    }else{
//                        Toast.makeText(getApplicationContext(),"Success %s"+result.getAuthorizationCode().substring(0,10),Toast.LENGTH_LONG).show();
//                    }
                    Intent intent=new Intent(getApplicationContext(), Inscription.class);
                    startActivity(intent);
                }

        }
    }











    ProgressDialog prg;
    public void loading(){
        prg=new ProgressDialog(login.this);
        prg.setMessage("Connexion en cours...");
//        prg.setCancelable(false);
        prg.show();
    }

    public void hideDialogue(){
        if(prg !=null){
            prg.dismiss();
            prg=null;
        }
    }
    public void login(){

        final String tel=pick.getSelectedCountryCodeWithPlus()+""+phone.getText().toString();
        final String password=passe.getText().toString();

        Log.e("MESDONNES",tel+" "+password);

        if(!tel.isEmpty() && !password.isEmpty())
        {
            loading();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    request.login(tel, password, new MyRequest.LoginCallback() {
                        @Override
                        public void onSuccess(String tel, String password, String nom, String prenom, String code, String ville, String pays, String compte,String photo)
                        {
                           hideDialogue();
                            sessionManager.inserUser(tel,code,nom+" "+prenom,code,pays,ville,compte,photo,nom,prenom);
                            Intent intent=new Intent(login.this,Interface.class);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onError(String message)
                        {
                            hideDialogue();
                            Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                        }


                    });
                }
            },2000);

        }

    }


}
