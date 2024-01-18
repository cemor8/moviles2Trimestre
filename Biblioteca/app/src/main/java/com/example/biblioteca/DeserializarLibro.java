package com.example.biblioteca;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class DeserializarLibro implements JsonDeserializer<Libro> {
    @Override
    public Libro deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject contenidoJson = json.getAsJsonObject();
        String fechaCompleta = contenidoJson.get("fecha_lanzamiento").getAsString();
        String año = fechaCompleta.substring(0,4);
        String titulo = contenidoJson.get("titulo").getAsString();
        String autor = contenidoJson.get("autor").getAsString();
        Integer paginas = contenidoJson.get("numero_paginas").getAsInt();

        Libro libro = new Libro(titulo,autor,paginas,año);
        return libro;
    }
}
