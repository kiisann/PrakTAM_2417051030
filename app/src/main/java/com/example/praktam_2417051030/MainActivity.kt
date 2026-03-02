package com.example.praktam_2417051030

import Model.Todolist
import Model.TodolistSource
import android.R
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier) {
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

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PrakTAM_2417051030Theme {
        Greeting()
    }
}