package ph.edu.cksc.college.appdev.appdev2025.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen (navController: NavHostController) {
    var searchQuery by remember { mutableStateOf("") }

    var isSearchExpanded by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Favorite Diary")
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }

                },
                actions = {
                    IconButton(onClick = {
                        isSearchExpanded = !isSearchExpanded
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search",
                        )
                    }
                },

                )
            if (isSearchExpanded) {
                SearchBar(
                    query = searchQuery,
                    onQueryChange = { query -> searchQuery = query },
                    onSearch = { },
                    active = false,
                    onActiveChange = { },
                    modifier = Modifier.fillMaxWidth()
                ) {
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("FavoriteScreen")
            }) {
                Icon(Icons.Filled.Stars,"")
            }
        },
    ) { innerPadding ->
        FavoriteScrollContent(innerPadding, navController)
    }
}

@Composable
fun FavoriteScrollContent(
    innerPadding: PaddingValues,
    navController: NavHostController
) {
    Box(
        modifier = Modifier.padding(innerPadding)
    ) {
        LazyColumn {

        }
    }
}