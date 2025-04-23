package ai.elimu.content_provider.utils.converter

import ai.elimu.model.v2.gson.content.SoundGson
import android.database.Cursor
import android.util.Log

object CursorToSoundGsonConverter {
    
    private const val TAG = "CursorToSoundGsonConverter"
    
    @JvmStatic
    fun getSoundGson(cursor: Cursor): SoundGson {
        Log.i(TAG, "getSoundGson")

        Log.i(TAG,
            "Arrays.toString(cursor.getColumnNames()): " + cursor.columnNames.contentToString()
        )

        val columnIndexId = cursor.getColumnIndex("id")
        val id = cursor.getLong(columnIndexId)
        Log.i(TAG, "id: $id")

        val columnIndexRevisionNumber = cursor.getColumnIndex("revisionNumber")
        val revisionNumber = cursor.getInt(columnIndexRevisionNumber)
        Log.i(TAG, "revisionNumber: $revisionNumber")

        val columnIndexValueIpa = cursor.getColumnIndex("valueIpa")
        val valueIpa = cursor.getString(columnIndexValueIpa)
        Log.i(TAG, "valueIpa: \"$valueIpa\"")

        val soundGson = SoundGson()
        soundGson.id = id
        soundGson.revisionNumber = revisionNumber
        soundGson.valueIpa = valueIpa

        return soundGson
    }
}
