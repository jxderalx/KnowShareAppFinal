package com.apg.knowshareapp.ui.utils

import androidx.activity.compose.BackHandler
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

@Composable
fun BackPressHandler(onExit: () -> Unit) {
    var showDialog by remember {
        mutableStateOf(false)
    }

    BackHandler {
        showDialog = true
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false },
            title = {
                Text(
                    text = "Salir de la aplicación") },
            text = {
                Text(
                    text = "¿Estás seguro de que deseas salir?") },
            confirmButton = {
                Button(
                    onClick = onExit,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007396))
                ) {
                    Text(
                        text = "Salir")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007396))
                ) {
                    Text(
                        text = "Cancelar")
                }
            }
        )
    }
}
