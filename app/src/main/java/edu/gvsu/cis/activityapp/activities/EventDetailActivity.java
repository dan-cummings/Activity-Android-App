package edu.gvsu.cis.activityapp.activities;

import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.gvsu.cis.activityapp.R;
import edu.gvsu.cis.activityapp.util.PlaceEvent;

public class EventDetailActivity extends AppCompatActivity {

    @BindView(R.id.event_photo)
    ImageView eventPhoto;

    @BindView(R.id.txtEventName)
    TextView eventName;

    @BindView(R.id.txtEventDetail1)
    TextView eventDetail1;

    @BindView(R.id.txtEventDetail2)
    TextView eventDetail2;

    @BindView(R.id.txtEventDetail3)
    TextView eventDetail3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        ButterKnife.bind(this);

        PlaceEvent event = Parcels.unwrap(getIntent().getParcelableExtra("EVENT"));

        eventName.setText(event.getmName());
    }
}
