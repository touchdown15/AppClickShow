package com.example.carlos.appclickshow.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.carlos.appclickshow.R;

public class EventUsuarioActivity extends AppCompatActivity {

    private Button btnPesquisarEvento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_usuario);

        btnPesquisarEvento = (Button) findViewById(R.id.btnPesquisarEventoU);

        //Botão indo para canto errado

        btnPesquisarEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAbrirPesquisarEvento = new Intent(EventUsuarioActivity.this, LoggedActivity.class);
                startActivity(intentAbrirPesquisarEvento);
                finish();
            }
        });

    }
}
