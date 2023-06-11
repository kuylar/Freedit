package dev.kuylar.freedit.api.models

class Flair(
	val cssClass: String,
	val richtext: String,
	val template: Template,
	val text: String,
	val textColor: String,
	val type: String
) {
	class Template(
		val id: String,
		val type: String,
		val text: String,
		val richtext: String,
		val textColor: String,
		val backgroundColor: String,
		val isEditable: Boolean,
		val isModOnly: Boolean,
		val cssClass: String,
		val maxEmojis: Long,
		val allowableContent: String
	)
}
