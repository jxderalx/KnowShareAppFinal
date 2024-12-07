package com.apg.knowshareapp.ui.add_book_screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.apg.knowshareapp.R
import com.apg.knowshareapp.data.Book
import com.apg.knowshareapp.ui.login.LoginButton
import com.apg.knowshareapp.ui.login.RoundedCornerTextField
import com.apg.knowshareapp.ui.theme.BoxFilterColor
import com.apg.knowshareapp.ui.theme.BoxFilterColorAddBook
import com.apg.knowshareapp.ui.theme.textAddBook
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

@Preview(showBackground = true)
@Composable
fun AddBookScreen() {
    val title = remember {
        mutableStateOf("")
    }
    val description = remember {
        mutableStateOf("")
    }
    val price = remember {
        mutableStateOf("")
    }

    val selectedImageUri = remember {
        mutableStateOf<Uri?>(null)
    }

    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {uri ->
        selectedImageUri.value = uri
    }

    Image(painter = rememberAsyncImagePainter(
        model = selectedImageUri.value),
        contentDescription = "BG",
        modifier = Modifier
            .fillMaxSize(),
        contentScale = ContentScale.Crop,
    )

    /*
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFFFFF), // Blanco puro
                        Color(0xFF00BCD5) // Azul
                    )
                )
            )
    )

     */



    Box(modifier = Modifier.fillMaxSize()
        .background(BoxFilterColorAddBook)
    )



    Column(modifier = Modifier.fillMaxSize().padding(
        start = 38.dp, end = 38.dp
    ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier.size(135.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Añadir nuevo libro",
            color = textAddBook,
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
            fontFamily = FontFamily.Serif
        )
        Spacer(modifier = Modifier.height(15.dp))
        RoundedCornerDropDownMenu {selectedItem ->

        }
        Spacer(modifier = Modifier.height(15.dp))
        RoundedCornerTextField(
            text = title.value,
            label = "Título",
        ){
            title.value = it
        }
        Spacer(modifier = Modifier.height(10.dp))
        RoundedCornerTextField(
            maxLines = 7,
            singleLine = false,
            text = description.value,
            label = "Descripción",
        ){
            description.value = it
        }
        Spacer(modifier = Modifier.height(10.dp))
        RoundedCornerTextField(
            text = price.value,
            label = "Precio",
        ){
            price.value = it
        }
        Spacer(modifier = Modifier.height(10.dp))
        LoginButton(text = "Subir imagen") {
            imageLauncher.launch("image/*")

        }
        LoginButton(text = "Guardar") {

        }
    }
}

private fun saveBookImage(
    uri: Uri,
    storage: FirebaseStorage,
    book: Book
){
    val timeStamp = System.currentTimeMillis()
    val storageRef = storage.reference
        .child("book_images")
        .child("image_$timeStamp.jpg")
    val uploadTask = storageRef.putFile(uri)
    uploadTask.addOnSuccessListener {
        storageRef.downloadUrl.addOnSuccessListener {url ->

        }

    }
}

private fun saveBookToFireStore(
    firestore: FirebaseFirestore,
    url: String,
    book: Book,
    onSaved: () -> Unit,
    onError: () -> Unit
){
    val db = firestore.collection("books")
    val key = db.document().id
    db.document(key)
        .set(
            book.copy(
                key = key,
                imageUrl = url)
        )
        .addOnSuccessListener {

        }
}










