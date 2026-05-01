package ph.edu.cksc.college.appdev.appdev2025.screens

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.PeopleAlt
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import ph.edu.cksc.college.appdev.appdev2025.DEVELOPER_SCREEN
import ph.edu.cksc.college.appdev.appdev2025.MAP_SCREEN

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("App Dev Demo")
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
                        navController.navigate(DEVELOPER_SCREEN)
                    }) {
                        Icon(
                            imageVector = Icons.Filled.PeopleAlt,
                            contentDescription = "Developers"
                        )
                    }
                    IconButton(onClick = {
                        navController.navigate(MAP_SCREEN)
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Map,
                            contentDescription = "Map"
                        )
                    }
                },

                )

        },

        ) { innerPadding ->
        AboutScrollContent(innerPadding)
    }
}

@Composable
fun AboutScrollContent(innerPadding: PaddingValues) {
    Box(
        modifier = Modifier.padding(innerPadding)
    ) {
        WebViewScreen()
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewScreen() {

    AndroidView(
        modifier = Modifier.fillMaxSize().background(Color(0xFF66caf1)),
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                webViewClient = WebViewClient()
                settings.loadWithOverviewMode = true
                settings.useWideViewPort = true
                settings.setSupportZoom(true)
            }
        },
        update = { webView ->
            webView.loadUrl("file:///android_asset/About.html")
        }
    )
}