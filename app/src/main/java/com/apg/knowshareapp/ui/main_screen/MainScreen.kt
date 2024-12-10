package com.apg.knowshareapp.ui.main_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.apg.knowshareapp.data.Book
import com.apg.knowshareapp.ui.favorites_screen.FavsScreen
import com.apg.knowshareapp.ui.login.data.MainScreenDataObject
import com.apg.knowshareapp.ui.main_screen.bottom_menu.BottomMenu
import com.apg.knowshareapp.ui.main_screen.bottom_menu.BottomMenuItem
import com.apg.knowshareapp.ui.settings_screen.SettingsScreen
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    navData: MainScreenDataObject,
    onBookEditClick: (Book) -> Unit,
    onAdminClick: () -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val booksListState = remember { mutableStateOf(emptyList<Book>()) }
    val isAdminState = remember { mutableStateOf(false) }

    // Estado para controlar la pantalla activa
    val selectedTab = remember { mutableStateOf(BottomMenuItem.Home.route) }

    LaunchedEffect(Unit) {
        val db = Firebase.firestore
        getAllBooks(db) { books ->
            booksListState.value = books
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        modifier = Modifier.fillMaxWidth(),
        drawerContent = {
            Column(modifier = Modifier.fillMaxWidth(0.7f)) {
                DrawerHeader(navData.email)
                DrawerBody(onAdmin = { isAdmin -> isAdminState.value = isAdmin }) {
                    coroutineScope.launch { drawerState.close() }
                    onAdminClick()
                }
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                BottomMenu(
                    selectedTab = selectedTab.value,
                    onTabSelected = { tab ->
                        selectedTab.value = tab
                    }
                )
            }
        ) { paddingValues ->
            Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                // Contenido dinámico basado en la pestaña seleccionada
                when (selectedTab.value) {
                    BottomMenuItem.Home.route -> {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(booksListState.value) { book ->
                                BookListItemUi(isAdminState.value, book) { book ->
                                    onBookEditClick(book)
                                }
                            }
                        }
                    }

                    BottomMenuItem.Favs.route -> {
                        FavsScreen()
                    }

                    BottomMenuItem.Settings.route -> {
                        SettingsScreen()
                    }
                }
            }
        }
    }
}


private fun getAllBooks(
    db: FirebaseFirestore,
    onBooks: (List<Book>) -> Unit
){
    db.collection("books")
        .get()
        .addOnSuccessListener {task ->
            val booksList = task.toObjects(Book::class.java)
            onBooks(booksList)
        }
        .addOnFailureListener{

        }
}