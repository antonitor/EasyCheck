package net.marcarni.easycheck;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CalendarView;



public class CalendarActivity extends AppCompatActivity {

    private  CalendarView calendar = null;
    private Long date = null;
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);


        calendar = (CalendarView)findViewById(R.id.calendarView);
        date = calendar.getDate();

            calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int i, int i1, int i2)  {

                int currentapiVersion = android.os.Build.VERSION.SDK_INT;

                if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP){
                    // Per lollipop o versions posteriors
                    enviarData(i,i1,i2);
                } else{
                    // per versions anteriors a lollipop
                    if(calendar.getDate() != date) {
                        date = calendar.getDate();
                        enviarData(i,i1,i2);
                    }
                }
            }
            private void enviarData (int i, int i1, int i2){
                String data = i2 + "/"+ (i1+1) + "/"+i;  // dia/mes/any
                Intent intent = new Intent(CalendarActivity.this,DniActivity.class);
                intent.putExtra("DATA",data);
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });
    }

}
