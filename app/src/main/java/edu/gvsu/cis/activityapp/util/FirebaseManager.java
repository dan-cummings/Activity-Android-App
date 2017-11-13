package edu.gvsu.cis.activityapp.util;

import android.content.Context;

import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Kyle Flynn on 11/8/2017.
 */

public class FirebaseManager {

    private static FirebaseManager instance;

    private FirebaseApp mApp;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    public FirebaseManager() {}

    public static FirebaseManager getInstance() {
        if (instance == null) {
            instance = new FirebaseManager();
        }
        return instance;
    }

    public void init(Context context) {
        FirebaseApp.initializeApp(context);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
    }

    public void signOut() {
        mAuth.signOut();
    }

    public Task<AuthResult> registerUser(String email, String password) {
        return mAuth.createUserWithEmailAndPassword(email, password);
    }

    public Task<AuthResult> loginUser(String email, String password) {
        return mAuth.signInWithEmailAndPassword(email, password);
    }

    public FirebaseUser getUser() {
        return mAuth.getCurrentUser();
    }

}
