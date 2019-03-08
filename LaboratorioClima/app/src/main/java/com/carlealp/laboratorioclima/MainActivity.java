package com.carlealp.laboratorioclima;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private TextView textView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);
        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWebsite();
            }
        });

    }

    private void getWebsite() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder builder = new StringBuilder();

                EditText txtciudad = (EditText) findViewById(R.id.editText2);

                String textofinal = txtciudad.getText().toString();

                try {
                    Document doc = Jsoup.connect("https://es.weather-forecast.com/locations/" + textofinal + "/forecasts/latest").get();

                    String title = doc.title();
                    String description = doc.select(".b-forecast__table-description-content").select("span").text();
                    //Elements links = doc.select("a[href]");

                    builder.append(title).append("\n");
                    builder.append("\n").append(description).append("\n\n");

                } catch (Exception e) {
                    builder.append("Error, No hemos encontrado esta ciudad : '" + textofinal + "' .");
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(builder.toString());
                    }
                });

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(txtciudad.getWindowToken(), 0);
            }
        }).start();
    }
}
