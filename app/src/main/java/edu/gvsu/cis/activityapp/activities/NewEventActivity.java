package edu.gvsu.cis.activityapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.parceler.Parcels;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.gvsu.cis.activityapp.R;
import edu.gvsu.cis.activityapp.util.PlaceEvent;

public class NewEventActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener{

    @BindView(R.id.eventName)
    EditText name;
    @BindView(R.id.dateSelect)
    TextView dateView;
    @BindView(R.id.timeSelect)
    TextView timeView;
    @BindView(R.id.privateEvent)
    CheckBox privateEvent;

    DateTime date;

    DateTime time;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
                Intent eventIntent = new Intent();
                PlaceEvent event = new PlaceEvent();
                event.setmName(name.getText().toString());
                event.setmOwner(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                event.setDate(fmt.print(date));
                event.setTime(fmt.print(time));
                //TODO add fields to event.
                Parcelable parcel = Parcels.wrap(event);
                eventIntent.putExtra("EVENT", parcel);
                setResult(RESULT_OK, eventIntent);
                finish();
            }
        });

        dateView.setOnClickListener((click) -> {
            selectDate();
        });

        timeView.setOnClickListener((click) -> {
            selectTime();
        });
    }

    private void selectDate() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH));
        dpd.setThemeDark(true);
        dpd.setVersion(DatePickerDialog.Version.VERSION_2);
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    private void selectTime() {
        Calendar now = Calendar.getInstance();
        TimePickerDialog dpd = TimePickerDialog.newInstance(this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                        false
        );
        dpd.setThemeDark(true);
        dpd.setVersion(TimePickerDialog.Version.VERSION_2);
        dpd.show(getFragmentManager(), "Timepickerdialog");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        date = new DateTime(year, monthOfYear+1,dayOfMonth,0,0);
        this.dateView.setText(
                date.getDayOfMonth() + "/" + date.getMonthOfYear() + "/" + date.getYear());

    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        time = new DateTime(0,1,1, hourOfDay, minute);
        if (time.getHourOfDay() > 12) {
            this.timeView.setText(time.getHourOfDay() % 12 + ":" + time.getMinuteOfHour() + " PM");
        } else {
            this.timeView.setText(time.getHourOfDay() + ":" + time.getMinuteOfHour() + " AM");
        }
    }
}
