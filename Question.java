package com.example.ryanh.onlinequiz;

import android.os.Parcel;
import android.os.Parcelable;

public class Question implements Parcelable {

    public static final String LevelDiff_Easy = "Easy";
    public static final String LevelDiff_Mid = "Medium";
    public static final String LevelDiff_Hard = "Hard";





    private String question;
    private String opt1;
    private String opt2;
    private String opt3;
    private int answerNr;
    private String level;


    public Question (){

    }

    public Question(String question, String option1, String option2, String option3, int answerNr, String level) {
        this.question = question;
        this.opt1 = option1;
        this.opt2 = option2;
        this.opt3 = option3;
        this.answerNr = answerNr;
        this.level = level;

    }

    protected Question(Parcel in) {
        question = in.readString();
        opt1 = in.readString();
        opt2 = in.readString();
        opt3 = in.readString();
        answerNr = in.readInt();
        level = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(question);
        dest.writeString(opt1);
        dest.writeString(opt2);
        dest.writeString(opt3);
        dest.writeInt(answerNr);
        dest.writeString(level);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOption1() {
        return opt1;
    }

    public void setOption1(String option1) {
        this.opt1 = option1;
    }

    public String getOption2() {
        return opt2;
    }

    public void setOption2(String option2) {
        this.opt2 = option2;
    }

    public String getOption3() {
        return opt3;
    }

    public void setOption3(String option3) {
        this.opt3 = option3;
    }

    public int getAnswerNr() {
        return answerNr;
    }

    public void setAnswerNr(int answerNr) {
        this.answerNr = answerNr;
    }


    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public static String [] getAllLevelDiff(){
        return new String[] {
                LevelDiff_Hard,
                LevelDiff_Easy,
                LevelDiff_Mid,


        };
    }
}
