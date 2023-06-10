package dev.kuylar.freedit.api.models

import java.util.Date

class RedditCSRFInfo(
	val authHeader: String,
	val expiry: Date,
	val tracker: String
) {
	fun expired(): Boolean {
		return expiry.before(Date())
	}
}
