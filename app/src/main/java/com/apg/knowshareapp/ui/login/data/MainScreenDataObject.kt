package com.apg.knowshareapp.ui.login.data

import kotlinx.serialization.Serializable

@Serializable
data class MainScreenDataObject(
    val uid: String = "",
    val email: String = "",
)
