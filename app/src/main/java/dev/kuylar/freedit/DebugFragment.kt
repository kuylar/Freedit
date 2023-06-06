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
			val sp = activity?.getSharedPreferences("main", Activity.MODE_PRIVATE)!!
			val api = RedditApi(sp.getString("cookie", null), sp.getString("modhash", null))
			thread {
				val home = api.gqlRequest(
					"d45d9e249839", hashMapOf(
						Pair("adContext", null),
						Pair("feedRankingContext", null),
						Pair("forceGeopopular", true),
						Pair("includeCommunityDUs", false),
						Pair("includeInterestTopics", false),
						Pair("includeFeaturedAnnouncements", false),
						Pair("includeLiveEvents", false),
						Pair("includeIdentity", true),
						Pair("includePostRecommendations", false),
						Pair("includeFreeMarketplaceElement", false),
						Pair("includeSubredditQuestions", false),
						Pair("includeExposureEvents", false),
						Pair("recentPostIds", emptyList<String>()),
						Pair("sort", "HOT")
					)
				)
				activity?.runOnUiThread {
					binding.buttonDebugCurrentUser.isEnabled = true
					val name =
						home.data!!.getAsJsonObject("identity")
							.getAsJsonObject("redditor")
							.getAsJsonPrimitive("name").asString
					Toast.makeText(
						activity, "name: $name", Toast.LENGTH_LONG
					).show()
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
	}
}