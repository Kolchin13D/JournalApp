package com.example.journalapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.journalapp.Util.JournalUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {

    EditText loginET, passwordET;
    Button signIN, logIN;

    //  firebase authentication
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();

    private CollectionReference collectionReference = database.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        signIN = findViewById(R.id.btnCreate);
        logIN = findViewById(R.id.btnLogin);
        loginET = findViewById(R.id.login);
        passwordET = findViewById(R.id.pass);

        firebaseAuth = FirebaseAuth.getInstance();

        signIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        logIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginEmailPass(
                        loginET.getText().toString().trim(),
                        passwordET.getText().toString().trim());
            }
        });

    }

    private void LoginEmailPass(String email, String pass) {
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)) {

            firebaseAuth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            assert user != null;
                            final String userId = user.getUid();

                            collectionReference
                                    .whereEqualTo("userId", userId)
                                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                                            if (error != null) {
                                                Toast.makeText(getApplicationContext(), "ERROR" + error.getMessage(),
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                            assert value != null;

                                            if (!value.isEmpty()) {
                                                for (QueryDocumentSnapshot snapshot : value) {

                                                    JournalUser journalUser = JournalUser.getInstance();
                                                    journalUser.setUsername(snapshot.getString("username"));
                                                    journalUser.setUserID(snapshot.getString("userId"));

                                                    startActivity(new Intent(MainActivity.this,
                                                            Journal_list.class));
                                                }
                                            }

                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Failed login  " + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

        } else {
            Toast.makeText(getApplicationContext(), "Enter login & password", Toast.LENGTH_SHORT).show();
        }
    }
}