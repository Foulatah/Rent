package com.foulatah.foulatah.ui.suggestions


import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuggestionScreen(navController: NavHostController, function: () -> Unit) {
    var tenantName by remember { mutableStateOf(TextFieldValue("")) }
    var suggestionText by remember { mutableStateOf(TextFieldValue("")) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Suggestions") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.DarkGray,
                    titleContentColor = Color.White,
                ),
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Submit a Suggestion",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                BasicTextField(
                    value = tenantName,
                    onValueChange = { tenantName = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    decorationBox = { innerTextField ->
                        TextFieldDefaults.OutlinedTextFieldDecorationBox(
                            value = tenantName.text,
                            visualTransformation = VisualTransformation.None,
                            innerTextField = innerTextField,
                            label = { Text("Your Name") },
                            enabled = true,
                            isError = false,
                            interactionSource = remember { MutableInteractionSource() },
                            singleLine = true,
                            colors = TextFieldDefaults.outlinedTextFieldColors()
                        )
                    }
                )

                BasicTextField(
                    value = suggestionText,
                    onValueChange = { suggestionText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    maxLines = 5,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    decorationBox = { innerTextField ->
                        TextFieldDefaults.OutlinedTextFieldDecorationBox(
                            value = suggestionText.text,
                            visualTransformation = VisualTransformation.None,
                            innerTextField = innerTextField,
                            label = { Text("Suggestion") },
                            enabled = true,
                            isError = false,
                            interactionSource = remember { MutableInteractionSource() },
                            singleLine = false,
                            colors = TextFieldDefaults.outlinedTextFieldColors()
                        )
                    }
                )

                Button(
                    onClick = {

                        tenantName = TextFieldValue("")
                        suggestionText = TextFieldValue("")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Submit")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Suggestions List",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Column {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "Name:", style = MaterialTheme.typography.bodyMedium)
                            Text(text = "Suggestion:", style = MaterialTheme.typography.bodyMedium)
                            Text(text = "Timestamp:", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        })
}

