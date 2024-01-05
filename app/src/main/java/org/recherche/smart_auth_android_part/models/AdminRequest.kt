package org.recherche.smart_auth_android_part.models

data class AdminRequest(
    val email_admin: String,
    val name_admin: String,
    val password_admin: String
)