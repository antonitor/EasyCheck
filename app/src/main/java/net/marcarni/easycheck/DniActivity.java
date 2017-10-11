package net.marcarni.easycheck;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import net.marcarni.easycheck.eines.ValidaDNI;
import net.marcarni.easycheck.settings.MenuAppCompatActivity;

import java.util.Calendar;

public class DniActivity extends MenuAppCompatActivity {
    static final int ACTIVITAT_DATA = 1;
    TextView textView;
    EditText editTextDni;
    TextView textViewData;
    EditText editTextLocaltizador;
    Button buttonCheckIn;
    ImageButton buttonData;
    String cadena = null;
    ValidaDNI validaDni = new ValidaDNI();
    String data_="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dni);

        textView = (TextView) findViewById(R.id.textView);
        editTextDni = (EditText) findViewById(R.id.editTextDni);
        textViewData = (TextView) findViewById(R.id.textViewData);
        editTextLocaltizador = (EditText) findViewById(R.id.editTextLocalitzador);
        buttonCheckIn = (Button) findViewById(R.id.buttonCheckIn);
        buttonData = (ImageButton) findViewById(R.id.imageButton);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            cadena = (String) bundle.get("DATO");
            if (cadena.equalsIgnoreCase("DNI")){
                editTextLocaltizador.setVisibility(View.GONE);
            } else {
                buttonData.setVisibility(View.GONE);
                editTextDni.setVisibility(View.GONE);
                textViewData.setVisibility(View.GONE);
            }
            textView.setText(cadena);
            String data = intent.getStringExtra("DATA");
            textViewData.setText(data);
        }
        buttonCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DniActivity.this,DetallActivity.class);
                if (cadena.equalsIgnoreCase("DNI")){
                    if (editTextDni.getText().toString().length() > 0)
                        if (validaDni.validarDni(editTextDni.getText().toString())){
                            intent.putExtra("DNI",editTextDni.getText().toString().toUpperCase());
                            startActivity(intent);
                        }
                        else  Toast.makeText(DniActivity.this, "Format DNI invàlid", Toast.LENGTH_LONG).show();
                    if (textViewData.getText().length() > 0){
                        intent.putExtra("DATA",textViewData.getText());
                        startActivity(intent);
                    } else if (editTextDni.getText().toString().length() == 0) Toast.makeText(DniActivity.this, "Introdueix DNI!", Toast.LENGTH_LONG).show();

                } else if(cadena.equalsIgnoreCase("LOCALITZADOR")){
                    if (editTextLocaltizador.getText().toString().length() > 0){
                        intent.putExtra("LOCALITZADOR",editTextLocaltizador.getText().toString());
                        startActivity(intent);
                    } else {
                        Toast.makeText(DniActivity.this, "Introdueix Localitzador!", Toast.LENGTH_LONG).show();
                    }

                }


            }
        });
        buttonData.setOnClickListener(new View.OnClickListener(){
  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            /////////////////////////////////////////////////////////////////////////////////////////
            //////////////              PICKER AÑADIDO      //////////////////////////////////////////
            //////////////////////////////////////////////////////////////////////////////////////////
            //////////////////////////////////////////////////////////////////////////////////////////

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {

                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker;
                mDatePicker = new DatePickerDialog(DniActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub
                    /*      Your code   to get date and time    */
                        selectedmonth = selectedmonth + 1;
                       data_="" + selectedday + "/" + selectedmonth + "/" + selectedyear;
                        textViewData.setText(data_);
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Selecciona Data");
                mDatePicker.show();
            }
        });

    }





               /* Intent intent = new Intent (DniActivity.this,CalendarActivity.class);
                startActivityForResult(intent, 1); // L'activitat retornarà un resultat.*/


   /* public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        //Comprovem a quina activitat correspon
        if(requestCode == ACTIVITAT_DATA)
        {
            //Si l'activitat ha acabat correctament
            if(resultCode == RESULT_OK)
            {
                //Recollim el text escollit al calendar (format data)
                String data = intent.getStringExtra("DATA");
                textViewData.setText(data);
            }
        }
    }*/





}
