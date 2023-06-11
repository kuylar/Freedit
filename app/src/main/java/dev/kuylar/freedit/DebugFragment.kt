package dev.kuylar.freedit

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import dev.kuylar.freedit.api.RedditApi
import dev.kuylar.freedit.api.enums.RedditSort
import dev.kuylar.freedit.databinding.FragmentDebugBinding
import kotlin.concurrent.thread

class DebugFragment : Fragment() {
	private lateinit var binding: FragmentDebugBinding

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
	): View {
		binding = FragmentDebugBinding.inflate(inflater)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.buttonDebugLogin.setOnClickListener {
			activity?.startActivity(Intent(activity, LoginActivity::class.java))
		}

		binding.buttonDumpCreds.setOnClickListener {
			val sp = activity?.getSharedPreferences("main", Activity.MODE_PRIVATE)!!
			Toast.makeText(
				activity,
				"⚠️ TOKENS WILL BE SHOWN AFTER THIS TOAST. MAKE SURE NO ONE IS LOOKING",
				Toast.LENGTH_LONG
			).show()
			Toast.makeText(
				activity,
				"cookie: ${sp.getString("cookie", "<no cookie saved>")}",
				Toast.LENGTH_SHORT
			).show()
			Toast.makeText(
				activity,
				"modhash: ${sp.getString("modhash", "<no cookie saved>")}",
				Toast.LENGTH_SHORT
			).show()
		}

		binding.buttonDebugCurrentUser.setOnClickListener {
			binding.buttonDebugCurrentUser.isEnabled = false
			val api = RedditApi.Static.instance(requireActivity())
			thread {
				val home = api.getFrontpage(RedditSort.HOT)
				activity?.runOnUiThread {
					binding.buttonDebugCurrentUser.isEnabled = true
					val identity =
						home.data!!.identity
					if (identity == null) {
						Toast.makeText(
							activity, "not logged in", Toast.LENGTH_LONG
						).show()
					} else {
						Toast.makeText(
							activity, "name: ${identity.redditor.name}", Toast.LENGTH_LONG
						).show()
					}
				}
			}
		}

		binding.buttonGetAuthHeader.setOnClickListener {
			binding.buttonGetAuthHeader.isEnabled = false
			thread {
				val h = RedditApi.Static.getAuthorizationHeader(
					hashMapOf(
						Pair(
							"User-Agent",
							"Mozilla/5.0 (X11; Linux x86_64; rv:109.0) Gecko/20100101 Firefox/112.0"
						)
					)
				)
				activity?.runOnUiThread {
					binding.buttonGetAuthHeader.isEnabled = true
					Toast.makeText(
						activity, "Authorization: $h", Toast.LENGTH_LONG
					).show()
				}
			}
		}

		binding.buttonGetFrontpageSubreddits.setOnClickListener {
			binding.buttonGetFrontpageSubreddits.isEnabled = false
			thread {
				val api = RedditApi.Static.instance(requireActivity())
				val frontpage = api.getFrontpage(RedditSort.HOT)
				activity?.runOnUiThread {
					binding.buttonGetFrontpageSubreddits.isEnabled = true
					frontpage.data!!.trendingSubreddits.forEach {
						Toast.makeText(
							activity,
							it.name,
							Toast.LENGTH_LONG
						).show()
					}
				}
			}
		}

		binding.buttonGetFrontpagePosts.setOnClickListener {
			binding.buttonGetFrontpagePosts.isEnabled = false
			thread {
				val api = RedditApi.Static.instance(requireActivity())
				val frontpage = api.getFrontpage(RedditSort.HOT)
				activity?.runOnUiThread {
					binding.buttonGetFrontpagePosts.isEnabled = true
					frontpage.data!!.home.elements.edges.take(5).forEach {
						Toast.makeText(
							activity,
							"u/${it.node.authorInfo.name} on r/${it.node.subreddit?.name ?: ("Sponsored? " + it.node.isAd())}\n${it.node.title}",
							Toast.LENGTH_LONG
						).show()
					}
				}
			}
		}
	}
}