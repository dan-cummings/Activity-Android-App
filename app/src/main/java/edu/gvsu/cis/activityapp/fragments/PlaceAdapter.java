package edu.gvsu.cis.activityapp.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.gvsu.cis.activityapp.R;
import edu.gvsu.cis.activityapp.fragments.PlaceFragment.OnListFragmentInteractionListener;
import edu.gvsu.cis.activityapp.util.PlaceEvent;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceEvent} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder> {

    private final List<PlaceEvent> mValues;
    private final OnListFragmentInteractionListener mListener;

    public PlaceAdapter(List<PlaceEvent> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_place, parent, false);
        return new ViewHolder(view);
    }

    public void updateFrom(List<PlaceEvent> events) {
        mValues.clear();
        mValues.addAll(events);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        holder.nameView.setText(mValues.get(position).getmName());
        holder.userView.setText(mValues.get(position).getmOwner());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView nameView;
        public final TextView userView;
        public PlaceEvent mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            nameView = (TextView) view.findViewById(R.id.event_name);
            userView = (TextView) view.findViewById(R.id.event_user_name);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + userView.getText() + "'";
        }
    }
}
