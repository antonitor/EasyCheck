package net.marcarni.easycheck.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.marcarni.easycheck.R;
import net.marcarni.easycheck.SubstitucioActionActivity;

import java.util.ArrayList;

/**
 * @author Maria  on 10/12/2017.
 *
 *
 * Classe pel adaptador creada per l'activitat de substitució.
 * Fa servir la classe Header_consulta com a model per seguir.
 */

public class HeaderAdapter_Substitucio  extends RecyclerView.Adapter<HeaderAdapter_Substitucio.ViewHolder> {
    private ArrayList<Header_Consulta> mDataset;

    public HeaderAdapter_Substitucio(ArrayList<Header_Consulta> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public HeaderAdapter_Substitucio.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // crea una nova vista
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_consulta, parent, false);
        // Estableix la mida de la vista, els marges, els farcits i els paràmetres de disseny
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(HeaderAdapter_Substitucio.ViewHolder holder, int position) {
        holder.nomTreballador.setText(mDataset.get(position).getNomTreballador());
        holder.descripcioServei.setText(mDataset.get(position).getDescripcioServei());
        holder.idServei.setText(mDataset.get(position).getIdServei());
        holder.dataServei.setText(mDataset.get(position).getDataServei());
        holder.horaInici.setText(mDataset.get(position).getHoraInici());
        holder.horaFi.setText(mDataset.get(position).getHoraFi());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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
     *
     * @param v view
     */
    public ViewHolder(View v) {
        super(v);
        nomTreballador = (TextView) v.findViewById(R.id.nomTreballador);
        descripcioServei = (TextView) v.findViewById(R.id.descripcio_servei);
        dataServei = (TextView) v.findViewById(R.id.dataServei);
        idServei = (TextView) v.findViewById(R.id.idServei);
        horaInici = (TextView) v.findViewById(R.id.horaInici);
        horaFi = (TextView) v.findViewById(R.id.horaFi);

        idServei.setVisibility(View.GONE);
        context = itemView.getContext();
        v.setOnClickListener(this);
    }

    /**
     * Mètode per gestionar l'esdeveniment onClick
     *
     * @param view que interacturarà
     */
    // @Override
    public void onClick(View view) {

        String  servei=idServei.getText().toString();
       Intent intent= new Intent(context, SubstitucioActionActivity.class);
        intent.putExtra("servei",servei);

       context.startActivity(intent);
    }

    }

        /**
         * Mètode per actualitzar el recycler un cop canviat el filtratge.
         *
         * @param llistaConsultes arrayList
         */
        public void actualitzaRecycler(ArrayList<Header_Consulta> llistaConsultes) {
            mDataset = llistaConsultes;
            this.notifyDataSetChanged();

    }}