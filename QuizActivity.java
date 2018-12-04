package com.example.ryanh.onlinequiz;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {
    public static final String EXTRA_POINT = "extraPoint";
    private static long COUNTDOWN_MILLIS = 30000;

    private static final String Score_KEY = "scoreKey";
    private static final String KEY_QUESTION_COUNT = "keyQuestCount";
    private static final String KEY_LEFT = "leftKey";
    private static final String KEY_ANS = "keyAns";
    private static final String KEY_QUEST_LIST ="keyQuestList";






    private TextView textViewQuestion;
    private TextView textViewScore;
    private TextView textViewQuestionCount;
    private TextView textViewQuestionCountDown;
    private RadioGroup rbGroup;
    private RadioButton b1;
    private RadioButton b2;
    private RadioButton b3;
    private Button buttonConfirmNext;
    private ColorStateList textColorDefaultRb;
    private ColorStateList getTextColorDefaultCd;

    private CountDownTimer countDownTimer;
    private long timeleft;


    private ArrayList<Question> questionList;
    private int questionCounter;
    private int questionCountTotal;
    private Question currentQuestion;

    private int score;
    private boolean ans;
    private long backPress;
    private long leftKey;
    private boolean keyAns;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        textViewQuestion = findViewById(R.id.text_view_question);
        textViewScore = findViewById(R.id.text_view_score);
        textViewQuestionCount = findViewById(R.id.text_view_question_count);
        textViewQuestionCountDown = findViewById(R.id.text_view_question_countdown);
        rbGroup = findViewById(R.id.radio_group);
        b1 = findViewById(R.id.radio_butt1);
        b2 = findViewById(R.id.radio_butt2);
        b3 = findViewById(R.id.radio_butt3);
        buttonConfirmNext = findViewById(R.id.confirm_buttnext);

        textColorDefaultRb = b1.getTextColors();
        getTextColorDefaultCd = textViewQuestionCountDown.getTextColors();

        new QuizDtb(this).fillQuestionsTable(); // insert questions

        if(savedInstanceState == null) {

            // DEBUG:
            Log.i("QuizActivity", "savedInstanceState == null");

            QuizDtb quizDtb = new QuizDtb(this);
            questionList = quizDtb.getQuestions("Easy"); // get all "Easy" questions
            questionCountTotal = questionList.size();
            Collections.shuffle(questionList);

            showNextQuestion();
        }else {
            questionList = savedInstanceState.getParcelableArrayList(KEY_QUEST_LIST);

            if(questionList == null ){
                finish();
            }
            questionCountTotal = questionList.size();
            questionCounter = savedInstanceState.getInt(KEY_QUESTION_COUNT);
            currentQuestion = questionList.get(questionCounter -1);
            score = savedInstanceState.getInt(Score_KEY);
            timeleft = savedInstanceState.getLong(KEY_LEFT);
            ans = savedInstanceState.getBoolean(KEY_ANS);

            if(!ans){

                countDownStart();
            } else{
                updateCountDownText();
                displayAns();
            }


        }

        // button
        buttonConfirmNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ans) {
                    if (b1.isChecked() || b2.isChecked() || b3.isChecked()) {
                        checkAns();

                    } else {

                        Toast.makeText(QuizActivity.this, "Choose an answer", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    showNextQuestion(); // show next question
                }

            }

        });

    }

    private void showNextQuestion() {
        b1.setTextColor(textColorDefaultRb);
        b2.setTextColor(textColorDefaultRb);
        b3.setTextColor(textColorDefaultRb);
        rbGroup.clearCheck();


        if (questionCounter < questionCountTotal) {
            currentQuestion = questionList.get(questionCounter);

            textViewQuestion.setText(currentQuestion.getQuestion());
            b1.setText(currentQuestion.getOption1());
            b2.setText(currentQuestion.getOption2());
            b3.setText(currentQuestion.getOption3());

            questionCounter++;
            textViewQuestionCount.setText("Question" + questionCounter + "/" + questionCountTotal);
            ans = false;
            buttonConfirmNext.setText("Confirm");

            timeleft = COUNTDOWN_MILLIS;
            countDownStart();


        } else {
            overQuiz();

        }


    }

    private void countDownStart() {
        countDownTimer = new CountDownTimer(timeleft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeleft = millisUntilFinished;
                updateCountDownText();


            }

            @Override
            public void onFinish() {

                timeleft = 0;
                updateCountDownText();
                checkAns();

            }
        }.start();
    }

    private void updateCountDownText() {
        int minutes = (int) (timeleft / 1000) / 60;
        int seconds = (int) (timeleft / 1000) % 60;

        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        textViewQuestionCountDown.setText(timeFormatted);

        if (timeleft < 10000) {
            textViewQuestionCountDown.setTextColor(Color.RED);
        } else {

            textViewQuestionCountDown.setTextColor(getTextColorDefaultCd);
        }

    }


    private void checkAns() {
        ans = true;

        countDownTimer.cancel();

        RadioButton bSelect = findViewById(rbGroup.getCheckedRadioButtonId());
        int nrAnswer = rbGroup.indexOfChild(bSelect) + 1;

        if (nrAnswer == currentQuestion.getAnswerNr()) {
            score++;
            textViewScore.setText("Score:" + score);
        }

        displayAns();

    }

    private void displayAns() {
        b1.setTextColor(Color.RED);
        b2.setTextColor(Color.RED);
        b3.setTextColor(Color.RED);

        switch (currentQuestion.getAnswerNr()) {
            case 1:
                b1.setTextColor(Color.GREEN);
                textViewQuestion.setText("'A' Answer is correct");
                break;
            case 2:
                b2.setTextColor(Color.GREEN);
                textViewQuestion.setText(" 'B' second Answer is correct");
                break;
            case 3:
                b3.setTextColor(Color.GREEN);
                textViewQuestion.setText(" 'C' Answer is correct");
                break;

        }

        if (questionCounter < questionCountTotal) {
            buttonConfirmNext.setText("Next");

        } else {

            buttonConfirmNext.setText("Finish");
        }

    }


    private void overQuiz() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_POINT, score);
        setResult(RESULT_OK, resultIntent);
        finish();

    }

    @Override
    public void onBackPressed() {
        if (backPress + 2000 > System.currentTimeMillis()) {
            overQuiz();
        } else {
            Toast.makeText(this, "Press back again", Toast.LENGTH_SHORT).show();
        }
        backPress = System.currentTimeMillis();



        super.onDestroy();
        if (countDownTimer != null){
            countDownTimer.cancel();

        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Score_KEY,score);
        outState.putInt(KEY_QUESTION_COUNT,questionCounter);
        outState.putLong(KEY_LEFT,timeleft);
        outState.putBoolean(KEY_ANS,ans);
        outState.putParcelableArrayList(KEY_QUEST_LIST,questionList);



    }
}

