package android.bignerdranch.com;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {
    //debug constants
    private static final String TAG = "QuizActivity";

    //save indexes
    private static final String KEY_INDEX = "index";
    private static final String KEY_QUESTIONS = "questions";
    private static final String KEY_CORRECT_ANSWERS = "questions";


    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mPrevButton;
    private ImageButton mNextButton;
    private TextView mQuestionTextView;

    private Question[] mQuestionBank;
    private int mCurrentIndex;
    private int mCorrectAnswersCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);

        // extracting saved data
        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);

            mQuestionBank = savedInstanceState.get(KEY_QUESTIONS) != null ?
                    (Question[]) savedInstanceState.get(KEY_QUESTIONS) :
                    new Question[]{
                            new Question(R.string.question_austalia, true),
                            new Question(R.string.question_oceans, true),
                            new Question(R.string.question_mideast, false),
                            new Question(R.string.question_africa, false),
                            new Question(R.string.question_americas, true),
                            new Question(R.string.question_asia, true)
                    };
            mCorrectAnswersCount = savedInstanceState.getInt(KEY_CORRECT_ANSWERS, 0);

        } else {
            mCurrentIndex = 0;
            mCorrectAnswersCount = 0;
            mQuestionBank = new Question[]{
                    new Question(R.string.question_austalia, true),
                    new Question(R.string.question_oceans, true),
                    new Question(R.string.question_mideast, false),
                    new Question(R.string.question_africa, false),
                    new Question(R.string.question_americas, true),
                    new Question(R.string.question_asia, true)
            };
        }

        mQuestionTextView = findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (++mCurrentIndex) % mQuestionBank.length;
                updateQuestion();
            }
        });


        mTrueButton = findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
                disableAnswer();
            }
        });
        mFalseButton = findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(false);
                disableAnswer();
            }
        });


        mNextButton = findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (++mCurrentIndex) % mQuestionBank.length;
                updateQuestion();

            }
        });
        mPrevButton = findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = mCurrentIndex > 0 ? --mCurrentIndex : --mCurrentIndex + mQuestionBank.length;
                updateQuestion();
            }
        });

        updateQuestion();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    public void enableAnswer() {
        mFalseButton.setEnabled(true);
        mTrueButton.setEnabled(true);
    }

    public void disableAnswer() {
        mFalseButton.setEnabled(false);
        mTrueButton.setEnabled(false);
    }

    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextRestId();
        mQuestionTextView.setText(question);
        if (mQuestionBank[mCurrentIndex].getAlreadyAnswer()) {
            disableAnswer();
        } else {
            enableAnswer();
        }
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        boolean isAlreadyAnswered = mQuestionBank[mCurrentIndex].getAlreadyAnswer();

        int messageResId;
        if (!isAlreadyAnswered) {
            if (userPressedTrue == answerIsTrue) {
                mCorrectAnswersCount++;
                messageResId = R.string.correct_toast;
                mQuestionBank[mCurrentIndex].setAlreadyAnswer();
            } else {
                messageResId = R.string.incorrect_toast;
                mQuestionBank[mCurrentIndex].setAlreadyAnswer();
            }

            Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
        } else {
            messageResId = R.string.alreadyAnswer;
            Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
        }

        calculateScore();
    }

    private void calculateScore() {
        for (Question question : mQuestionBank) {
            if (!question.getAlreadyAnswer()) return;
        }
        int score = mCorrectAnswersCount * 100 / mQuestionBank.length;
        String message = getString(R.string.toast_score, score);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
