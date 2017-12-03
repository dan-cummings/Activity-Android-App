package edu.gvsu.cis.activityapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.gvsu.cis.activityapp.R;
import edu.gvsu.cis.activityapp.util.FirebaseManager;
import edu.gvsu.cis.activityapp.util.User;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.text_register_email) AutoCompleteTextView mEmail;
    @BindView(R.id.text_register_name) EditText mFullName;
    @BindView(R.id.text_register_password) EditText mPassword;
    @BindView(R.id.text_register_conf_pass) EditText mConfirmPass;
    @BindView(R.id.text_register_error_output) TextView mErrorOutput;
    @BindView(R.id.btn_create_acct) Button mCreateAcct;
    @BindView(R.id.register_progressbar) ProgressBar mProgressBar;

    private FirebaseManager mFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // Set up the firebase singleton so we can register users.
        mFirebase = FirebaseManager.getInstance();

        //Sets up the register form
        ButterKnife.bind(this);

        // Setup our listeners
        mEmail.addTextChangedListener(getWatcher());
        mFullName.addTextChangedListener(getWatcher());
        mPassword.addTextChangedListener(getWatcher());
        mConfirmPass.addTextChangedListener(getWatcher());

        mErrorOutput.setVisibility(View.GONE);
        mCreateAcct.setEnabled(false);
    }

    private boolean updateErrorOutput() {
        if (!isValidEmail()) {
            mErrorOutput.setVisibility(View.VISIBLE);
            mErrorOutput.setText(R.string.error_invalid_email);
            mCreateAcct.setEnabled(false);
            return true;
        } else {
            mErrorOutput.setVisibility(View.GONE);
        }

        if (!isValidName()) {
            mCreateAcct.setEnabled(false);
            mErrorOutput.setVisibility(View.VISIBLE);
            mErrorOutput.setText(R.string.error_invalid_name);
            return true;
        } else {
            mErrorOutput.setVisibility(View.GONE);
        }

        if (!isValidPassword()) {
            mCreateAcct.setEnabled(false);
            mErrorOutput.setVisibility(View.VISIBLE);
            mErrorOutput.setText(R.string.error_invalid_password);
            return true;
        } else {
            mErrorOutput.setVisibility(View.GONE);
        }

        if (!passwordsMatch()) {
            mCreateAcct.setEnabled(false);
            mErrorOutput.setVisibility(View.VISIBLE);
            mErrorOutput.setText(R.string.error_password_no_match);
            return true;
        } else {
            mErrorOutput.setVisibility(View.GONE);
        }

        mCreateAcct.setEnabled(true);
        return true;
    }

    @OnClick(R.id.btn_create_acct)
    public void createAccount() {
        if (isValidPassword() && isValidEmail() && passwordsMatch() && isValidName()) {
            // Login and return for 'User' result from Firebase
            mProgressBar.setVisibility(View.VISIBLE);

            String email = mEmail.getText().toString();
            String pass = mPassword.getText().toString();
            Toast.makeText(this, "Creating account...", Toast.LENGTH_SHORT);
            mFirebase.registerUser(email, pass).addOnCompleteListener(this, (result) -> {
                Toast.makeText(this, "Finished " + result.isSuccessful(), Toast.LENGTH_SHORT);
                if (result.isSuccessful()) {
                    createUserProfile(result.getResult().getUser());
                } else {
                    mProgressBar.setVisibility(View.GONE);
                    result.getException().printStackTrace();
                    mErrorOutput.setVisibility(View.VISIBLE);
                    mErrorOutput.setText(result.getException().getMessage());
                }
            });

        } else {
            mErrorOutput.setVisibility(View.VISIBLE);
            mErrorOutput.setText(R.string.error_invalid_password);
        }
    }

    private void createUserProfile(FirebaseUser newUser) {
        String name = mFullName.getText().toString();
        UserProfileChangeRequest newProfile = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
        newUser.updateProfile(newProfile).addOnCompleteListener(this, (result) -> {
            mProgressBar.setVisibility(View.GONE);
            if (result.isSuccessful()) {
                User user = new User();
                user.setName(mFullName.getText().toString());
                user.setChats(new HashMap<>());
                user.setGroups(new HashMap<>());
                user.getGroups().put("hold", Boolean.TRUE);
                user.getChats().put("hold", Boolean.TRUE);
                FirebaseDatabase.getInstance().getReference("Users")
                        .child(newUser.getUid())
                        .setValue(user);
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else {
                result.getException().printStackTrace();
                mErrorOutput.setVisibility(View.VISIBLE);
                mErrorOutput.setText(result.getException().getMessage());
            }
        });
    }

    private boolean isValidName() {
        return mFullName.getText().toString().matches("([A-Z])\\w+ \\w+");
    }

    private boolean isValidPassword() {
        return mPassword.getText().length() > 6;
    }

    private boolean isValidEmail() {
        String email = mEmail.getText().toString();
        return email.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
    }

    private boolean passwordsMatch() {
        return mPassword.getText().toString().equals(mConfirmPass.getText().toString());
    }

    private TextWatcher getWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateErrorOutput();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

}