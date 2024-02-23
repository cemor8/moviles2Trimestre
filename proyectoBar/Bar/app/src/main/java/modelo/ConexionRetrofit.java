package modelo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConexionRetrofit {
    private static Retrofit instancia;
    private static String url = "http://10.0.2.2:3000";

    public static Retrofit getConexion() {
        if (instancia == null) {
            instancia = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return instancia;
    }
}
