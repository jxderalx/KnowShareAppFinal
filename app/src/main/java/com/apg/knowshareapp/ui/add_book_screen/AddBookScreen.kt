package com.apg.knowshareapp.ui.add_book_screen

import android.content.ContentResolver
import android.net.Uri
import android.util.Base64
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.apg.knowshareapp.R
import com.apg.knowshareapp.data.Book
import com.apg.knowshareapp.ui.data.AddScreenObject
import com.apg.knowshareapp.ui.login.LoginButton
import com.apg.knowshareapp.ui.login.RoundedCornerTextField
import com.apg.knowshareapp.ui.theme.BoxFilterColorAddBook
import com.apg.knowshareapp.ui.theme.textAddBook
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

@Preview(showBackground = true)
@Composable
fun AddBookScreen(
    navData: AddScreenObject = AddScreenObject(),
    onSaved: () -> Unit = {}
) {

    val cv =  LocalContext.current.contentResolver

    val selectedCategory = remember {
        mutableStateOf(navData.category)
    }

    val navImageUrl = remember {
        mutableStateOf(navData.imageUrl)
    }

    val title = remember {
        mutableStateOf(navData.title)
    }
    val description = remember {
        mutableStateOf(navData.description)
    }
    val price = remember {
        mutableStateOf(navData.price)
    }

    val selectedImageUri = remember {
        mutableStateOf<Uri?>(null)
    }

    val firestore = remember {
        Firebase.firestore
    }

    val storage = remember {
        Firebase.storage
    }

    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        navImageUrl.value = ""
        selectedImageUri.value = uri
    }

    Image(painter = rememberAsyncImagePainter(
        model = navImageUrl.value.ifEmpty {
            selectedImageUri.value
        }
    ),
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

    Box(modifier = Modifier
        .fillMaxSize()
        .background(BoxFilterColorAddBook)
    )

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(
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

        Text("Selecciona una categoría")
        RoundedCornerDropDownMenu(selectedCategory.value) { selectedItem ->
            selectedCategory.value = selectedItem
        }

        Spacer(modifier = Modifier.height(15.dp))

        RoundedCornerTextField(
            text = title.value,
            label = "Título",
            onValueChange = { title.value = it }
        )

        Spacer(modifier = Modifier.height(10.dp))

        RoundedCornerTextField(
            maxLines = 6,
            singleLine = false,
            text = description.value,
            label = "Descripción",
            onValueChange = { description.value = it }
        )

        Spacer(modifier = Modifier.height(10.dp))

        RoundedCornerTextField(
            text = price.value,
            label = "Precio",
            onValueChange = { price.value = it }
        )

        Spacer(modifier = Modifier.height(10.dp))

        LoginButton(text = "Subir imagen") {
            imageLauncher.launch("image/*")

        }

        LoginButton(text = "Guardar") {

            val book = Book(
                key = navData.key,
                title = title.value,
                description = description.value,
                price = price.value,
                category = selectedCategory.value,
            )

            if (selectedImageUri.value != null ){
                saveBookImage(
                    navData.imageUrl,
                    selectedImageUri.value!!,
                    storage,
                    firestore,
                    book,
                    onSaved = {
                        onSaved()
                    },
                    onError = {

                    }

                )
            }

            else {
                saveBookToFireStore(
                    firestore,
                    book.copy(imageUrl = navData.imageUrl),
                    onSaved = {
                        onSaved()
                    },
                    onError = {

                    }
                )
            }

        }
    }
}


private fun imageToBase64(uri: Uri, contentResolver: ContentResolver): String {
    val inputStream = contentResolver.openInputStream(uri)

    val bytes = inputStream?.readBytes()
    return bytes?.let {
        Base64.encodeToString(it, Base64.DEFAULT)
    } ?: ""
}


private fun saveBookImage(
    oldImageUrl: String,
    uri: Uri,
    storage: FirebaseStorage,
    firestore: FirebaseFirestore,
    book: Book,
    onSaved: () -> Unit,
    onError: () -> Unit
){
    val timeStamp = System.currentTimeMillis()
    val storageRef = if(oldImageUrl.isEmpty()) {
        storage.reference
            .child("book_images")
            .child("image_$timeStamp.jpg")
    } else{
        val path = oldImageUrl.substringAfter("/o/").substringBefore("?").replace("%2F", "/")
        storage.getReference(path)
    }
    val uploadTask = storageRef.putFile(uri)
    uploadTask.addOnSuccessListener {
        storageRef.downloadUrl.addOnSuccessListener {url ->
            saveBookToFireStore(
                firestore,
                book.copy(imageUrl = url.toString()),
                onSaved = {
                    onSaved()
                },
                onError = {
                    onError()
                }
            )
        }
    }.addOnFailureListener{
        onError()
    }
}

private fun saveBookToFireStore(
    firestore: FirebaseFirestore,
    book: Book,
    onSaved: () -> Unit,
    onError: () -> Unit
){
    val db = firestore.collection("books")
    val key = book.key.ifEmpty { db.document().id }
    db.document(key)
        .set(
            book.copy(key = key,)
        )
        .addOnSuccessListener {
            onSaved()
        }
        .addOnFailureListener{
            onError()
        }
}