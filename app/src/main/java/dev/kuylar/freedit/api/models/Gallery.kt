package dev.kuylar.freedit.api.models

data class Gallery(
	val items: List<Item>
)

data class Item(
	val id: String,
	val adEvents: Any? = null,
	val caption: String? = null,
	val outboundURL: Any? = null,
	val callToAction: Any? = null,
	val displayAddress: Any? = null,
	val media: Media
)

data class Media(
	val typename: String,
	val id: String,
	val userID: String,
	val mimetype: String,
	val width: Long,
	val height: Long,
	val url: String,
	val small: Image,
	val medium: Image,
	val large: Image,
	val xlarge: Image,
	val xxlarge: Image? = null,
	val xxxlarge: Image? = null,
	val originalObfuscated: Any? = null,
	val smallObfuscated: Any? = null,
	val mediumObfuscated: Any? = null,
	val largeObfuscated: Any? = null,
	val xlargeObfuscated: Any? = null,
	val xxlargeObfuscated: Any? = null,
	val xxxlargeObfuscated: Any? = null
)