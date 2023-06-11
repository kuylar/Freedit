package dev.kuylar.freedit.api.models.responses

import dev.kuylar.freedit.api.models.Page
import dev.kuylar.freedit.api.models.RedditIdentity
import dev.kuylar.freedit.api.models.Subreddit

class FrontpageResponse(
	val identity: RedditIdentity?,
	val trendingSubreddits: List<Subreddit>,
	val home: Home
) {
	class Home(
		val elements: Page
	)
}