package dev.kuylar.freedit.api

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import dev.kuylar.freedit.api.models.ApiResponse
import dev.kuylar.freedit.api.models.JsonWrappedResponse
import dev.kuylar.freedit.api.models.LoginResponse
import okhttp3.FormBody
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.regex.Pattern


class RedditApi(private val cookie: String? = null, private val modhash: String? = null) {
	val client = OkHttpClient.Builder().build()
	val gson = GsonBuilder().create()

	fun gqlRequest(id: String, variables: HashMap<String, Any?>):
			ApiResponse<JsonObject> {
		val body = hashMapOf<String, Any>(Pair("id", id), Pair("variables", variables))
		val request: Request = Request.Builder().apply {
			url("https://gql.reddit.com")
			post(gson.toJson(body).toRequestBody("application/json".toMediaType()))

			header(
				"User-Agent",
				"Mozilla/5.0 (X11; Linux x86_64; rv:109.0) Gecko/20100101 Firefox/112.0"
			)
			header(
				"cookie",
				"reddit_session=$cookie;"
			)
			header(
				"Authorization",
				"Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6IlNIQTI1NjpzS3dsMnlsV0VtMjVmcXhwTU40cWY4MXE2OWFFdWFyMnpLMUdhVGxjdWNZIiwidHlwIjoiSldUIn0.eyJzdWIiOiJ1c2VyIiwiZXhwIjoxNjg2MDkxMzI4Ljk0MDcxMiwiaWF0IjoxNjg2MDA0OTI4Ljk0MDcxMSwianRpIjoiQTB2aHJXZU1rbGtJNkwtMVAxaXRQZXNtZGFZUFBnIiwiY2lkIjoiOXRMb0Ywc29wNVJKZ0EiLCJsaWQiOiJ0Ml9kdXRoMTBxaiIsImFpZCI6InQyX2R1dGgxMHFqIiwibGNhIjoxNjI4NzAwNDQ4MjI2LCJzY3AiOiJlSnhra2RHTzlDQUloZC1GYTVfZ2Y1VV9tNDFWT2tOV3BRSHNaTjUtWXl1ZEpudlZBLWRUNGZRX1lJMVVJTUJHQkFGaVN0eWJRWUFrbURPWlFnRE1ORHByaVNRUTRFbHFMRzhJUUJtYmtRMVphTWNhVzN3Z0JLaWNFN2VWSHBjMm9hVWJpNTRkdjZweUxqeXBPVWZsM05qbUxXeFA5RE1icTAycFdOWlRtY1IxcFhRV0xfb1pPOVMzOXVVel9TYTBSOE9LcXZHQm95TVk4X0haV01aaUd2ZnhucHIwWkYwd3E3M0xRV3BmNnJHNzlrV1QwREs0X1J4dnZEYVRHWEplbXA3Ul90MzFTLWpBUGNfTDlOcUJHYXY3WHJydFdidF8xUTVVemlqUldKejROQnk1Y3ZrZXZ3VGJOZWxmNDNaa0xMNFpjZE1iZm1zNk9uSng0dENuOGZVYkFBRF9fMThTMkZFIiwicmNpZCI6IkJPeDVuWU1odzd5YWFKWmkybGhSZ2xxT25BU3VXUmt1UUJndEg4YmRhMzQiLCJmbG8iOjJ9.mt-kT657CFxKiq_PEFJyqKewiDCRPLwXzbJPVqe4tSPYIbm_xqbC7iF_CtbqmqbGrbAr2xZ02epgUjFhaqhDElJfPLBQV8T2MLNfI3ZJUfy3sTt2Y_EadykaYUlbCDzHlKCau9fBAznRhPZa-xCQ5M_a25oIVEvO_cO44RbJTm350hjamp_ykxQj_OH3bKc0J2bm3YDNvkxC6ld5FU2u4gZSsyAcABBNrYXe0vg_2QIap3IAYwpBPK7-JyIEeYfUqwekN0vTnoM7dDbuDgxbL0P7kCkzXgyFWAFBS_tFd3-SjKE7ijfLNcZvMBSKE4VPM00GzqjvHoja8_bGTpFvzA"
			)
		}.build()
		client.newCall(request).execute().use { response ->
			val json = response.body!!.string()
			return gson.fromJson(
				json,
				object : TypeToken<ApiResponse<JsonObject>>() {}.type
			)
		}
	}

	object Static {
		fun login(
			username: String,
			password: String
		): JsonWrappedResponse<ApiResponse<LoginResponse>> {
			val client = OkHttpClient.Builder().build()
			val gson = GsonBuilder().create()
			val request: Request = Request.Builder().apply {
				url("https://old.reddit.com/api/login")
				post(FormBody.Builder().apply {
					add("dest", "https://old.reddit.com")
					add("user", username)
					add("passwd", password)
					add("api_type", "json")
				}.build())

				header(
					"User-Agent",
					"Mozilla/5.0 (X11; Linux x86_64; rv:109.0) Gecko/20100101 Firefox/112.0"
				)
			}.build()
			client.newCall(request).execute().use { response ->
				return gson.fromJson(
					response.body!!.string(),
					object : TypeToken<JsonWrappedResponse<ApiResponse<LoginResponse>>>() {}.type
				)
			}
		}

		fun loginWithOtp(
			username: String,
			password: String,
			otp: String
		): JsonWrappedResponse<ApiResponse<LoginResponse>> {
			val client = OkHttpClient.Builder().build()
			val gson = GsonBuilder().create()
			val request: Request = Request.Builder().apply {
				url("https://old.reddit.com/api/login")
				post(FormBody.Builder().apply {
					add("dest", "https://old.reddit.com")
					add("user", username)
					add("passwd", password)
					add("otp_sent", "true")
					add("otp", otp)
					add("api_type", "json")
				}.build())

				header(
					"User-Agent",
					"Mozilla/5.0 (X11; Linux x86_64; rv:109.0) Gecko/20100101 Firefox/112.0"
				)
			}.build()
			client.newCall(request).execute().use { response ->
				return gson.fromJson(
					response.body!!.string(),
					object : TypeToken<JsonWrappedResponse<ApiResponse<LoginResponse>>>() {}.type
				)
			}
		}

		fun getAuthorizationHeader(headers: HashMap<String, String>): String {
			val client = OkHttpClient.Builder().build()
			val gson = GsonBuilder().create()
			val request: Request = Request.Builder().apply {
				url("https://www.reddit.com/")

				headers.forEach {
					header(it.key, it.value)
				}
			}.build()
			client.newCall(request).execute().use { response ->
				val html = response.body!!.string()
				val r = Regex("<script id=\"data\">window\\.___r = (.+?);</script>")
				val find = r.find(html)
				val data = gson.fromJson(find!!.groups[1]!!.value, JsonObject::class.java)
				return data
					.getAsJsonObject("user")
					.getAsJsonObject("session")
					.getAsJsonPrimitive("accessToken")
					.asString
			}
		}
	}
}