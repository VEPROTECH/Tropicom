package com.tropi.dvjl.tropicom.Annonceur;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tropi.dvjl.tropicom.Adapter.Utils.FindDay;
import com.tropi.dvjl.tropicom.Adapter.Utils.SlidingImage_Adapter;
import com.tropi.dvjl.tropicom.MyObject.ImageModel;
import com.tropi.dvjl.tropicom.R;
import com.tropi.dvjl.tropicom.SliderImage;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import ru.dimorinny.floatingtextbutton.FloatingTextButton;

public class DetailsAnnonce extends AppCompatActivity {
TextView title_annonce,day,dt_type,dt_condi,dt_qte,dt_qlite,dt_prix,dt_pays,prix,dt_contact,desciption,annonceur;
    FloatingTextButton action_flot,action_msg;
    Toolbar toolbar;
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private ArrayList<ImageModel> imageModelArrayList;
    AppBarLayout rel;
    LinearLayout layout_action1;
    LinearLayout layout_action2;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_details_annonce);



        toolbar=findViewById(R.id.toolbar);
        rel=findViewById(R.id.appBarLayout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Détails");
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_back));
        mPager = (ViewPager) findViewById(R.id.pager);

        imageModelArrayList = new ArrayList<>();
        imageModelArrayList = populateList();

//        SimpleDraweeView mImageView = (SimpleDraweeView)findViewById(R.id.image1);
//        final Uri uri = Uri.parse(getIntent().getStringExtra("img0"));
//        mImageView.setImageURI(uri);

        title_annonce= (TextView) findViewById(R.id.title_annonce);
        dt_type= (TextView) findViewById(R.id.dt_type);
        dt_condi= (TextView) findViewById(R.id.dt_condi);
        dt_qte= (TextView) findViewById(R.id.dt_qte);
        dt_qlite= (TextView) findViewById(R.id.dt_qlite);
        dt_prix= (TextView) findViewById(R.id.dt_prix);
        dt_pays= (TextView) findViewById(R.id.dt_pays);
        prix= (TextView) findViewById(R.id.prix);
        dt_contact= (TextView) findViewById(R.id.dt_contact);
        desciption= (TextView) findViewById(R.id.desciption);
        annonceur= (TextView) findViewById(R.id.annonceur);
        day= (TextView) findViewById(R.id.day);

        layout_action1=findViewById(R.id.layout_action1);
        layout_action2=findViewById(R.id.layout_action2);
        action_flot= (FloatingTextButton) findViewById(R.id.action_flot);
        action_msg= (FloatingTextButton) findViewById(R.id.action_msg);

        FindDay findDay=new FindDay();
        day.setText(findDay.getDiff(getIntent().getStringExtra("jr"),findDay.getDay())+", "+getIntent().getStringExtra("time"));
        title_annonce.setText(getIntent().getStringExtra("libelle"));
        dt_type.setText(getIntent().getStringExtra("typ"));
        dt_condi.setText(getIntent().getStringExtra("condi"));
        dt_prix.setText(formatMoney(""+Integer.parseInt(getIntent().getStringExtra("pu")))+" CFA");
        dt_qte.setText(getIntent().getStringExtra("qte")+" "+getIntent().getStringExtra("unit"));
        prix.setText(formatMoney(""+Integer.parseInt(getIntent().getStringExtra("pu")))+" CFA/"+getIntent().getStringExtra("unit"));
        dt_pays.setText(getIntent().getStringExtra("pays")+", "+getIntent().getStringExtra("ville"));
        dt_contact.setText(getIntent().getStringExtra("tel"));
        annonceur.setText(getIntent().getStringExtra("nom"));
        if(!getIntent().getStringExtra("qalite").equalsIgnoreCase("")){
            dt_qlite.setText(getIntent().getStringExtra("qalite"));
        }else {
            dt_qlite.setText("Contacter le vendeur pour plus de détails sur le produit");
        }


        if(getIntent().getStringExtra("des") != null){
            desciption.setText(getIntent().getStringExtra("des"));
        }else{
            desciption.setText("");
        }



        action_flot.setOnClickListener(call);
        action_msg.setOnClickListener(sms);

        init();

        rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle objetbundle =getIntent().getExtras();
                Intent i=new Intent(DetailsAnnonce.this, SliderImage.class);
                i.putStringArrayListExtra("mesImg",objetbundle.getStringArrayList("mesImg"));
                startActivity(i);
            }
        });


        layout_action1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent=new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String shareSub = getIntent().getStringExtra("typ");
                String shareBody = getIntent().getStringExtra("libelle")+" à vendre sur Tropicom. Télécharge vite l'application Tropicom sur play store pour avoir plus de produits tropicaux !";
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(shareIntent,"Partager sur ... "));

            }
        });

        layout_action2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DetailsAnnonce.this,Similaire.class);
                intent.putExtra("type",getIntent().getStringExtra("typ"));
                startActivity(intent);
            }
        });

    }

    private ArrayList<ImageModel> populateList()
    {
        Bundle objetbundle = this.getIntent().getExtras();
        ArrayList<String> myImageList = objetbundle.getStringArrayList("mesImg");

        ArrayList<ImageModel> list = new ArrayList<>();

        for(int i = 0; i < myImageList.size(); i++){
            ImageModel imageModel = new ImageModel();
            imageModel.setImage_url(myImageList.get(i));
            list.add(imageModel);
        }

        return list;
    }


    private void init() {

        mPager.setAdapter(new SlidingImage_Adapter(DetailsAnnonce.this,imageModelArrayList));

        CirclePageIndicator indicator = (CirclePageIndicator)
                findViewById(R.id.indicator);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        indicator.setRadius(5 * density);

        NUM_PAGES =imageModelArrayList.size();

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 8000, 8000);


        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }

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


    public View.OnClickListener call=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Uri uri = Uri.parse("tel:"+getIntent().getStringExtra("tel"));
            Intent intent = new Intent(Intent.ACTION_DIAL, uri);
            startActivity(intent);
        }
    };


    public View.OnClickListener sms=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String message="Bonjour Mr/Mme, je voudrais passé la commande de "+getIntent().getStringExtra("libelle")+". Pouvez-vous me donner plus d'informations sur le produit ?";
            Uri uri = Uri.parse("smsto:"+getIntent().getStringExtra("tel"));
            Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
            intent.putExtra("sms_body",message);
            startActivity(intent);
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
