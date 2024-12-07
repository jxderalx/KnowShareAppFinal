package com.apg.knowshareapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.apg.knowshareapp.ui.add_book_screen.AddBookScreen
import com.apg.knowshareapp.ui.data.AddScreenObject
import com.apg.knowshareapp.ui.login.LoginScreen
import com.apg.knowshareapp.ui.login.data.LoginScreenObject
import com.apg.knowshareapp.ui.login.data.MainScreenDataObject
import com.apg.knowshareapp.ui.main_screen.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = AddScreenObject) {

                composable<LoginScreenObject> {
                    LoginScreen{navData ->
                        navController.navigate(navData)
                    }
                }

                composable<MainScreenDataObject> {navEntry ->
                    val navData = navEntry.toRoute<MainScreenDataObject>()
                    MainScreen(navData)
                }

                composable<AddScreenObject> { navEntry ->
                    AddBookScreen()
                }
            }
        }
    }
}