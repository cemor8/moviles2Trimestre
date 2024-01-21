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
        String titulo = contenidoJson.get("titulo").getAsString();
        String fecha = contenidoJson.get("fecha").getAsString();
        Integer paginas = contenidoJson.get("paginas").getAsInt();


        return new Libro(titulo,null,paginas,fecha);
    }
}
