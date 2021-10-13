package com.example.projeto_firebase.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.projeto_firebase.MainActivity;
import com.example.projeto_firebase.R;

public class AberturaActivity extends AppCompatActivity implements Runnable{

    Thread thread;
    Handler handler;
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abertura);

        handler = new Handler();
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        i = 1;

        try {
            while (i<200) {
                Thread.sleep(20);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        i++;
                    }
                });
            }
        }catch (Exception e) {

        }

        startActivity(new Intent(this, MainActivity.class));
    }
}