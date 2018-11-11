package com.example.carlos.appclickshow.Entidades;

import com.example.carlos.appclickshow.Config.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Usuarios {

    private String id;
    private String email;
    private String senha;
    private String nome;
    private String numeroCel;
    private String admin;

    public Usuarios() {
    }

    public void salvar(){
        DatabaseReference referencialFirebase = ConfiguracaoFirebase.getFirebase();
        referencialFirebase.child("usuario").child(String.valueOf(getId())).setValue(this);

    }

    @Exclude
    public Map<String,Object> toMap(){
        HashMap<String,Object> hashMapUsuario = new HashMap<>();

        hashMapUsuario.put("id", getId());
        hashMapUsuario.put("email", getEmail());
        hashMapUsuario.put("senha", getSenha());
        hashMapUsuario.put("nome", getNome());
        hashMapUsuario.put("numeroCel", getNumeroCel());
        hashMapUsuario.put("admin", getAdmin());

        return hashMapUsuario;

    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    @Exclude
    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNumeroCel() {
        return numeroCel;
    }

    public void setNumeroCel(String numeroCel) {
        this.numeroCel = numeroCel;
    }
}
