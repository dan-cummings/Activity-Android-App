package edu.gvsu.cis.activityapp.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.gvsu.cis.activityapp.R;
import edu.gvsu.cis.activityapp.fragments.ChatFragment;
import edu.gvsu.cis.activityapp.fragments.CustomFragmentPageAdapter;
import edu.gvsu.cis.activityapp.util.Chat;
import edu.gvsu.cis.activityapp.util.MapManager;
import edu.gvsu.cis.activityapp.fragments.PlaceFragment;
import edu.gvsu.cis.activityapp.util.PlaceEvent;
import edu.gvsu.cis.activityapp.util.FirebaseManager;
import edu.gvsu.cis.activityapp.util.Message;
import edu.gvsu.cis.activityapp.util.User;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, PlaceFragment.OnListFragmentInteractionListener, ChatFragment.OnListFragmentInteractionListener {

    private MapManager mMapManager;
    private FirebaseManager mFirebase;
    private FirebaseUser mUser;

    private static int NEW_EVENT_REQUEST = 0;

    @BindView(R.id.nav_view) NavigationView drawerView;
    private TextView userName;
    private TextView userEmail;

    User userData;

    private boolean mLocationPermissionGranted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // All of these lines of code come default when creating a navigation drawer activity.
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Menu");
        setSupportActionBar(toolbar);

        // Initialize our GoogleAPIClient for map and location services.
        mMapManager = MapManager.getInstance();
        mMapManager.init(this);

        // Initialize our firebase singleton.
        mFirebase = FirebaseManager.getInstance();
        mFirebase.init(this);

        // Handle when the drawer opens/closes
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        drawerView.setNavigationItemSelectedListener(this);
        View headerView = drawerView.getHeaderView(0);
        userEmail = (TextView) headerView.findViewById(R.id.user_email);
        userName = (TextView) headerView.findViewById(R.id.user_name);

        // These lines of code designate the tab message_layout
        ViewPager vp_pages = (ViewPager) findViewById(R.id.vp_pages);
        PagerAdapter pagerAdapter = new CustomFragmentPageAdapter(getSupportFragmentManager());
        vp_pages.setAdapter(pagerAdapter);
        vp_pages.setOffscreenPageLimit(1);
        vp_pages.setCurrentItem(0,true);

        //Added action button for adding an event.
        FloatingActionButton addEvent = (FloatingActionButton) findViewById(R.id.addEventFab);
        addEvent.setOnClickListener((click) -> {
            Intent newEvent = new Intent(this, NewEventActivity.class);
            startActivityForResult(newEvent, NEW_EVENT_REQUEST);
        });

        TabLayout tbl_pages = (TabLayout) findViewById(R.id.tbl_pages);
        tbl_pages.setupWithViewPager(vp_pages);

        if (mFirebase.getUser() != null) {
            FirebaseDatabase.getInstance()
                    .getReference("Users")
                    .child(FirebaseAuth.getInstance().getUid())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            userData = dataSnapshot.getValue(User.class);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if the app has location permissions
        getLocationPermission();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Check if the app has location permissions.
        getLocationPermission();
        checkIfUserExists();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_login) {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_logout) {
            mFirebase.signOut();
            checkIfUserExists();
        } else if (id == R.id.nav_settings) {
            // Settings activity
        } else if (id == R.id.nav_about) {
            // Start about activity
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        mMapManager.setLocEnabled(false);
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Location Services Enabled", Toast.LENGTH_SHORT).show();
                    mLocationPermissionGranted = true;
                    mMapManager.setLocEnabled(true);
                }
            }
        }
    //updateLocationUI();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == NEW_EVENT_REQUEST) {
                Parcelable parcel = data.getParcelableExtra("EVENT");
                PlaceEvent event = Parcels.unwrap(parcel);
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                event.getMembers().put(user.getDisplayName(), true);
                event.setmOwner(user.getDisplayName());
                String initMessage = "Welcome to my event.";
                Chat newChat = new Chat(event.getmName(), initMessage, event.getmOwner());
                newChat.getMembers().put(event.getmOwner(), Boolean.TRUE);
                Message newMessage = new Message(initMessage, event.getmOwner());
                //Makes these changes to the database.
                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                rootRef.child("Messages")
                        .child(event.getmName())
                        .push()
                        .setValue(newMessage);
                rootRef.child("Chats")
                        .child(event.getmName())
                        .setValue(newChat);
                rootRef.child("Places")
                        .child(event.getmName())
                        .setValue(event);
                userData.getGroups().put(event.getmName(), Boolean.TRUE);
                userData.getChats().put(event.getmName(), Boolean.TRUE);
                rootRef.child("Users")
                        .child(user.getUid())
                        .setValue(userData);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void checkIfUserExists() {
        mUser = mFirebase.getUser();

        if (mUser != null) {
            userName.setText(mUser.getDisplayName());
            userEmail.setText(mUser.getEmail());

            drawerView.getMenu().getItem(0).setVisible(false);
            drawerView.getMenu().getItem(1).setVisible(true);
        } else {
            userName.setText("");
            userEmail.setText("You are not logged in.");
            drawerView.getMenu().getItem(0).setVisible(true);
            drawerView.getMenu().getItem(1).setVisible(false);
        }
    }

    // Handles location permissions. If a request is made, onRequestPermissionsResult is called.
    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            mMapManager.setLocEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    public MapManager getMapManager() {
        return this.mMapManager;
    }

    @Override
    public void onListFragmentInteraction(PlaceEvent item) {
        //TODO add details activity
    }

    @Override
    public void onListFragmentInteraction(Chat item) {
        Intent chatIntent = new Intent(this, ChatActivity.class);
        chatIntent.putExtra("GROUP", item.getEventName());
        startActivity(chatIntent);
    }
}
