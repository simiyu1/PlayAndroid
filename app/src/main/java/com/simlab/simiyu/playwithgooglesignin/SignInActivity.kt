package com.simlab.simiyu.playwithgooglesignin

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button
import android.widget.TextView;
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.*

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Activity to demonstrate basic retrieval of the Google user's ID, email address, and basic
 * profile.
 */
class SignInActivity : AppCompatActivity() {

    private var TAG = "SignInActivity";
    private var RC_SIGN_IN = 9001;

    //private GoogleSignInClient mGoogleSignInClient;
    var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .build()
    // Build a GoogleSignInClient with the options specified by gso.
    private var mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    var mStatusTextView = findViewById<TextView>(R.id.status)

    //private TextView mStatusTextView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Views
        //Java * mStatusTextView = findViewById(R.id.status);


        // Button listeners
        // findViewById(R.id.sign_in_button).setOnClickListener(this);
        // findViewById(R.id.sign_out_button).setOnClickListener(this);
        // findViewById(R.id.disconnect_button).setOnClickListener(this);

        findViewById<Button>(R.id.sign_in_button).setOnClickListener {
            signIn()
        }
        findViewById<Button>(R.id.sign_out_button).setOnClickListener {
            signOut()
        }
        findViewById<Button>(R.id.sign_out_and_disconnect).setOnClickListener {
            revokeAccess()
        }

        // [START configure_signin]
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
//        val gso GoogleSignInOptions = GoogleSignInOptions
//                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .build()
        val gso : GoogleSignInOptions = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // [END build_client]

        // [START customize_button]
        // Set the dimensions of the sign-in button.
//        SignInButton signInButton = findViewById(R.id.sign_in_button);
//        signInButton.setSize(SignInButton.SIZE_STANDARD);
//        signInButton.setColorScheme(SignInButton.COLOR_LIGHT);

        val signInButton = findViewById<Button>(R.id.sign_in_button)
        signInButton.textSize = SignInButton.SIZE_STANDARD.toFloat()
        signInButton.setTextColor(SignInButton.COLOR_LIGHT)
        // [END customize_button]
    }

    override fun onStart() {
        super.onStart();

        // [START on_start_sign_in]
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        var account : GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(this)
        updateUI(account)
        // [END on_start_sign_in]
    }

    // [START onActivityResult]
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            // Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            val result : GoogleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            // handleSignInResult(task);
            handleSignInResult(result)
        }
    }
    // [END onActivityResult]

    // [START handleSignInResult]
    private fun handleSignInResult(signInResult: GoogleSignInResult) {
        try {
            // val account : GoogleSignInAccount? = signInResult.getResult(ApiException.class);
            val account : GoogleSignInAccount? = signInResult.signInAccount

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
        mGoogleSignInClient.signOut()
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
            mStatusTextView.setText(getString(R.string.signed_in_fmt, account.getDisplayName()));

            // findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            // findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
            findViewById<Button>(R.id.sign_in_button).visibility = View.GONE
            findViewById<Button>(R.id.sign_out_and_disconnect).visibility = View.VISIBLE
        } else {
            mStatusTextView.setText(R.string.signed_out);

            // findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            // findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
            findViewById<Button>(R.id.sign_in_button).visibility = View.VISIBLE
            findViewById<Button>(R.id.sign_out_and_disconnect).visibility = View.GONE
        }
    }

    @Override
    public fun onClick(v: View) {
//        switch (v.getId()) {
//            case R.id.sign_in_button:
//                signIn();
//                break;
//            case R.id.sign_out_button:
//                signOut();
//                break;
//            case R.id.disconnect_button:
//                revokeAccess();
//                break;
//        }
        when (v.getId()) {
            R.id.sign_in_button -> signIn()
            R.id.sign_out_button -> signOut()
            R.id.disconnect_button -> revokeAccess()
        }
    }
 }