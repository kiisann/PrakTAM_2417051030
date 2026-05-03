package Model

import com.google.gson.annotations.SerializedName

data class Todolist(
    @SerializedName("kegiatan")
    val kegiatan: String,
    @SerializedName("deadline")
    val deadline: String,
    @SerializedName("prioritas")
    val prioritas: String,
    @SerializedName("catatan")
    val catatan: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("image_url")
    val imageUrl: Any
)
