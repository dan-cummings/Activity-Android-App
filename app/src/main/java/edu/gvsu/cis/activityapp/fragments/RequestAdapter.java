package edu.gvsu.cis.activityapp.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import edu.gvsu.cis.activityapp.R;
import edu.gvsu.cis.activityapp.util.FirebaseManager;
import edu.gvsu.cis.activityapp.util.Request;

/**
 * Created by daniel on 12/7/17.
 */

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {

    private List<Request> requests;
    private FirebaseManager mFireManager;

    public RequestAdapter(List<Request> items) {
        this.requests = items;
        mFireManager = FirebaseManager.getInstance();
    }

    @Override
    public RequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.request_layout, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RequestViewHolder holder, int position) {
        holder.req = this.requests.get(position);
        holder.requestUser.setText(holder.req.getFrom());
        holder.groupName.setText(holder.req.getGroup());
        holder.accept.setOnClickListener((click) -> {
            updateDB(holder.req);
        });
        holder.deny.setOnClickListener((clickyclick) -> {
            removeRequest(holder.req);
        });
    }

    public void refresh(final List<Request> reqs) {
        requests = reqs;
        notifyDataSetChanged();
    }

    private void updateDB(Request req) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("Requests")
                .child(req.get_key())
                .removeValue();
        ref.child("Places")
                .child(req.getGroup())
                .child("members")
                .child(req.getTo())
                .setValue(true);
        ref.child("Users")
                .child(mFireManager.getUser().getUid())
                .child("groups")
                .child(req.getGroup())
                .setValue(true);
        ref.child("Users")
                .child(mFireManager.getUser().getUid())
                .child("chats")
                .child(req.getGroup())
                .setValue(true);
    }

    private void removeRequest(Request req) {
        FirebaseDatabase.getInstance()
                .getReference("Requests")
                .child(req.get_key())
                .removeValue();
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public class RequestViewHolder extends RecyclerView.ViewHolder {

        public TextView requestUser;
        public TextView groupName;
        public Button accept;
        public Button deny;
        public Request req;

        public RequestViewHolder(View view) {
            super(view);
            requestUser = (TextView) view.findViewById(R.id.userName);
            groupName = (TextView) view.findViewById(R.id.eventName);
            accept = (Button) view.findViewById(R.id.accept_event_button);
            deny = (Button) view.findViewById(R.id.remove_request);
        }
    }
}
