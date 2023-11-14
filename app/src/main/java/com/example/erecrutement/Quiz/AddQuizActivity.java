package com.example.erecrutement.Quiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.erecrutement.R;


public class AddQuizActivity extends AppCompatActivity {
    private EditText questionEditText, option1EditText, option2EditText, option3EditText;
    private Spinner difficultySpinner, categorySpinner;
    private Button addQuestionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_quiz);
        // Récupérez la question à modifier de l'Intent
        Intent intent = getIntent();
        Question questionToModify = intent.getParcelableExtra("questionToModify");
        if (questionToModify != null) {
            // Remplissez les champs avec les valeurs de questionToModify
            questionEditText.setText(questionToModify.getQuestion());
            option1EditText.setText(questionToModify.getOption1());
            option2EditText.setText(questionToModify.getOption2());
            option3EditText.setText(questionToModify.getOption3());
           // difficultySpinner.setSelection(questionToModify.getDifficulty());
            // ... Autres champs

            // Vous pouvez également définir la catégorie et la difficulté si vous avez ces informations dans votre modèle Question
            // difficultySpinner.setSelection(...);
            // categorySpinner.setSelection(...);
        }
        questionEditText = findViewById(R.id.editTextQuestion);
        option1EditText = findViewById(R.id.editTextOption1);
        option2EditText = findViewById(R.id.editTextOption2);
        option3EditText = findViewById(R.id.editTextOption3);
        difficultySpinner = findViewById(R.id.spinnerDifficulty);
        categorySpinner = findViewById(R.id.spinnerCategory);
        addQuestionButton = findViewById(R.id.buttonAddQuestion);

        addQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addQuestionToDatabase();
            }
        });
    }

    private void addQuestionToDatabase() {
        // Récupérer les données saisies par l'utilisateur
        String question = questionEditText.getText().toString();
        String option1 = option1EditText.getText().toString();
        String option2 = option2EditText.getText().toString();
        String option3 = option3EditText.getText().toString();
        String difficulty = difficultySpinner.getSelectedItem().toString();
        int categoryID = categorySpinner.getSelectedItemPosition() + 1; // Ajuster l'indice de la catégorie selon votre modèle

        // Valider que tous les champs sont remplis
        if (question.isEmpty() || option1.isEmpty() || option2.isEmpty() || option3.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        // Ajouter la question à la base de données
        QuizDbHelper dbHelper = QuizDbHelper.getInstance(this);
        Question newQuestion = new Question(question, option1, option2, option3, 1, difficulty, categoryID);
        dbHelper.addQuestion(newQuestion);

        // Afficher un message de succès
        Toast.makeText(this, "Question ajoutée avec succès", Toast.LENGTH_SHORT).show();

        // Réinitialiser les champs après l'ajout
        questionEditText.setText("");
        option1EditText.setText("");
        option2EditText.setText("");
        option3EditText.setText("");
    }

}