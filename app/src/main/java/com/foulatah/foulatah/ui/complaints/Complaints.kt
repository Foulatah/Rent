package com.foulatah.foulatah.ui.complaints

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
fun ComplaintsScreen(navController: NavHostController, function: () -> Unit) {
    var tenantName by remember { mutableStateOf(TextFieldValue("")) }
    var houseNumber by remember { mutableStateOf(TextFieldValue("")) }
    var complaintText by remember { mutableStateOf(TextFieldValue("")) }
    var feedbackSubmitted by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Complaints") },
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
                    text = "Submit a Complaint",
                    style = MaterialTheme.typography.titleLarge.copy(color = Color.Black),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = "We are always happy to receive feedback from our clients, so please feel free to let us know any of your concerns.",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Tenant Name Field
                OutlinedTextField(
                    value = tenantName,
                    onValueChange = { tenantName = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    label = { Text("Your Name") },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
                )

                // House Number Field
                OutlinedTextField(
                    value = houseNumber,
                    onValueChange = { houseNumber = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    label = { Text("House Number") },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
                )

                // Complaint Text Field
                OutlinedTextField(
                    value = complaintText,
                    onValueChange = { complaintText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .padding(bottom = 16.dp),
                    label = { Text("Complaint") },
                    maxLines = 5,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
                )

                // Submit Button
                Button(
                    onClick = {
                        // Handle submit action
                        feedbackSubmitted = true
                        tenantName = TextFieldValue("")
                        houseNumber = TextFieldValue("")
                        complaintText = TextFieldValue("")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
                ) {
                    Text(text = "Submit", color = Color.White)
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (feedbackSubmitted) {
                    Text(
                        text = "Thank you for your feedback!",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.Green),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                Text(
                    text = "Complaints List",
                    style = MaterialTheme.typography.titleLarge.copy(color = Color.Black),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Complaints List
                Column {
                    // Example Card for displaying a complaint
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "Name: [Sample Name]", style = MaterialTheme.typography.bodyMedium)
                            Text(text = "House Number: [Sample House Number]", style = MaterialTheme.typography.bodyMedium)
                            Text(text = "Complaint: [Sample Complaint]", style = MaterialTheme.typography.bodyMedium)
                            Text(text = "Timestamp: [Sample Timestamp]", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    )
}
