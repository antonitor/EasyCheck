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
/**
 * @author Carlos Alberto Castro Cañabate
 */
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

    /**
     * @author Carlos Alberto Castro Cañabate
     * Mètode onCreate de la classe DniActivity
     * @param savedInstanceState Buncle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dni);
        // Relacionems els Views declarats amb els del layout:
        textView = (TextView) findViewById(R.id.textView);
        editTextDni = (EditText) findViewById(R.id.editTextDni);
        textViewData = (TextView) findViewById(R.id.textViewData);
        editTextLocaltizador = (EditText) findViewById(R.id.editTextLocalitzador);
        buttonCheckIn = (Button) findViewById(R.id.buttonCheckIn);
        buttonData = (ImageButton) findViewById(R.id.imageButton);
        // Rebem l'intent i agafem les dades oportunes per saber si mostrarem la UI per DNI o Localitzador
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            cadena = (String) bundle.get("DATO");
            // Si inicem la UI DNI llavors ocultem el editText del localitzador
            if (cadena.equalsIgnoreCase("DNI")){
                editTextLocaltizador.setVisibility(View.GONE);
            } else {
                // Si inicem la UI de Localitzador ocultem els views que mostrem a DNI
                buttonData.setVisibility(View.GONE);
                editTextDni.setVisibility(View.GONE);
                textViewData.setVisibility(View.GONE);
            }
            textView.setText(cadena);
            String data = intent.getStringExtra("DATA");
            textViewData.setText(data);
        }
        // implementem la gestió d'esdeveniments per al botó check-in:
        buttonCheckIn.setOnClickListener(new View.OnClickListener() {
            /**
             * @author Carlos Alberto Castro Cañabate
             * Mètode per gestionar l'esdeveniment onClick del view buttonCheckIn
             * @param view buttonChenckIn
             */
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DniActivity.this,DetallActivity.class);
                // Si realitzem la busqueda de reserva mitjançant la UI DNI:
                if (cadena.equalsIgnoreCase("DNI")){
                    // Si hi ha introducció de DNI
                    if (editTextDni.getText().toString().length() > 0)
                        // I si la introducció te format de DNI vàlid
                        if (validaDni.validarDni(editTextDni.getText().toString())){
                            // pasem el text del dni introduit al intent
                            intent.putExtra("DNI",editTextDni.getText().toString().toUpperCase());
                            startActivity(intent);
                        }
                        // Si el format del DNI no és un dni vàlid:
                        else  Toast.makeText(DniActivity.this, "Format DNI invàlid", Toast.LENGTH_LONG).show();
                    // Si hi ha data introduida, pasem la data al intent.
                    if (textViewData.getText().length() > 0){
                        intent.putExtra("DATA",textViewData.getText());
                        startActivity(intent);
                        // Si no hi ha data, ni DNI introduït:
                    } else if (editTextDni.getText().toString().length() == 0) Toast.makeText(DniActivity.this, "Introdueix DNI!", Toast.LENGTH_LONG).show();
                    // Si realitzem la busqueda de reserva mitjançant la UI LOCALITZADOR:
                } else if(cadena.equalsIgnoreCase("LOCALITZADOR")){
                    // Si hem introduït qualsevol localitzador:
                    if (editTextLocaltizador.getText().toString().length() > 0){
                        // pasem el text introduït al localitzador mitjançant el intent
                        intent.putExtra("LOCALITZADOR",editTextLocaltizador.getText().toString());
                        startActivity(intent);
                        // Si no hem introduït cap localitzador:
                    } else {
                        Toast.makeText(DniActivity.this, "Introdueix Localitzador!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        // implementem la gestió d'esdeveniments per al botó data:
        buttonData.setOnClickListener(new View.OnClickListener(){
            /**
             * @author Carlos Alberto Castro Cañabate
             * Metode per gestionar l'esdeveniment onClick del view buttonData
             * @param view buttonData
             */
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
                // Creem un DatePicker i guardem per el dia, mes i any:
                DatePickerDialog mDatePicker;
                mDatePicker = new DatePickerDialog(DniActivity.this, new DatePickerDialog.OnDateSetListener() {
                    /**
                     * Mètode per seleccionar una data del DatePickerDialog
                     * @param datepicker DatePicker
                     * @param selectedyear any selccionat
                     * @param selectedmonth mes seleccionat
                     * @param selectedday dia seleccionat
                     */
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
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
}
