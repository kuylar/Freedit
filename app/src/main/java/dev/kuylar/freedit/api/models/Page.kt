package dev.kuylar.freedit.api.models

class Page(
	val pageInfo: PageInfo,
	val dist: Int,
	val edges: List<NodeContainer<SubredditPost>>
)