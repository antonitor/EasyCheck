package net.marcarni.easycheck.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import net.marcarni.easycheck.R;
import net.marcarni.easycheck.SQLite.DBInterface;

import java.util.ArrayList;

/**
 * @author Maria  Remedios Ortega Cobos
 *
 * Classe adaptador que segueix el patró de disseny viewHolder i defineix classe interna que extend
 * de RecyclerView.ViewHolder
 */


public class HeaderAdapter extends RecyclerView.Adapter<HeaderAdapter.ViewHolder> {
private ArrayList<Header> mDataset;

    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemId, View v, TextView checkText);
    }

    /**
     * Constructor de la clase Headeradapter
     * @param myDataset dataSet
     */
    public HeaderAdapter(ArrayList<Header> myDataset, ListItemClickListener listener) {
        mDataset = myDataset;
        mOnClickListener = listener;
    }

    /**
     * Mètode que crea vistes noves (invocades pel gestor de disseny)
     * @param parent view parent
     * @param viewType tipus view
     * @return viewHolder
     */
    @Override
    public HeaderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // crea una nova vista
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_header, parent, false);
        // Estableix la mida de la vista, els marges, els farcits i els paràmetres de disseny
        return  new ViewHolder(v);
    }

    /**
     * Mètode que reemplaça els continguts d'una vista (invocada pel gestor de disseny)
     * @param holder holder
     * @param position posició
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Obteniu un element del vostre conjunt de dades en aquesta posició
        // Reemplaça els continguts de la vista amb aquest element
        holder._id = mDataset.get(position).get_id();
        holder.nom.setText(mDataset.get(position).getNom());
        holder.dni.setText(mDataset.get(position).getDni());
        holder.data.setText(mDataset.get(position).getData());
        holder.qr.setText(mDataset.get(position).getQr());
        holder.localitzacio.setText(mDataset.get(position).getLocalitzacio());
        holder.email.setText(mDataset.get(position).getEmail());
        holder.check.setText(mDataset.get(position).getCheck());
        holder.servei.setText(mDataset.get(position).getServei());
        String checkin = mDataset.get(position).getCheck();
        if (checkin.equalsIgnoreCase("0")) checkin="Check-In: No Realitzat"; else checkin="Check-In: Realitzat";
        // holder.check.setText(mDataset.get(position).getCheck());
        holder.checkText.setText(checkin);
    }

    /**
     * Mètode per retornar el tamany del dataset invocat per el layout manager
     * @return tamany de mDataset.
     */
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // nom,dni,data,qr,localitzacio,email,check;
        TextView nom,dni,data,qr,localitzacio,email,check,checkText,servei;
        View v;
        int _id;

        /**
         * Constructor de classe View Holder
         * @param v view
         */
        ViewHolder(View v) {
            super(v);
            nom = (TextView) v.findViewById(R.id.nomTreballador);
            dni = (TextView) v.findViewById(R.id.dni);
            data = (TextView) v.findViewById(R.id.data);
            qr = (TextView) v.findViewById(R.id.qr);
            localitzacio = (TextView) v.findViewById(R.id.localitzacio);
            email = (TextView) v.findViewById(R.id.email);
            check = (TextView) v.findViewById(R.id.check);
            checkText = (TextView) v.findViewById(R.id.checkText);
            servei = (TextView) v.findViewById(R.id.servei);
            check.setVisibility(View.GONE);
            v.setOnClickListener(this);
        }

        /**
         * @author Antoni Torres Marí
         *
         * Mètode per gestionar l'esdeveniment onClick
         * @param view que interacturarà
         */
        @Override
        public void onClick(View view) {
                //S'ha passat tota la funcionalitat a DetallActivity
                mOnClickListener.onListItemClick(_id, view, checkText);
        }
    }

    /**
     * @author Antoni Torres Marí
     *
     * Mètode per actualitzar el recycler
     * @param llistaConsultes arrayList
     */
    public void actualitzaRecycler(ArrayList<Header> llistaConsultes) {
        mDataset = llistaConsultes;
        this.notifyDataSetChanged();
    }
}
