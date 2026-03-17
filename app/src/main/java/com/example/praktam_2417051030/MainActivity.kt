package com.example.praktam_2417051030

import Model.Todolist
import Model.TodolistSource
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.praktam_2417051030.ui.theme.PrakTAM_2417051030Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PrakTAM_2417051030Theme {
                    TodoScreen()
            }
        }
    }
}

@Composable
fun Greeting() {
    Column(modifier = Modifier.fillMaxSize(). padding(24.dp)) {

        TodolistSource.dummyTodolist.forEach { todo ->
            Image(
                painter = painterResource(id = todo.imageRes),
                contentDescription = todo.kegiatan,
                modifier = Modifier.size(100.dp),
                contentScale = ContentScale.Crop
            )

            Text(text = "Kegiatan: ${todo.kegiatan}")
            Text(text = "Deadline: ${todo.deadline}")
            Text(text = "Prioritas: ${todo.prioritas}")
            Text(text = "Catatan: ${todo.catatan}")
            Text(text = "Status: ${todo.status}\n")
        }
    }
}

@Composable
fun TodoScreen() {
    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(24.dp)
    ) {
        TodolistSource.dummyTodolist.forEach { todo ->
            TodoDetailScreen(todo = todo)
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun TodoDetailScreen(todo: Todolist) {
    var isFavorite by remember { mutableStateOf(false) }
    Column(modifier = Modifier.fillMaxSize()) {
        Card(modifier = Modifier) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier) {
                    Image(
                        painter = painterResource(id = todo.imageRes),
                        contentDescription = todo.kegiatan,
                        modifier = Modifier.fillMaxWidth().height(200.dp)
                            .padding(16.dp, 16.dp, 16.dp, 16.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )

                    IconButton(
                        onClick = { isFavorite = !isFavorite },
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.TopEnd)
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) Color.Red else Color.White
                        )
                    }
                }

                Text(text = todo.kegiatan,
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.Black,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(text = "Deadline: ${todo.deadline}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(text = "Prioritas: ${todo.prioritas}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(text = "Catatan: ${todo.catatan}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(text = "Status: ${todo.status}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Button(onClick = { },
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) { Text(text = "Selesai") }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PrakTAM_2417051030Theme {
        TodoScreen()
    }
}