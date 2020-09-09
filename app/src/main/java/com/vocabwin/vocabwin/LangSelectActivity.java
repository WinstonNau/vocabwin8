package com.vocabwin.vocabwin;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LangSelectActivity extends AppCompatActivity {

    private static final String TAG = "LangSelectActivity";
    public static final String EXTRA_MESSAGE = "com.vocabwin.vocabwin." + TAG;
    public static final String EXTRA_MESSAGE_2 = "com.vocabwin.vocabwin." + TAG + ".lang";
    public String vocabularys;
    public String language = "german";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lang_select);

        Intent intent = getIntent();

        vocabularys = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
    }

    public void backToMenu(View view) {
        finish();
    }

    public void ENtoDE(View view) {

        switch(view.getId()) {
            case R.id.EnToDeButton:
                language = "german";
                break;
            case R.id.DeToEnButton:
                language = "english";
                break;
        }

        Intent intent = new Intent(this, GameActivity.class);

        intent.putExtra(EXTRA_MESSAGE, vocabularys);
        intent.putExtra(EXTRA_MESSAGE_2, language);

        startActivity(intent);
        finish();

    }
}
