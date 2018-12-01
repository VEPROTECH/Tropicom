package com.tropi.dvjl.tropicom.MyRequest;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.tropi.dvjl.tropicom.Url_File.UrlAdresse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Verbeck DEGBESSE on 29/09/2017.
 */

public class MyRequest {
    private Context context;
    private RequestQueue queue;
    public int code_statut=0;

    public MyRequest(Context context, RequestQueue queue) {
        this.context = context;
        this.queue = queue;
        this.code_statut=0;
    }



    UrlAdresse urladresse=new UrlAdresse();
    private String URL=urladresse.adresseUrl();

    public void saveAnnonce(final String user,final String libelle, final String qte, final String prix,
                            final String unite, final String pays, final String conditi,
                            final String ville, final String description, final String contact, final String produit,
                            final String qualite,final  ChangeCallBack  changeCallBack)
    {
            String url=URL+"annonce/create";

        JSONObject js=new JSONObject();
        Map<String,String> map=new HashMap<>();
        try {
            map.put("user",user);
            map.put("qualite",qualite);
            map.put("libelle",libelle);
            map.put("qte",qte);
            map.put("unite",unite);
            map.put("prix",prix);
            map.put("conditionnement",conditi);
            map.put("pays",pays);
            map.put("description",description);
            map.put("ville",ville);
            map.put("contact",contact);
            map.put("produit",produit);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        Log.e("valuer", String.valueOf(new JSONObject(map)));
        JsonObjectRequest jsonObj=new JsonObjectRequest(Request.Method.POST,url,new JSONObject(map),new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    Log.d("SUCC", String.valueOf(jsonObject));
                    changeCallBack.onSuccess(jsonObject.getString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(volleyError instanceof NetworkError){
                    changeCallBack.onError("Oups !, Problème de connexion !");
                }else{
                    if(volleyError != null){
                        Log.d("LOGH", String.valueOf(volleyError.toString()));
                    }
                }
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
               // headers.put("Content-Type", "application/json; charset=utf-8");

                return headers;
            }


            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };
        jsonObj.setRetryPolicy(new DefaultRetryPolicy( 200*30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsonObj);


    }


    public void supAnnonce(final int id,final  ChangeCallBack  changeCallBack)
    {
        String url=URL+"delete/annonces";

        JSONObject js=new JSONObject();
        Map<String,String> map=new HashMap<>();
        try {
            map.put("id", String.valueOf(id));
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObj=new JsonObjectRequest(Request.Method.PUT,url,new JSONObject(map),new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    Log.d("SUCC", String.valueOf(jsonObject));
                    if(jsonObject.has("msg"))
                    {
                        changeCallBack.onError(jsonObject.getString("msg"));
                    }else{
                        changeCallBack.onSuccess(jsonObject.getString("message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(volleyError instanceof NetworkError){
                    changeCallBack.onError("Oups !, Problème de connexion !");
                }else{
                    if(volleyError != null){
                        Log.d("LOGH", String.valueOf(volleyError.toString()));
                    }
                }
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                // headers.put("Content-Type", "application/json; charset=utf-8");

                return headers;
            }


            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };
        jsonObj.setRetryPolicy(new DefaultRetryPolicy( 200*30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsonObj);


    }


    public void supDemande(final int id,final  ChangeCallBack  changeCallBack)
    {
        String url=URL+"delete/demande";

        JSONObject js=new JSONObject();
        Map<String,String> map=new HashMap<>();
        try {
            map.put("id", String.valueOf(id));
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObj=new JsonObjectRequest(Request.Method.PUT,url,new JSONObject(map),new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    Log.d("SUCC", String.valueOf(jsonObject));
                    if(jsonObject.has("msg"))
                    {
                        changeCallBack.onError(jsonObject.getString("msg"));
                    }else{
                        changeCallBack.onSuccess(jsonObject.getString("message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(volleyError instanceof NetworkError){
                    changeCallBack.onError("Oups !, Problème de connexion !");
                }else{
                    if(volleyError != null){
                        Log.d("LOGH", String.valueOf(volleyError.toString()));
                    }
                }
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                // headers.put("Content-Type", "application/json; charset=utf-8");

                return headers;
            }


            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };
        jsonObj.setRetryPolicy(new DefaultRetryPolicy( 200*30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsonObj);


    }

    public void saveImage(final String img, final String id, final ChangeCallBack changeCallBack){
        String url=URL+"upload";
        Map<String,String> map=new HashMap<>();
        try {
            map.put("file",img);
            map.put("id",id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObj=new JsonObjectRequest(Request.Method.POST,url,new JSONObject(map),new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    Log.d("SUCC", String.valueOf(jsonObject));
                    changeCallBack.onSuccess(jsonObject.getString("data"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(volleyError instanceof NetworkError){
                    changeCallBack.onError("Oups !, Problème de connexion !");
                }else{
                    if(volleyError != null){
                        changeCallBack.onError("Oups !, Une erreur serveur s'est produite !");
                        Log.d("LOGH", String.valueOf(volleyError.toString()));
                    }
                }
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                // headers.put("Content-Type", "application/json; charset=utf-8");

                return headers;
            }


            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };
        jsonObj.setRetryPolicy(new DefaultRetryPolicy( 200*30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsonObj);


    }

    public void saveImageDemande(final String img, final String id, final ChangeCallBack changeCallBack){
        String url=URL+"upload_demande";
        Map<String,String> map=new HashMap<>();
        try {
            map.put("file",img);
            map.put("id",id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObj=new JsonObjectRequest(Request.Method.POST,url,new JSONObject(map),new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    Log.d("SUCC", String.valueOf(jsonObject));
                    changeCallBack.onSuccess(jsonObject.getString("data"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(volleyError instanceof NetworkError){
                    changeCallBack.onError("Oups !, Problème de connexion !");
                }else{
                    if(volleyError != null){
                        changeCallBack.onError("Oups !, Une erreur serveur s'est produite !");
                        Log.d("LOGH", String.valueOf(volleyError.toString()));
                    }
                }
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                // headers.put("Content-Type", "application/json; charset=utf-8");

                return headers;
            }


            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };
        jsonObj.setRetryPolicy(new DefaultRetryPolicy( 200*30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsonObj);


    }

    public void saveDemande(String code, String finalProduit,
                            String lib, String pay, String cit, String des,
                            String tel, String qte,String unite, ChangeCallBack changeCallBack) {
        String url=URL+"demande/create";

        JSONObject js=new JSONObject();
        Map<String,String> map=new HashMap<>();
        try {
            map.put("user",code);
            map.put("libelle",lib);
            map.put("pays",pay);
            map.put("description",des);
            map.put("ville",cit);
            map.put("contact",tel);
            map.put("produit",finalProduit);
            map.put("unite",unite);
            map.put("qte",qte);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObj=new JsonObjectRequest(Request.Method.POST,url,new JSONObject(map),new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    Log.d("SUCC", String.valueOf(jsonObject));
                    changeCallBack.onSuccess(jsonObject.getString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(volleyError instanceof NetworkError){
                    changeCallBack.onError("Oups !, Problème de connexion !");
                }else{
                    if(volleyError != null){
                        Log.d("LOGH", String.valueOf(volleyError.toString()));
                    }
                }
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                // headers.put("Content-Type", "application/json; charset=utf-8");

                return headers;
            }


            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };
        jsonObj.setRetryPolicy(new DefaultRetryPolicy( 200*30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsonObj);



    }


    public interface ChangeCallBack{
       void onSuccess(String message);
        void onError(String msg);
    }

    public void login(final String tel,final String password, final LoginCallback affichageCallBack){
        String url=URL+"connect";
        Map<String,String> map=new HashMap<>();
        try {
            map.put("login",tel);
            map.put("password",password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObj=new JsonObjectRequest(Request.Method.POST,url,new JSONObject(map),new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("RESULTAT", String.valueOf(response));
                try {
                    Boolean error=response.getBoolean("error");
                    Log.e("BOLLEAN", String.valueOf(error));

                    if(error){
                        JSONObject json=response.getJSONObject("data");
                        String tel=json.getString("tel");
                        String password=json.getString("password");
                        String nom=json.getString("nom");
                        String prenom=json.getString("prenom");
                        String code=json.getString("code");
                        String ville=json.getString("ville");
                        String pays=json.getString("pays");
                        String compte=json.getString("compte");
                        String photo=json.getString("photo");
                        affichageCallBack.onSuccess(tel,password,nom,prenom,code,ville,pays,compte,photo);
                    }else{
                        affichageCallBack.onError(response.getString("msg"));
                    }
                } catch (JSONException e) {
                    affichageCallBack.onError("Une erreur s'est produite, veuillez réessayer");
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error instanceof NetworkError){
                    affichageCallBack.onError("Oups !, Problème de connexion !");
                }else{
                    if(error instanceof VolleyError){
                        affichageCallBack.onError("Une erreur s'est produite, veuillez réessayer");
                    }
                }
            }
        });
        queue.add(jsonObj);
    }
    public interface LoginCallback{
        void onSuccess(String tel,String password, String nom,String prenom,String code,String ville,String pays,String compte,String photo);
        void onError(String message);
    }

    public void Verifylogin(final String lien, final VerifyCallback affichageCallBack){
        String url=URL+"reseau/verify";
        Map<String,String> map=new HashMap<>();
        try {
            map.put("lien",lien);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObj=new JsonObjectRequest(Request.Method.POST,url,new JSONObject(map),new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("RESULTAT", String.valueOf(response));
                try {
                    Boolean error=response.getBoolean("error");
                    Log.e("BOLLEAN", String.valueOf(error));

                    if(!error){

                        JSONObject json=response.getJSONObject("data");
                        String tel=json.getString("tel");
                        String password=json.getString("password");
                        String nom=json.getString("nom");
                        String prenom=json.getString("prenom");
                        String code=json.getString("code");
                        String ville=json.getString("ville");
                        String pays=json.getString("pays");
                        String compte=json.getString("compte");
                        String photo=json.getString("photo");

                        affichageCallBack.onSuccess(error,tel,password,nom,prenom,code,ville,pays,compte,photo);
                    }else{
                        affichageCallBack.onSuccess(error,"","","","","","","","","");
                    }
                } catch (JSONException e) {
                    affichageCallBack.onError("Une erreur s'est produite, veuillez réessayer");
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error instanceof NetworkError){
                    affichageCallBack.onError("Oups !, Problème de connexion !");
                }else{
                    if(error instanceof VolleyError){
                        affichageCallBack.onError("Une erreur s'est produite, veuillez réessayer");
                    }
                }
            }
        });
        queue.add(jsonObj);
    }
    public interface VerifyCallback{
        void onSuccess(Boolean data,String tel,String password, String nom,String prenom,String code,String ville,String pays,String compte,String photo);
        void onError(String message);
    }




    public void register(final String tel, final String nom, final String prenom,
                         final String pays, final String ville,final String password,
                         final String compte, final String societe,
                            final  RegisterCallBack  registerCallBack)
    {
        String url=URL+"user/create";

        Map<String,String> map=new HashMap<>();
        try {
            map.put("nom",nom);
            map.put("prenom",prenom);
            map.put("telephone",tel);
            map.put("societe",societe);
            map.put("password",password);
            map.put("pays",pays);
            map.put("compte",compte);
            map.put("ville",ville);

        } catch (Exception e) {
            e.printStackTrace();
        }

//        Log.e("valuer", String.valueOf(new JSONObject(map)));
        JsonObjectRequest jsonObj=new JsonObjectRequest(Request.Method.POST,url,new JSONObject(map),new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    Log.d("SUCC", String.valueOf(jsonObject));
                    if(Objects.equals(jsonObject.getString("statut"), "SUCCES"))
                    {
                        registerCallBack.onSuccess(jsonObject.getString("message"));
                    }else{
                        registerCallBack.onError(jsonObject.getString("msg"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(volleyError instanceof NetworkError){
                    registerCallBack.onError("Oups !, Problème de connexion !");
                }else{
                    if(volleyError != null){
                        Log.d("LOGH", String.valueOf(volleyError.toString()));
                    }
                }
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                // headers.put("Content-Type", "application/json; charset=utf-8");

                return headers;
            }


            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };
        jsonObj.setRetryPolicy(new DefaultRetryPolicy( 200*30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsonObj);


    }

    public void updatePasse(final String tel,final String password,
                         final  RegisterCallBack  registerCallBack)
    {
        String url=URL+"user/password/update";

        Map<String,String> map=new HashMap<>();
        try {
            map.put("login",tel);
            map.put("password",password);

        } catch (Exception e) {
            e.printStackTrace();
        }

//        Log.e("valuer", String.valueOf(new JSONObject(map)));
        JsonObjectRequest jsonObj=new JsonObjectRequest(Request.Method.PUT,url,new JSONObject(map),new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    Log.d("SUCC", String.valueOf(jsonObject));
                    if(Objects.equals(jsonObject.getString("statut"), "SUCCES"))
                    {
                        registerCallBack.onSuccess(jsonObject.getString("message"));
                    }else{
                        registerCallBack.onError(jsonObject.getString("msg"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(volleyError instanceof NetworkError){
                    registerCallBack.onError("Oups !, Problème de connexion !");
                }else{
                    if(volleyError != null){
                        Log.d("LOGH", String.valueOf(volleyError.toString()));
                    }
                }
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                // headers.put("Content-Type", "application/json; charset=utf-8");

                return headers;
            }


            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };
        jsonObj.setRetryPolicy(new DefaultRetryPolicy( 200*30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsonObj);


    }

    public void modifier(final String pays, final String ville,
                         final String photo, final String id, final  ModifierCallBack  registerCallBack)
    {
        String url=URL+"user/update";

        Map<String,String> map=new HashMap<>();
        try {
            map.put("pays",pays);
            map.put("ville",ville);
            map.put("photo",photo);
            map.put("code",id);

        } catch (Exception e) {
            e.printStackTrace();
        }

//        Log.e("valuer", String.valueOf(new JSONObject(map)));
        JsonObjectRequest jsonObj=new JsonObjectRequest(Request.Method.PUT,url,new JSONObject(map),new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    Log.d("SUCC", String.valueOf(jsonObject));
                    if(Objects.equals(jsonObject.getString("statut"), "SUCCES"))
                    {
                        JSONObject json=jsonObject.getJSONObject("data");

                        String nom=json.getString("nom");
                        String prenom=json.getString("prenom");
                        String ville=json.getString("ville");
                        String pays=json.getString("pays");
                        String compte=json.getString("compte");
                        String photo1=json.getString("photo");

                        registerCallBack.onData(nom,prenom,ville,pays,compte,photo1);

                        registerCallBack.onSuccess(jsonObject.getString("message"));
                    }else{
                        registerCallBack.onError(jsonObject.getString("msg"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(volleyError instanceof NetworkError){
                    registerCallBack.onError("Oups !, Problème de connexion !");
                }else{
                    if(volleyError != null){
                        Log.d("LOGH", String.valueOf(volleyError.toString()));
                    }
                }
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                // headers.put("Content-Type", "application/json; charset=utf-8");

                return headers;
            }


            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };
        jsonObj.setRetryPolicy(new DefaultRetryPolicy( 200*30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsonObj);


    }

    public interface ModifierCallBack{
        void onSuccess(String message);
        void onData(String nom,String prenom,String ville,String pays,String compte,String photo);
        void inputError(Map<String, String> errors);
        void onError(String message);
    }

    public interface RegisterCallBack{
        void onSuccess(String message);
        void inputError(Map<String, String> errors);
        void onError(String message);
    }


    public void registerSocial(final String tel, final String nom, final String prenom,
                         final String pays, final String ville,final String lien,final String photo,
                         final String compte, final String societe,
                               final LoginCallback affichageCallBack)
    {
        String url=URL+"user/social";

        Map<String,String> map=new HashMap<>();
        try {
            map.put("nom",nom);
            map.put("prenom",prenom);
            map.put("telephone",tel);
            map.put("societe",societe);
            map.put("urlphoto",photo);
            map.put("lien",lien);
            map.put("pays",pays);
            map.put("compte",compte);
            map.put("ville",ville);

        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.e("valuer", String.valueOf(new JSONObject(map)));
        JsonObjectRequest jsonObj=new JsonObjectRequest(Request.Method.POST,url,new JSONObject(map),new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    Boolean error=response.getBoolean("error");
                    Log.e("BOLLEAN", String.valueOf(error));
                    Log.e("JSONNN", String.valueOf(response));

                    if(!error){
                        Log.e("JSONNN", String.valueOf(response));
                        JSONObject json=response.getJSONObject("data");



                        String tel=json.getString("tel");
                        String password=json.getString("password");
                        String nom=json.getString("nom");
                        String prenom=json.getString("prenom");
                        String code=json.getString("code");
                        String ville=json.getString("ville");
                        String pays=json.getString("pays");
                        String compte=json.getString("compte");
                        String photo1=json.getString("photo");
                        affichageCallBack.onSuccess(tel,password,nom,prenom,code,ville,pays,compte,photo1);
                    }else{
                        affichageCallBack.onError(response.getString("msg"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(volleyError instanceof NetworkError){
                    affichageCallBack.onError("Oups !, Problème de connexion !");
                }else{
                    if(volleyError != null){
                        Log.d("LOGH", String.valueOf(volleyError.toString()));
                    }
                }
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                // headers.put("Content-Type", "application/json; charset=utf-8");

                return headers;
            }


            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };
        jsonObj.setRetryPolicy(new DefaultRetryPolicy( 200*30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsonObj);


    }



}



