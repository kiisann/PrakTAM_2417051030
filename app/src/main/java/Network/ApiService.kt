package Network

import Model.Todolist
import retrofit2.http.GET

interface ApiService {
    @GET(".")
    suspend fun getTodos(): List<Todolist>
}