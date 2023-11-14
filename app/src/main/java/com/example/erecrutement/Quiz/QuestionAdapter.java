package com.example.erecrutement.Quiz;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.erecrutement.R;

import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {

    private List<Question> questions;
    private Context context;
    private QuizDbHelper dbHelper;
    private static OnDeleteClickListener onDeleteClickListener;

    // Modifier le constructeur pour prendre le contexte en paramètre
    public QuestionAdapter(Context context, List<Question> questions, QuizDbHelper dbHelper) {
        this.context = context;
        this.questions = questions;
        this.dbHelper = dbHelper;
    }
    // Interface for handling modify button clicks
    public interface OnModifierClickListener {
        void onModifierClick(int position);
    }

    private static OnModifierClickListener onModifierClickListener;

    public void setOnModifierClickListener(OnModifierClickListener listener) {
        this.onModifierClickListener = listener;
    }
    // Interface for handling delete button clicks
    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }


    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.onDeleteClickListener = listener;
    }
    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_question, parent, false);
        return new QuestionViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        Question question = questions.get(holder.getAdapterPosition());
        holder.bind(question);
        holder.setOnDeleteClickListener(onDeleteClickListener);
        // Ajouter un écouteur de clics au bouton "Supprimer"
        // Ajouter un écouteur de clics au bouton "Supprimer"
        holder.btnDelete.setOnClickListener(v -> {
            if (onDeleteClickListener != null) {
                onDeleteClickListener.onDeleteClick(holder.getAdapterPosition());
            }
        });
        // Ajouter un écouteur de clics au bouton "Supprimer"
        holder.btnDelete.setOnClickListener(v -> {
            if (onDeleteClickListener != null) {
                onDeleteClickListener.onDeleteClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    static class QuestionViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewQuestion;
        private TextView textViewOption1;
        private TextView textViewOption2;
        private TextView textViewOption3;
        private TextView textViewDifficulty;
        private Button btnModifier;
        private OnModifierClickListener onModifierClickListener;
        //private Button btnDelete;

        private Button btnDelete;
        private OnDeleteClickListener onDeleteClickListener;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewQuestion = itemView.findViewById(R.id.textViewQuestion);
            textViewOption1 = itemView.findViewById(R.id.textViewOption1);
            textViewOption2 = itemView.findViewById(R.id.textViewOption2);
            textViewOption3 = itemView.findViewById(R.id.textViewOption3);
            textViewDifficulty = itemView.findViewById(R.id.textViewDifficulty);
            btnDelete = itemView.findViewById(R.id.btnDelete);  // Initialisez le bouton ici

            // Ajoutez l'écouteur de clics au bouton "Supprimer"
             btnDelete.setOnClickListener(v -> {
                if (onDeleteClickListener != null) {
                    onDeleteClickListener.onDeleteClick(getAdapterPosition());
                }
            });

            btnModifier = itemView.findViewById(R.id.btnModifier);

            // Ajoutez l'écouteur de clics au bouton "Modifier"
            btnModifier.setOnClickListener(v -> {
                if (onModifierClickListener != null) {
                    onModifierClickListener.onModifierClick(getAdapterPosition());
                }
            });
        }

        public void bind(Question question) {
            // Bind all question data to UI elements
            textViewQuestion.setText(question.getQuestion());
            textViewOption1.setText("Options: " + question.getOption1());
            textViewOption2.setText("Options: " + question.getOption2());
            textViewOption3.setText("Options: " + question.getOption3());
            textViewDifficulty.setText("Difficulty: " + question.getDifficulty());
            // Bind other attributes


        }
        // Ajoutez cette méthode pour définir l'écouteur de clics depuis l'extérieur
        public void setOnDeleteClickListener(OnDeleteClickListener listener) {
            this.onDeleteClickListener = listener;
        }
        public void setOnModifierClickListener(OnModifierClickListener listener) {
            this.onModifierClickListener = listener;
        }

    }

}
