package com.example.mncfstaxi;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class register extends AppCompatActivity {
    EditText mFullName,mEmail,mPassword,mPhone;
    TextView mRegisterBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        mFullName   = findViewById(R.id.inputUsername);
        mEmail      = findViewById(R.id.inputEmail);
        mPassword   = findViewById(R.id.inputPassword);
        mPhone      = findViewById(R.id.phone);
        mRegisterBtn= findViewById(R.id.btnRegister);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);

        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }


        mRegisterBtn.setOnClickListener(v -> {
            final String email = mEmail.getText().toString().trim();
            String password = mPassword.getText().toString().trim();
            final String fullName = mFullName.getText().toString();
            final String phone    = mPhone.getText().toString();

            if(TextUtils.isEmpty(email)){
                mEmail.setError("Email est obligatoire.");
                return;
            }

            if(TextUtils.isEmpty(password)){
                mPassword.setError("mot de passe est obligatoire.");
                return;
            }

            if(password.length() < 6){
                mPassword.setError("mot de passe doit être >= 6 Charactères");
                return;
            }

            progressBar.setVisibility(View.VISIBLE);

            // register the user in firebase

            fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                if(task.isSuccessful()){

                    // send verification link

                    FirebaseUser fuser = fAuth.getCurrentUser();
                    fuser.sendEmailVerification().addOnSuccessListener(aVoid -> Toast.makeText(register.this, "une verification Email est envoyé.", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Log.d("ERROR", "onFailure: Erreur " + e.getMessage()));

                    Toast.makeText(register.this, "Utilisateur crée.", Toast.LENGTH_SHORT).show();
                    userID = fAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = fStore.collection("users").document(userID);
                    Map<String,Object> user = new HashMap<>();
                    user.put("fName",fullName);
                    user.put("email",email);
                    user.put("phone",phone);
                    documentReference.set(user).addOnSuccessListener(aVoid -> Log.d("creation", "onSuccess: Profile client est crée pour "+ userID)).addOnFailureListener(e -> Log.d("error", "Erreur" + e.toString()));
                    startActivity(new Intent(getApplicationContext(),home.class));

                }else {
                    Toast.makeText(register.this, "Erreur ! " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            });
        });


        TextView alreadyHaveAccount=findViewById(R.id.alreadyHaveAccount);
        alreadyHaveAccount.setOnClickListener(v -> {
            Intent intent2 = new Intent(register.this,login.class);
            startActivity(intent2);
        });
    }
}