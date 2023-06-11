package dev.kuylar.freedit.ui.adapter

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.kuylar.freedit.R
import dev.kuylar.freedit.api.models.SubredditPost
import dev.kuylar.freedit.databinding.ItemPostCardBinding


class PostRecyclerAdapter(val posts: MutableList<SubredditPost>) :
	RecyclerView.Adapter<PostRecyclerAdapter.ViewHolder>() {

	class ViewHolder(private val binding: ItemPostCardBinding) :
		RecyclerView.ViewHolder(binding.root) {
		fun bind(post: SubredditPost) {
			val context = binding.root.context
			binding.postInfo.text = context.getString(
				R.string.post_title_template,
				post.authorInfo.name,
				post.subreddit?.name ?: "Sponsored",
				DateUtils.getRelativeTimeSpanString(
					post.createdAt.time,
					System.currentTimeMillis() / 1000,
					0L,
					DateUtils.FORMAT_ABBREV_ALL
				)
			)
			binding.postTitle.text = post.title
			binding.buttonUpvote.text = post.score?.toInt()?.toString() ?: "<score hidden>"
			if (post.media != null) {
				Glide.with(context)
					.load(post.media.still.source.url)
					.into(binding.postImage)
			} else {
				binding.postImage.visibility = View.GONE
			}
		}
	}

	override fun getItemCount(): Int = posts.size

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val inflater = LayoutInflater.from(parent.context)
		return ViewHolder(ItemPostCardBinding.inflate(inflater, parent, false))
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.bind(posts[position])
	}
}