package com.tropi.dvjl.tropicom.Annonceur;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.accountkit.AccountKit;
import com.facebook.login.LoginManager;
import com.tropi.dvjl.tropicom.R;
import com.tropi.dvjl.tropicom.SqliteData.SessionManager;
import com.tropi.dvjl.tropicom.TypeConnexion;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoriqueVendeur extends Fragment {

SessionManager sessionManager;
    public HistoriqueVendeur() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

      View v=inflater.inflate(R.layout.fragment_historique_vendeur, container, false);
        setHasOptionsMenu(true);

        sessionManager=new SessionManager(getContext());

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



