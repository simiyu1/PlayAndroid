package com.simlab.simiyu.playwithgooglesignin

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView;
import com.google.android.gms.auth.api.signin.*

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task

/**
 * Activity to demonstrate basic retrieval of the Google user's ID, email address, and basic
 * profile.
 */
class SignInActivity : AppCompatActivity() {

    private var TAG = "SignInActivity";
    private var RC_SIGN_IN = 9001;

    private lateinit var mGoogleSignInClient: GoogleSignInClient
     //var mStatusTextView = findViewById<TextView>(R.id.status)
    private lateinit var mStatusTextView: TextView

    //private TextView mStatusTextView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Views
        //Java * mStatusTextView = findViewById(R.id.status);
        mStatusTextView = findViewById(R.id.status)


        // Button listeners
        findViewById<SignInButton>(R.id.sign_in_button).setOnClickListener {
            signIn()
        }
        findViewById<Button>(R.id.sign_out_button).setOnClickListener {
            signOut()
        }
        findViewById<Button>(R.id.disconnect_button).setOnClickListener {
            revokeAccess()
        }

        // [START configure_signin]
        // Configure sign-in to request the user's ID, email address, and basic

        val gso : GoogleSignInOptions = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // [END build_client]

    }

    override fun onStart() {
        super.onStart();

        // [START on_start_sign_in]
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        val account = GoogleSignIn.getLastSignedInAccount(this)
        updateUI(account)
        // [END on_start_sign_in]
    }

    // [START onActivityResult]
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                //val account = task.getResult(ApiException::class.java)
                handleSignInResult(task)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
                // [START_EXCLUDE]
                updateUI(null)
                // [END_EXCLUDE]
            }

        }
    }

    // [END onActivityResult]

    // [START handleSignInResult]
    private fun handleSignInResult(signInResult: Task<GoogleSignInAccount>) {
        try {
            // val account : GoogleSignInAccount? = signInResult.getResult(ApiException.class);
            val account : GoogleSignInAccount? = signInResult.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (e : ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode())
            updateUI(null)
        }
    }
    // [END handleSignInResult]

    // [START signIn]
    private fun signIn() {
        val signInIntent : Intent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signIn]

    // [START signOut]
    private fun signOut() {
        // mGoogleSignInClient.signOut()
        updateUI(null)

//                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        // [START_EXCLUDE]
//                        updateUI(null);
//                        // [END_EXCLUDE]
//                    }
//                });
    }
    // [END signOut]

    // [START revokeAccess]
    private fun revokeAccess() {
        mGoogleSignInClient.revokeAccess()
        updateUI(null)
//                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        // [START_EXCLUDE]
//                        updateUI(null);
//                        // [END_EXCLUDE]
//                    }
//                });
    }
    // [END revokeAccess]

    private fun updateUI(account: GoogleSignInAccount?) {
        if (account != null) {
            mStatusTextView.text = getString(R.string.signed_in_fmt, account.getDisplayName());

            findViewById<SignInButton>(R.id.sign_in_button).visibility = View.GONE
            findViewById<LinearLayout>(R.id.sign_out_and_disconnect).visibility = View.VISIBLE
        } else {
            mStatusTextView.setText(R.string.signed_out);

            var msignin:SignInButton = findViewById(R.id.sign_in_button)
            var mdiscon:LinearLayout = findViewById(R.id.sign_out_and_disconnect)
            msignin.visibility = View.VISIBLE
            mdiscon.visibility = View.GONE
        }
    }


 }