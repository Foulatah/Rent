package com.foulatah.foulatah.ui.tenants

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.foulatah.foulatah.navigation.ROUTE_VIEW_TENANTS
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewBillsScreen(navController: NavController, tenantId: String) {
    var bills by remember { mutableStateOf<Bills?>(null) }

    LaunchedEffect(Unit) {
        fetchBills(tenantId) { fetchedBills ->
            bills = fetchedBills
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "View Bills",
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
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
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
            ) {
                bills?.let {
                    BillDetailRow("Rent", it.rent)
                    BillDetailRow("Arrears", it.arrears)
                    BillDetailRow("Garbage", it.garbage)
                    BillDetailRow("Water", it.water)
                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))
                    BillDetailRow("Total", it.total)
                } ?: run {
                    Text(text = "Loading...", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    )
}

@Composable
fun BillDetailRow(label: String, amount: Double) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
        Text(text = "$$amount", style = MaterialTheme.typography.bodyMedium)
    }
}

suspend fun fetchBills(tenantId: String, onBillsFetched: (Bills?) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    val billsCollection = db.collection("bills")

    try {
        val querySnapshot = billsCollection.whereEqualTo("tenantId", tenantId).get().await()
        val documentSnapshot = querySnapshot.documents.firstOrNull()
        if (documentSnapshot != null) {
            val billsData = documentSnapshot.data ?: return
            val bills = Bills(
                id = documentSnapshot.id,
                tenantId = billsData["tenantId"] as String,
                rent = billsData["rent"] as Double,
                arrears = billsData["arrears"] as Double,
                garbage = billsData["garbage"] as Double,
                water = billsData["water"] as Double,
                dueDate = billsData["dueDate"] as String
            )
            onBillsFetched(bills)
        } else {
            onBillsFetched(null)
        }
    } catch (e: Exception) {
        onBillsFetched(null)
    }
}

data class Bills(
    val id: String = "",
    val tenantId: String = "",
    val rent: Double = 0.0,
    val arrears: Double = 0.0,
    val garbage: Double = 0.0,
    val water: Double = 0.0,
    val dueDate: String = ""
) {
    val total: Double
        get() = rent + arrears + garbage + water
}
