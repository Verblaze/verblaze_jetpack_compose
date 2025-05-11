package com.example.verblaze_kotlin

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.verblaze_kotlin.cache.exampleViewModel
import com.example.verblaze_kotlin.pages.TestPage1
import com.example.verblaze_kotlin.pages.TestPage2
import com.example.verblaze_kotlin.ui.theme.Verblaze_KotlinTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Verblaze_KotlinTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding),
                        this
                    )
                }
            }
        }
    }
}

@Composable
internal fun Greeting(name:String,modifier:Modifier,context:Context) {
    /////////////////////////////////////////////
    //It is important to set provider and start configure before setting others
    val provider = VerblazeBase.FunctionProvider
    provider.configure(context,"YOUR_API_TOKEN")
    ////////////////////////////////////////////
    val examplevm = exampleViewModel(provider)
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "TestPage1"
    ) {
        composable(route = "TestPage1") {
            TestPage1(navController,examplevm,provider)
        }
        composable(route = "TestPage2") { TestPage2(navController) }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Verblaze_KotlinTheme {
        //Greeting("Android")
    }
}