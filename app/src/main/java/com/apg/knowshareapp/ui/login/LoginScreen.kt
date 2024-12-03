package com.apg.knowshareapp.ui.login

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apg.knowshareapp.R
import com.apg.knowshareapp.ui.theme.BoxFilterColor
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoginScreen() {


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

    Box(modifier = Modifier.fillMaxSize()
        .background(BoxFilterColor)
    )

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
        ){
            emailState.value = it
        }
        Spacer(modifier = Modifier.height(10.dp))
        RoundedCornerTextField(
            text = passwordState.value,
            label = "Contraseña",
        ){
            passwordState.value = it
        }
        Spacer(modifier = Modifier.height(10.dp))
        LoginButton(text = "Sign In") {

        }
        LoginButton(text = "Sign Up") { }

    }
}

private fun signUp(auth: FirebaseAuth, email: String, password: String){
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener{
            if(it.isSuccessful){
                Log.d("MyLog", "Registro Correcto!!")
            } else {
                Log.d("MyLog", "Registro Fallido!!")
            }
        }
}

private fun signIn(auth: FirebaseAuth, email: String, password: String){
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener{
            if(it.isSuccessful){
                Log.d("MyLog", "Inicio de Sesión Correcto!!")
            } else {
                Log.d("MyLog", "Inicio de Sesión Fallido!!")
            }
        }
}

private fun deleteAccount(auth: FirebaseAuth, email: String, password: String){
    val credential = EmailAuthProvider.getCredential(email, password)
    auth.currentUser?.reauthenticate(credential)?.addOnCompleteListener{
        if(it.isSuccessful){
            auth.currentUser?.delete()?.addOnCompleteListener{
                if(it.isSuccessful){
                    Log.d("MyLog", "Cuenta Eliminada Correctamente!!")
                } else {
                    Log.d("MyLog", "Error al Eliminar la Cuenta!!")
                }
            }
        } else {
            Log.d("MyLog", "Error al Autenticar!!")
        }
    }
}

private fun signOut(auth: FirebaseAuth){
    auth.signOut()
    Log.d("MyLog", "Sesión Cerrada Correctamente!!")
}

