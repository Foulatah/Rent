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
import com.foulatah.foulatah.navigation.ROUTE_HOME
import com.foulatah.foulatah.navigation.ROUTE_VIEW_TENANTS
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(navController: NavController, tenantId: () -> Unit) {

    var amount by remember { mutableStateOf("") }
    var paymentDate by remember { mutableStateOf("") }
    var paymentMethod by remember { mutableStateOf("") }

    // Track if fields are empty
    var amountError by remember { mutableStateOf(false) }
    var paymentDateError by remember { mutableStateOf(false) }
    var paymentMethodError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Payment Details",
                        fontSize = 30.sp,
                        color = Color.White
                    )
                },
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.systemBars),
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate(ROUTE_VIEW_TENANTS)
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
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    keyboardActions = KeyboardActions(onDone = { /* Handle Done action */ }),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = paymentDate,
                    onValueChange = { paymentDate = it },
                    label = { Text("Payment Date") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    keyboardActions = KeyboardActions(onDone = { /* Handle Done action */ }),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = paymentMethod,
                    onValueChange = { paymentMethod = it },
                    label = { Text("Payment Method") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                if (amountError) {
                    Text("Amount is required", color = Color.Red)
                }
                if (paymentDateError) {
                    Text("Payment Date is required", color = Color.Red)
                }
                if (paymentMethodError) {
                    Text("Payment Method is required", color = Color.Red)
                }

                Button(
                    onClick = {
                        // Reset error flags
                        amountError = amount.isBlank()
                        paymentDateError = paymentDate.isBlank()
                        paymentMethodError = paymentMethod.isBlank()

                        // Add payment if all fields are filled
                        if (!amountError && !paymentDateError && !paymentMethodError) {
                            addPaymentToFirestore(
                                navController,
                                tenantId.toString(),
                                amount.toDouble(),
                                paymentDate,
                                paymentMethod
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Add Payment")
                }
            }
        }
    )
}

private fun addPaymentToFirestore(navController: NavController, tenantId: String, amount: Double, paymentDate: String, paymentMethod: String) {
    if (amount.isNaN() || paymentDate.isEmpty() || paymentMethod.isEmpty()) {
        // Validate input fields
        return
    }

    val paymentId = UUID.randomUUID().toString()

    val firestore = FirebaseFirestore.getInstance()
    val paymentData = hashMapOf(
        "amount" to amount,
        "paymentDate" to paymentDate,
        "paymentMethod" to paymentMethod,
        "tenantId" to tenantId
    )

    firestore.collection("payments").document(paymentId)
        .set(paymentData)
        .addOnSuccessListener {
            // Display toast message
            Toast.makeText(
                navController.context,
                "Payment added successfully!",
                Toast.LENGTH_SHORT
            ).show()

            // Navigate to another screen
            navController.navigate(ROUTE_HOME)
        }
        .addOnFailureListener {
            // Handle error adding payment to Firestore
        }
}
