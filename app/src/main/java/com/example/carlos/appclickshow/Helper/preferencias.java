package com.example.carlos.appclickshow.Helper;

import android.content.Context;
import android.content.SharedPreferences;

public class preferencias {

    private Context context;
    private SharedPreferences preferences;
    private String nomeArquivo = "projetoApp.preferencias";
    private int MODE = 0;
    private SharedPreferences.Editor editor;

    private final String chaveIdentificador = "identificarUsuarioLogado";
    private final String chaveNome = "nomeUsuarioLogado";

    public preferencias (Context context){
        this.context = context;
        preferences = context.getSharedPreferences(nomeArquivo, MODE);

        editor = preferences.edit();
    }

    public void salvarUsuarioPreferencias (String identificadorUsuario, String nomeUsuario){
        editor.putString(chaveIdentificador, identificadorUsuario);
        editor.putString(chaveNome, nomeUsuario);
        editor.commit();
    }

    public String getIdentificador(){

        return preferences.getString(chaveIdentificador,null);
    }

    public String getNome(){

        return preferences.getString(chaveNome,null);
    }

}
