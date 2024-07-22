package com.foulatah.foulatah.ui.about

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.foulatah.foulatah.navigation.ROUTE_HOME

@Composable
fun AboutScreen(navController: NavHostController) {
    val callLauncher: ManagedActivityResultLauncher<Intent, ActivityResult> =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { _ ->

        }
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "About",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = "Pane",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(vertical = 16.dp)
        )


        Text(
            text = "Call me",
            fontSize = 20.sp,
            modifier = Modifier
                .padding(16.dp)
                .clickable {

                    val intent = Intent(Intent.ACTION_DIAL)
                    intent.data = Uri.parse("tel:+25470000000")

                    callLauncher.launch(intent)
                }
        )

        Text(
            text = "Go home",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .clickable {
                    navController.navigate(ROUTE_HOME)
                }
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}

