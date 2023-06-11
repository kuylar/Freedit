package dev.kuylar.freedit.api.models

data class PageInfo (
	val hasNextPage: Boolean,
	val hasPreviousPage: Boolean,
	val startCursor: String,
	val endCursor: String
)