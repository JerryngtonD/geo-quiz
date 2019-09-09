package android.bignerdranch.com;

public class Question {
    private int mTextRestId;
    private boolean mAnswerTrue;
    private boolean mAlreadyAnswer;


    public Question(int textRestId, boolean answerTrue) {
        mTextRestId = textRestId;
        mAnswerTrue = answerTrue;
    }

    public boolean getAlreadyAnswer() {
        return mAlreadyAnswer;
    }

    public void setAlreadyAnswer() {
        mAlreadyAnswer = true;
    }

    public int getTextRestId() {
        return mTextRestId;
    }

    public void setTextRestId(int textRestId) {
        mTextRestId = textRestId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }
}
