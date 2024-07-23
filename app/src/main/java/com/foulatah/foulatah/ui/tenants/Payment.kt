import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
    var houseNumber by remember { mutableStateOf("") }
    var totalToBePaid by remember { mutableStateOf("") }

    // Track if fields are empty
    var amountError by remember { mutableStateOf(false) }
    var paymentDateError by remember { mutableStateOf(false) }
    var paymentMethodError by remember { mutableStateOf(false) }
    var houseNumberError by remember { mutableStateOf(false) }
    var totalToBePaidError by remember { mutableStateOf(false) }

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
            ) {
                Spacer(modifier = Modifier.height(80.dp))

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
                    ),modifier = Modifier.fillMaxWidth()
                )
                if (houseNumberError) {
                    Text("House Number is required", color = Color.Red)
                }
                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = totalToBePaid,
                    onValueChange = { totalToBePaid = it },
                    label = { Text("Total to be Paid") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    keyboardActions = KeyboardActions(onDone = { /* Handle Done action */ }),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.LightGray,
                        focusedIndicatorColor = Color.Gray,
                        unfocusedIndicatorColor = Color.Gray
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                if (totalToBePaidError) {
                    Text("Total to be Paid is required", color = Color.Red)
                }
                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount Being Paid") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    keyboardActions = KeyboardActions(onDone = { /* Handle Done action */ }),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.LightGray,
                        focusedIndicatorColor = Color.Gray,
                        unfocusedIndicatorColor = Color.Gray
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                if (amountError) {
                    Text("Amount Paid is required", color = Color.Red)
                }
                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = paymentDate,
                    onValueChange = { paymentDate = it },
                    label = { Text("Payment Date") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    keyboardActions = KeyboardActions(onDone = { /* Handle Done action */ }),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.LightGray,
                        focusedIndicatorColor = Color.Gray,
                        unfocusedIndicatorColor = Color.Gray
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                if (paymentDateError) {
                    Text("Payment Date is required", color = Color.Red)
                }
                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = paymentMethod,
                    onValueChange = { paymentMethod = it },
                    label = { Text("Payment Method") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    keyboardActions = KeyboardActions(onDone = { /* Handle Done action */ }),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.LightGray,
                        focusedIndicatorColor = Color.Gray,
                        unfocusedIndicatorColor = Color.Gray
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                if (paymentMethodError) {
                    Text("Payment Method is required", color = Color.Red)
                }

                Button(
                    onClick = {
                        // Reset error flags
                        houseNumberError = houseNumber.isBlank()
                        totalToBePaidError = totalToBePaid.isBlank()
                        amountError = amount.isBlank()
                        paymentDateError = paymentDate.isBlank()
                        paymentMethodError = paymentMethod.isBlank()

                        // Add payment if all fields are filled
                        if (!houseNumberError && !totalToBePaidError && !amountError && !paymentDateError && !paymentMethodError) {
                            addPaymentToFirestore(
                                navController,
                                tenantId.toString(),
                                houseNumber,
                                totalToBePaid.toDouble(),
                                amount.toDouble(),
                                paymentDate,
                                paymentMethod
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray) // Changed button color to gray
                ) {
                    Text("Add Payment")
                }
            }
        }
    )
}

private fun addPaymentToFirestore(navController: NavController, tenantId: String, houseNumber: String, totalToBePaid: Double, amount: Double, paymentDate: String, paymentMethod: String) {
    if (houseNumber.isEmpty() || totalToBePaid.isNaN() || amount.isNaN() || paymentDate.isEmpty() || paymentMethod.isEmpty()) {
        // Validate input fields
        return
    }

    val paymentId = UUID.randomUUID().toString()

    val firestore = FirebaseFirestore.getInstance()
    val paymentData = hashMapOf(
        "houseNumber" to houseNumber,
        "totalToBePaid" to totalToBePaid,
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
