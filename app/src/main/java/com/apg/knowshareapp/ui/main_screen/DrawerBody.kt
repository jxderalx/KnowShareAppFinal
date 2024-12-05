package com.apg.knowshareapp.ui.main_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.apg.knowshareapp.R

@Composable
fun DrawerBody() {
    val categoriesList = listOf(
        "Favoritos",
        "Fantasía",
        "Drama",
        "BestSellers"
    )

    Box(modifier = Modifier.fillMaxSize()){
        Image(
            modifier = Modifier.fillMaxSize(),
                //.padding(top = 170.dp), //necesario para que no se coma el fondo azul el logo de la tienda
            painter = painterResource(id = R.drawable.drawer_body),
            contentDescription = "",
            alpha = 0.2f,
            contentScale = ContentScale.Crop
        )
        Column(modifier = Modifier.fillMaxSize()) {

        }
    }
}