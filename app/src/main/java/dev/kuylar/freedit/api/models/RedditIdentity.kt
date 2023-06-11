package dev.kuylar.freedit.api.models

data class RedditIdentity(
	val redditor: Redditor,
	val inbox: Inbox,
	val isEmployee: Boolean,
	val isModerator: Boolean,
	val isEmailVerified: Boolean,
	val isSuspended: Boolean,
	val isForcePasswordReset: Boolean,
	val premium: Premium,
	val modMail: ModMail,
	val interactions: Interactions,
	val coins: Long,
	val createdAt: String,
	val isPasswordSet: Boolean,
	val isNameEditable: Boolean,
	val isSubredditCreationAllowed: Boolean,
	val suspensionExpiresAt: Any? = null,
	val preferences: Preferences
)

data class Inbox(
	val unreadCount: Long
)

data class Interactions(
	val isLayoutSwitchAware: Boolean,
	val isGiveAwardTooltipAware: Boolean,
	val isRedesignModalAware: Boolean,
	val isSubredditChatAware: Boolean,
	val isAdblockModalAware: Boolean,
	val isDefaultPostLayoutAware: Boolean
)

data class ModMail(
	val isUnread: Boolean
)

data class Preferences(
	val isNsfwMediaBlocked: Boolean,
	val isNsfwLabelShown: Boolean,
	val isNightModeEnabled: Boolean,
	val isVideoAutoplayDisabled: Boolean,
	val isRecentPostsShown: Boolean,
	val geopopular: String,
	val defaultCommentSort: String,
	val isReduceAnimationsFromAwardsEnabled: Boolean,
	val isSuggestedSortIgnored: Boolean,
	val isClickTrackingEnabled: Boolean,
	val isLocationBasedRecommendationEnabled: Boolean,
	val isMessageAutoReadEnabled: Boolean,
	val isNsfwContentShown: Boolean,
	val isOnlinePresenceShown: Boolean,
	val isInRedesignBeta: Boolean,
	val postFeedLayout: String,
	val globalCommunityPostFeedSort: GlobalCommunityPostFeedSort,
	val isCommunityPostFeedSortingPreserved: Boolean,
	val isCommunityLayoutPreserved: Boolean,
	val isNewTabOpenedForPostView: Boolean,
	val isMarkdownDefaultEditorMode: Boolean,
	val isCommunityStylingEnabled: Boolean,
	val isTrendingSubredditsShown: Boolean,
	val surveyLastSeenAt: String
)

data class GlobalCommunityPostFeedSort(
	val sort: String,
	val range: Any? = null
)

data class Premium(
	val expiresAt: Any? = null,
	val creddits: Long,
	val subscription: Subscription
)

data class Subscription(
	val isGoldAvailable: Boolean,
	val isPaypalAvailable: Boolean,
	val isStripeAvailable: Boolean
)

data class Redditor(
	val typename: String,
	val id: String,
	val name: String,
	val isGilded: Boolean,
	val profile: Profile,
	val karma: Karma,
	val isLinkedToExternalAccount: Boolean,
	val prefixedName: String,
	val awardedLastMonth: AwardedLastMonth,
	val snoovatarIcon: Icon
)

data class AwardedLastMonth(
	val topAward: Any? = null,
	val totalCount: Long
)

data class Karma(
	val fromAwardsGiven: Long,
	val fromAwardsReceived: Long,
	val fromComments: Long,
	val fromPosts: Long,
	val total: Long
)

data class Profile(
	val id: String,
	val name: String,
	val styles: Styles
)