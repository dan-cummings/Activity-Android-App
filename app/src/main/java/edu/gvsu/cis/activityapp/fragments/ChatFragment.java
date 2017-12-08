package edu.gvsu.cis.activityapp.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Random;

import edu.gvsu.cis.activityapp.R;
import edu.gvsu.cis.activityapp.util.Chat;
import edu.gvsu.cis.activityapp.util.FirebaseManager;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ChatFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private FirebaseRecyclerAdapter<Chat, ChatHolder> adapter;
    private FirebaseManager mFirebase;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ChatFragment() { }



    @SuppressWarnings("unused")
    public static ChatFragment newInstance(int columnCount) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (this.mFirebase.getUser() != null) {
            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (this.mFirebase.getUser() != null){
            adapter.stopListening();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        this.mFirebase = FirebaseManager.getInstance();

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_list, container, false);

        if (this.mFirebase.getUser() != null) {
            // Set the adapter
            if (view instanceof RecyclerView) {

                Query keyRef = FirebaseDatabase.getInstance()
                        .getReference("Users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("chats");
                DatabaseReference valRef = FirebaseDatabase.getInstance()
                        .getReference("Chats");
                FirebaseRecyclerOptions<Chat> options = new FirebaseRecyclerOptions.Builder<Chat>()
                        .setIndexedQuery(keyRef, valRef, Chat.class)
                        .build();
                Context context = view.getContext();
                RecyclerView recyclerView = (RecyclerView) view;
                if (mColumnCount <= 1) {
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                } else {
                    recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
                }
                adapter = new FirebaseRecyclerAdapter<Chat, ChatHolder>(options) {
                    @Override
                    protected void onBindViewHolder(ChatHolder holder, int position, Chat model) {
                        holder.mItem = model;
                        holder.mTitleView.setText(model.getEventName());
                        holder.mMessageSentView.setText(model.getLastMessage());
                        holder.mSenderName.setText(model.getSender());

                        holder.mView.setOnClickListener((click) -> {
                            if (null != mListener) {
                                // Notify the active callbacks interface (the activity, if the
                                // fragment is attached to one) that an item has been selected.
                                mListener.onListFragmentInteraction(holder.mItem);
                            }

                        });

                        Random rnd = new Random();
                        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                        holder.eventPhoto.setBackgroundColor(color);
                    }

                    @Override
                    public ChatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.fragment_chat, parent, false);
                        return new ChatHolder(view);
                    }
                };
                recyclerView.setAdapter(adapter);
            }
        }

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Chat item);
    }

    public class ChatHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitleView;
        public final TextView mMessageSentView;
        public final TextView mSenderName;
        public final ProgressBar progressBar;
        public final ImageView eventPhoto;
        public Chat mItem;

        public ChatHolder(View view) {
            super(view);
            mView = view;
            mTitleView = (TextView) view.findViewById(R.id.title_view);
            mMessageSentView = (TextView) view.findViewById(R.id.last_message_view);
            mSenderName = (TextView) view.findViewById(R.id.sender_name);
            progressBar = (ProgressBar) view.findViewById(R.id.chatPhotoBar);
            eventPhoto = (ImageView) view.findViewById(R.id.chatEventPhoto);
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mMessageSentView.getText() + "'";
        }
    }
}
