package com.foulatah.foulatah.ui.tenants

import Bill
import Tenant
import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import com.foulatah.foulatah.navigation.ROUTE_DASHBOARD
import com.foulatah.foulatah.navigation.ROUTE_VIEW_BILLS
import com.foulatah.foulatah.navigation.ROUTE_VIEW_TENANTS
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateBillsScreen(navController: NavController, tenantId: () -> Unit) {

    var houseNumber by remember { mutableStateOf("") }
    var rent by remember { mutableStateOf("") }
    var arrears by remember { mutableStateOf("") }
    var garbage by remember { mutableStateOf("") }
    var water by remember { mutableStateOf("") }

    // Track if fields are empty
    var houseNumberError by remember { mutableStateOf(false) }
    var rentError by remember { mutableStateOf(false) }
    var arrearsError by remember { mutableStateOf(false) }
    var garbageError by remember { mutableStateOf(false) }
    var waterError by remember { mutableStateOf(false) }

    // Track the total
    var total by remember { mutableStateOf<Double?>(null) }

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
                        navController.navigate(ROUTE_DASHBOARD)
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
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
            ) {
                Spacer(modifier = Modifier.height(90.dp))

                TextField(
                    value = houseNumber,
                    onValueChange = { houseNumber = it },
                    label = { Text("House Number") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    keyboardActions = KeyboardActions(onDone = { /* Handle Done action */ }),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.LightGray,
                        focusedIndicatorColor = Color.Gray,
                        unfocusedIndicatorColor = Color.Gray
                    ),
                    modifier = Modifier
                )
                if (houseNumberError) {
                    Text("House Number is required", color = Color.Red)
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = rent,
                    onValueChange = { rent = it },
                    label = { Text("Rent") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    keyboardActions = KeyboardActions(onDone = { /* Handle Done action */ }),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.LightGray,
                        focusedIndicatorColor = Color.Gray,
                        unfocusedIndicatorColor = Color.Gray
                    ),
                    modifier = Modifier
                )
                if (rentError) {
                    Text("Rent is required", color = Color.Red)
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = arrears,
                    onValueChange = { arrears = it },
                    label = { Text("Arrears") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    keyboardActions = KeyboardActions(onDone = { /* Handle Done action */ }),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.LightGray,
                        focusedIndicatorColor = Color.Gray,
                        unfocusedIndicatorColor = Color.Gray
                    ),
                    modifier = Modifier
                )
                if (arrearsError) {
                    Text("Arrears is required", color = Color.Red)
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = garbage,
                    onValueChange = { garbage = it },
                    label = { Text("Garbage") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    keyboardActions = KeyboardActions(onDone = { /* Handle Done action */ }),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.LightGray,
                        focusedIndicatorColor = Color.Gray,
                        unfocusedIndicatorColor = Color.Gray
                    ),
                    modifier = Modifier
                )
                if (garbageError) {
                    Text("Garbage is required", color = Color.Red)
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = water,
                    onValueChange = { water = it },
                    label = { Text("Water") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    keyboardActions = KeyboardActions(onDone = { /* Handle Done action */ }),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.LightGray,
                        focusedIndicatorColor = Color.Gray,
                        unfocusedIndicatorColor = Color.Gray
                    ),
                    modifier = Modifier
                )
                Spacer(modifier = Modifier.height(16.dp))

                if (waterError) {
                    Text("Water is required", color = Color.Red)
                }

                Button(
                    onClick = {
                        // Reset error flags
                        houseNumberError = houseNumber.isBlank()
                        rentError = rent.isBlank()
                        arrearsError = arrears.isBlank()
                        garbageError = garbage.isBlank()
                        waterError = water.isBlank()

                        // Update bills if all fields are filled
                        if (!houseNumberError && !rentError && !arrearsError && !garbageError && !waterError) {
                            total = rent.toDouble() + arrears.toDouble() + garbage.toDouble() + water.toDouble()
                            updateBillsInFirestore(
                                navController,
                                houseNumber,
                                rent.toDouble(),
                                arrears.toDouble(),
                                garbage.toDouble(),
                                water.toDouble()
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
                ) {
                    Text("Update Bills")
                }

                // Display the total if it's not null
                total?.let {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Total: $it",
                        fontSize = 24.sp,
                        color = Color.Black
                    )
                }
            }
        }
    )
}

private fun updateBillsInFirestore(navController: NavController, houseNumber: String, rent: Double, arrears: Double, garbage: Double, water: Double) {
    val firestore = FirebaseFirestore.getInstance()
    val billsData = hashMapOf(
        "houseNumber" to houseNumber,
        "rent" to rent,
        "arrears" to arrears,
        "garbage" to garbage,
        "water" to water
    )

    firestore.collection("bills").document(houseNumber)
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

suspend fun fetchTenantWithHouseNumber(houseNumber: String): Pair<Tenant, List<Bill>> {
    val validHouseNumbers = listOf("A1", "A2", "A3", "A4", "B1", "B2", "B3", "B4", "C1", "C2", "C3", "C4", "D1", "D2", "D3", "D4", "E1", "E2", "E3", "E4")

    if (!validHouseNumbers.contains(houseNumber)) {
        throw IllegalArgumentException("Invalid house number")
    }

    val firestore = Firebase.firestore

    val tenantQuerySnapshot = firestore.collection("tenants")
        .whereEqualTo("houseNumber", houseNumber)
        .get().await()

    val tenantDocument = tenantQuerySnapshot.documents.firstOrNull()
    val tenant = tenantDocument?.toObject(Tenant::class.java)

    val billsQuerySnapshot = firestore.collection("bills")
        .whereEqualTo("houseNumber", houseNumber)
        .get().await()

    val bills = billsQuerySnapshot.documents.map { it.toObject(Bill::class.java)!! }

    return Pair(tenant!!, bills)
}
