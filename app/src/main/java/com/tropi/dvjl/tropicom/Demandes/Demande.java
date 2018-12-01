package com.tropi.dvjl.tropicom.Demandes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.facebook.accountkit.AccountKit;
import com.facebook.login.LoginManager;
import com.tropi.dvjl.tropicom.Adapter.Utils.DemandeAdapter;
import com.tropi.dvjl.tropicom.MyObject.Annonce;
import com.tropi.dvjl.tropicom.MyRequest.MyRequest;
import com.tropi.dvjl.tropicom.R;
import com.tropi.dvjl.tropicom.SqliteData.SessionManager;
import com.tropi.dvjl.tropicom.TypeConnexion;
import com.tropi.dvjl.tropicom.Url_File.UrlAdresse;
import com.tropi.dvjl.tropicom.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ru.dimorinny.floatingtextbutton.FloatingTextButton;


public class Demande extends Fragment implements SearchView.OnQueryTextListener
{


    FloatingTextButton action_flot;
    RecyclerView list_annonce;
    DemandeAdapter annoncesAdapter;
    TextView msg;
    Button actu;
    SessionManager sessionManager;
    UrlAdresse urlAdresse=new UrlAdresse();
    private RequestQueue queue;
    private MyRequest request;
    public static List<Annonce> annonces;
    ProgressBar progressBar;
    String url=urlAdresse.adresseUrl()+"demande";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_demande, container, false);
        setHasOptionsMenu(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Demandes");
        action_flot=view.findViewById(R.id.action_flot);
        actu=view.findViewById(R.id.actu);
        list_annonce=view.findViewById(R.id.list_annonce);
        msg=view.findViewById(R.id.msg);
        progressBar=view.findViewById(R.id.progess);
        sessionManager=new SessionManager(getContext());
        action_flot.setOnClickListener(adddemande);

        list_annonce.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy > 0 && action_flot.getVisibility() == View.VISIBLE)
                {
                    action_flot.setVisibility(View.GONE);
                }
                else
                    if(dy < 0 && action_flot.getVisibility() != View.VISIBLE)
                {
                    action_flot.setVisibility(View.VISIBLE);
                }
            }
        });

        Log.e("COMPR", sessionManager.getCompte());
        if(Objects.equals(sessionManager.getCompte(), "Acheteur"))
        {
            action_flot.setVisibility(View.VISIBLE);

        }else{

            action_flot.setVisibility(View.GONE);

        }


        queue= VolleySingleton.getInstance(getContext()).getRequestQueue();
        request=new MyRequest(getContext(),queue);

        annonces=new ArrayList<>();

        actu.setVisibility(View.INVISIBLE);
        msg.setVisibility(View.INVISIBLE);
        actu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listDemande();
            }
        });

        listDemande();
        return view;
    }

    ProgressDialog prg;
    public void loading(){
        prg=new ProgressDialog(getContext());
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


    public void listDemande(){
        progressBar.setVisibility(View.VISIBLE);
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                annonces.clear();

                if(response.length()>0)
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

                            annonce.setTel(obj.getString("contact"));
                            annonce.setLibelle(obj.getString("libelle"));

                            annonce.setProduit(obj.getString("produit"));

                            annonce.setDescription(obj.getString("description"));

                            annonce.setUnite(obj.getString("unite"));
                            annonce.setQte(Integer.parseInt(obj.getString("qte")));

                            List<String> mesImages=new ArrayList<>();
                            String[] myImageList;
                            for(int p=0;p<images.length();p++)
                            {
                                JSONObject im1=images.getJSONObject(p);
                                myImageList=new String[]{im1.getString("images")};

                                for(int k=0;k<myImageList.length; k++)
                                {
                                    mesImages.add(myImageList[k]);
                                }

                                if(p==0)
                                {
                                    annonce.setImg1(im1.getString("images"));
                                }

                            }

                            Log.e("LIST", String.valueOf(mesImages));
                            annonce.setMesImg(mesImages);

                            annonces.add(annonce);
                            msg.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            actu.setVisibility(View.GONE);
                            actu.setVisibility(View.GONE);

                            annoncesAdapter=new DemandeAdapter(getActivity(),annonces);
                            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                            list_annonce.setLayoutManager(layoutManager);
                            list_annonce.setAdapter(annoncesAdapter);
                            annoncesAdapter.notifyDataSetChanged();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                }else {
                    if(response.length()<=0)
                    {
                        hideDialogue();
                        Toast.makeText(getContext(),"Aucun résultat trouvé !",Toast.LENGTH_LONG).show();
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
//                CrudSqlite crudSqlite=new CrudSqlite(getContext());
//
//                try {
//                    List<Annonce> ann=crudSqlite.getAnonceList();
//                    if(!ann.isEmpty() || ann != null){
//                        msg.setVisibility(View.GONE);
//                        annoncesAdapter=new AnnoncesAdapter(getActivity(),ann);
//
//                        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
//                        list_annonce.setLayoutManager(layoutManager);
//                        list_annonce.setAdapter(annoncesAdapter);
//                        annoncesAdapter.notifyDataSetChanged();
//                    } else{
//                        msg.setVisibility(View.VISIBLE);
//                    }
//                }catch (Exception e){
//                    System.out.print(e.getMessage());
//                }




//            for(int j=0;j<crudSqlite.getAnonceList().size();j++){
//                Annonce annonce=new Annonce();
//                annonce.setId(obj.getString().getId());
//                annonce.setNom(obj.getString().getNom());
//                annonce.setPrenom(obj.getString().getPrenom());
//                annonce.setDayAnnonce(obj.getString().getDayAnnonce());
//                annonce.setHeuAnnonce(obj.getString().getHeuAnnonce());
//                annonce.setCity(obj.getString().getCity());
//                annonce.setPays(obj.getString().getPays());
//                annonce.setConditionnement(obj.getString().getConditionnement());
//                annonce.setQte(obj.getString().getQte());
//                annonce.setQualite(obj.getString().getQualite());
//                annonce.setTel(obj.getString().getTel());
//                annonce.setLibelle(obj.getString().getLibelle());
//                annonce.setPrix_unitaire(obj.getString().getPrix_unitaire());
//                annonce.setPrix_vente(obj.getString().getPrix_vente());
//                annonce.setProduit(obj.getString().getProduit());
//                annonce.setUnite(obj.getString().getUnite());
//
//
//            }


    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.cad,menu);
        MenuItem item=menu.findItem(R.id.rech);


        MenuItem decon=menu.findItem(R.id.deconnecter);
        decon.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                LoginManager.getInstance().logOut();
                sessionManager.logout();
                AccountKit.logOut();

                Intent intent=new Intent(getContext(),TypeConnexion.class);
                startActivity(intent);
                getActivity().finish();


                return true;
            }
        });
//        SearchManager searchManager=(SearchManager)getActivity().getSystemService(Context.SEARCH_SERVICE);
        android.support.v7.widget.SearchView searchView=(android.support.v7.widget.SearchView) MenuItemCompat.getActionView(item);
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setQueryHint("Rechercher un produit");
        searchView.setOnQueryTextListener(this);

    }

    public View.OnClickListener adddemande=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent=new Intent(getContext(),addDemande.class);
            startActivity(intent);
        }
    };

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        try {

            if(!annonces.isEmpty() || annonces.size() != 0){
                annoncesAdapter.getFilter().filter(s);
            }
        }catch (Exception e){
            System.out.print(e.getMessage());
        }

        return false;
    }


}
