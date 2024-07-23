import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import com.foulatah.foulatah.navigation.ROUTE_DASHBOARD
import com.foulatah.foulatah.navigation.ROUTE_HOME
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewTenantScreen(navController: NavHostController, tenantId: () -> Unit) {
    var tenant by remember { mutableStateOf<Tenant?>(null) }
    var listenerRegistration by remember { mutableStateOf<ListenerRegistration?>(null) }

    val firestore = FirebaseFirestore.getInstance()

    LaunchedEffect(tenantId) {
        // Set up real-time listener for the tenant details
        listenerRegistration = firestore.collection("tenants").document(tenantId.toString())
            .addSnapshotListener { snapshot, _ ->
                tenant = snapshot?.toObject(Tenant::class.java)
            }
    }

    DisposableEffect(Unit) {
        onDispose {
            listenerRegistration?.remove() // Clean up listener when the composable is disposed
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Tenant Details")
                },
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.systemBars),
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate(ROUTE_DASHBOARD)
                    }) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.DarkGray,
                    titleContentColor = Color.White,
                ),
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                if (tenant != null) {
                    SubcomposeAsyncImage(
                        model = coil.request.ImageRequest.Builder(LocalContext.current)
                            //.data(tenant?.imageUrl) // Uncomment and use actual image URL
                            .crossfade(true)
                            .build(),
                        loading = {
                            CircularProgressIndicator()
                        },
                        contentDescription = "tenant",
                        modifier = Modifier
                            .clip(RoundedCornerShape(10))
                            .size(200.dp)
                            .align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = tenant?.name ?: "Loading...",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Email: ${tenant?.email ?: "Loading..."}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Phone: ${tenant?.phone ?: "Loading..."}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(8.dp))



                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "House Number: ${tenant?.houseNumber ?: "Loading..."}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                } else {
                    // Display a loading indicator while fetching tenant data
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }
            }
        }
    )
}
