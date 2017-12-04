package edu.gvsu.cis.activityapp.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.gvsu.cis.activityapp.R;
import edu.gvsu.cis.activityapp.util.Chat;
import edu.gvsu.cis.activityapp.util.Message;

public class ChatActivity extends AppCompatActivity {

    @BindView(R.id.messageList)
    ListView messagelist;
    @BindView(R.id.sendActionButton)
    FloatingActionButton sendButton;
    @BindView(R.id.input)
    EditText messageText;

    FirebaseListAdapter<Message> adapter;

    String groupname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        Intent sent = getIntent();

        groupname = sent.getStringExtra("GROUP");

        sendButton.setOnClickListener((click) -> {
            sendMessage();
        });

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Messages")
                .child(groupname)
                .limitToLast(50);

        FirebaseListOptions<Message> options = new FirebaseListOptions.Builder<Message>()
                .setQuery(query, Message.class)
                .setLayout(R.layout.message_layout)
                .build();
        adapter = new FirebaseListAdapter<Message>(options) {
            @Override
            protected void populateView(View v, Message model, int position) {
                TextView message = (TextView) v.findViewById(R.id.messageText);
                TextView sender = (TextView) v.findViewById(R.id.userLabel);
                TextView time = (TextView) v.findViewById(R.id.timeLabel);

                message.setText(model.getMessage());
                sender.setText(model.getUser());
                time.setText(model.getTime());
            }
        };

        messagelist.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        adapter.startListening();
        super.onStart();
    }

    @Override
    protected void onStop() {
        adapter.stopListening();
        super.onStop();
    }

    private void sendMessage() {
        //TODO place message in event list.
        if (!this.messageText.getText().toString().isEmpty()) {
            String user = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
            FirebaseDatabase.getInstance()
                    .getReference("Messages")
                    .child(groupname)
                    .push()
                    .setValue(new Message(this.messageText.getText().toString(), user));
            Map<String, Object> values;

        }
    }
}
