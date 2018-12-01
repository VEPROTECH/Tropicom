package com.tropi.dvjl.tropicom;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.tropi.dvjl.tropicom.MyRequest.MyRequest;
import com.tropi.dvjl.tropicom.SqliteData.SessionManager;

import java.util.Arrays;

public class TypeConnexion extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
Button conti_tel,facebook,google;
    LoginManager loginManager;
    AccessTokenTracker accessTokenTracker;
    ProfileTracker profileTracker;
    CallbackManager callback;
    GoogleApiClient googleApiClient;
    SessionManager sessionManager;
    MyRequest request;
    RequestQueue queue;
    private static final int APP_REQUEST_CODE=233;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_type_connexion);
        sessionManager=new SessionManager(getApplicationContext());
        queue= VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        request=new MyRequest(getApplicationContext(),queue);

        conti_tel= (Button) findViewById(R.id.conti_tel);
        facebook= (Button) findViewById(R.id.facebook);
        google= (Button) findViewById(R.id.google);

        conti_tel.setOnClickListener(telephone);
        facebook.setOnClickListener(face);
        google.setOnClickListener(letGo);


        //Initizlisation de la connexion google+
        GoogleSignInOptions signInOptions= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient=new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions).build();

        loginManager = LoginManager.getInstance();
         callback=CallbackManager.Factory.create();
        accessTokenTracker=new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

            }
        };

        profileTracker=new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                nextActivity(currentProfile);
            }
        };
        loginManager.registerCallback(callback, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Profile profile=Profile.getCurrentProfile();
                nextActivity(profile);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        accessTokenTracker.startTracking();
        profileTracker.startTracking();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Profile profile=Profile.getCurrentProfile();
        nextActivity(profile);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        accessTokenTracker.startTracking();
        profileTracker.startTracking();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callback.onActivityResult(requestCode, resultCode, data);

        if(requestCode==APP_REQUEST_CODE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }

    }

    public void nextActivity(Profile profile)
    {
        if(profile != null)
        {
            request.Verifylogin(profile.getId(), new MyRequest.VerifyCallback() {
                @Override
                public void onSuccess(Boolean data, String tel, String password, String nom, String prenom, String code, String ville, String pays, String compte, String photo) {
                    if (data==true)
                    {
                        Intent intent=new Intent(TypeConnexion.this,Social_connect.class);
                        intent.putExtra("nom",profile.getFirstName());
                        intent.putExtra("prenom",profile.getLastName());
                        intent.putExtra("adresse",profile.getLinkUri().toString());
                        intent.putExtra("photoUrl",profile.getProfilePictureUri(200,200).toString());
                        intent.putExtra("lien",profile.getId());
                        startActivity(intent);
                        finish();
                    }else if(data==false)
                    {
                        sessionManager.inserUser(tel,code,nom+" "+prenom,code,pays,ville,compte,photo,nom,prenom);
                        Intent intent=new Intent(TypeConnexion.this,Interface.class);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onError(String message) {

                }
            });

        }
    }



    public View.OnClickListener telephone=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent=new Intent(TypeConnexion.this,login.class);
            startActivity(intent);
            finish();
        }
    };

    public View.OnClickListener face=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            loginManager.logInWithReadPermissions(TypeConnexion.this, Arrays.asList("email", "public_profile", "user_birthday"));
        }
    };

    public View.OnClickListener letGo=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
                signInGoogle();
        }
    };


    public void signInGoogle(){
        Intent intent=Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent,APP_REQUEST_CODE);
    }

    public void handleResult(GoogleSignInResult result){

        if(result.isSuccess()){
            GoogleSignInAccount account=result.getSignInAccount();
            String nomf=account.getGivenName();
            String prenomf=account.getFamilyName();
            String lien=account.getIdToken();
            String emailf=account.getEmail();
            Uri img_url=account.getPhotoUrl();

            request.Verifylogin(emailf, new MyRequest.VerifyCallback() {
                @Override
                public void onSuccess(Boolean data, String tel, String password, String nom, String prenom, String code, String ville, String pays, String compte, String photo) {
                    if (data==true)
                    {
                        if(img_url !=null){
                            Intent intent=new Intent(TypeConnexion.this,Social_connect.class);
                            intent.putExtra("nom",nomf);
                            intent.putExtra("prenom",prenomf);
                            intent.putExtra("adresse",emailf);
                            String img=img_url.toString();
                            intent.putExtra("photoUrl",img);
                            intent.putExtra("lien",emailf);
                            intent.putExtra("VALUE","google");
                            startActivityForResult(intent,APP_REQUEST_CODE);
                            TypeConnexion.this.finish();
                        }else{
                            Intent intent=new Intent(TypeConnexion.this,Social_connect.class);
                            intent.putExtra("nom",nomf);
                            intent.putExtra("prenom",prenomf);
                            intent.putExtra("adresse",emailf);
                            intent.putExtra("photoUrl","nothing");
                            intent.putExtra("lien",emailf);
                            intent.putExtra("VALUE","google");
                            startActivityForResult(intent,APP_REQUEST_CODE);
                            TypeConnexion.this.finish();
                        }

                    }else if(data==false)
                    {
                        sessionManager.inserUser(tel,code,nom+" "+prenom,code,pays,ville,compte,photo,nom,prenom);
                        Intent intent=new Intent(TypeConnexion.this,Interface.class);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onError(String message) {

                }
            });



        }else{
            Toast.makeText(getApplicationContext(),"Veuillez réessayer !",Toast.LENGTH_LONG).show();
        }
    }




    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(),"Problème de connexion",Toast.LENGTH_LONG).show();
    }
}
