package com.example.verblaze_kotlin.pages

import AutoTranslateWidget.AutoTranslatedWidget
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.verblaze_kotlin.cache.exampleViewModel
import extensions.vbt

@Composable
internal fun TestPage1(
    navController : NavController,
    examplevm : exampleViewModel,
    verblazeProvider : VerblazeBase.FunctionProvider
) {
    AutoTranslatedWidget {
        var isExpanded by remember { mutableStateOf(false) }

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
                Text(
                    text = examplevm.selectedLanguage.local,
                    modifier = Modifier.clickable(onClick = { isExpanded = !isExpanded })
                )
                DropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false },
                ) {
                    examplevm.optionlist.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(text = item.local) },
                            onClick = {
                                examplevm.selectedLanguage = item
                                verblazeProvider.setCurrentLanguage(item.code)
                                isExpanded = false
                            }
                        )
                    }
                }
                Button(
                    onClick = {
                        navController.navigate(route = "TestPage2")
                    }
                ) {
                    Text(text = "testpage1.nextpage".vbt)
                }
            }
        }
    }
}