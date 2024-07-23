package com.foulatah.foulatah.ui.dashboard

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.foulatah.foulatah.navigation.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DashboardScreen(navController: NavHostController) {
    var tenant by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    val currentUser = FirebaseAuth.getInstance().currentUser
    val firestore = FirebaseFirestore.getInstance()
    var user: User? by remember { mutableStateOf(null) }
    var isLoading by remember { mutableStateOf(true) }
    var tenantCount by remember { mutableStateOf(0) }

    BackHandler {
        navController.popBackStack()
    }

    // Fetch user details from Firestore
    LaunchedEffect(key1 = currentUser?.uid) {
        if (currentUser != null) {
            val userDocRef = firestore.collection("users").document(currentUser.uid)
            userDocRef.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        user = document.toObject<User>()
                        tenant = user?.tenant ?: ""  // Update tenant state
                        name = user?.name ?: ""      // Update name state
                    }
                    isLoading = false
                }
                .addOnFailureListener { _ ->
                    // Handle failure
                    isLoading = false
                }
        }
    }

    LaunchedEffect(Unit) {
        firestore.collection("Tenants")
            .get()
            .addOnSuccessListener { result ->
                tenantCount = result.size()
            }
            .addOnFailureListener { _ ->
                // Handle failures
            }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Dashboard", color = Color.White, fontSize = 30.sp)
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Gray,
                    titleContentColor = Color.White,
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(ROUTE_HOME) }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back Icon", tint = Color.White)
                    }
                },
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.LightGray),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                val dashboardItems = listOf(
                    DashboardItemData(
                        title = "Complaints",
                        icon = Icons.Filled.Email,
                        badgeCount = 4,
                        onClick = { navController.navigate(ROUTE_COMPLAINTS) }
                    ),
                    DashboardItemData(
                        title = "Suggestions",
                        icon = Icons.Filled.MailOutline,
                        badgeCount = 4,
                        onClick = { navController.navigate(ROUTE_SUGGESTIONS) }
                    ),
                    DashboardItemData(
                        title = "About",
                        icon = Icons.Filled.Build,
                        badgeCount = 4,
                        onClick = { navController.navigate(ROUTE_ABOUT) }
                    ),
                    DashboardItemData(
                        title = "Tenants",
                        icon = Icons.Filled.Person,
                        badgeCount = 4,
                        onClick = { navController.navigate(ROUTE_VIEW_TENANTS) }
                    ),
                    DashboardItemData(
                        title = "Bills",
                        icon = Icons.Filled.AddCircle,
                        badgeCount = 4,
                        onClick = { navController.navigate(ROUTE_UPDATE_BILLS) }
                    ),
                    DashboardItemData(
                        title = "Lease",
                        icon = Icons.Filled.Info,
                        badgeCount = 4,
                        onClick = { navController.navigate(ROUTE_AGREEMENT) }
                    ),
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(1), // Adjust grid cells count for better layout
                    modifier = Modifier.padding(16.dp),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(dashboardItems) { item ->
                        DashboardItem(item)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    )
}

@Composable
fun DashboardItem(item: DashboardItemData) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = item.onClick,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.title,
                tint = Color.Black,
                modifier = Modifier.size(36.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black
            )
            Spacer(modifier = Modifier.weight(1f))
            if (item.badgeCount > 0) {
                Badge(count = item.badgeCount)
            }
        }
    }
}

@Composable
fun Badge(count: Int) {
    Box(
        modifier = Modifier
            .padding(start = 8.dp)
            .size(24.dp)
            .clip(CircleShape)
            .background(Color.Red),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = count.toString(),
            style = MaterialTheme.typography.bodySmall,
            color = Color.White
        )
    }
}

data class DashboardItemData(
    val title: String,
    val icon: ImageVector,
    val badgeCount: Int,
    val onClick: () -> Unit
)

data class User(
    val tenantId: String = "",
    val tenant: String = "",
    val name: String = ""
)
