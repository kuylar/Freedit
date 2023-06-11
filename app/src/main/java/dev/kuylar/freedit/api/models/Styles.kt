package dev.kuylar.freedit.api.models

data class Styles(
	val legacyIcon: Icon? = null,
	val primaryColor: Any? = null,
	val icon: String? = null
)

data class Icon(
	val url: String
)