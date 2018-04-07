package com.hackdtu.healthhistory.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hackdtu.healthhistory.FirebaseReference;
import com.hackdtu.healthhistory.R;
import com.hackdtu.healthhistory.model.User;
import com.hackdtu.healthhistory.utils.SuperPrefs;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private GoogleSignInClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);

        authInitialisation();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

    public void authInitialisation() {
        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = GoogleSignIn.getClient(this, gso);
    }

    private void signIn() {
        Intent signInIntent =  mGoogleApiClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            GoogleSignInAccount account = null;
            try {
                progressStart();
                account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                e.printStackTrace();
                Log.e(TAG, "signInResult:failed code=" + e.getStatusCode());
                Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Log.e(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            login(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void login(final FirebaseUser currentUser) {
        FirebaseReference.userReference.child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    //Existing User, retrieve data
                    recoverUserFromDatabase(currentUser);
                }
                else {
                    //Create new user in firebase database
                    createNewUser(currentUser);
                }
                FirebaseReference.userReference.child(currentUser.getUid()).removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void recoverUserFromDatabase(final FirebaseUser firebaseUser) {
        getFirebaseUserReference(firebaseUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG, "Existing user");
                User currUser = dataSnapshot.getValue(User.class);
                saveUserDetailsToPref(currUser);
                progressStop();
                updateUI(firebaseUser);
                getFirebaseUserReference(firebaseUser).removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    DatabaseReference getFirebaseUserReference(FirebaseUser firebaseUser) {
        return FirebaseReference.userReference.child(firebaseUser.getUid());
    }
    private void createNewUser(FirebaseUser firebaseUser) {
        DatabaseReference users = getFirebaseUserReference(firebaseUser);
        Log.d(TAG, "createNewUser: " + firebaseUser.getDisplayName() + users.getKey());
        User user = new User(firebaseUser.getDisplayName(), users.getKey(), "", "", "", FirebaseInstanceId.getInstance().getToken());
        users.setValue(user);
        saveUserDetailsToPref(user);
        progressStop();
        updateUI(firebaseUser);
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        // Log.e("yeh chal rha hai",currentUser.toString());
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            Log.d("updateUi", "Asking for perm");
            //Ask for permission here
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);

            startActivity(intent);
            finish();
        }
        if (currentUser == null) {
            Log.e(TAG, "updateUI: null aa rha hai");
        }
    }

    private void saveUserDetailsToPref(User user) {
        SuperPrefs pref = new SuperPrefs(MainActivity.this);
        pref.setString("user-id", user.getUserID());
        pref.setString("userName", user.getName());
    }

    void progressStart() {
        //pb = new ProgressBar(this,null,android.R.attr.progressBarStyleHorizontal);
        pd = new ProgressDialog(MainActivity.this);
        pd.setMessage("Logging In Please Wait...");
        pd.setCancelable(false);
        pd.show();
    }

    void progressStop() {
        pd.dismiss();
    }
}
