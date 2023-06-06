package dev.kuylar.freedit.api.models

import com.google.gson.JsonArray

class ApiResponse<T>(
	val errors: JsonArray,
	val data: T?
)