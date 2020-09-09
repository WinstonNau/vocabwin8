package com.vocabwin.vocabwin;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class EndGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);

        Intent intent = getIntent();

        String points = intent.getStringExtra(GameActivity.EXTRA_MESSAGE);
        String maxPoints = intent.getStringExtra(GameActivity.EXTRA_MESSAGE_2);

        TextView point = findViewById(R.id.point);

        point.setText(points + " / " + maxPoints + " Points");
    }

    public void backToMainMenu(View view) {
        finish();
    }
}
