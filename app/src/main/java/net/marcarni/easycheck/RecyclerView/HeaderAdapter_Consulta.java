package net.marcarni.easycheck.RecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.marcarni.easycheck.R;

import java.util.ArrayList;

/**
 * Created by Maria on 07/10/2017.
 */

public class HeaderAdapter_Consulta extends RecyclerView.Adapter<HeaderAdapter_Consulta.ViewHolder> {
    private ArrayList<Header_Consulta> mDataset;

    public HeaderAdapter_Consulta(ArrayList<Header_Consulta> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public HeaderAdapter_Consulta.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_consulta, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return  new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.nomTreballador.setText(mDataset.get(position).getNomTreballador());
        //holder.cognom1Treballador.setText(mDataset.get(position).getCognom1Treballador());
        //holder.cognom2Treballador.setText(mDataset.get(position).getCognom2Treballador());
        holder.descripcioServei.setText(mDataset.get(position).getDescripcioServei());

    }


    @Override
    public int getItemCount() {
       return mDataset.size();
    }





    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView  nomTreballador;
        //TextView cognom1Treballador;
        //TextView cognom2Treballador;
        TextView descripcioServei;
        View v;

        public ViewHolder(View v) {
            super(v);
            nomTreballador=(TextView)v.findViewById(R.id.nomTreballador);
            //cognom1Treballador=(TextView)v.findViewById(R.id.cognom1Treballador);
            //cognom2Treballador=(TextView)v.findViewById(R.id.cognom2Treballador);
            descripcioServei=(TextView)v.findViewById(R.id.descripcio_servei);
        }

    }

}