package com.tropi.dvjl.tropicom.Annonceur;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.accountkit.AccountKit;
import com.facebook.login.LoginManager;
import com.tropi.dvjl.tropicom.CircleImage.GlideCircleTransformation;
import com.tropi.dvjl.tropicom.Demandes.HistoryDemande;
import com.tropi.dvjl.tropicom.ModifierCompte;
import com.tropi.dvjl.tropicom.R;
import com.tropi.dvjl.tropicom.SqliteData.SessionCount;
import com.tropi.dvjl.tropicom.SqliteData.SessionManager;
import com.tropi.dvjl.tropicom.TypeConnexion;
import com.tropi.dvjl.tropicom.login;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfilVendeur extends Fragment {

    TextView name,counthistory,text_action2,deccon;
    TextInputEditText statu,ville,pays,tel;
    LinearLayout layout_action1,layout_action2;
    SessionCount sessionCount;
    ImageView imgprofil;
    SessionManager sessionManager;
    Button pro;


    public ProfilVendeur() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();

        name.setText(sessionManager.getNAME());

        statu.setText(sessionManager.getCompte());

        ville.setText(sessionManager.getVille());

        pays.setText(sessionManager.getPays());

        if(sessionManager.getPhoto() != "null" || sessionManager.getPhoto() != "nothing"  || sessionManager.getPhoto() != "" )
        {
            Glide.with(ProfilVendeur.this).load(sessionManager.getPhoto())
                    .thumbnail(0.5f)
                    .crossFade()
                    .placeholder(R.drawable.tof)
                    .error(R.drawable.tof)
                    .bitmapTransform(new GlideCircleTransformation(getContext()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgprofil);
        }else{
            Glide.with(ProfilVendeur.this).load(R.drawable.tof)
                    .thumbnail(0.5f)
                    .crossFade()
                    .placeholder(R.drawable.quali_tropicom)
                    .error(R.drawable.quali_tropicom)
                    .bitmapTransform(new GlideCircleTransformation(getContext()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgprofil);
        }


    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_profil_vendeur, container, false);
        setHasOptionsMenu(true);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Mon Tropicom");
        counthistory=v.findViewById(R.id.counthistory);
        text_action2=v.findViewById(R.id.text_action2);
        pro=v.findViewById(R.id.pro);
        deccon=v.findViewById(R.id.deconn);

        sessionManager=new SessionManager(getContext());
        imgprofil=v.findViewById(R.id.imgprofil);

        sessionCount=new SessionCount(getContext());
        counthistory.setText(sessionCount.getAnnonceNbre()+" publication(s)");
        text_action2.setText(sessionCount.getDemandeNbre()+" demande(s)");


        name=v.findViewById(R.id.name);
        name.setText(sessionManager.getNAME());

        statu=v.findViewById(R.id.statu);
        statu.setText(sessionManager.getCompte());

        ville=v.findViewById(R.id.ville);
        ville.setText(sessionManager.getVille());

        pays=v.findViewById(R.id.pays);
        pays.setText(sessionManager.getPays());

        tel=v.findViewById(R.id.tel);
        if(!Objects.equals(sessionManager.getTel(), "") || sessionManager.getTel() != null)
        {
            tel.setVisibility(View.VISIBLE);
            tel.setText(sessionManager.getTel());
        }else{
            tel.setVisibility(View.GONE);
        }



        layout_action1=v.findViewById(R.id.layout_action1);
        layout_action2=v.findViewById(R.id.layout_action2);

        layout_action2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getContext(),HistoryDemande.class);
                startActivity(i);
            }
        });

        layout_action1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getContext(),History.class);
                startActivity(i);
            }
        });

        if(sessionManager.getPhoto() != "null" || sessionManager.getPhoto() != "nothing"  || sessionManager.getPhoto() != "" )
        {
            Glide.with(ProfilVendeur.this).load(sessionManager.getPhoto())
                    .thumbnail(0.5f)
                    .crossFade()
                    .placeholder(R.drawable.tof)
                    .error(R.drawable.tof)
                    .bitmapTransform(new GlideCircleTransformation(getContext()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgprofil);
        }else{
            Glide.with(ProfilVendeur.this).load(R.drawable.tof)
                    .thumbnail(0.5f)
                    .crossFade()
                    .placeholder(R.drawable.quali_tropicom)
                    .error(R.drawable.quali_tropicom)
                    .bitmapTransform(new GlideCircleTransformation(getContext()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgprofil);
        }


        deccon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.logout();
                AccountKit.logOut();
                LoginManager.getInstance().logOut();
                Intent intent=new Intent(getContext(),login.class);
                startActivity(intent);
                ((AppCompatActivity)getActivity()).finish();
            }
        });

        pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), ModifierCompte.class);
                startActivity(intent);

            }
        });

        return  v;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.cad_2,menu);

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
    }

}
