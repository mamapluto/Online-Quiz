package com.example.ryanh.onlinequiz;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
private static final int REQUEST_CODE_QUIZ=1;

public static final String SHARED_PRE= "sharePre";
public static final String HIGH_SCOREx= "highScorex";

private TextView textViewHighScore;




private int highscore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewHighScore = findViewById(R.id.text_view_score);

        loadScore();


        Button buttonStartQuiz = findViewById(R.id.button_start);
        buttonStartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuiz();

            }
        });

        new QuizDtb(this).deleteQuestions(); // delete questions
    }

    private void startQuiz(){
        Intent intent = new Intent(MainActivity.this,QuizActivity.class);

        startActivityForResult(intent,REQUEST_CODE_QUIZ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_QUIZ){
            if(resultCode== RESULT_OK){
                int score = data.getIntExtra(QuizActivity.EXTRA_POINT,0);
                if (score > highscore){
                    updateScore (score);
                }

            }
        }
    }

    private void loadScore(){
        SharedPreferences prefs = getSharedPreferences(SHARED_PRE, MODE_PRIVATE);
        highscore = prefs.getInt(HIGH_SCOREx, 0);
        textViewHighScore.setText("Highscore:" + highscore);

    }
    private void updateScore(int newHighScore){
        highscore = newHighScore;
        textViewHighScore.setText("Highscore:" + highscore);

        SharedPreferences prefs = getSharedPreferences(SHARED_PRE, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(HIGH_SCOREx, highscore);
        editor.apply();
    }
}

