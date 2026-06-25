package com.example.youtubesearch

data class SearchResponse(val items: List<SearchItem> = emptyList())

data class SearchItem(val id: VideoId? = null, val snippet: Snippet? = null)

data class VideoId(val videoId: String? = null)

data class Snippet(
    val title: String? = null,
    val description: String? = null,
    val publishTime: String? = null,
    val publishedAt: String? = null,
    val channelTitle: String? = null,
    val thumbnails: Thumbnails? = null
)

data class Thumbnails(
    val default: Thumb? = null,
    val medium: Thumb? = null,
    val high: Thumb? = null
)

data class Thumb(val url: String? = null)
