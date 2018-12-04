package com.example.ryanh.onlinequiz;

import android.provider.BaseColumns;

public final class Contract {

    private Contract(){


    }

    public static class QuestionTable implements BaseColumns {

        public static final String Table_Name = "quiz_questions";
        public static final String Column_question = "questions";
        public static final String Column_option1 = "option1";
        public static final String Column_option2 = "option2";
        public static final String Column_option3 = "option3";
        public static final String Column_answer_nr = "answer_nr";
        public static final String Column_LevelDiff = "difficulty";

        }
}
