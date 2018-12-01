package com.tropi.dvjl.tropicom.Annonceur;

import android.annotation.SuppressLint;
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
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.volley.RequestQueue;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tropi.dvjl.tropicom.Images;
import com.tropi.dvjl.tropicom.MyRequest.MyRequest;
import com.tropi.dvjl.tropicom.R;
import com.tropi.dvjl.tropicom.SqliteData.SessionManager;
import com.tropi.dvjl.tropicom.VolleySingleton;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Annonce2 extends AppCompatActivity {
    private final  static  int PICK_IMAGE_REQUEST1 =123;
    private final  static  int PICK_IMAGE_REQUEST2 =456;
    private final  static  int PICK_IMAGE_REQUEST3 =789;
    private final  static  int PICK_IMAGE_REQUEST1_1 =12;
    private final  static  int PICK_IMAGE_REQUEST2_2 =45;
    private final  static  int PICK_IMAGE_REQUEST3_3 =78;

    MaterialBetterSpinner spinner_type;
    private Bitmap bitmap1;
    private Bitmap bitmap2;
    private Bitmap bitmap3;
    Toolbar toolbar;
    Button btnsuivant;
    EditText prix_unitaire,qte,prix_vente,produit,autr;
    CardView card1,card2,card3;
    ImageView img1,img2,img3;
    RadioGroup group;
    RadioButton ra1,ra2;

    MyRequest request;
    private RequestQueue queue;
    Handler handler;

    @Override
    protected void onStart() {
        saveTypeProduit();
        super.onStart();
    }
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annonce2);
        toolbar=(Toolbar)findViewById(R.id.matoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_back));

        handler=new Handler();

        spinner_type=(MaterialBetterSpinner)findViewById(R.id.spinner_type);
        group= (RadioGroup) findViewById(R.id.group);
        prix_unitaire=(EditText)findViewById(R.id.prix_unitaire);
        qte=(EditText)findViewById(R.id.qte);
        prix_vente=(EditText)findViewById(R.id.prix_vente);
        prix_vente.setText("0 FCFA");
        card1=(CardView) findViewById(R.id.card1);
        card2=(CardView) findViewById(R.id.card2);
        card3=(CardView) findViewById(R.id.card3);
        img1=(ImageView) findViewById(R.id.img1);
        img2=(ImageView) findViewById(R.id.img2);
        img3=(ImageView) findViewById(R.id.img3);
        autr=findViewById(R.id.autr);

        queue= VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        request=new MyRequest(getApplicationContext(),queue);
        handler=new Handler();


        btnsuivant=(Button)findViewById(R.id.btnsuivant);
        btnsuivant.setOnClickListener(publier);


        card1.setOnClickListener(selected1);
        card2.setOnClickListener(selected2);
        card3.setOnClickListener(selected3);

        prix_unitaire.addTextChangedListener(pri_un);
        qte.addTextChangedListener(change_qte);
        sessionManager=new SessionManager(getApplicationContext());

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if(radioGroup.getCheckedRadioButtonId() == R.id.ra1){
                    prix_unitaire.setHint(R.string.prix_unitaire_kg);
                }else{
                    if(radioGroup.getCheckedRadioButtonId() == R.id.ra2){
                        prix_unitaire.setHint(R.string.prix_unitaire_tonne);
                    }
                }
            }
        });



    }




    ProgressDialog prg;
    public void loading(){
        prg=new ProgressDialog(Annonce2.this);
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



    public View.OnClickListener publier=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String produi=spinner_type.getText().toString();
            if(Objects.equals(produi,"Autres..."))
            {
               produi=autr.getText().toString();
            }
            final String qt=qte.getText().toString();
          String unit = null;
            if(group.getCheckedRadioButtonId() == R.id.ra1){
                unit="Kg";
                prix_unitaire.setHint(R.string.prix_unitaire_kg);
            }else{
                if(group.getCheckedRadioButtonId() == R.id.ra2){
                    unit="Tonne";
                    prix_unitaire.setHint(R.string.prix_unitaire_tonne);
                }

            }

            final String pu=prix_unitaire.getText().toString();
            final String pv=prix_vente.getText().toString();
            if(!produi.isEmpty()){
                if(!qt.isEmpty()){
                        if(!pu.isEmpty()){
                            if(!pv.isEmpty()){
                                if(getIntent().hasExtra("qalite")){
                                    loading();
                                    final String finalUnit = unit;
                                    String finalProdui = produi;
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            String tel=getIntent().getStringExtra("phone");
                                            String conditi=getIntent().getStringExtra("conditionnement");
                                            String lib=getIntent().getStringExtra("libelle");
                                            String cit=getIntent().getStringExtra("city");
                                            String pays=getIntent().getStringExtra("pays");
                                            String des=getIntent().getStringExtra("des");
                                            String code=sessionManager.getCODE();
                                            String qualite=getIntent().getStringExtra("qalite");

                                            request.saveAnnonce(code,lib, qt, pu, finalUnit, pays, conditi, cit, des, tel, finalProdui,qualite, new MyRequest.ChangeCallBack() {
                                                @Override
                                                public void onSuccess(String message) {
                                                    hideDialogue();
                                                    Intent intent=new Intent(Annonce2.this, Images.class);
                                                    intent.putExtra("id",message);
                                                    startActivity(intent);
                                                    finish();
                                                }

                                                @Override
                                                public void onError(String msg) {
                                                    hideDialogue();
                                                }
                                            });

                                        }
                                    },2000);

                                }else{
                                    loading();
                                    final String finalUnit = unit;
                                    String finalProdui1 = produi;
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            String tel=getIntent().getStringExtra("phone");
                                            String conditi=getIntent().getStringExtra("conditionnement");
                                            String lib=getIntent().getStringExtra("libelle");
                                            String cit=getIntent().getStringExtra("city");
                                            String pays=getIntent().getStringExtra("pays");
                                            String des=getIntent().getStringExtra("des");
                                            String code=sessionManager.getCODE();
                                            String qualite="";

                                            request.saveAnnonce(code,lib, qt, pu, finalUnit, pays, conditi, cit, des, tel, finalProdui1,qualite, new MyRequest.ChangeCallBack() {
                                                @Override
                                                public void onSuccess(String message) {
                                                    hideDialogue();
                                                    Intent intent=new Intent(Annonce2.this, Images.class);
                                                    intent.putExtra("id",message);
                                                    startActivity(intent);
                                                    finish();
                                                }

                                                @Override
                                                public void onError(String msg) {
                                                    hideDialogue();
                                                }
                                            });

                                        }
                                    },2000);

                                }

                            }else{
                                prix_vente.setError("Champ vide !");
                            }
                        }else{
                            prix_unitaire.setError("Champ vide !");
                        }

                }else{
                    qte.setError("Champ vide !");
                }
            }else{
                spinner_type.setError("Champ vide !");
            }

        }
    };

    public View.OnClickListener selected1=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            showFileChooser1();
        }
    };

    public View.OnClickListener selected2=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            showFileChooser2();
        }
    };

    public View.OnClickListener selected3=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            showFileChooser3();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST1 &&  data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                Glide.with(Annonce2.this).loadFromMediaStore(filePath)
                        .thumbnail(0.5f)
                        .crossFade()
                        .placeholder(R.drawable.ic_tof)
                        .error(R.drawable.ic_tof)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(img1);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        if (requestCode == PICK_IMAGE_REQUEST2 &&  data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap2 = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                Glide.with(Annonce2.this).loadFromMediaStore(filePath)
                        .thumbnail(0.5f)
                        .crossFade()
                        .placeholder(R.drawable.ic_tof)
                        .error(R.drawable.ic_tof)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(img2);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        if (requestCode == PICK_IMAGE_REQUEST3 &&  data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap3 = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                Glide.with(Annonce2.this).loadFromMediaStore(filePath)
                        .thumbnail(0.5f)
                        .crossFade()
                        .placeholder(R.drawable.ic_tof)
                        .error(R.drawable.ic_tof)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(img3);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        if (requestCode == PICK_IMAGE_REQUEST1_1 && resultCode == Activity.RESULT_OK) {
            bitmap1 = (Bitmap) data.getExtras().get("data");
            img1.setImageBitmap(bitmap1);
        }

        if (requestCode == PICK_IMAGE_REQUEST2_2 && resultCode == Activity.RESULT_OK) {
            bitmap2 = (Bitmap) data.getExtras().get("data");
            img2.setImageBitmap(bitmap2);
        }

        if (requestCode == PICK_IMAGE_REQUEST3_3 && resultCode == Activity.RESULT_OK) {
            bitmap3 = (Bitmap) data.getExtras().get("data");
            img3.setImageBitmap(bitmap3);
        }
    }

    private void showFileChooser1() {
        String str[]=new String[]{"Caméra", "Gallerie"};
        new AlertDialog.Builder(Annonce2.this).setItems(str, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                performImgPicAction1(which);
            }
        }).show();

    }

    private void showFileChooser2() {
        String str[]=new String[]{"Caméra", "Gallerie"};
        new AlertDialog.Builder(Annonce2.this).setItems(str, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                performImgPicAction2(which);
            }
        }).show();
    }

    private void showFileChooser3() {
        String str[]=new String[]{"Caméra", "Gallerie"};
        new AlertDialog.Builder(Annonce2.this).setItems(str, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                performImgPicAction3(which);
            }
        }).show();
    }

    private void performImgPicAction2(int which){
        Intent intent = new Intent();
        if (which==1){
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Selectionnez une photo"),PICK_IMAGE_REQUEST2);
        }else{
            intent= new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, PICK_IMAGE_REQUEST2_2);
        }
    }

    private void performImgPicAction3(int which){
        Intent intent = new Intent();
        if (which==1){
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Selectionnez une photo"),PICK_IMAGE_REQUEST3);
        }else{
            intent= new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, PICK_IMAGE_REQUEST3_3);
        }
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

    //Conversion de l'image en string
    public String getStringImage(Bitmap bmp)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    public TextWatcher change_qte=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String pv=prix_vente.getText().toString();
            String pu=prix_unitaire.getText().toString();
            String qt=qte.getText().toString();
            int cal=0;
            if(!qt.isEmpty() && !pu.isEmpty()){
                cal=Integer.parseInt(pu) * Integer.parseInt(qt);
                prix_vente.setText(formatMoney(""+cal)+" FCFA");
            }else{
                prix_vente.setText("0 FCFA");
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    public TextWatcher pri_un=new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


        }
        @SuppressLint("SetTextI18n")
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String pv=prix_vente.getText().toString();
            String pu=prix_unitaire.getText().toString();
            String qt=qte.getText().toString();
            int cal=0;
            if(!qt.isEmpty() && !pu.isEmpty()){
                cal=Integer.parseInt(pu) * Integer.parseInt(qt);
                prix_vente.setText(formatMoney(""+cal)+" FCFA");
            }else{
                prix_vente.setText("0 FCFA");
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private String formatMoney(String money){
        String out = "";
        int j = 0;
        for (int i=money.length();i>0;i--){
            out = money.charAt(i-1)+out;
            j++;
            if (j==3){
                out = " "+out;
                j=0;
            }
        }
        return out;
    }

    public void saveTypeProduit(){
            String [] listProduits=new String[]{"Noix de cadjou",
                "Soja",
                "Riz",
                "Maïs",
                "Noix de karité",
                "Mil",
                "Sorgho",
                "Gingembre",
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
