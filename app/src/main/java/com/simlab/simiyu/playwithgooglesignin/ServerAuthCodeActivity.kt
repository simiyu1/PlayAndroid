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
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

/**
 * Demonstrates retrieving an offline access one-time code for the current Google user, which
 * can be exchanged by your server for an access token and refresh token.
 */
class ServerAuthCodeActivity : AppCompatActivity() {

    // public static final String TAG = "ServerAuthCodeActivity";
    // private static final int RC_GET_AUTH_CODE = 9003;
    var TAG: String = "ServerAuthCodeActivity";
    private var RC_GET_AUTH_CODE : Int = 9003;

    // private GoogleSignInClient mGoogleSignInClient;
    // private TextView mAuthCodeTextView;
    var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .build()
    // Build a GoogleSignInClient with the options specified by gso.
    private var mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
   // private var mGoogleSignInClient : GoogleSignInClient
    private var mAuthCodeTextView:TextView = findViewById(R.id.detail)


    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Views
        mAuthCodeTextView = findViewById(R.id.detail);

        // Button click listeners
//        findViewById(R.id.sign_in_button).setOnClickListener(this);
//        findViewById(R.id.sign_out_button).setOnClickListener(this);
//        findViewById(R.id.disconnect_button).setOnClickListener(this);
        //findViewById<Button>(R.id.sign_in_button).setOnClickListener()

        // For sample only: make sure there is a valid server client ID.
        validateServerClientID();

        // [START configure_signin]
        // Configure sign-in to request offline access to the user's ID, basic
        // profile, and Google Drive. The first time you request a code you will
        // be able to exchange it for an access token and refresh token, which
        // you should store. In subsequent calls, the code will only result in
        // an access token. By asking for profile access (through
        // DEFAULT_SIGN_IN) you will also get an ID Token as a result of the
        // code exchange.
        var serverClientId : String = getString(R.string.server_client_id);
        var gso : GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(Scope(Scopes.DRIVE_APPFOLDER))
                .requestServerAuthCode(serverClientId)
                .requestEmail()
                .build();
        // [END configure_signin]

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private fun getAuthCode() {
        // Start the retrieval process for a server auth code.  If requested, ask for a refresh
        // token.  Otherwise, only get an access token if a refresh token has been previously
        // retrieved.  Getting a new access token for an existing grant does not require
        // user consent.
        var signInIntent : Intent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_GET_AUTH_CODE);
    }

    private fun signOut() {
        mGoogleSignInClient.signOut()
        updateUI(null)
//            .addOnCompleteListener(this, new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                updateUI(null);
//            }
//        });
    }

    private fun revokeAccess() {
        mGoogleSignInClient.revokeAccess()
        updateUI(null)
//            .addOnCompleteListener(this,
//                new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        updateUI(null);
//                    }
//                });
    }


    @Override
    override  fun onActivityResult(requestCode:Int, resultCode:Int, data:Intent) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_GET_AUTH_CODE) {
            // [START get_auth_code]
            var task: Task<GoogleSignInAccount>  = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                var account: GoogleSignInAccount = task.getResult(ApiException.class);
                var authCode:String = account.getServerAuthCode().toString();

                // Show signed-un UI
                updateUI(account);

                // TODO(developer): send code to server and exchange for access/refresh/ID tokens
            } catch (e: ApiException) {
                Log.w(TAG, "Sign-in failed", e);
                updateUI(null);
            }
            // [END get_auth_code]
        }
    }

    private fun updateUI(@Nullable account : GoogleSignInAccount?) {
        if (account != null) {
            // ((TextView) findViewById(R.id.status)).setText(R.string.signed_in);
            findViewById<TextView>(R.id.status).setText(R.string.signed_in)

            //findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            // findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
            findViewById<Button>(R.id.sign_in_button).visibility= View.GONE
            findViewById<Button>(R.id.sign_out_and_disconnect).visibility= View.VISIBLE

            var authCode = account.getServerAuthCode();
            mAuthCodeTextView.setText(getString(R.string.auth_code_fmt, authCode));
        } else {
            // ((TextView) findViewById(R.id.status)).setText(R.string.signed_out);
            findViewById<TextView>(R.id.status).setText(R.string.sign_out)
            mAuthCodeTextView.setText(getString(R.string.auth_code_fmt, "null"));

//            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
//            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
            findViewById<Button>(R.id.sign_in_button).visibility = View.VISIBLE
            findViewById<Button>(R.id.sign_out_and_disconnect).visibility = View.GONE

        }
    }

    /**
     * Validates that there is a reasonable server client ID in strings.xml, this is only needed
     * to make sure users of this sample follow the README.
     */
    private fun validateServerClientID() {
        var serverClientId = getString(R.string.server_client_id);
        var suffix = ".apps.googleusercontent.com";
        if (!serverClientId.trim().endsWith(suffix)) {
            var message = "Invalid server client ID in strings.xml, must end with " + suffix;

            Log.w(TAG, message);
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    fun onClick(v: View) {
        when (v.getId()) {
            R.id.sign_in_button -> getAuthCode()
            R.id.sign_out_button -> signOut()
            R.id.disconnect_button -> revokeAccess()
        }
    }
}