package dev.kuylar.freedit.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import dev.kuylar.freedit.api.RedditApi
import dev.kuylar.freedit.api.enums.RedditSort
import dev.kuylar.freedit.api.models.SubredditPost
import dev.kuylar.freedit.databinding.FragmentFrontpageBinding
import dev.kuylar.freedit.ui.adapter.PostRecyclerAdapter
import kotlin.concurrent.thread

class FrontpageFragment : Fragment() {
	private lateinit var binding: FragmentFrontpageBinding
	private val posts: MutableList<SubredditPost> = mutableListOf()
	private val adapter: PostRecyclerAdapter = PostRecyclerAdapter(posts)
	private lateinit var api: RedditApi

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
	): View {
		api = RedditApi.Static.instance(requireContext())
		binding = FragmentFrontpageBinding.inflate(inflater)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.recycler.adapter = adapter
		binding.recycler.layoutManager =
			LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
		loadMore(null)
	}

	private fun loadMore(nextPageToken: String?) {
		if (nextPageToken == null) {
			thread {
				val frontpage = api.getFrontpage(RedditSort.HOT)
				val p = frontpage.data!!.home.elements.edges.map { it.node }.filter { it.__typename == "SubredditPost" }
				val lastLength = posts.size
				activity?.runOnUiThread {
					posts.addAll(p)
					adapter.notifyItemRangeInserted(lastLength, p.size)
				}
			}
		} else {
			TODO("Paging is not implemented yet")
		}
	}
}