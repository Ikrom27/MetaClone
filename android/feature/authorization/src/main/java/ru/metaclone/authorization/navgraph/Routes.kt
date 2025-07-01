package ru.metaclone.authorization.navgraph

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRoute(
    val defaultLogin: String = ""
)