package com.simples.acesso.Models;

public class SlidesIntro {

    int Image;
    String titulo;
    String texto;

    public SlidesIntro(int image, String titulo, String texto) {
        Image = image;
        this.titulo = titulo;
        this.texto = texto;
    }

    public int getImage() {
        return Image;
    }

    public void setImage(int image) {
        Image = image;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}
