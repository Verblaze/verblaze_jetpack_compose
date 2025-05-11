package com.example.verblaze_kotlin.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import extensions.vbt
import extensions.vbtWithLang

@Composable
internal fun TestPage2(navController: NavController) {
    var wordList: MutableList<String> = mutableListOf(
        "testpage2.school".vbt,
        "testpage2.car".vbt,
        "testpage2.book".vbt,
        "testpage2.computer".vbt,
        "testpage2.stadium".vbtWithLang("tr-TR"),
    )
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            wordList.forEach {
                Text(
                    text = it
                )
            }
            Button(
                onClick = {
                    navController.popBackStack()
                }
            ) {
                Text(
                    text = "testpage2.backpage".vbt
                )
            }
        }
    }
}