package org.recherche.smart_auth_android_part.models

data class AdminResponse(
    val create_at: String,
    val email_admin: String,
    val id_admin: Int,
    val is_active: Boolean,
    val name_admin: String,
    val password_admin: String,
    val authority: Int
)