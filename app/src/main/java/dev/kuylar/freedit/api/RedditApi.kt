package dev.kuylar.freedit.api

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import dev.kuylar.freedit.api.enums.RedditRange
import dev.kuylar.freedit.api.enums.RedditSort
import dev.kuylar.freedit.api.models.ApiResponse
import dev.kuylar.freedit.api.models.JsonWrappedResponse
import dev.kuylar.freedit.api.models.LoginResponse
import dev.kuylar.freedit.api.models.RedditCSRFInfo
import dev.kuylar.freedit.api.models.responses.FrontpageResponse
import okhttp3.FormBody
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.Locale


class RedditApi(private val cookie: String? = null, private val modhash: String? = null) {
	val client = OkHttpClient.Builder().build()
	val gson = GsonBuilder().create()

	fun <T> gqlRequest(
		id: String,
		variables: HashMap<String, Any?>,
		type: Type = object : TypeToken<ApiResponse<JsonObject>>() {}.type
	): ApiResponse<T> {
		val body = hashMapOf<String, Any>(Pair("id", id), Pair("variables", variables))
		val request: Request = Request.Builder().apply {
			url("https://gql.reddit.com")
			post(gson.toJson(body).toRequestBody("application/json".toMediaType()))

			val auth = Static.getAuthorizationHeader(
				hashMapOf(
					Pair(
						"User-Agent",
						Static.ua
					),
					Pair(
						"cookie",
						"reddit_session=$cookie;"
					)
				)
			)

			header(
				"User-Agent",
				Static.ua
			)
			header(
				"cookie",
				"reddit_session=${auth.tracker};"
			)
			header(
				"Authorization",
				"Bearer ${auth.authHeader}"
			)
		}.build()
		client.newCall(request).execute().use { response ->
			val json = response.body!!.string()
			return gson.fromJson(json, type)
		}
	}

	fun getFrontpage(
		sort: RedditSort,
		range: RedditRange? = null,
		forceGeopopular: Boolean = false,
		includeCommunityDUs: Boolean = false,
		includeInterestTopics: Boolean = false,
		includeFeaturedAnnouncements: Boolean = false,
		includeLiveEvents: Boolean = false,
		includeIdentity: Boolean = true,
		includePostRecommendations: Boolean = false,
		includeFreeMarketplaceElement: Boolean = false,
		includeSubredditQuestions: Boolean = false,
		includeExposureEvents: Boolean = false,
		recentPostIds: List<String> = listOf()
	): ApiResponse<FrontpageResponse> {
		// maybe also implement:
		/*
		"adContext": {
			"layout": "CARD",
			"clientSignalSessionData": {
				"adsSeenCount": 3,
				"totalPostsSeenCount": 55,
				"sessionStartTime": "2023-06-10T20:23:04.028Z"
			}
		},
		"feedRankingContext": {
			"servingId": "a uuid",
			"loggedOutAllowNsfw": true
		}
		 */
		return gqlRequest(
			"d45d9e249839", hashMapOf(
				Pair("adContext", null),
				Pair("feedRankingContext", null),
				Pair("forceGeopopular", forceGeopopular),
				Pair("includeCommunityDUs", includeCommunityDUs),
				Pair("includeInterestTopics", includeInterestTopics),
				Pair("includeFeaturedAnnouncements", includeFeaturedAnnouncements),
				Pair("includeLiveEvents", includeLiveEvents),
				Pair("includeIdentity", includeIdentity),
				Pair("includePostRecommendations", includePostRecommendations),
				Pair("includeFreeMarketplaceElement", includeFreeMarketplaceElement),
				Pair("includeSubredditQuestions", includeSubredditQuestions),
				Pair("includeExposureEvents", includeExposureEvents),
				Pair("recentPostIds", recentPostIds),
				Pair("sort", sort)
			), object : TypeToken<ApiResponse<FrontpageResponse>>() {}.type
		)
	}

	object Static {
		private var csrfInfo: RedditCSRFInfo? = null
		val ua = "Mozilla/5.0 (X11; Linux x86_64; rv:109.0) Gecko/20100101 Firefox/112.0"
		private val TAG = "RedditApi.Static"

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

		fun getAuthorizationHeader(
			headers: HashMap<String, String>,
			overrideSaved: Boolean = false
		): RedditCSRFInfo {
			if (csrfInfo != null && !csrfInfo!!.expired() && !overrideSaved) {
				Log.i(TAG, "Using existsing CSRF info (expires at ${csrfInfo!!.expiry})")
				return csrfInfo!!
			}
			Log.i(TAG, "Refreshing CSRF info")
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
				val session = data
					.getAsJsonObject("user")
					.getAsJsonObject("session")
				val csrf = RedditCSRFInfo(
					session
						.getAsJsonPrimitive("accessToken")
						.asString,
					SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).parse(
						session
							.getAsJsonPrimitive("expires")
							.asString
					)!!,
					data.getAsJsonObject("user")
						.getAsJsonPrimitive("sessionTracker")
						.asString,
				)
				csrfInfo = csrf
				return csrf
			}
		}

		fun instance(context: Context): RedditApi {
			val sp = context.getSharedPreferences("main", Activity.MODE_PRIVATE)!!
			val cookie = sp.getString("cookie", null)
			val modhash = sp.getString("modhash", null)
//			thread {
//				getAuthorizationHeader(
//					hashMapOf(
//						Pair("User-Agent", ua),
//						Pair("Cookie", "reddit_session=$cookie;")
//					), overrideSaved = true
//				)
//			}
			return RedditApi(cookie, modhash)
		}
	}
}