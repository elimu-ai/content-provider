package ai.elimu.content_provider.provider

import ai.elimu.content_provider.BuildConfig
import ai.elimu.content_provider.util.SharedPreferencesHelper
import ai.elimu.content_provider.utils.SharedDataKeys
import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri

/**
 * A ContentProvider that provides access to shared data to external apps in elimu's ecosystem.
 * Shared data might be language, theme, etc.
 * The shared data is not persisted in Database but in memory, which helps reducing the complexity
 * of the system.
 */
class SharedDataProvider: ContentProvider() {

    companion object {
        private const val AUTHORITY: String = BuildConfig.APPLICATION_ID + ".provider.shared_data"

        private const val TABLE_SHARED_DATA = "shared_data"
        private const val CODE_SHARED_DATA = 1

        private const val COL_ID = "_id"
        private const val COL_LANGUAGE = SharedDataKeys.KEY_LANGUAGE

        private const val ROW_LANGUAGE = 1

        private val MATCHER = UriMatcher(UriMatcher.NO_MATCH)

        init {
            MATCHER.addURI(AUTHORITY, TABLE_SHARED_DATA, CODE_SHARED_DATA)
        }
    }

    private val dataList by lazy {
        mutableListOf(
            mapOf(COL_ID to ROW_LANGUAGE, COL_LANGUAGE to SharedPreferencesHelper.getLanguage(context))
        )
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor {
        return when (MATCHER.match(uri)) {
            CODE_SHARED_DATA -> {
                val cursor = MatrixCursor(arrayOf(COL_ID, COL_LANGUAGE))
                dataList.forEach { row ->
                    cursor.addRow(arrayOf(row[COL_ID], row[COL_LANGUAGE]))
                }
                cursor
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun getType(uri: Uri): String? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        return 0
    }
}