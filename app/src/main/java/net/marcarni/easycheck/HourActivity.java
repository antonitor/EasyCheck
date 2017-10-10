package net.marcarni.easycheck;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TimePicker;

public class HourActivity extends AppCompatActivity {
TimePicker hora;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hour);
        hora= (TimePicker)findViewById(R.id.timePicker);
      
    }
}
