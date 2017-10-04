package net.marcarni.easycheck;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.CalendarView;

public class CalendarActivity extends AppCompatActivity implements CalendarView.OnDateChangeListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(this);
    }

    @Override
    public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
        String data = i2 + "/"+ (i1+1) + "/"+i;  // dia/mes/any
        Intent intent = new Intent(CalendarActivity.this,DniActivity.class);
        intent.putExtra("DATA",data);
        setResult(Activity.RESULT_OK,intent);
        finish();
    }

}
