package Data.Api

import Data.Model.Todolist
import retrofit2.http.GET

interface ApiService {
    @GET(".")
    suspend fun getTodos(): List<Todolist>
}