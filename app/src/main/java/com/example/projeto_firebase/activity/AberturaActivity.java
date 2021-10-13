package com.example.projeto_firebase.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.projeto_firebase.MainActivity;
import com.example.projeto_firebase.R;

public class AberturaActivity extends AppCompatActivity {

    String TAG = "Aula de Reforco";
    int tempoDEEspera = 1000 *5;//aqui coloca o tempo de espera

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abertura);

        Log.d(TAG, "Tela Carregada ");
        trocarTela();
    }

    private void trocarTela(){
        Log.d(TAG, "instartando a Main ");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent trocarDeTela = new Intent(AberturaActivity.this,MainActivity.class);
                startActivity(trocarDeTela);
                finish();

            }
        },tempoDEEspera);

    }
}