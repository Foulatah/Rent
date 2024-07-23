package com.foulatah.foulatah.ui.suggestions

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuggestionScreen(navController: NavHostController, function: () -> Unit) {
    var suggestionText by remember { mutableStateOf(TextFieldValue("")) }
    var feedbackSubmitted by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Suggestions") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.DarkGray,
                    titleContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back Icon",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Submit a Suggestion",
                    style = MaterialTheme.typography.titleLarge.copy(color = Color.Black),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Suggestion Text Field
                OutlinedTextField(
                    value = suggestionText,
                    onValueChange = { suggestionText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .padding(bottom = 24.dp),
                    label = { Text("Suggestion") },
                    maxLines = 5,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                )

                // Submit Button
                Button(
                    onClick = {
                        // Handle submit action
                        feedbackSubmitted = true
                        suggestionText = TextFieldValue("")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
                ) {
                    Text(text = "Submit", color = Color.White)
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (feedbackSubmitted) {
                    Text(
                        text = "Thank you for your valuable input!",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.Green),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                Text(
                    text = "Suggestions List",
                    style = MaterialTheme.typography.titleLarge.copy(color = Color.Black),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Suggestions List
                Column {
                    // Example Card for displaying a suggestion
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "Name: [Sample Name]", style = MaterialTheme.typography.bodyMedium)
                            Text(text = "Suggestion: [Sample Suggestion]", style = MaterialTheme.typography.bodyMedium)
                            Text(text = "Timestamp: [Sample Timestamp]", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    )
}
