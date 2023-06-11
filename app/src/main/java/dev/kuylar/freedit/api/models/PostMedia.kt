package dev.kuylar.freedit.api.models

data class PostMedia (
	val typeHint: String,
	val still: Still,
	val obfuscatedStill: ObfuscatedStill,
	val animated: Any? = null,
	val streaming: Any? = null,
	val packagedMedia: Any? = null,
	val video: Any? = null
)

data class ObfuscatedStill (
	val source: Any? = null
)

data class Still (
	val source: Image,
	val small: Image,
	val medium: Image,
	val large: Image,
	val xlarge: Image,
	val xxlarge: Image,
	val xxxlarge: Image
)