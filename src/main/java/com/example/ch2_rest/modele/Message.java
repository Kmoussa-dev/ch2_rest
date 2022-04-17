package com.example.ch2_rest.modele;

public class Message {
    private Long id;
    private String texte;

    public Message(Long id, String texte) {
        this.id = id;
        this.texte = texte;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTexte() {
        return texte;
    }

    public void setTexte(String texte) {
        this.texte = texte;
    }
}
