package com.tropi.dvjl.tropicom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.hbb20.CountryCodePicker;
import com.tropi.dvjl.tropicom.MyRequest.MyRequest;

import java.util.Map;

public class ForgotPasse extends AppCompatActivity {
    CountryCodePicker pick;
    TextInputEditText phone,passe,passe2;
    MyRequest request;
    RequestQueue queue;
    Handler handler;
    Button connect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_passe);
        Window window=this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        handler=new Handler();
        pick=findViewById(R.id.pick);
        phone=findViewById(R.id.phone);
        passe=findViewById(R.id.passe);
        passe2=findViewById(R.id.passe2);
        connect=(Button)findViewById(R.id.connect);

        queue= VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        request=new MyRequest(getApplicationContext(),queue);
        handler=new Handler();

        connect.setOnClickListener(reinial);

    }


    ProgressDialog prg;
    public void loading(){
        prg=new ProgressDialog(ForgotPasse.this);
        prg.setMessage("Réinitialisation en cours...");
//        prg.setCancelable(false);
        prg.show();
    }

    public void hideDialogue(){
        if(prg !=null){
            prg.dismiss();
            prg=null;
        }
    }

    public View.OnClickListener reinial=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String tel=pick.getSelectedCountryCodeWithPlus()+""+phone.getText().toString();
            String pax=passe.getText().toString();
            String pax2=passe2.getText().toString();

            if(tel.length()>0 && pax.length()>0 && pax2.length()>0)
            {
                if(pax.length()>=6)
                {
                    if(pax.equals(pax2)){

                        loading();
                       handler.postDelayed(new Runnable() {
                           @Override
                           public void run() {
                               request.updatePasse(tel, pax, new MyRequest.RegisterCallBack() {
                                   @Override
                                   public void onSuccess(String message) {
                                       hideDialogue();
                                       Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                                       Intent intent=new Intent(ForgotPasse.this,login.class);
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
                        Toast.makeText(getApplicationContext(),"Les deux mot de passe doivent être les mêmes !",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Le mot de passe doit contenir au moins 06 caractère !",Toast.LENGTH_LONG).show();
                }

            }

        }
    };
}
