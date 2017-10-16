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


public class HeaderAdapter extends RecyclerView.Adapter<HeaderAdapter.ViewHolder> {
private ArrayList<Header> mDataset;

    /**
     * Constructor de la clase Headeradapter
     * @param myDataset dataSet
     */
    public HeaderAdapter(ArrayList<Header> myDataset) {
        mDataset = myDataset;
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
    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // nom,dni,data,qr,localitzacio,email,check;
        TextView nom,dni,data,qr,localitzacio,email,check,checkText,servei;
        View v;
        int _id;

        /**
         * Constructor de classe statica View Holder
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
         * Mètode per gestionar l'esdeveniment onClick
         * @param view que interacturarà
         */
        @Override
        public void onClick(View view) {
            v = view;
            if  (check.getText().toString().equalsIgnoreCase("0")) {   // Si NO TE el check-in FET
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Vols Confirmar el Check-IN?")
                        .setTitle("Atenció!!")
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.cancel();
                            }
                        })
                        .setPositiveButton("Acceptar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        v.setBackgroundColor(Color.rgb(165, 246, 149));
                                        Toast.makeText(v.getContext(), "Check-in Realitzat", Toast.LENGTH_LONG).show();
                                        check.setText("1");
                                        Log.d("proba", "onClick: "+dni.getText().toString().substring(5,14));
                                        checkText.setText("Check-In:  Realitzat");
                                        DBInterface db=new DBInterface(v.getContext());
                                        db.obre();
                                        db.ActalitzaCheckInReserva(_id);
                                        db.tanca();
                                    }
                                }
                        );
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                Toast.makeText(v.getContext(), "Aquesta reserva ja te check-in!", Toast.LENGTH_LONG).show();
                v.setBackgroundColor(Color.rgb(255, 51, 30));
            }
        }
    }
}
