package com.apg.knowshareapp.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apg.knowshareapp.R
import com.apg.knowshareapp.ui.login.data.MainScreenDataObject
import com.apg.knowshareapp.ui.theme.textError
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun LoginScreen(
    onNavigateToMainScreen: (MainScreenDataObject) -> Unit
) {

    val auth = remember {
        Firebase.auth
    }

    val errorState = remember {
        mutableStateOf("")
    }

    val emailState = remember {
        mutableStateOf("")
    }
    val passwordState = remember {
        mutableStateOf("")
    }

    Image(painter = painterResource(
        id = R.drawable.store_background),
        contentDescription = "BG",
        modifier = Modifier
            .fillMaxSize(),
        contentScale = ContentScale.Crop
    )

    /*
    Box(modifier = Modifier.fillMaxSize()
        .background(BoxFilterColor)
    )
     */


    Column(modifier = Modifier.fillMaxSize().padding(
        start = 38.dp, end = 38.dp
    ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier.size(175.dp)
        )
        Spacer(modifier = Modifier.height(80.dp))
        Text(
            text = "KnowShare",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            fontFamily = FontFamily.Serif
        )
        Spacer(modifier = Modifier.height(15.dp))
        RoundedCornerTextField(
            text = emailState.value,
            label = "Email",
            onValueChange = { emailState.value = it }
        )

        Spacer(modifier = Modifier.height(10.dp))

        RoundedCornerTextField(
            text = passwordState.value,
            label = "Contraseña",
            isPasswordField = true,
            onValueChange = { passwordState.value = it }
        )

        Spacer(modifier = Modifier.height(10.dp))
        if(errorState.value.isNotEmpty()){
            Text(
                text = errorState.value,
                fontSize = 17.sp,
                color = textError,
                textAlign = TextAlign.Center
                )
        }
        LoginButton(text = "Inicia sesión") {
            signIn(
                auth,
                emailState.value,
                passwordState.value,
                onSignInSuccess = {navData ->
                    onNavigateToMainScreen(navData)
                },
                onSignInFailure = { error ->
                    errorState.value = error
                }
            )
        }
        LoginButton(text = "Registrarse") {
            signUp(
                auth,
                emailState.value,
                passwordState.value,
                onSignUpSuccess = {navData ->
                    onNavigateToMainScreen(navData)
                },
                onSignUpFailure = { error ->
                    errorState.value = error
                }
            )
        }
    }
}

fun signUp(
    auth: FirebaseAuth,
    email: String,
    password: String,
    onSignUpSuccess: (MainScreenDataObject) -> Unit,
    onSignUpFailure: (String) -> Unit
){
    if (email.isBlank() || password.isBlank()){
        onSignUpFailure("Para registrarte necesitas un correo y una contraseña.")
        return
    }
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSignUpSuccess(
                    MainScreenDataObject(
                        task.result.user?.uid!!,
                        task.result.user?.email!!
                    )
                )
            }        }
        .addOnFailureListener{
            onSignUpFailure(it.message ?: "Error de registro")
        }
}

fun signIn(
    auth: FirebaseAuth,
    email: String,
    password: String,
    onSignInSuccess: (MainScreenDataObject) -> Unit,
    onSignInFailure: (String) -> Unit
){
    if (email.isBlank() || password.isBlank()){
        onSignInFailure("Por favor, ingresa un correo y una contraseña para iniciar sesión.")
        return
    }

    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSignInSuccess(
                    MainScreenDataObject(
                        task.result.user?.uid!!,
                        task.result.user?.email!!
                    )
                )
            }
        }
        .addOnFailureListener{
            onSignInFailure(it.message ?: "Error de inicio de sesión")
        }
}