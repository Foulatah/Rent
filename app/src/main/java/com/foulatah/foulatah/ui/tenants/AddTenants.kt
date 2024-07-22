package com.foulatah.foulatah.ui.tenants

import android.annotation.SuppressLint
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.foulatah.foulatah.navigation.ROUTE_VIEW_TENANTS
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import java.util.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.foulatah.foulatah.navigation.ROUTE_ADD_TENANTS
import com.foulatah.foulatah.ui.home.HomeScreen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTenantDetailsScreen(navController: NavController, onTenantAdded: () -> Unit) {
    var tenantName by remember { mutableStateOf("") }
    var tenantEmail by remember { mutableStateOf("") }
    var tenantPhone by remember { mutableStateOf("") }
    var tenantHouseNumber by remember { mutableStateOf("") }
    var tenantPhotoUri by remember { mutableStateOf<Uri?>(null) }

    var tenantNameError by remember { mutableStateOf(false) }
    var tenantEmailError by remember { mutableStateOf(false) }
    var tenantPhoneError by remember { mutableStateOf(false) }
    var tenantHouseNumberError by remember { mutableStateOf(false) }
    var tenantPhotoError by remember { mutableStateOf(false) }

    val allowedHouseNumbers = setOf("A1", "A2", "A3", "A4", "B1", "B2", "B3", "B4", "C1", "C2", "C3", "C4", "D1", "D2", "D3", "D4", "E1", "E2", "E3", "E4")

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            tenantPhotoUri = it
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Add Tenant Details", fontSize = 30.sp, color = Color.White)
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate(ROUTE_VIEW_TENANTS)
                    }) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Back Icon",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.DarkGray,
                    titleContentColor = Color.White,
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
        },


        content = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                item {
                    if (tenantPhotoUri != null) {
                        Image(
                            painter = rememberImagePainter(tenantPhotoUri),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                                .clip(CircleShape)
                                .background(Color.LightGray),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No Image Selected", color = Color.Black)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { launcher.launch("image/*") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                    ) {
                        Text("Select Photo")
                    }
                    if (tenantPhotoError) {
                        Text("Tenant Photo is required", color = Color.Red)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    TextField(
                        value = tenantName,
                        onValueChange = { tenantName = it },
                        label = { Text("Tenant Name") },
                        isError = tenantNameError,
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.LightGray,
                            focusedIndicatorColor = Color.Gray,
                            unfocusedIndicatorColor = Color.Gray
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (tenantNameError) {
                        Text("Tenant Name is required", color = Color.Red)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = tenantEmail,
                        onValueChange = { tenantEmail = it },
                        label = { Text("Tenant Email") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        isError = tenantEmailError,
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.LightGray,
                            focusedIndicatorColor = Color.Gray,
                            unfocusedIndicatorColor = Color.Gray
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (tenantEmailError) {
                        Text("Tenant Email is required", color = Color.Red)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = tenantPhone,
                        onValueChange = { tenantPhone = it },
                        label = { Text("Tenant Phone") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        keyboardActions = KeyboardActions(onDone = { /* Handle Done action */ }),
                        isError = tenantPhoneError,
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.LightGray,
                            focusedIndicatorColor = Color.Gray,
                            unfocusedIndicatorColor = Color.Gray
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (tenantPhoneError) {
                        Text("Tenant Phone is required", color = Color.Red)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = tenantHouseNumber,
                        onValueChange = { houseNumber ->
                            tenantHouseNumber = houseNumber
                            tenantHouseNumberError = !allowedHouseNumbers.contains(houseNumber)
                        },
                        label = { Text("House Number") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        isError = tenantHouseNumberError,
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.LightGray,
                            focusedIndicatorColor = Color.Gray,
                            unfocusedIndicatorColor = Color.Gray
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (tenantHouseNumberError) {
                        Text("Invalid House Number. Please enter a valid one.", color = Color.Red)
                    }
                    Spacer(modifier = Modifier.height(16.dp))


                    Button(
                        onClick = {
                            tenantNameError = tenantName.isBlank()
                            tenantEmailError = tenantEmail.isBlank()
                            tenantPhoneError = tenantPhone.isBlank()
                            tenantPhotoError = tenantPhotoUri == null

                            tenantHouseNumberError = !allowedHouseNumbers.contains(tenantHouseNumber)

                            if (!tenantNameError && !tenantEmailError && !tenantPhoneError && !tenantHouseNumberError && !tenantPhotoError) {
                                addTenantToFirestore(
                                    navController,
                                    onTenantAdded,
                                    tenantName,
                                    tenantEmail,
                                    tenantPhone,
                                    tenantHouseNumber,
                                    tenantPhotoUri
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                    ) {
                        Text("Add Tenant")
                    }
                }
            }
        }
    )
}



private fun addTenantToFirestore(
    navController: NavController,
    onTenantAdded: () -> Unit,
    tenantName: String,
    tenantEmail: String,
    tenantPhone: String,
    tenantHouseNumber: String,
    tenantPhotoUri: Uri?
) {
    val tenantId = UUID.randomUUID().toString()

    val firestore = Firebase.firestore
    val tenantData = hashMapOf(
        "name" to tenantName,
        "email" to tenantEmail,
        "phone" to tenantPhone,
        "houseNumber" to tenantHouseNumber,
        "photoUrl" to ""
    )

    firestore.collection("tenants").document(tenantId)
        .set(tenantData)
        .addOnSuccessListener {
            uploadPhotoToStorage(navController, tenantId, tenantPhotoUri) { photoUrl ->
                firestore.collection("tenants").document(tenantId)
                    .update("photoUrl", photoUrl)
                    .addOnSuccessListener {
                        // Display toast message
                        Toast.makeText(
                            navController.context,
                            "Tenant added successfully!",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Navigate to Home screen with house number
                        navController.navigate("home/${tenantHouseNumber}") {
                            popUpTo("home") { inclusive = true }
                        }

                        // Invoke the onTenantAdded callback
                        onTenantAdded()
                    }
                    .addOnFailureListener {
                        // Handle error updating tenant document
                        Toast.makeText(
                            navController.context,
                            "Failed to update photo URL",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        }
        .addOnFailureListener {
            // Handle error adding tenant to Firestore
            Toast.makeText(
                navController.context,
                "Failed to add tenant",
                Toast.LENGTH_SHORT
            ).show()
        }
}


private fun uploadPhotoToStorage(
    navController: NavController,
    tenantId: String,
    photoUri: Uri?,
    onSuccess: (String) -> Unit
) {
    if (photoUri == null) {
        onSuccess("")
        return
    }

    val storageRef = Firebase.storage.reference
    val photosRef = storageRef.child("tenants/$tenantId.jpg")

    photosRef.putFile(photoUri)
        .addOnSuccessListener {
            photosRef.downloadUrl
                .addOnSuccessListener { downloadUrl ->
                    onSuccess(downloadUrl.toString())
                }
        }
        .addOnFailureListener {
            // Handle error uploading photo
            Toast.makeText(
                navController.context,
                "Failed to upload photo",
                Toast.LENGTH_SHORT
            ).show()
        }
}

data class Tenant(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val houseNumber: String,
    val photoUrl: String
)

data class Bill(
    val id: String,
    val tenantId: String,
    val rent: Double,
    val arrears: Double,
    val garbage: Double,
    val water: Double,
    val dueDate: String
) {
    val total: Double
        get() = rent + arrears + garbage + water
}

suspend fun fetchTenantWithBills(tenantId: String): Pair<Tenant, List<Bill>> {
    val firestore = Firebase.firestore

    val tenantDocument = firestore.collection("tenants").document(tenantId).get().await()
    val tenant = tenantDocument.toObject(Tenant::class.java)

    val billsQuerySnapshot = firestore.collection("bills")
        .whereEqualTo("tenantId", tenantId)
        .get().await()
    val bills = billsQuerySnapshot.documents.map { it.toObject(Bill::class.java)!! }

    return Pair(tenant!!, bills)
}
