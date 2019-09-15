package android.bignerdranch.com;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private static final String EXTRA_ANSWER_IS_TRUE = "android.bignerdranch.com.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "android.bignerdranch.com.answer_shown";
    private static final String CHEAT_ATTEMPT_COUNTER = "android.bignerdranch.com.cheat_count";


    private boolean mAnswerIsTrue;
    private int mAvailableCheatCounter;

    private TextView mAnswerTextView;
    private Button mShowAnswerButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);
        mAvailableCheatCounter = getIntent().getIntExtra(CHEAT_ATTEMPT_COUNTER, 0);

        mAnswerTextView = (TextView) findViewById(R.id.answer_text_view);
        mShowAnswerButton = (Button) findViewById(R.id.show_answer_button);
        mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAnswerIsTrue) {
                    mAnswerTextView.setText(R.string.true_button);
                } else {
                    mAnswerTextView.setText(R.string.false_button);
                }
                mAvailableCheatCounter--;
                setAnswerShownResult(true);
            }
        });
    }

    private void setAnswerShownResult(boolean isAnswerShown) {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        data.putExtra(CHEAT_ATTEMPT_COUNTER, mAvailableCheatCounter);
        setResult(RESULT_OK, data);
    }

    public static Intent newIntent(Context packageContext, boolean answerIsTrue, int availableCheatCount) {
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        intent.putExtra(CHEAT_ATTEMPT_COUNTER, availableCheatCount);
        return intent;
    }
}
