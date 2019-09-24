package com.tenovaters.android.writer;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {


    private EditText emailid,passwordid;
    private TextView forgetpassword;
    private ImageView img;
    private Button signin;
    private FirebaseAuth auth;
    ProgressBar pro;
    private Button guest;
    private Button register;
    private int a=0;
    private DatabaseReference databaseArtist;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailid=(EditText)findViewById(R.id.username);
        passwordid=(EditText)findViewById(R.id.password);
        signin = (Button) findViewById(R.id.login);
        guest = (Button) findViewById(R.id.guest_mode);
        register = (Button) findViewById(R.id.regiter);
        pro=(ProgressBar)findViewById(R.id.loading) ;
        img=(ImageView)findViewById(R.id.image_pass);
        forgetpassword=(TextView)findViewById(R.id.forgetpassword);

        auth = FirebaseAuth.getInstance();

        databaseArtist = FirebaseDatabase.getInstance().getReference("Feedback");


        forgetpassword.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                        String email = emailid.getText().toString().trim();

                        if (TextUtils.isEmpty(email)) {
                            Toast.makeText(getApplication(), "Enter your registered email id", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else{
                            pro.setVisibility(View.VISIBLE);
                            auth.sendPasswordResetEmail(email)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(LoginActivity.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(LoginActivity.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                                            }
                                            pro.setVisibility(View.GONE);
                                        }
                                    });
                        }

            }
        });
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(a==0){
                    passwordid.setTransformationMethod(null);
                    img.setImageResource(R.drawable.ic_viewrs_green);
                    a=1;
                }
                else{
                    passwordid.setTransformationMethod(new PasswordTransformationMethod());
                    img.setImageResource(R.drawable.ic_viewrs_green);
                    a=0;
                }

            }
        });

        guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,HomeActivity.class));
            }
        });

        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(haveNetwork()){
                    String email = emailid.getText().toString().trim();
                    String password = passwordid.getText().toString().trim();

                    if (TextUtils.isEmpty(email)) {
                        Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (TextUtils.isEmpty(password)) {
                        Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                        return;
                    }


                    if (password.length() < 6) {
                        Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                        return;
                    }


                    pro.setVisibility(View.VISIBLE);

                    auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    pro.setVisibility(View.GONE);
                                    if (!task.isSuccessful()) {
                                        // there was an error

                                        Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_LONG).show();

                                    } else {
                                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }

                            });

                }
                else{
                    Toast.makeText(LoginActivity.this, "Check your internet connection", Toast.LENGTH_LONG).show();
                }

            }
        });
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


}
