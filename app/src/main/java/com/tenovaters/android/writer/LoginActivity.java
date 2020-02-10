package com.tenovaters.android.writer;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions.Builder;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.tenovaters.android.writer.GuestMode.guest;

public class LoginActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 234;
    private static final String TAG = "simplifiedcoding";
    /* access modifiers changed from: private */

    /* renamed from: a */
    public int f574a = 0;
    /* access modifiers changed from: private */
    public FirebaseAuth auth;
    private DatabaseReference databaseArtist;
    private Boolean emailAddressChecker;
    /* access modifiers changed from: private */
    public EditText emailid;
    private TextView forgetpassword;
    private Button guest;
    FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;
    /* access modifiers changed from: private */
    public EditText passwordid;
    ProgressBar pro;
    private Button register;
    /* access modifiers changed from: private */
    public TextView show;
    private Button signin;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_login);
        this.emailid = (EditText) findViewById(R.id.username);
        this.passwordid = (EditText) findViewById(R.id.password);
        this.signin = (Button) findViewById(R.id.login);
        this.guest = (Button) findViewById(R.id.guest_mode);
        this.register = (Button) findViewById(R.id.regiter);
        this.pro = (ProgressBar) findViewById(R.id.loading);
        this.show = (TextView) findViewById(R.id.tv_password);
        this.forgetpassword = (TextView) findViewById(R.id.forgetpassword);
        this.auth = FirebaseAuth.getInstance();
        this.mAuth = FirebaseAuth.getInstance();
        this.mGoogleSignInClient = GoogleSignIn.getClient((Activity) this, new Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build());
        findViewById(R.id.sign_in_button).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                LoginActivity.this.signIn();
            }
        });
        this.forgetpassword.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String email = LoginActivity.this.emailid.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(LoginActivity.this.getApplication(), "Enter your registered email id", Toast.LENGTH_SHORT).show();
                    return;
                }
                LoginActivity.this.pro.setVisibility(View.VISIBLE);
                LoginActivity.this.auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                        }
                        LoginActivity.this.pro.setVisibility(View.GONE);
                    }
                });
            }
        });
        this.show.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (LoginActivity.this.f574a == 0) {
                    LoginActivity.this.passwordid.setTransformationMethod(null);
                    LoginActivity.this.show.setText("Hide Password");
                    LoginActivity.this.f574a = 1;
                    return;
                }
                LoginActivity.this.passwordid.setTransformationMethod(new PasswordTransformationMethod());
                LoginActivity.this.show.setText("Show Password");
                LoginActivity.this.f574a = 0;
            }
        });
        this.guest.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                LoginActivity loginActivity = LoginActivity.this;
                loginActivity.startActivity(new Intent(loginActivity, guest.class));
            }
        });
        this.register.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                LoginActivity loginActivity = LoginActivity.this;
                loginActivity.startActivity(new Intent(loginActivity, RegisterActivity.class));
            }
        });
        this.signin.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (LoginActivity.this.haveNetwork().booleanValue()) {
                    String email = LoginActivity.this.emailid.getText().toString().trim();
                    String password = LoginActivity.this.passwordid.getText().toString().trim();
                    if (TextUtils.isEmpty(email)) {
                        Toast.makeText(LoginActivity.this.getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(password)) {
                        Toast.makeText(LoginActivity.this.getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    } else if (password.length() < 6) {
                        Toast.makeText(LoginActivity.this.getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    } else {
                        LoginActivity.this.pro.setVisibility(View.VISIBLE);
                        LoginActivity.this.auth.signInWithEmailAndPassword(email, password).
                                addOnCompleteListener((Activity) LoginActivity.this,
                                        new OnCompleteListener<AuthResult>() {
                            public void onComplete(Task<AuthResult> task) {
                                LoginActivity.this.pro.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_LONG).show();
                                } else {
                                    LoginActivity.this.emailverification();
                                }
                            }
                        });
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Check your internet connection", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public Boolean haveNetwork() {
        NetworkInfo[] networkInfos;
        boolean have_MobileData = false;
        boolean z = false;
        boolean have_WIFI = false;
        for (NetworkInfo info : ((ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE)).getAllNetworkInfo()) {
            if (info.getTypeName().equalsIgnoreCase("WIFI") && info.isConnected()) {
                have_WIFI = true;
            }
            if (info.getTypeName().equalsIgnoreCase("MOBILE") && info.isConnected()) {
                have_MobileData = true;
            }
        }
        if (have_MobileData || have_WIFI) {
            z = true;
        }
        return Boolean.valueOf(z);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            try {
                firebaseAuthWithGoogle((GoogleSignInAccount) GoogleSignIn.getSignedInAccountFromIntent(data).getResult(ApiException.class));
            } catch (ApiException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        StringBuilder sb = new StringBuilder();
        sb.append("firebaseAuthWithGoogle:");
        sb.append(acct.getId());
        Log.d(TAG, sb.toString());
        this.mAuth.signInWithCredential(GoogleAuthProvider.getCredential(acct.getIdToken(), null)).addOnCompleteListener((Activity) this, new OnCompleteListener<AuthResult>() {
            public void onComplete(Task<AuthResult> task) {
                boolean isSuccessful = task.isSuccessful();
                String str = LoginActivity.TAG;
                if (isSuccessful) {
                    Log.d(str, "signInWithCredential:success");
                    FirebaseUser currentUser = LoginActivity.this.mAuth.getCurrentUser();
                    LoginActivity loginActivity = LoginActivity.this;
                    loginActivity.startActivity(new Intent(loginActivity, HomeActivity.class));
                    Toast.makeText(LoginActivity.this, "User Signed In", Toast.LENGTH_SHORT).show();
                    LoginActivity.this.finish();
                    return;
                }
                Log.w(str, "signInWithCredential:failure", task.getException());
                Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /* access modifiers changed from: private */
    public void emailverification() {
        this.emailAddressChecker = Boolean.valueOf(this.auth.getCurrentUser().isEmailVerified());
        if (this.emailAddressChecker.booleanValue()) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
            return;
        }
        Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show();
        this.auth.signOut();
    }

    /* access modifiers changed from: private */
    public void signIn() {
        startActivityForResult(this.mGoogleSignInClient.getSignInIntent(), RC_SIGN_IN);
    }
}
