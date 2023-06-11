package dev.kuylar.freedit.api.models

data class Subreddit(
	val id: String,
	val styles: Styles,
	val name: String,
	val subscribers: Long,
	val title: String,
	val type: String,
	val path: String,
	val isFavorite: Boolean,
	val isNSFW: Boolean,
	val isQuarantined: Boolean,
	val isSubscribed: Boolean,
	val wls: String,
	val prefixedName: String,
	val postFlairSettings: PostFlairSettings,
	val isThumbnailsEnabled: Boolean,
	val isFreeFormReportingAllowed: Boolean,
	val isPredictionsTournamentAllowed: Boolean,
	val isPredictionAllowed: Boolean,
	val answerableQuestions: List<Any?>
)

data class PostFlairSettings(
	val position: Any? = null,
	val isEnabled: Boolean,
	val isSelfAssignable: Boolean
)

