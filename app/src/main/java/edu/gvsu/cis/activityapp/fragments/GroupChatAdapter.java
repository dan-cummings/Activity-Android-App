package edu.gvsu.cis.activityapp.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.gvsu.cis.activityapp.R;
import edu.gvsu.cis.activityapp.fragments.ChatFragment.OnListFragmentInteractionListener;
import edu.gvsu.cis.activityapp.util.ChatContent.Chat;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Chat} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class GroupChatAdapter extends RecyclerView.Adapter<GroupChatAdapter.ViewHolder> {

    private final List<Chat> mValues;
    private final OnListFragmentInteractionListener mListener;

    public GroupChatAdapter(List<Chat> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_chat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mTitleView.setText(mValues.get(position).getmEventName());
        holder.mMessageSentView.setText(mValues.get(position).getmLastMessage());
        holder.mSenderName.setText(mValues.get(position).getmSender());

        holder.mView.setOnClickListener((click) -> {
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener.onListFragmentInteraction(holder.mItem);
            }

        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    /**
     * View holder for chat items.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitleView;
        public final TextView mMessageSentView;
        public final TextView mSenderName;
        public Chat mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = (TextView) view.findViewById(R.id.title_view);
            mMessageSentView = (TextView) view.findViewById(R.id.last_message_view);
            mSenderName = (TextView) view.findViewById(R.id.sender_name);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mMessageSentView.getText() + "'";
        }
    }
}
