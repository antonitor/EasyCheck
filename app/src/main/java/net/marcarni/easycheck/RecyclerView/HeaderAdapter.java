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

// Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder



    // Provide a suitable constructor (depends on the kind of dataset)
    public HeaderAdapter(ArrayList<Header> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public HeaderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_header, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return  new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.headerCode.setText(mDataset.get(position).getHeaderCode());
        holder.responsableName.setText(mDataset.get(position).getResponsableName());
        holder.dni.setText(mDataset.get(position).getDni());
        holder.data.setText(mDataset.get(position).getData());
        holder.check.setText(mDataset.get(position).getCheck());
        String checkin = mDataset.get(position).getCheck();
        if (checkin.equalsIgnoreCase("0")) checkin="Check-In: No Realitzat"; else checkin="Check-In: Realitzat";
        // holder.check.setText(mDataset.get(position).getCheck());
        holder.checkText.setText(checkin);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // each data item is just a string in this case
        TextView headerCode;
        TextView responsableName;
        TextView dni, data;
        TextView check,checkText;
        View v;
        ViewHolder(View v) {
            super(v);

            headerCode = (TextView) v.findViewById(R.id.headerCode);
            responsableName = (TextView) v.findViewById(R.id.headerDetall);
            dni = (TextView) v.findViewById(R.id.dni);
            data = (TextView) v.findViewById(R.id.data);
            check = (TextView) v.findViewById(R.id.check);
            checkText = (TextView) v.findViewById(R.id.checkText);
            //check.setVisibility(View.GONE);
            v.setOnClickListener(this);

        }
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
                                        Log.d("proba", "onClick: "+responsableName.getText().toString().substring(5,14));

                                        String dni=responsableName.getText().toString().substring(5,14);
                                        DBInterface db=new DBInterface(v.getContext());
                                        db.obre();
                                        db.ActalitzaCheckInReserva(dni);
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
