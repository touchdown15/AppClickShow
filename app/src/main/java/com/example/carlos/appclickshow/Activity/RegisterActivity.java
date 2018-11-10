package com.example.carlos.appclickshow.Activity;

import android.content.Intent;
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
import com.example.carlos.appclickshow.Helper.PreferenciasAndroid;
import com.example.carlos.appclickshow.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private EditText editNome;
    private EditText editCel;
    private EditText editEmail;
    private EditText editSenha;
    private EditText editConfirmaSenha;
    private Button btnGravar;
    private Usuarios usuarios;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editNome = (EditText)findViewById(R.id.edtCadNome);
        editCel = (EditText)findViewById(R.id.edtCodCel);
        editEmail = (EditText)findViewById(R.id.edtCodEmail);
        editSenha = (EditText)findViewById(R.id.edtCodSenha);
        editConfirmaSenha = (EditText)findViewById(R.id.edtCodConfirmaSenha);
        btnGravar = (Button) findViewById(R.id.btnGravar);

        btnGravar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editSenha.getText().toString().equals(editConfirmaSenha.getText().toString())){
                    usuarios = new Usuarios();
                    usuarios.setNome(editNome.getText().toString());
                    usuarios.setNumeroCel(editCel.getText().toString());
                    usuarios.setEmail(editEmail.getText().toString());
                    usuarios.setSenha(editSenha.getText().toString());

                    cadastrarUsuario();

                }else{

                    Toast.makeText(RegisterActivity.this, "As senhas não são correspondentes", Toast.LENGTH_LONG).show();

                }
            }

        });

    }

    private void cadastrarUsuario(){

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuarios.getEmail(),
                usuarios.getSenha()
        ).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "Usuário cadastrado com sucesso", Toast.LENGTH_LONG).show();

                    String identificadorUsuario = Base64Custom.codificarBase64(usuarios.getEmail());
                    FirebaseUser usuarioFirebase = task.getResult().getUser();
                    usuarios.setId(identificadorUsuario);
                    usuarios.salvar();

                    PreferenciasAndroid preferenciasAndroid = new PreferenciasAndroid(RegisterActivity.this);
                    preferenciasAndroid.salvarUsuarioPreferencias(identificadorUsuario, usuarios.getNome());

                    abrirLoginUsuario();
                }else{
                    String erroExcecao = "";

                    try{

                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        erroExcecao = "Digite uma senha mais forte, contendo no minimo 8 caracteres de letras e números";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        erroExcecao = "O e-mail digitado é inválido, digite um novo e-mail";
                    }catch (FirebaseAuthUserCollisionException e) {
                        erroExcecao = "Esse e-mail já está cadastrado no sistema";
                    }catch (Exception e) {
                        erroExcecao = "Erro ao efetuar o cadastro!";
                        e.printStackTrace();
                    }

                    Toast.makeText(RegisterActivity.this, "Erro: " + erroExcecao, Toast.LENGTH_LONG).show();

                }
            }
        });

    }

    public void abrirLoginUsuario(){
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


}
