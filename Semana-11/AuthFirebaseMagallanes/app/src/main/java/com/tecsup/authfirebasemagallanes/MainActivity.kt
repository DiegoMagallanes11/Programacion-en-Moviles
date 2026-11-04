package com.tecsup.authfirebasemagallanes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.tecsup.authfirebasemagallanes.ui.theme.AuthFirebaseMagallanesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AuthFirebaseMagallanesTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AuthApp(Modifier.safeDrawingPadding())
                }
            }
        }
    }
}
