package com.example.ryanh.onlinequiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.ryanh.onlinequiz.Contract.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;


public class QuizDtb extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MyAwesomeQuiz.db";
    private static final int DATABASE_VERSION = 2;

    public QuizDtb(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                QuestionTable.Table_Name + " ( " +
                QuestionTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionTable.Column_question + " TEXT, " +
                QuestionTable.Column_option1 + " TEXT, " +
                QuestionTable.Column_option2 + " TEXT, " +
                QuestionTable.Column_option3 + " TEXT, " +
                QuestionTable.Column_answer_nr + " INTEGER," +
                QuestionTable.Column_LevelDiff + " TEXT);";

        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + QuestionTable.Table_Name + ";");
        onCreate(db);
    }

    public void fillQuestionsTable() {

        Question q1 = new Question(" What is the capital of Malaysia?", "Kuala Lumpur", "Georgetown", "Subang Jaya", 1, Question.LevelDiff_Easy);
        addQuestion(q1);
        Question q2 = new Question(" What is the tallest Mountain in the world?", "Mt. Kinabalu", "Mt. Everest", "Mt. Suribachi", 2, Question.LevelDiff_Easy);
        addQuestion(q2);
        Question q3 = new Question("  What is the biggest state in the Malaysia? ", "Penang", "Pahang", "Sarawak", 3, Question.LevelDiff_Easy);
        addQuestion(q3);
        Question q4 = new Question("  What is the national flower in Malaysia?", "Hibiscus", "Orchid", "Sunflower", 1, Question.LevelDiff_Easy);
        addQuestion(q4);
        Question q5 = new Question("  What will happen to Thanos in the next Avenger film", "Die", "Kill everyone", "Make peace with God ? ", 2, Question.LevelDiff_Easy);
        addQuestion(q5);
        Question q6 = new Question("  What does MK stand for ?", "Maanoj Kumar", "Mushroom Kingdom", "Master Key", 3, Question.LevelDiff_Easy);
        addQuestion(q6);
        Question q7 = new Question("  What is the tallest building in the world ?", "Burj Khalifah ", "Empire State Building", "Petronas Twin Towers", 1, Question.LevelDiff_Easy);
        addQuestion(q7);
        Question q8 = new Question("  What year did World War 2 start ? ", "1941", "1939", "1940", 2, Question.LevelDiff_Easy);
        addQuestion(q8);
        Question q9 = new Question("  Who plays the character StarLord in the Marvel Cinematic Universe ?", "Chris Pratt", "Chris Evans", "Chris Hemsworth", 1, Question.LevelDiff_Easy);
        addQuestion(q9);
        Question q10 = new Question(" What is the name of the Malaysian National Anthem ?", "Duli Yang Maha Mulia", "On the Floor", "Negaraku ", 3, Question.LevelDiff_Easy);
        addQuestion(q10);
    }

    // call this method in onDestroy
    public void deleteQuestions() {

        SQLiteDatabase db = getWritableDatabase();
        // use back your table name
        db.execSQL("DELETE FROM " + QuestionTable.Table_Name + ";"); //delete all rows in a table
        db.close();

        // DEBUG
        Log.i("onDestroy", "Questions deleted.");
    }

    private void addQuestion(Question question) {

        // need to get writable database
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(QuestionTable.Column_question, question.getQuestion());
        cv.put(QuestionTable.Column_option1, question.getOption1());
        cv.put(QuestionTable.Column_option2, question.getOption2());
        cv.put(QuestionTable.Column_option3, question.getOption3());
        cv.put(QuestionTable.Column_answer_nr, question.getAnswerNr()); // int
        cv.put(QuestionTable.Column_LevelDiff, question.getLevel());

        db.insert(QuestionTable.Table_Name, null, cv);

        // need to close the db
        db.close();
    }


    // no problem, it was here
    public ArrayList<Question> getAllQuestions() {

        ArrayList<Question> questionList = new ArrayList<>();

        // get readable database because no need to update data
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + QuestionTable.Table_Name + ";", null);

        if (c.moveToFirst()) {
            do {
                Question question = new Question();
                question.setQuestion(c.getString(c.getColumnIndex(QuestionTable.Column_question)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionTable.Column_option1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionTable.Column_option2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionTable.Column_option3)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionTable.Column_answer_nr)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionTable.Column_LevelDiff)));
                questionList.add(question);
            } while (c.moveToNext());
        }

        c.close(); // close cursor
        db.close(); // close db

        return questionList;
    }


    public ArrayList<Question> getQuestions(String level) {

        ArrayList<Question> questionList = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        String[]selectionArgs = new String[]{level};
        Cursor c = db.rawQuery(
                "SELECT * FROM " + QuestionTable.Table_Name + " WHERE " + QuestionTable.Column_LevelDiff + " = ? ",
                selectionArgs);

        if (c.moveToFirst()) {
            do {
                Question question = new Question();
                question.setQuestion(c.getString(c.getColumnIndex(QuestionTable.Column_question)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionTable.Column_option1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionTable.Column_option2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionTable.Column_option3)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionTable.Column_answer_nr))); // int
                questionList.add(question);
            } while (c.moveToNext());
        }

        c.close();
        db.close(); // close db

        return questionList;
    }
}