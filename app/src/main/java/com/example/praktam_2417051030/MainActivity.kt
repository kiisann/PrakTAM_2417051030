package com.example.praktam_2417051030

import Data.Api.RetrofitClient
import Data.Model.Todolist
import Data.Model.TodolistSource
import Data.Repository.TodoRepository
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.praktam_2417051030.ui.theme.PrakTAM_2417051030Theme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import coil.compose.AsyncImage
import com.example.praktam_2417051030.R

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PrakTAM_2417051030Theme {
                val navController = rememberNavController()
                AppNavigation(navController)
            }
        }
    }
}

@Composable
fun AppNavigation(navController: NavHostController) {
    var todolist by remember { mutableStateOf(TodolistSource.dummyTodolist) }

    LaunchedEffect(Unit) {
        try {
            val fetchedData = RetrofitClient.instance.getTodos()
            if (fetchedData.isNotEmpty()) {
                todolist = fetchedData
            }
        } catch (e: Exception) {
        }
    }

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            TodoScreen(navController, todolist)
        }
        composable("detail/{todoId}") { backStackEntry ->
            val todoId = backStackEntry.arguments?.getString("todoId")
            val todo = todolist.find { it.kegiatan == todoId }
            if (todo != null) {
                TodoDetailScreen(todo = todo, navController = navController, isFullScreen = true)
            }
        }
    }
}

@Composable
fun TodoScreen(navController: NavController, todolist: List<Todolist>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            Text(
                text = "Kegiatan Hari Ini",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                items(todolist) { todo ->
                    TodoRowItem(todo = todo, navController = navController)
                }
            }

            Spacer(modifier = Modifier.height(45.dp))

            Text(
                text = "Daftar Kegiatan",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        items(todolist) { todo ->
            TodoDetailScreen(todo = todo, navController = navController, isFullScreen = false)
        }
    }
}

@Composable
fun TodoRowItem(todo: Todolist, navController: NavController) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .clickable { navController.navigate("detail/${todo.kegiatan}") },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Column {
            AsyncImage(
                model = todo.imageUrl,
                contentDescription = todo.kegiatan,
                modifier = Modifier.fillMaxWidth().height(100.dp),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.rapat),
                error = painterResource(id = R.drawable.belajar)
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(text = todo.kegiatan, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Text(text = todo.deadline, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
fun TodoDetailScreen(todo: Todolist, navController: NavController, isFullScreen: Boolean = false, onTodoLoaded: (List<Todolist>) -> Unit = {}) {
    var isFavorite by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var isActionLoading by remember { mutableStateOf(false) } // State baru khusus tombol
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var isError by remember { mutableStateOf(false) }
    var todos by remember { mutableStateOf<List<Todolist>>(emptyList()) }
    val Repository = remember { TodoRepository() }

    LaunchedEffect(Unit) {
        try {
            todos = Repository.getTodos()
            onTodoLoaded(todos)
            isLoading = false
            isError = todos.isEmpty()
        } catch (e: Exception) {
            isLoading = false
            isError = true
        }
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (isError || (isFullScreen && todos.isEmpty())) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Gagal Memuat Data",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Pastikan koneksi internet Anda menyala",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(if (isFullScreen) Modifier.verticalScroll(rememberScrollState()) else Modifier)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth()
                        .padding(if (isFullScreen) 0.dp else 4.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Box(modifier = Modifier) {
                            AsyncImage(
                                model = todo.imageUrl,
                                contentDescription = todo.kegiatan,
                                placeholder = painterResource(R.drawable.rapat),
                                error = painterResource(R.drawable.belajar),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .padding(16.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )

                            IconButton(
                                onClick = { isFavorite = !isFavorite },
                                modifier = Modifier.padding(16.dp).align(Alignment.TopEnd)
                            ) {
                                Icon(
                                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                                    contentDescription = "Favorite",
                                    tint = if (isFavorite) Color.Red else Color.White
                                )
                            }
                        }

                        Text(
                            text = todo.kegiatan,
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.Black,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Text(
                            text = "Deadline: ${todo.deadline}",
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Text(
                            text = "Prioritas: ${todo.prioritas}",
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Text(
                            text = "Catatan: ${todo.catatan}",
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Text(
                            text = "Status: ${todo.status}",
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        if (isFullScreen) {
                            Button(
                                onClick = {
                                    coroutineScope.launch {
                                        isActionLoading = true
                                        delay(2000)
                                        isActionLoading = false
                                        snackbarHostState.showSnackbar("Kegiatan ${todo.kegiatan} selesai!")
                                    }
                                },
                                modifier = Modifier.fillMaxWidth().padding(16.dp),
                                enabled = !isActionLoading
                            ) {
                                if (isActionLoading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(24.dp),
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Text("Selesai")
                                }
                            }

                            Button(
                                onClick = { navController.popBackStack() },
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                            ) {
                                Text("Kembali")
                            }
                        }
                    }
                }
            }
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TodoScreenPreview() {
    PrakTAM_2417051030Theme {
        val navController = rememberNavController()
        TodoScreen(navController = navController, todolist = TodolistSource.dummyTodolist)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TodoDetailPreview() {
    PrakTAM_2417051030Theme {
        val navController = rememberNavController()
        TodoDetailScreen(
            todo = TodolistSource.dummyTodolist[0],
            navController = navController,
            isFullScreen = true
        )
    }
}
