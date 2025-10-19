package mx.tec.tickets.model

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    // 10.0.2.2 esta es la coso para que jale en local, ya luego se cambia la api para cuando esté en amazon
    private const val BASE_URL = "http://10.0.2.2:3000/"

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
