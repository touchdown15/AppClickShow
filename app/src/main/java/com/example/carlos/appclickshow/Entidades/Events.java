package com.example.carlos.appclickshow.Entidades;

import com.example.carlos.appclickshow.Config.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;

public class Events {

    private String idEvento;
    private String nomeEvento;
    private String descricaoEvento;

    public Events() {
    }

    public String getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(String idEvento) {
        this.idEvento = idEvento;
    }

    public String getNomeEvento() {
        return nomeEvento;
    }

    public void setNomeEvento(String nomeEvento) {
        this.nomeEvento = nomeEvento;
    }

    public String getDescricaoEvento() {
        return descricaoEvento;
    }

    public void setDescricaoEvento(String descricaoEvento) {
        this.descricaoEvento = descricaoEvento;
    }
}
