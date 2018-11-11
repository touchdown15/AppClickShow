package com.example.carlos.appclickshow.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.carlos.appclickshow.Config.ConfiguracaoFirebase;
import com.example.carlos.appclickshow.Entidades.Events;
import com.example.carlos.appclickshow.Entidades.Usuarios;
import com.example.carlos.appclickshow.R;
import com.google.firebase.database.DatabaseReference;

public class CriarEventoActivity extends AppCompatActivity {

    private EditText editIdEvento;
    private EditText editNomeEvento;
    private EditText editDescricaoEvento;
    private Button btnGravarEvento;
    private Events events;
    private DatabaseReference firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_evento);

        editIdEvento = (EditText)findViewById(R.id.edtIdEvento);
        editNomeEvento = (EditText)findViewById(R.id.edtNomeEvento);
        editDescricaoEvento = (EditText)findViewById(R.id.edtDescricaoEvento);
        btnGravarEvento = (Button) findViewById(R.id.btnGravarEvento);

        btnGravarEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                events = new Events();
                events.setIdEvento(editIdEvento.getText().toString());
                events.setNomeEvento(editNomeEvento.getText().toString());
                events.setDescricaoEvento(editDescricaoEvento.getText().toString());

                cadastrarEvento(events);
                chamarTelaInicial();

            }

        });
    }

    private boolean cadastrarEvento(Events events) {
        try {
            firebase = ConfiguracaoFirebase.getFirebase().child("addEventos");
            firebase.child(events.getIdEvento()).setValue(events);
            Toast.makeText(CriarEventoActivity.this, "Evento inserido com sucesso", Toast.LENGTH_LONG).show();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    private void chamarTelaInicial(){
        Intent intent = new Intent(CriarEventoActivity.this, EventAdminActivity.class);
        startActivity(intent);
        finish();
    }

}
