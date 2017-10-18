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

/**
 * @author Maria  Remedios Ortega Cobos
 *
 * Classe adaptador que segueix el patró de disseny viewHolder i defineix classe interna que extend
 * de RecyclerView.ViewHolder
 */

public class HeaderAdapter_Consulta extends RecyclerView.Adapter<HeaderAdapter_Consulta.ViewHolder> {
    private ArrayList<Header_Consulta> mDataset;

    /**
     * Constructor de la clase Headeradapter_Consulta
     * @param myDataset dataSet
     */
    public HeaderAdapter_Consulta(ArrayList<Header_Consulta> myDataset) {
        mDataset = myDataset;
    }

    /**
     * Mètode que crea vistes noves (invocades pel gestor de disseny)
     * @param parent view parent
     * @param viewType tipus view
     * @return viewHolder
     */
    @Override
    public HeaderAdapter_Consulta.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // crea una nova vista
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_consulta, parent, false);
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
        holder.nomTreballador.setText(mDataset.get(position).getNomTreballador());
        holder.descripcioServei.setText(mDataset.get(position).getDescripcioServei());
        holder.idServei.setText(mDataset.get(position).getIdServei());
        holder.dataServei.setText(mDataset.get(position).getDataServei());
        holder.horaInici.setText(mDataset.get(position).getHoraInici());
        holder.horaFi.setText(mDataset.get(position).getHoraFi());
    }

    /**
     * Mètode per retornar el tamany del dataset invocat per el layout manager
     * @return tamany de mDataset.
     */
    @Override
    public int getItemCount() {
       return mDataset.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        TextView nomTreballador;
        TextView descripcioServei;
        TextView idServei;
        TextView dataServei;
        TextView horaInici;
        TextView horaFi;
        View v;
        Context context;
        /**
         * Constructor de classe statica View Holder
         * @param v view
         */
        public ViewHolder(View v) {
            super(v);
            nomTreballador=(TextView)v.findViewById(R.id.nomTreballador);
            descripcioServei=(TextView)v.findViewById(R.id.descripcio_servei);
            dataServei=(TextView) v.findViewById(R.id.dataServei);
            idServei = (TextView) v.findViewById(R.id.idServei);
            horaInici = (TextView) v.findViewById(R.id.horaInici);
            horaFi = (TextView) v.findViewById(R.id.horaFi);

            idServei.setVisibility(View.GONE);
            context = itemView.getContext();
            v.setOnClickListener(this);
        }

        /**
         * Mètode per gestionar l'esdeveniment onClick
         * @param view que interacturarà
         */
        @Override
        public void onClick(View view) {
          //  Toast.makeText(view.getContext(), idServei.getText().toString(), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(context,DetallActivity.class);
            intent.putExtra("ID_SERVEI",idServei.getText().toString());
            context.startActivity(intent);
        }
    }

    /**
     * Mètode per actualitzar el recycler un cop canviat el filtratge.
     * @param llistaConsultes arrayList
     */
    public void actualitzaRecycler(ArrayList<Header_Consulta> llistaConsultes) {
        mDataset = llistaConsultes;
        this.notifyDataSetChanged();
    }
}