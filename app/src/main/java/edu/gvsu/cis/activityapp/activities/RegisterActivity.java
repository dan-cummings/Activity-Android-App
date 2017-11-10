package edu.gvsu.cis.activityapp.activities;

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

import edu.gvsu.cis.activityapp.R;
import edu.gvsu.cis.activityapp.util.FirebaseManager;

public class RegisterActivity extends AppCompatActivity {

    private AutoCompleteTextView mEmail;
    private EditText mPassword;
    private EditText mConfirmPass;
    private TextView mErrorOutput;
    private Button mCreateAcct;
    private ProgressBar mProgressBar;

    private FirebaseManager mFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // Set up the firebase singleton so we can register users.
        mFirebase = FirebaseManager.getInstance();

        // Set up the register form.
        mEmail = (AutoCompleteTextView) findViewById(R.id.text_register_email);
        mPassword = (EditText) findViewById(R.id.text_register_password);
        mConfirmPass = (EditText) findViewById(R.id.text_register_conf_pass);
        mCreateAcct = (Button) findViewById(R.id.btn_create_acct);
        mErrorOutput = (TextView) findViewById(R.id.text_register_error_output);
        mProgressBar = (ProgressBar) findViewById(R.id.register_progressbar);

        // Setup our listeners
        mEmail.addTextChangedListener(getWatcher());
        mPassword.addTextChangedListener(getWatcher());
        mConfirmPass.addTextChangedListener(getWatcher());
        mCreateAcct.setOnClickListener((click) -> createAccount());

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

    public void createAccount() {
        if (isValidPassword() && isValidEmail() && passwordsMatch()) {
            // Login and return for 'User' result from Firebase
            Toast.makeText(this, "Validated!", Toast.LENGTH_SHORT).show();
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mErrorOutput.setVisibility(View.VISIBLE);
            mErrorOutput.setText(R.string.error_invalid_password);
        }
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