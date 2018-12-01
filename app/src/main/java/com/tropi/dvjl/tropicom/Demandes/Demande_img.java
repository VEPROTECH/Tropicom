package com.tropi.dvjl.tropicom.Demandes;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tropi.dvjl.tropicom.Interface;
import com.tropi.dvjl.tropicom.MyRequest.MyRequest;
import com.tropi.dvjl.tropicom.R;
import com.tropi.dvjl.tropicom.VolleySingleton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Demande_img extends AppCompatActivity {
    private final  static  int PICK_IMAGE_REQUEST1 =123;
    private final  static  int PICK_IMAGE_REQUEST1_1 =12;
    private Bitmap bitmap1;
    MyRequest request;
    private RequestQueue queue;
    Handler handler;
    CardView card1;
    ImageView img1;
    Toolbar toolbar;
    Button btnsuivant;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demande_img);

        toolbar=(Toolbar)findViewById(R.id.matoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setTitle("Ajout d'image");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_back));

        img1=(ImageView) findViewById(R.id.img1);
        handler=new Handler();
        card1=(CardView) findViewById(R.id.card1);
        queue= VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        request=new MyRequest(getApplicationContext(),queue);
        handler=new Handler();

        btnsuivant=(Button)findViewById(R.id.btnsuivant);
        btnsuivant.setOnClickListener(publier);


        card1.setOnClickListener(selected1);
    }





    public View.OnClickListener publier=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(bitmap1 != null){
                final String img=getStringImage(bitmap1);
                Log.e("IMG",img );
                final String id=getIntent().getStringExtra("id");
                loading();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        request.saveImageDemande(img, id, new MyRequest.ChangeCallBack() {
                            @Override
                            public void onSuccess(String message) {
                                hideDialogue();
                                Toast.makeText(getApplicationContext(),"Vous avez ajouté "+message+" image ",Toast.LENGTH_LONG).show();
                                img1.setImageDrawable(getResources().getDrawable(R.drawable.ic_tof));
                            }

                            @Override
                            public void onError(String msg) {
                                hideDialogue();
                                Log.e("ERRORm",msg );
                            }
                        });
                    }
                },2000);


            }
        }
    };

    //Conversion de l'image en string
    public String getStringImage(Bitmap bmp)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            Intent i=new Intent(Demande_img.this,Interface.class);
            startActivity(i);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(Demande_img.this,Interface.class);
        startActivity(i);
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
                Glide.with(Demande_img.this).loadFromMediaStore(filePath)
                        .thumbnail(0.5f)
                        .crossFade()
                        .centerCrop()
                        .placeholder(R.drawable.ic_tof)
                        .error(R.drawable.ic_tof)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(img1);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        if (requestCode == PICK_IMAGE_REQUEST1_1 && resultCode == Activity.RESULT_OK) {
            bitmap1 = (Bitmap) data.getExtras().get("data");
            img1.setImageBitmap(bitmap1);
        }

    }
    private void showFileChooser1() {
        String str[]=new String[]{"Caméra", "Gallerie"};
        new AlertDialog.Builder(Demande_img.this).setItems(str, new DialogInterface.OnClickListener() {
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

        }else{
            intent= new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, PICK_IMAGE_REQUEST1_1);
        }

    }
    public View.OnClickListener selected1=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            showFileChooser1();
        }
    };

    ProgressDialog prg;
    public void loading(){
        prg=new ProgressDialog(Demande_img.this);
        prg.setMessage("Chargement de l'image...");
//        prg.setCancelable(false);
        prg.show();
    }

    public void hideDialogue(){
        if(prg !=null){
            prg.dismiss();
            prg=null;
        }
    }

}
