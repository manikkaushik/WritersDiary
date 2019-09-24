package com.tenovaters.android.writer;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailid,password;
    private Button register;
    private FirebaseAuth auth;
    ProgressBar pro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailid=(EditText)findViewById(R.id.username_reg);
        password=(EditText)findViewById(R.id.password_reg);

        register=(Button)findViewById(R.id.regiter_reg);
        pro=(ProgressBar)findViewById(R.id.loading_reg);

        auth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(haveNetwork()){
                    String email = emailid.getText().toString().trim();
                    String passwordid = password.getText().toString().trim();


                    if(TextUtils.isEmpty(email)){
                        Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    }
                    else if(TextUtils.isEmpty(passwordid)){
                        Toast.makeText(getApplicationContext(), "Enter Your Password!", Toast.LENGTH_SHORT).show();
                    }
                    else if(passwordid.length()<6){
                        Toast.makeText(getApplicationContext(), "Password Length should be greater than 6", Toast.LENGTH_SHORT).show();
                    }
                    else if (!isEmailValid(email)){
                        Toast.makeText(getApplicationContext(), "Your Email Id is Invalid.", Toast.LENGTH_SHORT).show();

                    }
                    else{
                        pro.setVisibility(View.VISIBLE);
                        auth.createUserWithEmailAndPassword(email, passwordid)
                                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        Toast.makeText(RegisterActivity.this, "createUserWithEmail:onComplete: " + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                        pro.setVisibility(View.GONE);
                                        if (!task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(), "Either there is some error or Email already exist", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Account created Successfully", Toast.LENGTH_SHORT).show();
                                            auth.signOut();
                                        }
                                    }
                                });
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private Boolean haveNetwork() {
        boolean have_WIFI = false;
        boolean have_MobileData = false;

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();

        for (NetworkInfo info : networkInfos) {
            if (info.getTypeName().equalsIgnoreCase("WIFI"))
                if (info.isConnected())
                    have_WIFI = true;
            if (info.getTypeName().equalsIgnoreCase("MOBILE"))
                if (info.isConnected())
                    have_MobileData = true;
        }
        return have_MobileData || have_WIFI;

    }

    @Override
    public void onBackPressed(){
        finish();
        super.onBackPressed();
    }
}
