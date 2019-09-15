package android.bignerdranch.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;


public class QuizActivity extends AppCompatActivity {

    //debug constants
    private static final String TAG = "QuizActivity";

    //service constant
    private static final String EXTRA_ANSWER_IS_TRUE = "android.bignerdranch.com.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "android.bignerdranch.com.answer_shown";
    private static final String CHEAT_ATTEMPT_COUNTER = "android.bignerdranch.com.cheat_count";


    //save indexes
    private static final String KEY_INDEX = "index";
    private static final String KEY_QUESTIONS = "questions";
    private static final String KEY_CORRECT_ANSWERS = "questions_сount";
    private static final String CHEAT_COUNTER = "cheat_attempts";

    //intent's codes
    private static final int REQUEST_CODE_CHEAT = 0;

    //buttons
    private Button mTrueButton;
    private Button mFalseButton;
    private Button mCheatButton;
    private ImageButton mPrevButton;
    private ImageButton mNextButton;

    //textview
    private TextView mQuestionTextView;

    private Question[] mQuestionBank;
    private int mCurrentIndex;
    private int mCorrectAnswersCount;

    private static int cheatAttemptsCounter = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);

        // extracting saved data
        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            Gson gson = new Gson();

            mQuestionBank = savedInstanceState.get(KEY_QUESTIONS) != null ?
                    gson.fromJson(savedInstanceState.get(KEY_QUESTIONS).toString(), Question[].class) :
                    new Question[]{
                            new Question(R.string.question_austalia, true),
                            new Question(R.string.question_oceans, true),
                            new Question(R.string.question_mideast, false),
                            new Question(R.string.question_africa, false),
                            new Question(R.string.question_americas, true),
                            new Question(R.string.question_asia, true)
                    };
            mCorrectAnswersCount = savedInstanceState.getInt(KEY_CORRECT_ANSWERS, 0);
            cheatAttemptsCounter = savedInstanceState.getInt(CHEAT_COUNTER, 3);

        } else {
            cheatAttemptsCounter = 3;
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


        mCheatButton = findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // start activity
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(QuizActivity.this, answerIsTrue, cheatAttemptsCounter);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });


        //initializing questions
        updateQuestion();
        checkCheat();
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
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        Gson gson = new Gson();
        String json = gson.toJson(mQuestionBank);

        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putString(KEY_QUESTIONS, json);
        savedInstanceState.putInt(CHEAT_COUNTER, cheatAttemptsCounter);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mQuestionBank[mCurrentIndex].setCheated(wasAnswerShown(data));
            cheatAttemptsCounter = getCheatAttemptsCounter(data);
        }
    }

    public void enableAnswer() {
        mFalseButton.setEnabled(true);
        mTrueButton.setEnabled(true);
    }

    public void disableAnswer() {
        mFalseButton.setEnabled(false);
        mTrueButton.setEnabled(false);
    }

    public void checkCheat() {
        if(cheatAttemptsCounter == 0) {
            mCheatButton.setEnabled(false);
        } else {
            mCheatButton.setEnabled(true);
        }
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

        if (cheatAttemptsCounter == 0) {
            mCheatButton.setEnabled(false);
        }

        int messageResId;
        if (!isAlreadyAnswered) {
            if (mQuestionBank[mCurrentIndex].isCheated()) {
                messageResId = R.string.judgment_toast;
            } else {
                if (userPressedTrue == answerIsTrue) {
                    mCorrectAnswersCount++;
                    messageResId = R.string.correct_toast;
                } else {
                    messageResId = R.string.incorrect_toast;
                }
            }
            mQuestionBank[mCurrentIndex].setAlreadyAnswer();
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

    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

    public static int getCheatAttemptsCounter(Intent result) {
        return result.getIntExtra(CHEAT_ATTEMPT_COUNTER, 3);
    }
}
