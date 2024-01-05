package org.recherche.smart_auth_android_part.models

data class EmployerResponse(
    val create_at: String,
    val email_employer: String,
    val id_employer: Int,
    val identifier_employer: String,
    val lastname_employer: String,
    val name_employer: String,
    val is_active: Boolean
)