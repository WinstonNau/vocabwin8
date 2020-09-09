package com.vocabwin.vocabwin;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GameActivity extends AppCompatActivity {

    private static final String TAG = "GameActivity";

    public static final String EXTRA_MESSAGE = "com.vocabwin.vocabwin" + TAG;
    public static final String EXTRA_MESSAGE_2 = "com.vocabwin.vocabwin" + TAG + ".max";

    private JSONArray words;
    private String vocabularys;
    private String language;
    private int points = 0;
    private int round = 0;
    private int randomAnswerChoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();

        vocabularys = intent.getStringExtra(LangSelectActivity.EXTRA_MESSAGE);
        language = intent.getStringExtra(LangSelectActivity.EXTRA_MESSAGE_2);

        try {
            words = new JSONArray(vocabularys);
            setNewText();
        } catch (JSONException e) {
            Log.v(TAG, e.getMessage());
        }

        Log.v(TAG, vocabularys + "\n" + language);
    }

    public void onClick(View view) {

        final TextView choice_1 = findViewById(R.id.choice1);
        final TextView choice_2 = findViewById(R.id.choice2);
        final TextView choice_3 = findViewById(R.id.choice3);
        final TextView choice_4 = findViewById(R.id.choice4);

        choice_1.setEnabled(false);
        choice_2.setEnabled(false);
        choice_3.setEnabled(false);
        choice_4.setEnabled(false);

        switch(view.getId()) {
            case R.id.choice1:
                switch (randomAnswerChoice) {
                     case 1:
                         choice_1.setBackgroundColor(Color.parseColor("#25E91B"));
                         points += 1;
                         break;
                     case 2:
                         choice_2.setBackgroundColor(Color.parseColor("#25E91B"));
                         choice_1.setBackgroundColor(Color.parseColor("#EA0707"));
                         break;
                     case 3:
                         choice_3.setBackgroundColor(Color.parseColor("#25E91B"));
                         choice_1.setBackgroundColor(Color.parseColor("#EA0707"));
                         break;
                     case 4:
                         choice_4.setBackgroundColor(Color.parseColor("#25E91B"));
                         choice_1.setBackgroundColor(Color.parseColor("#EA0707"));
                         break;
                }
                break;
            case R.id.choice2:
                switch (randomAnswerChoice) {
                    case 1:
                        choice_1.setBackgroundColor(Color.parseColor("#25E91B"));
                        choice_2.setBackgroundColor(Color.parseColor("#EA0707"));
                        break;
                    case 2:
                        choice_2.setBackgroundColor(Color.parseColor("#25E91B"));
                        points += 1;
                        break;
                    case 3:
                        choice_3.setBackgroundColor(Color.parseColor("#25E91B"));
                        choice_2.setBackgroundColor(Color.parseColor("#EA0707"));
                        break;
                    case 4:
                        choice_4.setBackgroundColor(Color.parseColor("#25E91B"));
                        choice_2.setBackgroundColor(Color.parseColor("#EA0707"));
                        break;
                }
                break;
            case R.id.choice3:
                switch (randomAnswerChoice) {
                    case 1:
                        choice_1.setBackgroundColor(Color.parseColor("#25E91B"));
                        choice_3.setBackgroundColor(Color.parseColor("#EA0707"));
                        break;
                    case 2:
                        choice_2.setBackgroundColor(Color.parseColor("#25E91B"));
                        choice_3.setBackgroundColor(Color.parseColor("#EA0707"));
                        break;
                    case 3:
                        choice_3.setBackgroundColor(Color.parseColor("#25E91B"));
                        points += 1;
                        break;
                    case 4:
                        choice_4.setBackgroundColor(Color.parseColor("#25E91B"));
                        choice_3.setBackgroundColor(Color.parseColor("#EA0707"));
                        break;
                }
                break;
            case R.id.choice4:
                switch (randomAnswerChoice) {
                    case 1:
                        choice_1.setBackgroundColor(Color.parseColor("#25E91B"));
                        choice_4.setBackgroundColor(Color.parseColor("#EA0707"));
                        break;
                    case 2:
                        choice_2.setBackgroundColor(Color.parseColor("#25E91B"));
                        choice_4.setBackgroundColor(Color.parseColor("#EA0707"));
                        break;
                    case 3:
                        choice_3.setBackgroundColor(Color.parseColor("#25E91B"));
                        choice_4.setBackgroundColor(Color.parseColor("#EA0707"));
                        break;
                    case 4:
                        choice_4.setBackgroundColor(Color.parseColor("#25E91B"));
                        points += 1;
                        break;
                }
                break;
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkRound();
                setNewText();
            }
        }, 1000);
    }

    public void backToMainMenu(View view) {
        finish();
    }

    private void checkRound() {
        if (round >= 19) {
            round = 20;
            goEnd();
        } else {
            ++round;
        }
    }

    private void goEnd() {
        Intent intent = new Intent(this, EndGameActivity.class);
        intent.putExtra(EXTRA_MESSAGE, "" + points);
        intent.putExtra(EXTRA_MESSAGE_2, "" + round);
        startActivity(intent);
        finish();
    }

    private void setNewText() {
        try {
            final TextView mainVocabulary = findViewById(R.id.textView);

            TextView pointsView = findViewById(R.id.points);
            TextView roundView = findViewById(R.id.round);

            final TextView choice_1 = findViewById(R.id.choice1);
            final TextView choice_2 = findViewById(R.id.choice2);
            final TextView choice_3 = findViewById(R.id.choice3);
            final TextView choice_4 = findViewById(R.id.choice4);

            JSONObject jsonObj = words.getJSONObject(round);
            final String answer = jsonObj.getString(language);
            final String question = language.equals("english") ? jsonObj.getString("german") : jsonObj.getString("english");
            String speech = jsonObj.getString("speech");

            Log.v(TAG, jsonObj + " " + words);

            RequestQueue queue = Volley.newRequestQueue(this);

            String answerWithoutSpaces = answer.replaceAll(" ", "%20");

            String answerWithoutSpacesAndSharpS = answerWithoutSpaces.replaceAll("ß", "%DF");

            String url = "https://vocabwin.herokuapp.com/falseChoices?word=" + answerWithoutSpacesAndSharpS + "&lang=" + language + "&speech=" + speech;

            pointsView.setText("" + points);

            roundView.setText("" + (round + 1));

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.v(TAG, response + " 1");

                            try {

                                Log.v(TAG, response + " 2");

                                JSONArray jsonArrayyy = new JSONArray(response);

                                Log.v(TAG, "test");

                                JSONObject jsonObj1 = jsonArrayyy.getJSONObject(0);
                                JSONObject jsonObj2 = jsonArrayyy.getJSONObject(1);
                                JSONObject jsonObj3 = jsonArrayyy.getJSONObject(2);

                                Log.v(TAG, jsonObj1 + " " + jsonObj2 + " " + jsonObj3);

                                choice_1.setBackgroundColor(Color.parseColor("#33B5E5"));
                                choice_2.setBackgroundColor(Color.parseColor("#33B5E5"));
                                choice_3.setBackgroundColor(Color.parseColor("#33B5E5"));
                                choice_4.setBackgroundColor(Color.parseColor("#33B5E5"));

                                randomAnswerChoice = (int) (Math.random() * 4 + 1);

                                Log.v(TAG, randomAnswerChoice + "");

                                mainVocabulary.setText(question);

                                switch(randomAnswerChoice) {
                                    case 1:
                                        choice_1.setText(answer);
                                        choice_2.setText(jsonObj1.getString(language));
                                        choice_3.setText(jsonObj2.getString(language));
                                        choice_4.setText(jsonObj3.getString(language));
                                        Log.v(TAG, "case 1");
                                        break;
                                    case 2:
                                        choice_2.setText(answer);
                                        choice_1.setText(jsonObj1.getString(language));
                                        choice_3.setText(jsonObj2.getString(language));
                                        choice_4.setText(jsonObj3.getString(language));
                                        Log.v(TAG, "case 2");
                                        break;
                                    case 3:
                                        choice_3.setText(answer);
                                        choice_1.setText(jsonObj1.getString(language));
                                        choice_2.setText(jsonObj2.getString(language));
                                        choice_4.setText(jsonObj3.getString(language));
                                        Log.v(TAG, "case 3");
                                        break;
                                    case 4:
                                        choice_4.setText(answer);
                                        choice_1.setText(jsonObj1.getString(language));
                                        choice_2.setText(jsonObj2.getString(language));
                                        choice_3.setText(jsonObj3.getString(language));
                                        Log.v(TAG, "case 4");
                                        break;
                                }

                                choice_1.setEnabled(true);
                                choice_2.setEnabled(true);
                                choice_3.setEnabled(true);
                                choice_4.setEnabled(true);

                            } catch (JSONException e) {
                                Log.v(TAG, e.getMessage() + "\n\n" + e);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.v(TAG, error.getMessage() + "");
                }

            });

            queue.add(stringRequest);
        } catch (JSONException e) {
            if (round < 19) {
                Log.v(TAG, e.getMessage() + "");
                goEnd();
            }
        }

    }
}
