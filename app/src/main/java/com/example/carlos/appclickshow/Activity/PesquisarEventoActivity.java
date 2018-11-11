package com.example.carlos.appclickshow.Activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.carlos.appclickshow.Entidades.Events;
import com.example.carlos.appclickshow.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class PesquisarEventoActivity extends AppCompatActivity {

    private EditText editPalavra;
    private ListView listVPesquisa;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mRef;

    private List<Events> listEventos = new ArrayList<Events>();
    private ArrayAdapter<Events> arrayAdapterEventos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisar_evento);

        inicializarComponentes();
        inicializarFirebase();
        eventoPesquisa();

    }

    private void eventoPesquisa(){
        editPalavra.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String palavra = editPalavra.getText().toString().trim();
                pesquisarPalavra(palavra);
            }
        });
    }

    private void pesquisarPalavra (final String palavra){
        Query query;
        if (palavra.equals("")){
            query = mRef.child("addEventos");
            //.orderByChild("nomeEvento")
        }else{
            query = mRef.child("addEventos").equalTo(palavra);
        }

        listEventos.clear();

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot objSnapshot:dataSnapshot.getChildren()){
                    Events event = objSnapshot.getValue(Events.class);
                    listEventos.add(event);
                }


                arrayAdapterEventos = new ArrayAdapter<Events>(PesquisarEventoActivity.this,
                        android.R.layout.simple_list_item_1,listEventos);
                listVPesquisa.setAdapter(arrayAdapterEventos);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void inicializarFirebase(){
        FirebaseApp.initializeApp(PesquisarEventoActivity.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        mRef = firebaseDatabase.getReference();
    }

    private void inicializarComponentes(){
        editPalavra = (EditText) findViewById(R.id.editPesquisaADM);
        listVPesquisa = (ListView) findViewById(R.id.listVpesquisa);
    }

    @Override
    protected void onResume() {
        super.onResume();
        pesquisarPalavra("");
    }
}
