package net.marcarni.easycheck.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.marcarni.easycheck.DetallActivity;
import net.marcarni.easycheck.R;

import java.util.ArrayList;


public class HeaderAdapter_Consulta extends RecyclerView.Adapter<HeaderAdapter_Consulta.ViewHolder> {
    private ArrayList<Header_Consulta> mDataset;


    public HeaderAdapter_Consulta(ArrayList<Header_Consulta> myDataset) {
        mDataset = myDataset;
    }
    
    public void actualitzaRecycler(ArrayList<Header_Consulta> llistaConsultes) {
        mDataset = llistaConsultes;
        this.notifyDataSetChanged();
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
        holder.descripcioServei.setText(mDataset.get(position).getDescripcioServei());
        holder.idServei.setText(mDataset.get(position).getIdServei());
    }


    @Override
    public int getItemCount() {
       return mDataset.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        TextView nomTreballador;
        TextView descripcioServei;
        TextView idServei;
        View v;
        Context context;

        public ViewHolder(View v) {
            super(v);
            nomTreballador=(TextView)v.findViewById(R.id.nomTreballador);
            descripcioServei=(TextView)v.findViewById(R.id.descripcio_servei);
            idServei = (TextView) v.findViewById(R.id.idServei);
            idServei.setVisibility(View.GONE);
            context = itemView.getContext();
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
          //  Toast.makeText(view.getContext(), idServei.getText().toString(), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(context,DetallActivity.class);
            intent.putExtra("ID_SERVEI",idServei.getText().toString());
            context.startActivity(intent);
        }
    }
}