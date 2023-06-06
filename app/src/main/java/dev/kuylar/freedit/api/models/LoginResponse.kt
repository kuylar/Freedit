package dev.kuylar.freedit.api.models

class LoginResponse(
	val cookie: String?,
	val modhash: String?,
	val details: String?
)