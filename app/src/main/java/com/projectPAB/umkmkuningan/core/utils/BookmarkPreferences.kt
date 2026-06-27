package com.projectPAB.umkmkuningan.core.utils

import android.content.Context
import android.content.SharedPreferences

class BookmarkPreferences(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("bookmark_prefs", Context.MODE_PRIVATE)

    fun addBookmark(productId: String) {
        val bookmarks = getBookmarks().toMutableSet()
        bookmarks.add(productId)
        prefs.edit().putStringSet("BOOKMARKS", bookmarks).apply()
    }

    fun removeBookmark(productId: String) {
        val bookmarks = getBookmarks().toMutableSet()
        bookmarks.remove(productId)
        prefs.edit().putStringSet("BOOKMARKS", bookmarks).apply()
    }

    fun isBookmarked(productId: String): Boolean {
        return getBookmarks().contains(productId)
    }

    fun getBookmarks(): Set<String> {
        return prefs.getStringSet("BOOKMARKS", emptySet()) ?: emptySet()
    }
}
