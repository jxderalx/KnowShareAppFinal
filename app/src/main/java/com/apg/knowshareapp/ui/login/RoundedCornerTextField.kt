package com.apg.knowshareapp.ui.login

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.apg.knowshareapp.R
import com.apg.knowshareapp.ui.theme.ButtonColor

@Composable
fun RoundedCornerTextField(
    maxLines: Int = 1,
    singleLine: Boolean = true,
    text: String,
    label: String,
    onValueChange: (String) -> Unit,
    isPasswordField: Boolean = false
) {
    val passwordVisibility = remember { mutableStateOf(false) }

    TextField(
        value = text,
        onValueChange = { onValueChange(it) },
        shape = RoundedCornerShape(25.dp),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, ButtonColor, RoundedCornerShape(25.dp)),
        label = {
            Text(text = label, color = Color.Gray)
        },
        singleLine = singleLine,
        maxLines = maxLines,
        visualTransformation = if (isPasswordField && !passwordVisibility.value) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        trailingIcon = {
            if (isPasswordField) {
                IconButton(onClick = {
                    passwordVisibility.value = !passwordVisibility.value
                }) {
                    Icon(
                        painter = painterResource(
                            id = if (passwordVisibility.value) {
                                R.drawable.ic_visibility
                            } else {
                                R.drawable.ic_visibility_off
                            }
                        ),
                        contentDescription = if (passwordVisibility.value) {
                            "Ocultar contraseña"
                        } else {
                            "Mostrar contraseña"
                        }
                    )
                }
            }
        }
    )
}
