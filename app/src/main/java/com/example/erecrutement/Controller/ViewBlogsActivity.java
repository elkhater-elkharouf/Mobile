package com.example.erecrutement.Controller;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.erecrutement.R;
import com.example.erecrutement.Controller.Controller.BlogDataSource;
import com.example.erecrutement.Controller.Model.BlogAdapter;

public class ViewBlogsActivity extends AppCompatActivity {

    Button addButtonView ;
    private BlogDataSource blogDataSource;
    private BlogAdapter blogAdapter; // Vous devez créer un adaptateur personnalisé pour votre RecyclerView

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_blogs);

        // Initialize your database and open it
        blogDataSource = new BlogDataSource(this);
        blogDataSource.open();

        // Initialize RecyclerView and its adapter
        RecyclerView recyclerViewBlogs = findViewById(R.id.recyclerViewBlogs);
        blogAdapter = new BlogAdapter(getAllBlogsFromDatabase());
        recyclerViewBlogs.setAdapter(blogAdapter);
        recyclerViewBlogs.setLayoutManager(new LinearLayoutManager(this));


        addButtonView= findViewById(R.id.addButtonview);
        addButtonView.setOnClickListener(
                view -> {
                    Intent i = new Intent( ViewBlogsActivity.this, AddBlogActivity.class);
                    startActivity(i);
                }
        );
    }

    // Méthode pour récupérer tous les blogs depuis la base de données
    private Cursor getAllBlogsFromDatabase() {
        return blogDataSource.getAllBlogs();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Fermez la base de données lorsque l'activité est détruite
        blogDataSource.close();
    }
}
