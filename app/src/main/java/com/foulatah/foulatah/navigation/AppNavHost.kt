package com.foulatah.foulatah.navigation

import AddTenantDetailsScreen
import AgreementScreen
import PaymentScreen
import ViewTenantScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.foulatah.foulatah.ui.SplashScreen
import com.foulatah.foulatah.ui.about.AboutScreen
import com.foulatah.foulatah.ui.auth.LoginScreen
import com.foulatah.foulatah.ui.auth.SignUpScreen
import com.foulatah.foulatah.ui.complaints.ComplaintsScreen
import com.foulatah.foulatah.ui.dashboard.DashboardScreen
import com.foulatah.foulatah.ui.home.HomeScreen
import com.foulatah.foulatah.ui.suggestions.SuggestionScreen
import com.foulatah.foulatah.ui.tenants.UpdateBillsScreen


@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = ROUTE_LOGIN


) {

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {



        composable(ROUTE_HOME) {
            HomeScreen(navController)
        }


        composable(ROUTE_ABOUT) {
            AboutScreen(navController)
        }


        composable(ROUTE_VIEW_BILLS) {
            ViewTenantScreen(navController = navController){}

        }

        composable(ROUTE_AGREEMENT) {
            AgreementScreen(navController = navController){}

        }

        composable(ROUTE_SPLASH) {
            SplashScreen(navController)
        }

        composable(ROUTE_VIEW_TENANTS) {
            ViewTenantScreen(navController = navController){}
        }

        composable(ROUTE_UPDATE_BILLS) {
            UpdateBillsScreen(navController = navController){}
        }


        composable(ROUTE_DASHBOARD) {
            DashboardScreen(navController)
        }

        composable(ROUTE_REGISTER) {
            SignUpScreen(navController = navController) {}
        }

        composable(ROUTE_LOGIN) {
            LoginScreen(navController = navController){}
        }

        composable(ROUTE_ADD_TENANTS) {
            AddTenantDetailsScreen(navController = navController){}
        }

        composable(ROUTE_PAYMENT) {
            PaymentScreen(navController = navController){}
        }

        composable(ROUTE_SUGGESTIONS) {
            SuggestionScreen(navController = navController){}
        }

        composable(ROUTE_COMPLAINTS) {
            ComplaintsScreen(navController = navController){}
        }

























    }
}