package dev.kuylar.freedit.api.models

data class Image(
	val url: String,
	val dimensions: Dimensions
)

data class Dimensions(
	val width: Long,
	val height: Long
)
