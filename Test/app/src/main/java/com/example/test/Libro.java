package com.example.test;

public class Libro {
    private String titulo;

    public Libro(String titulo) {
        this.titulo = titulo;
    }

    @Override
    public String toString() {
        return "Libro{" +
                "titulo='" + titulo + '\'' +
                '}';
    }
}
