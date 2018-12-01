package com.tropi.dvjl.tropicom;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hbb20.CountryCodePicker;
import com.tropi.dvjl.tropicom.CircleImage.GlideCircleTransformation;
import com.tropi.dvjl.tropicom.MyRequest.MyRequest;
import com.tropi.dvjl.tropicom.SqliteData.SessionManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

public class ModifierCompte extends AppCompatActivity {
    FloatingActionButton action_flot;
    ImageView imgProfil;
    CountryCodePicker country;
    TextInputEditText ville;
    Button modifier;
    MyRequest request;
    RequestQueue queue;
    private final  static  int PICK_IMAGE_REQUEST1 =123;
    private final  static  int PICK_IMAGE_REQUEST1_1 =12;
    private Bitmap bitmap1;

    SessionManager sessionManager;
    Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_compte);

        getSupportActionBar().setTitle("Modification");
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_back));


        action_flot=findViewById(R.id.action_flot);
        imgProfil=findViewById(R.id.imgProfil);
        country=findViewById(R.id.pays);
        ville=findViewById(R.id.ville);
        modifier=findViewById(R.id.modifier);
        handler=new Handler();
        sessionManager=new SessionManager(getApplicationContext());


        if(sessionManager.getPhoto().substring(0,5)=="https")
        {
            action_flot.setVisibility(View.GONE);
        }

        Glide.with(getApplicationContext()).load(sessionManager.getPhoto())
                .thumbnail(0.5f)
                .crossFade()
                .placeholder(R.drawable.tof)
                .error(R.drawable.tof)
                .bitmapTransform(new GlideCircleTransformation(getApplicationContext()))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfil);

        ville.setText(sessionManager.getVille());


        queue= VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        request=new MyRequest(getApplicationContext(),queue);
        action_flot.setOnClickListener(selected1);
        modifier.setOnClickListener(modif);



    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST1 &&  data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                Glide.with(ModifierCompte.this).loadFromMediaStore(filePath)
                        .thumbnail(0.5f)
                        .crossFade()
                        .placeholder(R.drawable.tof)
                        .error(R.drawable.tof)
                        .bitmapTransform(new GlideCircleTransformation(getApplicationContext()))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imgProfil);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == PICK_IMAGE_REQUEST1_1 && resultCode == Activity.RESULT_OK) {
            bitmap1 = (Bitmap) data.getExtras().get("data");
            Glide.with(ModifierCompte.this).load(bitmap1)
                    .thumbnail(0.5f)
                    .crossFade()
                    .placeholder(R.drawable.tof)
                    .error(R.drawable.tof)
                    .bitmapTransform(new GlideCircleTransformation(getApplicationContext()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgProfil);
           // imgProfil.setImageBitmap(bitmap1);
        }


    }

    private void showFileChooser1() {
        String str[]=new String[]{"Caméra", "Gallerie"};
        new AlertDialog.Builder(ModifierCompte.this).setItems(str, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                performImgPicAction1(which);
            }
        }).show();

    }

    private void performImgPicAction1(int which) {
        Intent intent = new Intent();
        if (which==1){
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Selectionnez une photo"),PICK_IMAGE_REQUEST1);

        }
        else{
            intent= new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, PICK_IMAGE_REQUEST1_1);
        }
    }

    //Conversion de l'image en string
    public String getStringImage(Bitmap bmp)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 60, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    ProgressDialog prg;
    public void loading(){
        prg=new ProgressDialog(ModifierCompte.this);
        prg.setMessage("Modification en cours...");
//        prg.setCancelable(false);
        prg.show();
    }

    public void hideDialogue(){
        if(prg !=null){
            prg.dismiss();
            prg=null;
        }
    }

    public View.OnClickListener modif=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            modification();
        }
    };


    public View.OnClickListener selected1=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            showFileChooser1();
        }
    };



    public void modification()
    {
        final String pays=country.getSelectedCountryName(); //le pays ici
        final String vil=ville.getText().toString();
        String photo = null;
        final String id=sessionManager.getCODE();

        if(bitmap1 != null)
        {
            if(sessionManager.getPhoto().substring(0,5) == "https"){
                photo=sessionManager.getPhoto();
            }else{
                photo=getStringImage(bitmap1);
            }


                    if(!vil.isEmpty())
                    {
                          loading();
                        String finalPhoto = photo;
                        handler.postDelayed(new Runnable() {
                              @Override
                              public void run() {
                                  request.modifier(pays, vil,finalPhoto,id, new MyRequest.ModifierCallBack() {
                                      @Override
                                      public void onSuccess(String message)
                                      {
                                          hideDialogue();
                                          AlertDialog dialog=new AlertDialog.Builder(ModifierCompte.this).create();
                                          dialog.setMessage(message);
                                          dialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                                              @Override
                                              public void onClick(DialogInterface dialogInterface, int i) {
                                                  dialog.dismiss();
                                              }
                                          });
                                          dialog.show();
                                      }

                                      @Override
                                      public void onData(String nom, String prenom, String ville, String pays, String compte, String photo) {
                                        sessionManager.inserUser(sessionManager.getTel(),sessionManager.getId(), nom+" "+prenom,
                                                sessionManager.getCODE(),
                                                pays,ville,compte,photo,nom,prenom
                                                );
                                          hideDialogue();
                                      }

                                      @Override
                                      public void inputError(Map<String, String> errors) {

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
                        ville.setError("Champ vide !");
                    }

        }else{
            photo="";

                        if(!vil.isEmpty())
                        {
                            loading();
                            String finalPhoto1 = photo;
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    request.modifier(pays, vil, finalPhoto1,id, new MyRequest.ModifierCallBack() {
                                        @Override
                                        public void onSuccess(String message)
                                        {
                                            hideDialogue();
                                            AlertDialog dialog=new AlertDialog.Builder(ModifierCompte.this).create();
                                            dialog.setMessage(message);
                                            dialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialog.dismiss();
                                                }
                                            });
                                            dialog.show();
                                        }

                                        @Override
                                        public void onData(String nom, String prenom, String ville, String pays, String compte, String photo) {
                                            sessionManager.inserUser(sessionManager.getTel(),
                                                    nom+" "+prenom,
                                                    sessionManager.getId(),
                                                    sessionManager.getCODE(),
                                                    pays,ville,compte,photo,nom,prenom
                                            );
                                        }

                                        @Override
                                        public void inputError(Map<String, String> errors) {

                                        }

                                        @Override
                                        public void onError(String message) {

                                        }
                                    });
                                }
                            },2000);

                        }else{
                            ville.setError("Champ vide !");
                        }


        }
    }


}
