package com.tropi.dvjl.tropicom.Annonceur;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
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

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.facebook.accountkit.AccountKit;
import com.facebook.login.LoginManager;
import com.tropi.dvjl.tropicom.Adapter.Utils.AnnoncesAdapter;
import com.tropi.dvjl.tropicom.Adapter.Utils.SlidingImage_Adapter;
import com.tropi.dvjl.tropicom.MyObject.Annonce;
import com.tropi.dvjl.tropicom.MyObject.ImageModel;
import com.tropi.dvjl.tropicom.MyRequest.MyRequest;
import com.tropi.dvjl.tropicom.R;
import com.tropi.dvjl.tropicom.SqliteData.SessionCount;
import com.tropi.dvjl.tropicom.SqliteData.SessionManager;
import com.tropi.dvjl.tropicom.TypeConnexion;
import com.tropi.dvjl.tropicom.Url_File.UrlAdresse;
import com.tropi.dvjl.tropicom.VolleySingleton;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;
import jp.wasabeef.recyclerview.animators.FadeInUpAnimator;
import ru.dimorinny.floatingtextbutton.FloatingTextButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnnoncePublier extends Fragment implements android.support.v7.widget.SearchView.OnQueryTextListener {

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private ArrayList<ImageModel> imageModelArrayList;
    WaveSwipeRefreshLayout waveSwipeRefreshLayout;

    FloatingTextButton action_flot;
    RecyclerView list_annonce;
    AnnoncesAdapter annoncesAdapter;
    TextView msg;
    Button actu;
    SessionManager sessionManager;
    UrlAdresse urlAdresse = new UrlAdresse();
    private RequestQueue queue;
    private MyRequest request;

    List<Annonce> annonces;
    List<Annonce> tmpannonces =new ArrayList<>();


    ProgressBar progressBar;
    String url = urlAdresse.adresseUrl() + "annonces";
    SessionCount sessionCount;
    CirclePageIndicator indicator;
    int currentSize = 0;
    int cuz = 0;

    public AnnoncePublier() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_annonce_publier, container, false);
        final StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Tropicom");
        setHasOptionsMenu(true);

        queue = VolleySingleton.getInstance(getContext()).getRequestQueue();
        request = new MyRequest(getContext(), queue);

        action_flot = view.findViewById(R.id.action_flot);
        actu = view.findViewById(R.id.actu);
        waveSwipeRefreshLayout=view.findViewById(R.id.wave);
        list_annonce = view.findViewById(R.id.list_annonce);


        msg = view.findViewById(R.id.msg);
        progressBar = view.findViewById(R.id.progess);
        sessionManager = new SessionManager(getContext());
        action_flot.setOnClickListener(addannonce);
        sessionCount = new SessionCount(getContext());

        mPager = view.findViewById(R.id.pager);
        indicator = view.findViewById(R.id.indicator);

