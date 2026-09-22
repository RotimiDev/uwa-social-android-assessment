package com.akeemrotimi.uwasocial

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.akeemrotimi.uwasocial.ui.FeedScreen
import com.akeemrotimi.uwasocial.ui.theme.UwaSocialTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UwaSocialFeed()
        }
    }
}

@Composable
private fun UwaSocialFeed() {
    UwaSocialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            FeedScreen()
        }
    }
}
