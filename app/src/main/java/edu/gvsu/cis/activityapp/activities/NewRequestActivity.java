package edu.gvsu.cis.activityapp.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.gvsu.cis.activityapp.R;
import edu.gvsu.cis.activityapp.fragments.PlaceFragment;
import edu.gvsu.cis.activityapp.util.PlaceEvent;
import edu.gvsu.cis.activityapp.util.User;

public class NewRequestActivity extends AppCompatActivity implements PlaceFragment.OnListFragmentInteractionListener{

    @BindView(R.id.add_floating_button)
    FloatingActionButton addButton;
    @BindView(R.id.invite_name)
    EditText username;

    List<String> names;

    PlaceEvent selectedEvent;

    boolean validName;

    private ValueEventListener valListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot child : dataSnapshot.getChildren()) {
                names.add(child.getValue(User.class).getName());
            }

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_request);
        ButterKnife.bind(this);
        validName = false;
        FirebaseDatabase.getInstance()
                .getReference("Users")
                .addListenerForSingleValueEvent(valListener);

        names = new ArrayList<>();

        addButton.setOnClickListener((clicked) -> {
            if (names.contains(username.getText().toString()) && selectedEvent != null) {
                Intent data = new Intent();
                data.putExtra("NAME", username.getText().toString());
                data.putExtra("GROUP", selectedEvent.getmName());
                setResult(RESULT_OK, data);
                finish();
            } else {
                Toast.makeText(this.getBaseContext(), "User not found.", Toast.LENGTH_SHORT).show();
            }
        });
    }




    @Override
    public void onListFragmentInteraction(PlaceEvent item) {
        selectedEvent = item;
    }
}
