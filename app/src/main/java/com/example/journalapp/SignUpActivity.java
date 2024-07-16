package com.example.journalapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    EditText loginNew, passwordNew, usernameET;
    Button btnSave;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = database.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        btnSave = findViewById(R.id.btnSave);
        loginNew = findViewById(R.id.loginNew);
        passwordNew = findViewById(R.id.passNew);
        usernameET  = findViewById(R.id.username);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseUser = firebaseAuth.getCurrentUser();

                if (firebaseUser != null) {

                } else {

                }
            }
        };

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty((loginNew.getText().toString()))
                        && !TextUtils.isEmpty(passwordNew.getText().toString())) {

                    String login = loginNew.getText().toString().trim();
                    String password = passwordNew.getText().toString().trim();
                    String username = usernameET.getText().toString().trim();

                    CreateUser(login, password, username);

                }else {
                    Toast.makeText(getApplicationContext(), "Fields empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void CreateUser(String login, String password, final String username) {
        if (!TextUtils.isEmpty((loginNew.getText().toString()))
                && !TextUtils.isEmpty(passwordNew.getText().toString())) {

            firebaseAuth.createUserWithEmailAndPassword(login, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){

                                // take user to next activity
                                firebaseUser = firebaseAuth.getCurrentUser();
                                assert  firebaseUser != null;
                                final String CurrentUserID = firebaseUser.getUid();

                                // create usetMap
                                Map<String, String> userMap = new HashMap<>();
                                userMap.put("userId", CurrentUserID);
                                userMap.put("userName", username);

                            }
                        }
                    });

        }else {
            Toast.makeText(getApplicationContext(), "Fields empty", Toast.LENGTH_SHORT).show();
        }
        
    }
}