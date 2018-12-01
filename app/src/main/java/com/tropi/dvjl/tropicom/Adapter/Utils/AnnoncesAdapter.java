package com.tropi.dvjl.tropicom.Adapter.Utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tropi.dvjl.tropicom.Annonceur.DetailsAnnonce;
import com.tropi.dvjl.tropicom.MyObject.Annonce;
import com.tropi.dvjl.tropicom.R;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.RecyclerView.NO_POSITION;

/**
 * Created by Verbeck DEGBESSE on 26/05/2018.
 */

public class AnnoncesAdapter extends RecyclerView.Adapter<AnnoncesAdapter.ViewHolder> implements Filterable{
    private Context ctx;
    private List<Annonce> annonceList;
    private LayoutInflater inflater;
    private List<Annonce> mStringFilterList;
    private ValueFilter valueFilter;
    private int currentPosition=0;

    public AnnoncesAdapter(Context ctx, List<Annonce> annonceList){
        this.ctx=ctx;
        this.annonceList=annonceList;
        this.mStringFilterList=annonceList;
    }

    public void onItemsRemoved(RecyclerView recyclerView, int positionStart, int itemCount) {
        if (getItemCount() == 0) {
            currentPosition = NO_POSITION;
        } else if (currentPosition >= positionStart) {
            currentPosition = Math.max(0, currentPosition - itemCount);
        }
    }


    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public final View mView;
        SimpleDraweeView img_custom;
        //  TextView nomprenom=row.findViewById(R.id.nomprenom);
        TextView day;
        TextView libelle,cat;
        TextView prix;
        CardView card;

        public ViewHolder(View row, View mView) {
            super(mView);
            this.mView = mView;
            img_custom=(SimpleDraweeView) mView.findViewById(R.id.image1);
            day=mView.findViewById(R.id.day);
            libelle=mView.findViewById(R.id.libelle);
            prix=mView.findViewById(R.id.prix);
            cat=mView.findViewById(R.id.cat);
            card=mView.findViewById(R.id.card);

        }


    }
    @Override
    public AnnoncesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_annonce, parent, false);
        return new ViewHolder(view, view);
    }


        public void add(int i, Annonce item)
        {
            annonceList.add(i,item);
           notifyItemInserted(i);
        }


        public void remove(Annonce item)
        {
            int i=annonceList.indexOf(item);
            annonceList.remove(i);
            notifyItemRemoved(i);
        }



    @Override
    public void onBindViewHolder(ViewHolder holder, int i)
    {
        final Annonce annonce=annonceList.get(i);
        // nomprenom.setText(annonce.getNom()+" "+annonce.getPrenom());
        FindDay findDay=new FindDay();
        holder.day.setText(findDay.getDiff(annonce.getDayAnnonce(),findDay.getDay())+", "+annonce.getHeuAnnonce());
        holder.libelle.setText(annonce.getLibelle());
        holder.prix.setText(formatMoney(""+Integer.parseInt(annonce.getPrix_unitaire()))+" CFA/"+annonce.getUnite());
        holder.cat.setText(annonce.getProduit());

        final Uri uri = Uri.parse(annonce.getImg1());
        holder.img_custom.setImageURI(uri);

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    String qt=""+annonce.getQte();
                    Intent intent=new Intent(ctx, DetailsAnnonce.class);
                    intent.putExtra("libelle",annonce.getLibelle());
                    intent.putExtra("condi",annonce.getConditionnement());
                    intent.putExtra("typ",annonce.getProduit());
                    intent.putExtra("qte",qt);
                    intent.putExtra("qalite",annonce.getQualite());
                    intent.putExtra("pays",annonce.getPays());
                    intent.putExtra("ville",annonce.getCity());
                    intent.putExtra("tel",annonce.getTel());
                    intent.putExtra("nom",annonce.getPrenom()+" "+annonce.getNom().charAt(0)+".");


                    intent.putExtra("unit",annonce.getUnite());
                    intent.putExtra("img0",annonce.getImg1());
                    intent.putExtra("pu",annonce.getPrix_unitaire());
                    intent.putExtra("time",annonce.getHeuAnnonce());
                    intent.putExtra("jr",annonce.getDayAnnonce());


                    intent.putExtra("des",annonce.getDescription());
                    intent.putStringArrayListExtra("mesImg", (ArrayList<String>) annonce.getMesImg());


                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ctx.startActivity(intent);
                }catch (Exception e){
                    System.out.print(e.getMessage());
                }

            }
        });




    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return annonceList == null ? 0 : annonceList.size();
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


    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                List<Annonce> filterList = new ArrayList<>();
                for (int i = 0; i < mStringFilterList.size(); i++) {
                    if ((mStringFilterList.get(i).getProduit().toUpperCase())
                            .contains(constraint.toString().toUpperCase()) ||
                    (mStringFilterList.get(i).getLibelle().toUpperCase())
                            .contains(constraint.toString().toUpperCase()) ||
                            (mStringFilterList.get(i).getCity().toUpperCase())
                                    .contains(constraint.toString().toUpperCase())) {

                        Annonce object = new Annonce();
                        object.setLibelle(mStringFilterList.get(i).getLibelle());
                        object.setPrix_unitaire(mStringFilterList.get(i).getPrix_unitaire());
                        object.setCity(mStringFilterList.get(i).getCity());
                        object.setVisible(mStringFilterList.get(i).getVisible());
                        object.setConditionnement(mStringFilterList.get(i).getConditionnement());
                        object.setPrix_vente(mStringFilterList.get(i).getPrix_vente());
                        object.setDayAnnonce(mStringFilterList.get(i).getDayAnnonce());
                        object.setHeuAnnonce(mStringFilterList.get(i).getHeuAnnonce());
                        object.setTel(mStringFilterList.get(i).getTel());
                        object.setQte(mStringFilterList.get(i).getQte());
                        object.setQualite(mStringFilterList.get(i).getQualite());
                        object.setNom(mStringFilterList.get(i).getNom());
                        object.setPrenom(mStringFilterList.get(i).getPrenom());
                        object.setPays(mStringFilterList.get(i).getPays());
                        object.setProduit(mStringFilterList.get(i).getProduit());
                        object.setUnite(mStringFilterList.get(i).getUnite());
                        object.setImg1(mStringFilterList.get(i).getImg1());
                        object.setMesImg(mStringFilterList.get(i).getMesImg());



                        filterList.add(object);
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = mStringFilterList.size();
                results.values = mStringFilterList;
            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            annonceList = (List<Annonce>) filterResults.values;
            notifyDataSetChanged();
        }
    }
    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

}
