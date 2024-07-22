package com.foulatah.foulatah.ui.tenants

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.foulatah.foulatah.navigation.ROUTE_VIEW_BILLS
import com.foulatah.foulatah.navigation.ROUTE_VIEW_TENANTS
import com.google.firebase.firestore.FirebaseFirestore

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateBillsScreen(navController: NavController, tenantId: () -> Unit) {

    var rent by remember { mutableStateOf("") }
    var arrears by remember { mutableStateOf("") }
    var garbage by remember { mutableStateOf("") }
    var water by remember { mutableStateOf("") }

    // Track if fields are empty
    var rentError by remember { mutableStateOf(false) }
    var arrearsError by remember { mutableStateOf(false) }
    var garbageError by remember { mutableStateOf(false) }
    var waterError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Update Bills",
                        fontSize = 30.sp,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate(ROUTE_VIEW_BILLS)
                    }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            "backIcon",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.DarkGray,
                    titleContentColor = Color.White,
                )
            )

        },

        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(16.dp),
            ) { Spacer(modifier = Modifier.height(80.dp))
                TextField(
                    value = rent,
                    onValueChange = { rent = it },
                    label = { Text("Rent") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    keyboardActions = KeyboardActions(onDone = { /* Handle Done action */ }),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = arrears,
                    onValueChange = { arrears = it },
                    label = { Text("Arrears") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    keyboardActions = KeyboardActions(onDone = { /* Handle Done action */ }),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = garbage,
                    onValueChange = { garbage = it },
                    label = { Text("Garbage") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    keyboardActions = KeyboardActions(onDone = { /* Handle Done action */ }),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = water,
                    onValueChange = { water = it },
                    label = { Text("Water") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    keyboardActions = KeyboardActions(onDone = { /* Handle Done action */ }),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                if (rentError) {
                    Text("Rent is required", color = Color.Red)
                }
                if (arrearsError) {
                    Text("Arrears is required", color = Color.Red)
                }
                if (garbageError) {
                    Text("Garbage is required", color = Color.Red)
                }
                if (waterError) {
                    Text("Water is required", color = Color.Red)
                }

                Button(
                    onClick = {
                        // Reset error flags
                        rentError = rent.isBlank()
                        arrearsError = arrears.isBlank()
                        garbageError = garbage.isBlank()
                        waterError = water.isBlank()

                        // Update bills if all fields are filled
                        if (!rentError && !arrearsError && !garbageError && !waterError) {
                            updateBillsInFirestore(
                                navController,
                                tenantId.toString(),
                                rent.toDouble(),
                                arrears.toDouble(),
                                garbage.toDouble(),
                                water.toDouble()
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Update Bills")
                }
            }
        }
    )
}

private fun updateBillsInFirestore(navController: NavController, tenantId: String, rent: Double, arrears: Double, garbage: Double, water: Double) {
    val firestore = FirebaseFirestore.getInstance()
    val billsData = hashMapOf(
        "rent" to rent,
        "arrears" to arrears,
        "garbage" to garbage,
        "water" to water
    )

    firestore.collection("bills").document(tenantId)
        .set(billsData)
        .addOnSuccessListener {
            // Display toast message
            Toast.makeText(
                navController.context,
                "Bills updated successfully!",
                Toast.LENGTH_SHORT
            ).show()

            // Navigate to another screen
            navController.navigate(ROUTE_VIEW_TENANTS)
        }
        .addOnFailureListener {
            // Handle error updating bills in Firestore
        }
}
