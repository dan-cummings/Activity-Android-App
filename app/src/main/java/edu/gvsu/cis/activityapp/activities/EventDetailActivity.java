package edu.gvsu.cis.activityapp.activities;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlacePhotoMetadata;

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

    @BindView(R.id.txtEventDetail1)
    TextView eventDetail1;

    @BindView(R.id.txtEventDetail2)
    TextView eventDetail2;

    @BindView(R.id.txtEventDetail3)
    TextView eventDetail3;

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
        eventDetail1.setText("");
        eventDetail2.setText("");
        eventDetail3.setText(String.format(getResources().getString(R.string.txt_event_detail_owner), event.getOwner()));

        if (event.getPlaceId() != null) {
            mapManager.getPlaceByID(event.getPlaceId()).addOnCompleteListener((task) -> {
                if (task.isSuccessful() && task.getResult().getCount() > 0) {
                    eventPlace = task.getResult().get(0);
                    eventDetail1.setPaintFlags(eventDetail1.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    eventDetail1.setText(eventPlace.getWebsiteUri().toString());
                    eventDetail2.setText(eventPlace.getAddress());
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
               }
            });
        }

    }

    @OnClick(R.id.txtEventDetail1)
    public void openWebView() {
        if (eventDetail1.getText().toString().length() > 1) {
            Intent intent = new Intent(this, WebViewActivity.class);
            intent.putExtra("URL", eventDetail1.getText());
            startActivity(intent);
        }
    }

}
