package com.example.carlos.appclickshow.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.carlos.appclickshow.Config.ConfiguracaoFirebase;
import com.example.carlos.appclickshow.Entidades.Usuarios;
import com.example.carlos.appclickshow.Helper.Base64Custom;
import com.example.carlos.appclickshow.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail;
    private EditText edtSenha;
    private Button btnLogar;
    private FirebaseAuth autenticacao;
    private DatabaseReference mRef;
    private Usuarios usuarios;
    private String adminOrUser;
    private ProgressDialog barraDeProgresso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtSenha = (EditText) findViewById(R.id.edtSenha);
        btnLogar = (Button) findViewById(R.id.btnLogar);

        barraDeProgresso = new ProgressDialog(this);


        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edtEmail.getText().toString().equals("") && !edtSenha.getText().toString().equals("") ){

                    usuarios = new Usuarios();
                    usuarios.setEmail(edtEmail.getText().toString());
                    usuarios.setSenha(edtSenha.getText().toString());
                    validarLogin();

                }else{
                    Toast.makeText(LoginActivity.this, "Preencha os campos do e-mail e senha", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void validarLogin(){

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(usuarios.getEmail(), usuarios.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    barraDeProgresso.setMessage("Aguarde...");
                    barraDeProgresso.show();

                    String identificadorUsuario = Base64Custom.codificarBase64(usuarios.getEmail());
                    mRef = ConfiguracaoFirebase.getFirebase().child("usuario").child(identificadorUsuario).child("admin");

                    mRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            adminOrUser= dataSnapshot.getValue().toString();

                            if (adminOrUser.equals("Sim")){
                                barraDeProgresso.dismiss();
                                abrirTelaPosLoginAdmin();
                                Toast.makeText(LoginActivity.this, "Bem Vindo", Toast.LENGTH_SHORT).show();

                            }else{
                                barraDeProgresso.dismiss();
                                abrirTelaPosLoginUsuario();
                                Toast.makeText(LoginActivity.this, "Bem Vindo", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }else{
                    Toast.makeText(LoginActivity.this, "E-mail ou senha inválidos", Toast.LENGTH_SHORT).show();
                }

            }

            });
    }



    public void abrirTelaPosLoginAdmin(){

        Intent intentTelaPosLoginAdmin = new Intent(LoginActivity.this, EventAdminActivity.class);
        startActivity(intentTelaPosLoginAdmin);
        finish();

    }

    public void abrirTelaPosLoginUsuario(){

        Intent intentTelaposLoginUsuario = new Intent(LoginActivity.this, EventUsuarioActivity.class);
        startActivity(intentTelaposLoginUsuario);
        finish();

    }


}
