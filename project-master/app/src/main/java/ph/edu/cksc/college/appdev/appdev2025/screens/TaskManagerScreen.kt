package ph.edu.cksc.college.appdev.appdev2025.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import ph.edu.cksc.college.appdev.appdev2025.data.TaskModel
import ph.edu.cksc.college.appdev.appdev2025.service.TaskStorageService
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskManagerScreen(
    navController: NavHostController,
    auth: FirebaseAuth,
    firestore: FirebaseFirestore
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val taskService = remember { TaskStorageService(auth, firestore) }

    val allTasks by taskService.getTasks().collectAsState(initial = emptyList())
    var taskList by remember { mutableStateOf(allTasks) }

    var showEditor by remember { mutableStateOf(false) }
    var taskToEdit by remember { mutableStateOf<TaskModel?>(null) }

    LaunchedEffect(allTasks) {
        taskList = allTasks
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("My Tasks") },
                actions = {
                    IconButton(onClick = {
                        scope.launch {
                            taskService.clearAll()
                        }
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "Clear All")
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                taskToEdit = null
                showEditor = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { padding ->
        Box(Modifier.padding(padding)) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(taskList) { task ->
                    TaskCard(
                        task = task,
                        onToggleComplete = { done ->
                            scope.launch {
                                taskService.update(task.copy(isDone = done))
                            }
                        },
                        onEditClick = {
                            taskToEdit = task
                            showEditor = true
                        },
                        onDeleteClick = {
                            scope.launch {
                                taskService.delete(task.id)
                            }
                        }
                    )
                }
            }
        }
    }

    if (showEditor) {
        TaskEditorBottomSheet(
            task = taskToEdit,
            onDismiss = { showEditor = false },
            onSave = { updatedTask ->
                scope.launch {
                    if (updatedTask.id.isEmpty()) {
                        taskService.save(updatedTask)
                        Toast.makeText(context, "Task Added", Toast.LENGTH_SHORT).show()
                    } else {
                        taskService.update(updatedTask)
                        Toast.makeText(context, "Task Updated", Toast.LENGTH_SHORT).show()
                    }
                }
                showEditor = false
            }
        )
    }
}

@Composable
fun TaskCard(
    task: TaskModel,
    onToggleComplete: (Boolean) -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val formatter = DateTimeFormatter.ofPattern("MMM d, yyyy • h:mm a")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEditClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = task.isDone,
                    onCheckedChange = onToggleComplete
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(task.title, style = MaterialTheme.typography.titleMedium)
                    Text(task.description, style = MaterialTheme.typography.bodyMedium)
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
            }
            Text(
                text = try {
                    formatter.format(LocalDateTime.parse(task.timestamp))
                } catch (e: Exception) {
                    ""
                },
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskEditorBottomSheet(
    task: TaskModel?,
    onDismiss: () -> Unit,
    onSave: (TaskModel) -> Unit
) {
    var title by remember { mutableStateOf(task?.title ?: "") }
    var description by remember { mutableStateOf(task?.description ?: "") }
    var isDone by remember { mutableStateOf(task?.isDone ?: false) }
    val id = task?.id ?: ""
    val timestamp = task?.timestamp ?: LocalDateTime.now().toString()

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
        ) {
            Text(if (task == null) "New Task" else "Edit Task", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = isDone, onCheckedChange = { isDone = it })
                Text("Completed")
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    onSave(
                        TaskModel(
                            id = id,
                            title = title,
                            description = description,
                            isDone = isDone,
                            timestamp = timestamp
                        )
                    )
                },
                enabled = title.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save")
            }
        }
    }
}