package com.example.erecrutement.Quiz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.erecrutement.R;

import java.util.List;

public class AllQuestionsActivity extends AppCompatActivity implements QuestionAdapter.OnDeleteClickListener, QuestionAdapter.OnModifierClickListener {
    private List<Question> questions;
    private QuestionAdapter myQuestionAdapter;
    private QuizDbHelper dbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_questions);

        // Retrieve the list of questions from the intent
        Intent intent = getIntent();
        questions = (List<Question>) intent.getSerializableExtra("questions");
        dbHelper = QuizDbHelper.getInstance(this);

        if (questions != null && !questions.isEmpty()) {
            // Use the list of questions to display data in your new page
            displayQuestions(questions);
        } else {
            // Handle the case where the list of questions is null or empty
            Toast.makeText(this, "No questions to display", Toast.LENGTH_SHORT).show();
        }
        // Instantiate your adapter and set the listener
        myQuestionAdapter = new QuestionAdapter(this, questions,dbHelper);
        myQuestionAdapter.setOnDeleteClickListener(this);
        myQuestionAdapter.setOnModifierClickListener(this);

    }

    private void displayQuestions(List<Question> questions) {
        RecyclerView recyclerView = findViewById(R.id.recyclerViewQuestions);

        // Create and set the adapter for the RecyclerView
        myQuestionAdapter = new QuestionAdapter(this, questions,dbHelper);
        recyclerView.setAdapter(myQuestionAdapter);

        // Use a LinearLayoutManager (or another layout manager) to organize items in the list
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    @Override
    public void onDeleteClick(int position) {
        // Implement the logic to delete the question at the specified position
        if (position >= 0 && position < questions.size()) {
            Question deletedQuestion = questions.get(position);
            int deletedQuestionId = deletedQuestion.getId();

            // Delete the question from the database
            int deletedRows = dbHelper.deleteQuestion(deletedQuestionId);

            if (deletedRows > 0) {
                // Remove the question from the list
                questions.remove(position);
                myQuestionAdapter.notifyItemRemoved(position);

                Toast.makeText(this, "Question deleted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to delete question", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onModifierClick(int position) {
        if (position >= 0 && position < questions.size()) {
            // Récupérez la question sélectionnée
            Question modifiedQuestion = questions.get(position);

            // Modifiez les propriétés de la question selon vos besoins
            modifiedQuestion.setQuestion("Nouvelle question");
            modifiedQuestion.setOption1("Nouvelle option 1");
            modifiedQuestion.setOption2("Nouvelle option 2");
            modifiedQuestion.setOption3("Nouvelle option 3");
            // ... Mettez à jour d'autres propriétés selon vos besoins

            // Mettez à jour la question dans la base de données
            dbHelper.updateQuestion(modifiedQuestion);

            // Rafraîchissez la liste des questions dans l'adaptateur
            myQuestionAdapter.notifyDataSetChanged();

            Toast.makeText(this, "Question modifiée avec succès", Toast.LENGTH_SHORT).show();
        }
    }

}