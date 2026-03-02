package Model
import androidx.annotation.DrawableRes

data class Todolist(
    val kegiatan: String,
    val deadline: String,
    val prioritas: String,
    val catatan: String,
    val status: String,
    @DrawableRes val imageRes: Int
)
