package edu.gvsu.cis.activityapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.gvsu.cis.activityapp.activities.NewRequestActivity;
import edu.gvsu.cis.activityapp.util.Request;
import edu.gvsu.cis.activityapp.R;
import edu.gvsu.cis.activityapp.util.FirebaseManager;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RequestFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RequestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RequestFragment extends Fragment {

    private static int NEW_EVENT_INVITE = 1;

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private RequestAdapter adapter;
    private DatabaseReference reqRef;
    private List<Request> items;

    private ValueEventListener valListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            adapter.refresh(items);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Request req = dataSnapshot.getValue(Request.class);
            if (req != null && req.getTo().equals(mFirebase.getUser().getDisplayName())) {
                req.set_key(dataSnapshot.getKey());
                items.add(req);
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            Request req =  dataSnapshot.getValue(Request.class);
            List<Request> requests = new ArrayList<>();
            for (Request r : items) {
                if (!r.get_key().equals(dataSnapshot.getKey())) {
                    requests.add(r);
                }
            }
            items = requests;
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    private FirebaseManager mFirebase;

    public RequestFragment() {
        items = new ArrayList<>();
    }


    public static RequestFragment newInstance(int columnCount) {
        RequestFragment fragment = new RequestFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mFirebase.getUser() != null) {
            reqRef.addValueEventListener(valListener);
            reqRef.addChildEventListener(childEventListener);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mFirebase.getUser() != null) {
            reqRef.removeEventListener(valListener);
            reqRef.removeEventListener(childEventListener);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebase = FirebaseManager.getInstance();
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request, container, false);
        Button inviteButton = (Button) view.findViewById(R.id.invite_button);
        inviteButton.setOnClickListener((clickerclick) -> {
            Intent intent = new Intent(getContext(), NewRequestActivity.class);
            getActivity().startActivityForResult(intent, NEW_EVENT_INVITE);
        });
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.requestList);
        // Inflate the layout for this fragment
        if (mFirebase.getUser() != null) {
            if (rv != null) {
                reqRef = FirebaseDatabase.getInstance().getReference("Requests");
                reqRef.addValueEventListener(valListener);
                reqRef.addChildEventListener(childEventListener);
                adapter = new RequestAdapter(items);
                rv.setAdapter(adapter);
            }

        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
