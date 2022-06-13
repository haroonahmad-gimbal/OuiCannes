package com.gimbal.kotlin.ouicannes.data.model

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class LoggedInUser(
    val userId: String,
    val name: String,
    val company : String,
    val phone : String,
    val points: Int,
    val title : String,
)

fun LoggedInUser.toMap() : HashMap<String, Any> {
        return hashMapOf(
            "ar_id" to "123",
            "uid" to userId,
            "name" to name,
            "company" to company,
            "title" to title,
            "phone" to phone,
            "points" to points
        )
}

