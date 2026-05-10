package Data.Repository

import Data.Api.RetrofitClient
import Data.Model.Todolist

class TodoRepository {
    suspend fun getTodos(): List<Todolist> {
        return try {
            RetrofitClient.instance.getTodos()
        } catch (e: Exception) {
            emptyList()
        }
    }
}