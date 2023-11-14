package com.example.erecrutement.Controller;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.erecrutement.R;
import com.example.erecrutement.Controller.Controller.BlogDataSource;

public class AddBlogActivity extends AppCompatActivity {
    private BlogDataSource blogDataSource;
    private EditText editTextTitle;
    private EditText editTextDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_blog);

        // Initialize your database and open it
        blogDataSource = new BlogDataSource(this);
        blogDataSource.open();

        // Initialize your views
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);

        // ...

        Button buttonAddBlog = findViewById(R.id.buttonAddBlog);
        buttonAddBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the data from the EditText fields
                String title = editTextTitle.getText().toString();
                String description = editTextDescription.getText().toString();

                // Validate input
                if (title.isEmpty() || description.isEmpty()) {
                    Toast.makeText(AddBlogActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                } else {
                    // Add the blog to the database
                    long newBlogId = blogDataSource.insertBlog(title, description);

                    // Check if the blog was added successfully
                    if (newBlogId != -1) {
                        Toast.makeText(AddBlogActivity.this, "Blog ajouté avec succès", Toast.LENGTH_SHORT).show();

                        // Lancer ViewBlogsActivity après l'ajout réussi
                        Intent intent = new Intent(AddBlogActivity.this, ViewBlogsActivity.class);
                        startActivity(intent);

                        finish(); // Fermer l'activité après l'ajout du blog
                    } else {
                        Toast.makeText(AddBlogActivity.this, "Échec de l'ajout du blog", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

// ...

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close the database when the activity is destroyed
        blogDataSource.close();
    }
}

