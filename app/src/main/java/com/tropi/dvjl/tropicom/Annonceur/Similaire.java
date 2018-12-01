package com.tropi.dvjl.tropicom.Annonceur;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.tropi.dvjl.tropicom.Adapter.Utils.AnnoncesAdapter;
import com.tropi.dvjl.tropicom.MyObject.Annonce;
import com.tropi.dvjl.tropicom.MyRequest.MyRequest;
import com.tropi.dvjl.tropicom.R;
import com.tropi.dvjl.tropicom.SqliteData.SessionManager;
import com.tropi.dvjl.tropicom.Url_File.UrlAdresse;
import com.tropi.dvjl.tropicom.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ru.dimorinny.floatingtextbutton.FloatingTextButton;

public class Similaire extends AppCompatActivity {

    FloatingTextButton action_flot;
    RecyclerView list_annonce;
    AnnoncesAdapter annoncesAdapter;
    TextView msg;
    Button actu;
    SessionManager sessionManager;
    UrlAdresse urlAdresse;
    private RequestQueue queue;
    private MyRequest request;
    public static List<Annonce> annonces;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_similaire);
       getSupportActionBar().setTitle(getIntent().getStringExtra("type"));
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_back));


        urlAdresse=new UrlAdresse();

        action_flot=findViewById(R.id.action_flot);
        actu=findViewById(R.id.actu);
        list_annonce=findViewById(R.id.list_annonce);
        msg=findViewById(R.id.msg);
        progressBar=findViewById(R.id.progess);
        sessionManager=new SessionManager(getApplicationContext());
        action_flot.setOnClickListener(addannonce);


        list_annonce.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy > 0 && action_flot.getVisibility() == View.VISIBLE)
                {
                    action_flot.setVisibility(View.GONE);
                    getSupportActionBar().hide();

                }else if(dy < 0 && action_flot.getVisibility() != View.VISIBLE)
                {
                    action_flot.setVisibility(View.VISIBLE);
                   getSupportActionBar().show();
                }
            }
        });

        Log.e("COMPR", sessionManager.getCompte());

        queue= VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        request=new MyRequest(getApplicationContext(),queue);

        annonces=new ArrayList<>();

        actu.setVisibility(View.INVISIBLE);
        msg.setVisibility(View.INVISIBLE);
        actu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listAnnonce();
            }
        });

        listAnnonce();



    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }


    public View.OnClickListener addannonce=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent=new Intent(Similaire.this,PublierAnnonce.class);
            startActivity(intent);
        }
    };

    ProgressDialog prg;
    public void loading(){
        prg=new ProgressDialog(Similaire.this);
        prg.setMessage("Chargement des données en cours...");
//        prg.setCancelable(false);
        prg.show();
    }

    public void hideDialogue(){
        if(prg !=null){
            prg.dismiss();
            prg=null;
        }
    }

    public void listAnnonce(){
        String url=urlAdresse.adresseUrl()+"annonces/"+getIntent().getStringExtra("type");

        progressBar.setVisibility(View.VISIBLE);
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                annonces.clear();
                if(response.length() > 0)
                {
                for(int i=0; i<response.length();i++){
                    try {
                        hideDialogue();


                        JSONObject data=response.getJSONObject(i);

                        JSONArray images= (JSONArray) data.get("images");
                        if(images.length() > 0)
                        {
                            JSONObject obj= (JSONObject) data.get("data");

                            Annonce annonce=new Annonce();
                            annonce.setId(Integer.parseInt(obj.getString("id")));

                            JSONArray name= (JSONArray) data.get("username");
                            JSONObject no=name.getJSONObject(0);
                            annonce.setNom(no.getString("nom"));
                            annonce.setPrenom(no.getString("prenom"));

                            annonce.setDayAnnonce(data.getString("date"));
                            annonce.setHeuAnnonce(data.getString("heure"));

                            annonce.setCity(obj.getString("ville"));
                            annonce.setPays(obj.getString("pays"));
                            annonce.setConditionnement(obj.getString("conditionnement"));
                            annonce.setQte(Integer.parseInt(obj.getString("qte")));
                            annonce.setQualite(obj.getString("qualite"));
                            annonce.setTel(obj.getString("contact"));
                            annonce.setLibelle(obj.getString("libelle"));
                            annonce.setPrix_unitaire(obj.getString("prix"));
                            annonce.setProduit(obj.getString("produit"));

                            int vente=Integer.parseInt(obj.getString("qte")) * Integer.parseInt(obj.getString("prix"));
                            annonce.setPrix_vente(String.valueOf(vente));
                            annonce.setDescription(obj.getString("description"));
                            annonce.setUnite(obj.getString("unite"));


                            List<String> mesImages=new ArrayList<>();
                            String[] myImageList;
                            for(int p=0;p<images.length();p++)
                            {
                                JSONObject im1=images.getJSONObject(p);
                                myImageList=new String[]{im1.getString("image")};

                                for(int k=0;k<myImageList.length; k++){
                                    mesImages.add(myImageList[k]);
                                }

                                if(p==0){
                                    annonce.setImg1(im1.getString("image"));
                                }
//                                    if(p==1){
//                                        annonce.setImg2(im1.getString("image"));
//                                    }
//
//                                    if(p==2){
//                                        annonce.setImg3(im1.getString("image"));
//                                    }

                            }

                            Log.e("LIST", String.valueOf(mesImages));
                            annonce.setMesImg(mesImages);



                            annonces.add(annonce);
                            msg.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            actu.setVisibility(View.GONE);
                            actu.setVisibility(View.GONE);

                            annoncesAdapter=new AnnoncesAdapter(getApplicationContext(),annonces);
                            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                            list_annonce.setLayoutManager(layoutManager);
                            list_annonce.setHasFixedSize(true);
                            list_annonce.setItemAnimator(null);
                            list_annonce.setAdapter(annoncesAdapter);
                            list_annonce.getRecycledViewPool().clear();
                            annoncesAdapter.notifyDataSetChanged();

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                }else{
                    if(response.length()<=0){
                        hideDialogue();
                        Toast.makeText(getApplicationContext(),"Aucun résultat trouvé !",Toast.LENGTH_LONG).show();
                        actu.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideDialogue();
                actu.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                // Toast.makeText(getContext(),"Oups !, Problème de connexion !",Toast.LENGTH_LONG).show();
            }
        });
        queue.add(jsonArrayRequest);


    }


}
