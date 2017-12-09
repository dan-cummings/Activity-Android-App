package edu.gvsu.cis.activityapp.activities;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlacePhotoMetadata;

import org.joda.time.DateTime;
import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.gvsu.cis.activityapp.R;
import edu.gvsu.cis.activityapp.util.MapManager;
import edu.gvsu.cis.activityapp.util.PlaceEvent;

public class EventDetailActivity extends AppCompatActivity {

    @BindView(R.id.event_photo)
    ImageView eventPhoto;

    @BindView(R.id.txtEventName)
    TextView eventName;

    @BindView(R.id.txtEventWebsite)
    TextView eventWebsite;

    @BindView(R.id.txtEventAddress)
    TextView eventAddress;

    @BindView(R.id.txtEventCreator)
    TextView eventDetail3;

    @BindView(R.id.txtTimeView)
    TextView eventTime;

    private MapManager mapManager;
    private Place eventPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        ButterKnife.bind(this);

        PlaceEvent event = Parcels.unwrap(getIntent().getParcelableExtra("EVENT"));
        mapManager = MapManager.getInstance();

        eventName.setText(event.getName());
        eventWebsite.setText("");
        eventAddress.setText("");
        eventDetail3.setText(String.format(getResources().getString(R.string.txt_event_detail_owner), event.getOwner()));
        DateTime day = DateTime.parse(event.getDate());
        DateTime hour = DateTime.parse(event.getTime());
        System.out.println(hour.toString());
        System.out.println(day.toString());
        eventTime.setText(String.format(getResources().getString(R.string.details_time_format),
                day.getMonthOfYear(), day.getDayOfMonth(), day.getYear(),
                hour.getHourOfDay(), hour.getMinuteOfHour()));


        if (event.getPlaceId() != null) {
            mapManager.getPlaceByID(event.getPlaceId()).addOnCompleteListener((task) -> {
                if (task.isSuccessful() && task.getResult().getCount() > 0) {
                    eventPlace = task.getResult().get(0);
                    eventWebsite.setPaintFlags(eventWebsite.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    eventWebsite.setText(eventPlace.getWebsiteUri().toString());
                    eventAddress.setText(eventPlace.getAddress());
                }
            });
            mapManager.getPlacePhoto(event.getPlaceId()).addOnCompleteListener((task) -> {
               if (task.isSuccessful() && task.getResult().getPhotoMetadata().getCount() > 0) {
                   PlacePhotoMetadata metadata = task.getResult().getPhotoMetadata().get(0);
                   mapManager.getBitmapPhoto(metadata).addOnCompleteListener((photoTask) -> {
                       if (photoTask.isSuccessful()) {
                           eventPhoto.setScaleType(ImageView.ScaleType.FIT_XY);
                           eventPhoto.setImageBitmap(photoTask.getResult().getBitmap());
                       }
                   });
                   task.getResult().getPhotoMetadata().release();
               }
            });
        }

    }

    @OnClick(R.id.txtEventWebsite)
    public void openWebView() {
        if (eventWebsite.getText().toString().length() > 1) {
            Intent intent = new Intent(this, WebViewActivity.class);
            intent.putExtra("URL", eventWebsite.getText());
            startActivity(intent);
        }
    }

    @OnClick(R.id.directions_button)
    public void getDirections() {
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + eventPlace.getAddress()
                .toString().replace(' ', '+') + "&avoid=tf&mode=d");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }
}
