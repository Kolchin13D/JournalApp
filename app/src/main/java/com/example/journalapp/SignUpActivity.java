package com.example.journalapp;

import android.content.Intent;
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

import com.example.journalapp.Util.JournalUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    EditText loginNew, passwordNew, usernameET;
    Button btnSave;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = database.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();

        btnSave = findViewById(R.id.btnSave);
        loginNew = findViewById(R.id.loginNew);
        passwordNew = findViewById(R.id.passNew);
        usernameET = findViewById(R.id.username);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();

                if (currentUser != null) {

                }else {

                }
            }
        };

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(loginNew.getText().toString())
                        && !TextUtils.isEmpty(usernameET.getText().toString())
                        && !TextUtils.isEmpty(passwordNew.getText().toString())){

                    String email = loginNew.getText().toString().trim();
                    String password = passwordNew.getText().toString().trim();
                    String username = usernameET.getText().toString().trim();

                    CreateUser(email, password, username);

                } else {
                    Toast.makeText(getApplicationContext(), "Fields empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void CreateUser(String email, String password, final String username) {
        if (!TextUtils.isEmpty(email)
                && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(username)){

            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                // take user to next activity
                                currentUser = firebaseAuth.getCurrentUser();
                                assert currentUser != null;
                                final String currentUserID = currentUser.getUid();

                                // create usetMap
                                Map<String, String> userMap = new HashMap<>();
                                userMap.put("userId", currentUserID);
                                userMap.put("username", username);

                                // add user fo firestore
                                collectionReference.add(userMap)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                documentReference.get()
                                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                if (Objects.requireNonNull(task.getResult().exists())) {
                                                                    String name = task.getResult().getString("username");

                                                                    // use of flobal journal user
                                                                    JournalUser journalUser = JournalUser.getInstance();
                                                                    journalUser.setUserID(currentUserID);
                                                                    journalUser.setUsername(name);


                                                                    //go to new activity
                                                                    Intent intent = new Intent(SignUpActivity.this,
                                                                            AddJournalActivity.class);
                                                                    intent.putExtra("username", name);
                                                                    intent.putExtra("userId", currentUserID);

                                                                    Toast.makeText(getApplicationContext(), "User created" + name,
                                                                            Toast.LENGTH_SHORT).show();

                                                                    startActivity(intent);
                                                                }
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(getApplicationContext(), "Exception - " + e,
                                                                        Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            }
                                        });
                            }
                        }
                    });

        } else {
            Toast.makeText(getApplicationContext(), "Fields empty", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        currentUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
}