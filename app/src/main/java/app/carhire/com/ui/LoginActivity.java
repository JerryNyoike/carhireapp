package app.carhire.com.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import app.carhire.com.BuildConfig;
import app.carhire.com.R;

public class LoginActivity extends AppCompatActivity {
    private DatabaseReference mDatabaseReference;
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegister;
    String email, password;
    private FirebaseAuth mAuth;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private String prefFile = BuildConfig.APPLICATION_ID + ".PREFERENCE_FILE_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //if user is already logged in proceed
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null){
            startActivity(new Intent(LoginActivity.this, ViewCars.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
        }

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        mAuth = FirebaseAuth.getInstance();


        sharedPref = getApplicationContext().getSharedPreferences(prefFile, Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = etEmail.getText().toString().trim();
                password = etPassword.getText().toString().trim();

                if(validateForm()){
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
                                editor.putString("UserId", getUserId());
                                editor.apply();
                                editor.putString("UserEmail", email);
                                editor.apply();

                                startActivity(new Intent(getApplicationContext(), ViewCars.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                finish();
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

    }

    protected boolean validateForm(){
        if(email.isEmpty()){
            etEmail.setError("Email can't be blank");
            return false;
        } else if (password.isEmpty()){
            etPassword.setError("Password can't be blank");
            return false;
        }
        return true;
    }

    private String getUserId(){
        final String[] uid = new String[1];
        ValueEventListener postListener = new ValueEventListener() {
            String userId;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                String key = mDatabaseReference.push().getKey();
                userId = dataSnapshot.child("user").child(key).getValue(String.class);
                uid[0] = userId;
                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
//                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        return uid[0];
    }
}