//rendre visible les éléments de chargement !
        actu.setVisibility(View.INVISIBLE);
        msg.setVisibility(View.INVISIBLE);


        annonces= new ArrayList<>();

        //liste des annonces
        listAnnonce();


        list_annonce.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && action_flot.getVisibility() == View.VISIBLE) {
                    action_flot.setVisibility(View.GONE);
                    ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

                } else if (dy < 0 && action_flot.getVisibility() != View.VISIBLE) {
                    action_flot.setVisibility(View.VISIBLE);
                    ((AppCompatActivity) getActivity()).getSupportActionBar().show();
                }
            }
        });


        //actualisation des données
        actu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listAnnonce();
            }
        });

        waveSwipeRefreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                annonces.clear();
                queue.getCache().clear();
                listAnnonce();
                populateList();


                countAnnonce();
                countDemande();
            }
        });



        imageModelArrayList = new ArrayList<>();
        populateList();


        countAnnonce();
        countDemande();


        return view;
    }


    public void listAnnonce() {
        progressBar.setVisibility(View.VISIBLE);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                annonces.clear();
                if(response.length() > 0)
                {
                for (int i = 0; i < response.length(); i++)
                {
                    try {
                        hideDialogue();
                        waveSwipeRefreshLayout.setRefreshing(false);
                        JSONObject data = response.getJSONObject(i);

                        JSONArray images = (JSONArray) data.get("images");
                        if (images.length() > 0) {
                            JSONObject obj = (JSONObject) data.get("data");
                            Annonce annonce = new Annonce();
                            annonce.setId(Integer.parseInt(obj.getString("id")));

                            JSONArray name = (JSONArray) data.get("username");
                            JSONObject no = name.getJSONObject(0);
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

                            int vente = Integer.parseInt(obj.getString("qte")) * Integer.parseInt(obj.getString("prix"));
                            annonce.setPrix_vente(String.valueOf(vente));
                            annonce.setDescription(obj.getString("description"));
                            annonce.setUnite(obj.getString("unite"));

                            List<String> mesImages = new ArrayList<>();
                            String[] myImageList;
                            for (int p = 0; p < images.length(); p++) {
                                JSONObject im1 = images.getJSONObject(p);
                                myImageList = new String[]{im1.getString("image")};

                                for (int k = 0; k < myImageList.length; k++) {
                                    mesImages.add(myImageList[k]);
                                }

                                if (p == 0) {
                                    annonce.setImg1(im1.getString("image"));
                                }


                            }

                            annonce.setMesImg(mesImages);
                            annonces.add(annonce);

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                msg.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                actu.setVisibility(View.GONE);
                actu.setVisibility(View.GONE);

                    //initialize recyclerView
                    annoncesAdapter=new AnnoncesAdapter(getContext(),annonces);
                    StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                    list_annonce.setLayoutManager(layoutManager);
                    list_annonce.setHasFixedSize(true);
                    list_annonce.setItemAnimator(new FadeInUpAnimator());
                    list_annonce.setAdapter(annoncesAdapter);
                    list_annonce.getRecycledViewPool().clear();
                    annoncesAdapter.notifyDataSetChanged();

                }else
                {
                    if (response.length() <= 0)
                    {
                        hideDialogue();
                        Toast.makeText(getContext(), "Aucun résultat trouvé !", Toast.LENGTH_LONG).show();
                        actu.setVisibility(View.VISIBLE);
                        msg.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideDialogue();
                waveSwipeRefreshLayout.setRefreshing(false);
                actu.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                msg.setVisibility(View.VISIBLE);
                // Toast.makeText(getContext(),"Oups !, Problème de connexion !",Toast.LENGTH_LONG).show();
            }
        })
          {
                @Override
                protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
                    Cache.Entry cacheEntry= HttpHeaderParser.parseCacheHeaders(response);
                    if(cacheEntry == null)
                    {
                        cacheEntry=new Cache.Entry();
                    }
                    final long cacheHitButRefresh=3*60*1000;
                    final long cacheExpired=24*60*60*1000;
                    long now=System.currentTimeMillis();
                    final long softExpire=now + cacheHitButRefresh;
                    final long ttl=now+cacheExpired;
                    cacheEntry.data=response.data;
                    cacheEntry.softTtl=softExpire;
                    cacheEntry.ttl=ttl;
                    String headerValue;
                    headerValue=response.headers.get("Date");
                    if(headerValue !=null)
                    {
                        cacheEntry.serverDate=HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }

                    headerValue = response.headers.get("Last-Modified");
                    if(headerValue !=null)
                    {
                        cacheEntry.serverDate=HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }

                    cacheEntry.responseHeaders=response.headers;
                    try {
                        final String jsonString=new String(response.data,HttpHeaderParser.parseCharset(response.headers));
                        return Response.success(new JSONArray(jsonString),cacheEntry);
                    } catch (UnsupportedEncodingException | JSONException e) {
                        e.printStackTrace();
                        return Response.error(new ParseError(e));
                    }

                }

                @Override
                protected void deliverResponse(JSONArray response) {
                    super.deliverResponse(response);
                }

                @Override
                public void deliverError(VolleyError error) {
                    super.deliverError(error);
                }

                @Override
                protected VolleyError parseNetworkError(VolleyError volleyError) {
                    return super.parseNetworkError(volleyError);
                }
            };

        int socketTimeout = 0;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonArrayRequest.setRetryPolicy(policy);
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


    public void populateList() {
        imageModelArrayList.clear();
        ArrayList<ImageModel> list = new ArrayList<>();
        String urla = urlAdresse.adresseUrl() + "slide";
        JsonArrayRequest arrayRequest = new JsonArrayRequest(urla, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.e("IMG", String.valueOf(response));
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject data = response.getJSONObject(i);
                        ImageModel imageModel = new ImageModel();
                        imageModel.setImage_url(data.getString("image"));
                        list.add(imageModel);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


                imageModelArrayList = list;

                try {
                    queue.getCache().clear();
                    init(imageModelArrayList);
                } catch (Exception e) {

                }


            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        })
        {
            @Override
            protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
                Cache.Entry cacheEntry= HttpHeaderParser.parseCacheHeaders(response);
                if(cacheEntry == null)
                {
                    cacheEntry=new Cache.Entry();
                }
                final long cacheHitButRefresh=3*60*1000;
                final long cacheExpired=24*60*60*1000;
                long now=System.currentTimeMillis();
                final long softExpire=now + cacheHitButRefresh;
                final long ttl=now+cacheExpired;
                cacheEntry.data=response.data;
                cacheEntry.softTtl=softExpire;
                cacheEntry.ttl=ttl;
                String headerValue;
                headerValue=response.headers.get("Date");
                if(headerValue !=null)
                {
                    cacheEntry.serverDate=HttpHeaderParser.parseDateAsEpoch(headerValue);
                }

                headerValue = response.headers.get("Last-Modified");
                if(headerValue !=null)
                {
                    cacheEntry.serverDate=HttpHeaderParser.parseDateAsEpoch(headerValue);
                }

                cacheEntry.responseHeaders=response.headers;
                try {
                    final String jsonString=new String(response.data,HttpHeaderParser.parseCharset(response.headers));
                    return Response.success(new JSONArray(jsonString),cacheEntry);
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                    return Response.error(new ParseError(e));
                }

            }

            @Override
            protected void deliverResponse(JSONArray response) {
                super.deliverResponse(response);
            }

            @Override
            public void deliverError(VolleyError error) {
                super.deliverError(error);
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                return super.parseNetworkError(volleyError);
            }
        };

        int socketTimeout = 0;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        arrayRequest.setRetryPolicy(policy);

        queue.add(arrayRequest);

    }

    public void init(ArrayList<ImageModel> imageModelArrayList) {

        mPager.setAdapter(new SlidingImage_Adapter(getContext(), imageModelArrayList));

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        indicator.setRadius(5 * density);

        NUM_PAGES = imageModelArrayList.size();

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

    ProgressDialog prg;

    public void loading() {
        prg = new ProgressDialog(getContext());
        prg.setMessage("Chargement des données en cours...");
//        prg.setCancelable(false);
        prg.show();
    }

    public void hideDialogue() {
        if (prg != null) {
            prg.dismiss();
            prg = null;
        }
    }

    public void countAnnonce() {
        progressBar.setVisibility(View.VISIBLE);
        String url = urlAdresse.adresseUrl() + "history/annonces/" + sessionManager.getId();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                annonces.clear();
                int j = 0;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        hideDialogue();
                        JSONObject data = response.getJSONObject(i);
                        JSONArray images = (JSONArray) data.get("images");
                        if (images.length() > 0) {
                            j = j + 1;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                sessionCount.inserNbre("" + j);


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

    public void countDemande() {
        progressBar.setVisibility(View.VISIBLE);
        String url = urlAdresse.adresseUrl() + "history/demande/" + sessionManager.getId();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                int j = 0;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        hideDialogue();
                        JSONObject data = response.getJSONObject(i);
                        JSONArray images = (JSONArray) data.get("images");
                        if (images.length() > 0) {
                            j = j + 1;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                sessionCount.inserNbreDemande("" + j);


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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.cad, menu);
        MenuItem item = menu.findItem(R.id.rech);


        MenuItem decon = menu.findItem(R.id.deconnecter);
        decon.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                LoginManager.getInstance().logOut();
                sessionManager.logout();
                AccountKit.logOut();

                Intent intent = new Intent(getContext(), TypeConnexion.class);
                startActivity(intent);
                getActivity().finish();


                return true;
            }
        });
//        SearchManager searchManager=(SearchManager)getActivity().getSystemService(Context.SEARCH_SERVICE);
        android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) MenuItemCompat.getActionView(item);
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setQueryHint("Rechercher un produit");
        searchView.setOnQueryTextListener(this);

    }

    public View.OnClickListener addannonce = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getContext(), PublierAnnonce.class);
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

            if (!annonces.isEmpty() || annonces.size() != 0) {
                annoncesAdapter.getFilter().filter(s);
            }
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }

        return false;
    }


}
