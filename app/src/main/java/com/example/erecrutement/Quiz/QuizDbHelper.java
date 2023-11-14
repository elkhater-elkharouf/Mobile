package com.example.erecrutement.Quiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.erecrutement.Quiz.QuizContract.CategoriesTable;
import com.example.erecrutement.Quiz.QuizContract.QuestionsTable;

import java.util.ArrayList;
import java.util.List;

public class QuizDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MyAwesomeQuiz.db";
    private static final int DATABASE_VERSION = 1;

    private static QuizDbHelper instance;

    private SQLiteDatabase db;

    private QuizDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized QuizDbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new QuizDbHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        final String SQL_CREATE_CATEGORIES_TABLE = "CREATE TABLE " +
                CategoriesTable.TABLE_NAME + "( " +
                CategoriesTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CategoriesTable.COLUMN_NAME + " TEXT " +
                ")";


        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                QuestionsTable.TABLE_NAME + " ( " +
                QuestionsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionsTable.COLUMN_QUESTION + " TEXT, " +
                QuestionsTable.COLUMN_OPTION1 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION2 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION3 + " TEXT, " +
                QuestionsTable.COLUMN_ANSWER_NR + " INTEGER, " +
                QuestionsTable.COLUMN_DIFFICULTY + " TEXT, " +
                QuestionsTable.COLUMN_CATEGORY_ID + " INTEGER, " +
                "FOREIGN KEY(" + QuestionsTable.COLUMN_CATEGORY_ID + ") REFERENCES " +
                CategoriesTable.TABLE_NAME + "(" + CategoriesTable._ID + ")" + "ON DELETE CASCADE" +
                ")";

        db.execSQL(SQL_CREATE_CATEGORIES_TABLE);
        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
        fillCategoriesTable();
        fillQuestionsTable();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CategoriesTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + QuestionsTable.TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    private void fillCategoriesTable() {
        Category c1 = new Category("Programming");
        addCategory(c1);
        Category c2 = new Category("Geography");
        addCategory(c2);
        Category c3 = new Category("Math");
        addCategory(c3);
    }

    private void addCategory(Category category) {
        ContentValues cv = new ContentValues();
        cv.put(CategoriesTable.COLUMN_NAME, category.getName());
        db.insert(CategoriesTable.TABLE_NAME, null, cv);
    }

    private void fillQuestionsTable() {
        Question q1 = new Question("Programming, Easy: A is correct",
                "A", "B", "C", 1,
                Question.DIFFICULTY_EASY, Category.PROGRAMMING);
        addQuestion(q1);
        Question q2 = new Question("Geography, Medium: B is correct",
                "A", "B", "C", 2,
                Question.DIFFICULTY_MEDIUM, Category.GEOGRAPHY);
        addQuestion(q2);
        Question q3 = new Question("Math, Hard: C is correct",
                "A", "B", "C", 3,
                Question.DIFFICULTY_HARD, Category.MATH);
        addQuestion(q3);
        Question q4 = new Question("Math, Easy: A is correct",
                "A", "B", "C", 1,
                Question.DIFFICULTY_EASY, Category.MATH);
        addQuestion(q4);
        Question q5 = new Question("Non existing, Easy: A is correct",
                "A", "B", "C", 1,
                Question.DIFFICULTY_EASY, 4);
        addQuestion(q5);
        Question q6 = new Question("Non existing, Medium: B is correct",
                "A", "B", "C", 2,
                Question.DIFFICULTY_MEDIUM, 5);
        addQuestion(q6);
    }

    public void addQuestion(Question question) {
        ContentValues cv = new ContentValues();
        cv.put(QuestionsTable.COLUMN_QUESTION, question.getQuestion());
        cv.put(QuestionsTable.COLUMN_OPTION1, question.getOption1());
        cv.put(QuestionsTable.COLUMN_OPTION2, question.getOption2());
        cv.put(QuestionsTable.COLUMN_OPTION3, question.getOption3());
        cv.put(QuestionsTable.COLUMN_ANSWER_NR, question.getAnswerNr());
        cv.put(QuestionsTable.COLUMN_DIFFICULTY, question.getDifficulty());
        cv.put(QuestionsTable.COLUMN_CATEGORY_ID, question.getCategoryID());
        db.insert(QuestionsTable.TABLE_NAME, null, cv);
    }

    public List<Category> getAllCategories() {
        List<Category> categoryList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + CategoriesTable.TABLE_NAME, null);

        if (c.moveToFirst()) {
            do {
                Category category = new Category();
                int idIndex = c.getColumnIndex(CategoriesTable._ID);
                int nameIndex = c.getColumnIndex(CategoriesTable.COLUMN_NAME);
                if (idIndex != -1) {
                    category.setId(c.getInt(idIndex));
                }
                // Log column indices for debugging
                Log.d("ColumnIndices", "ID Index: " + idIndex);
                Log.d("ColumnIndices", "Name Index: " + nameIndex);

                category.setName(c.getString(nameIndex));
                category.setId(c.getInt(idIndex));
                categoryList.add(category);
            } while (c.moveToNext());
        }

        c.close();
        return categoryList;
    }

    public ArrayList<Question> getAllQuestions() {
        ArrayList<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + QuestionsTable.TABLE_NAME, null);

        if (c.moveToFirst()) {
            do {
                Question question = new Question();

                // Log column indices for debugging
                int idIndex = c.getColumnIndex(QuestionsTable._ID);
                int questionIndex = c.getColumnIndex(QuestionsTable.COLUMN_QUESTION);
                int option1Index = c.getColumnIndex(QuestionsTable.COLUMN_OPTION1);
                int option2Index = c.getColumnIndex(QuestionsTable.COLUMN_OPTION2);
                int option3Index = c.getColumnIndex(QuestionsTable.COLUMN_OPTION3);
                int answerNrIndex = c.getColumnIndex(QuestionsTable.COLUMN_ANSWER_NR);
                int difficultyIndex = c.getColumnIndex(QuestionsTable.COLUMN_DIFFICULTY);

                int categoryIDIndex = c.getColumnIndex(QuestionsTable.COLUMN_CATEGORY_ID);
                if (categoryIDIndex != -1) {
                    question.setCategoryID(c.getInt(categoryIDIndex));
                }
                if (idIndex != -1) {
                    question.setId(c.getInt(idIndex));
                }
                // Log column indices for debugging
                Log.d("ColumnIndices", "Question Index: " + questionIndex);
                Log.d("ColumnIndices", "Option1 Index: " + option1Index);
                Log.d("ColumnIndices", "Option2 Index: " + option2Index);
                Log.d("ColumnIndices", "Option3 Index: " + option3Index);
                Log.d("ColumnIndices", "AnswerNr Index: " + answerNrIndex);
                Log.d("ColumnIndices", "Difficulty Index: " + difficultyIndex);
                Log.d("ColumnIndices", "ID Index: " + idIndex);
                question.setQuestion(c.getString(questionIndex));
                question.setOption1(c.getString(option1Index));
                question.setOption2(c.getString(option2Index));
                question.setOption3(c.getString(option3Index));
                question.setAnswerNr(c.getInt(answerNrIndex));
                question.setDifficulty(c.getString(difficultyIndex));
                questionList.add(question);
            } while (c.moveToNext());
        }

        c.close();
        return questionList;
    }

    public ArrayList<Question> getQuestions(int categoryID, String difficulty) {
        ArrayList<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();

        String selection = QuestionsTable.COLUMN_CATEGORY_ID + " = ? " +
                " AND " + QuestionsTable.COLUMN_DIFFICULTY + " = ? ";
        String[] selectionArgs = new String[]{String.valueOf(categoryID), difficulty};

        Cursor c = db.query(
                QuestionsTable.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (c.moveToFirst()) {
            do {
                Question question = new Question();
                // Log column indices for debugging
                int idIndex = c.getColumnIndex(QuestionsTable._ID);
                int questionIndex = c.getColumnIndex(QuestionsTable.COLUMN_QUESTION);
                int option1Index = c.getColumnIndex(QuestionsTable.COLUMN_OPTION1);
                int option2Index = c.getColumnIndex(QuestionsTable.COLUMN_OPTION2);
                int option3Index = c.getColumnIndex(QuestionsTable.COLUMN_OPTION3);
                int answerNrIndex = c.getColumnIndex(QuestionsTable.COLUMN_ANSWER_NR);
                int difficultyIndex = c.getColumnIndex(QuestionsTable.COLUMN_DIFFICULTY);
                int categoryIDIndex = c.getColumnIndex(QuestionsTable.COLUMN_CATEGORY_ID);


                // Check if the column index is valid before using it
                if (idIndex != -1) {
                    question.setId(c.getInt(idIndex));
                }
                if (questionIndex != -1) {
                    question.setQuestion(c.getString(questionIndex));
                }
                if (option1Index != -1) {
                    question.setOption1(c.getString(option1Index));
                }
                if (option2Index != -1) {
                    question.setOption2(c.getString(option2Index));
                }
                if (option3Index != -1) {
                    question.setOption3(c.getString(option3Index));
                }
                if (answerNrIndex != -1) {
                    question.setAnswerNr(c.getInt(answerNrIndex));
                }
                if (difficultyIndex != -1) {
                    question.setDifficulty(c.getString(difficultyIndex));
                }

                // Log column indices for debugging
                Log.d("ColumnIndices", "ID Index: " + idIndex);
                Log.d("ColumnIndices", "Question Index: " + questionIndex);
                Log.d("ColumnIndices", "Option1 Index: " + option1Index);
                Log.d("ColumnIndices", "Option2 Index: " + option2Index);
                Log.d("ColumnIndices", "Option3 Index: " + option3Index);
                Log.d("ColumnIndices", "AnswerNr Index: " + answerNrIndex);
                Log.d("ColumnIndices", "Difficulty Index: " + difficultyIndex);
                Log.d("ColumnIndices", "CategoryID Index: " + categoryIDIndex);

                questionList.add(question);
            } while (c.moveToNext());
        }

        c.close();
        return questionList;
    }

    public int deleteQuestion(int questionId) {
        SQLiteDatabase db = getWritableDatabase();
        String selection = QuestionsTable._ID + " = ?";
        String[] selectionArgs = {String.valueOf(questionId)};
        int deletedRows = db.delete(QuestionsTable.TABLE_NAME, selection, selectionArgs);
        db.close();
        return deletedRows;
    }
    public void updateQuestion(Question question) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(QuestionsTable.COLUMN_QUESTION, question.getQuestion());
        cv.put(QuestionsTable.COLUMN_OPTION1, question.getOption1());
        cv.put(QuestionsTable.COLUMN_OPTION2, question.getOption2());
        cv.put(QuestionsTable.COLUMN_OPTION3, question.getOption3());
        cv.put(QuestionsTable.COLUMN_ANSWER_NR, question.getAnswerNr());
        cv.put(QuestionsTable.COLUMN_DIFFICULTY, question.getDifficulty());
        cv.put(QuestionsTable.COLUMN_CATEGORY_ID, question.getCategoryID());

        String selection = QuestionsTable._ID + " = ?";
        String[] selectionArgs = {String.valueOf(question.getId())};

        db.update(QuestionsTable.TABLE_NAME, cv, selection, selectionArgs);

        db.close();
    }

}