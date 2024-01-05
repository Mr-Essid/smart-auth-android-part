package org.recherche.smart_auth_android_part.models

import java.util.Locale.Builder

data class EmployerUpdate(
    val id_employer: Int,
    var lastname_employer: String? = null,
    var name_employer: String? = null
)