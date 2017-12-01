package edu.gvsu.cis.activityapp.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.gvsu.cis.activityapp.R;
import edu.gvsu.cis.activityapp.util.FirebaseManager;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.text_login_email) AutoCompleteTextView mEmail;
    @BindView(R.id.text_login_password) EditText mPassword;
    @BindView(R.id.text_login_message) TextView mLoginMessage;
    @BindView(R.id.btn_login) Button mLogin;
    @BindView(R.id.login_progressbar) ProgressBar mLoginProgress;
    @BindView(R.id.btn_register) Button mRegister;

    private FirebaseManager mFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        ButterKnife.bind(this);
        mLoginMessage.setVisibility(View.GONE);
        mLoginProgress.setVisibility(View.GONE);

        mEmail.addTextChangedListener(getWatcher());
        mPassword.addTextChangedListener(getWatcher());

        mRegister.setOnClickListener((click) -> {
            Intent i = new Intent(this, RegisterActivity.class);
            startActivity(i);
        });

        mFirebase = FirebaseManager.getInstance();
    }

    @OnClick(R.id.btn_login)
    public void login() {
        mLoginProgress.setVisibility(View.VISIBLE);

        String email = mEmail.getText().toString();
        String pass = mPassword.getText().toString();
        mFirebase.loginUser(email, pass).addOnCompleteListener(this, (result) -> {
            mLoginProgress.setVisibility(View.GONE);
            if (result.isSuccessful()) {

                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else {
                result.getException().printStackTrace();
                mLoginMessage.setVisibility(View.VISIBLE);
                mLoginMessage.setText(result.getException().getMessage());
            }
        });
    }

    private boolean updateErrorOutput() {
        if (!isValidEmail()) {
            mLoginMessage.setVisibility(View.VISIBLE);
            mLoginMessage.setText(R.string.error_invalid_email);
            mLogin.setEnabled(false);
            return true;
        } else {
            mLoginMessage.setVisibility(View.GONE);
        }

        if (!isValidPassword()) {
            mLogin.setEnabled(false);
            mLoginMessage.setVisibility(View.VISIBLE);
            mLoginMessage.setText(R.string.error_invalid_password);
            return true;
        } else {
            mLoginMessage.setVisibility(View.GONE);
        }

        mLogin.setEnabled(true);

        return true;
    }

    private boolean isValidPassword() {
        return mPassword.getText().length() > 0;
    }

    private boolean isValidEmail() {
        String email = mEmail.getText().toString();
        return email.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
    }

    private TextWatcher getWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateErrorOutput();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };
    }

}

