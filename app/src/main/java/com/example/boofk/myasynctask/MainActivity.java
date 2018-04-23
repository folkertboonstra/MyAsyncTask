package com.example.boofk.myasynctask;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private final static long PASSWORD = 12345;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick_StartTask(View v) {
        long rate;
        long range;

        EditText editRate = (EditText) findViewById (R.id.rate);
        EditText editRange = (EditText) findViewById (R.id.range);
        rate = Long.valueOf(editRate.getText().toString());
        range = Long.valueOf(editRange.getText().toString());

        displayProgress("");
        clearAnswer();

        PasswordGuesserTask crackerTask = new PasswordGuesserTask();
        crackerTask.execute(range, rate);
    }

    public void displayProgress(String message) {
        TextView textview = (TextView) findViewById(R.id.status);
        textview.setText(message);
    }

    public void clearAnswer() {
        String message = "";
        TextView textview = (TextView) findViewById (R.id.answer);
        textview.setText(message);
    }
    public void displayAnswer(long answer) {
        String message = " I know the password: " + answer;
        TextView textview = (TextView) findViewById (R.id.answer);
        textview.setText(message);
    }

    /**
     * Generics:
     * Long - Type of reference(s) passed to doInBackground
     * String - Type of reference passed to onProgressUpdate
     * Long - Type of reference returned by doInBackground
     *          value passed to onPostExecute
     */
    private class PasswordGuesserTask extends AsyncTask<Long,String,Long> {

        @Override
        protected void onPostExecute(Long guess) {
            super.onPostExecute(guess);
            displayAnswer(guess);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

            String message = "";
            for (String str: values)
                message += str + ", ";
            displayProgress(message);
        }

        @Override
        protected Long doInBackground(Long... params) {
            long range = params[0];
            long publishRate = params[1];

            long guess = 0;
            long count = 0;
            Random rand = new Random();
            while (guess != PASSWORD) {
                guess = Math.abs(rand.nextLong()) % range;
                count++;
                if (count % publishRate == 0) {
                    publishProgress("Guess #:" + count, "Last guess: " + guess);
                }
            }
            return guess;
        }
    }
}
