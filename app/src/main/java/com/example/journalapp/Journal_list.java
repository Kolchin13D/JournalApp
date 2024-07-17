package com.example.journalapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.journalapp.Model.Journal;
import com.example.journalapp.Util.JournalUser;
import com.example.journalapp.adapter.JournalAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Journal_list extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private List<Journal> journalList;
    private RecyclerView recyclerView;
    private JournalAdapter journalItemAdapter;
    private CollectionReference collectionReference = database.collection("Journal");
    private TextView noPostsEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_list);

        // Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        noPostsEntry = findViewById(R.id.list_no_posts);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        journalList = new ArrayList<>();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                if (user != null && firebaseAuth != null) {
                    startActivity(new Intent(
                            Journal_list.this,
                            AddJournalActivity.class
                    ));
                }
                break;
            case R.id.action_signout:
                // Signing out the user
                if (user != null && firebaseAuth != null) {
                    firebaseAuth.signOut();

                    startActivity(new Intent(
                            Journal_list.this,
                            MainActivity.class
                    ));
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        collectionReference
                .whereEqualTo("userID", JournalUser.getInstance().getUserID())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if (!queryDocumentSnapshots.isEmpty()) {

                    for (QueryDocumentSnapshot journals : queryDocumentSnapshots) {
                        Journal journal = journals.toObject(Journal.class);
                        journalList.add(journal);
                    }

                    journalItemAdapter = new JournalAdapter(Journal_list.this, journalList);
                    recyclerView.setAdapter(journalItemAdapter);
                    journalItemAdapter.notifyDataSetChanged();
                    noPostsEntry.setVisibility(View.INVISIBLE);

                } else {
                    Toast.makeText(getApplicationContext(), "queryDocumentSnapshots is empty!",
                            Toast.LENGTH_SHORT).show();
                    noPostsEntry.setVisibility(View.VISIBLE);
                }

            }
        });
    }
}