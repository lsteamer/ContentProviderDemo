package com.github.lsteamer.contentproviderdemo

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri

class AcronymProvider : ContentProvider() {

    lateinit var db: SQLiteDatabase


    //only needed for the retrieving?
    companion object {
        val PROVIDER_NAME = "com.github.lsteamer.contentproviderdemo/AcronymProvider"
        val URL = "content://$PROVIDER_NAME/ACTABLE"
        val CONTENT_URI = Uri.parse(URL)

        val _ID = "_ID"
        val NAME = "NAME"
        val MEANING = "MEANING"
    }

    override fun onCreate(): Boolean {
        val helper = ContentDemoDatabase(context)
        db = helper.writableDatabase
        return true
    }

    override fun insert(uri: Uri, cv: ContentValues?): Uri? {
        db.insert("ACTABLE", null, cv)
        context?.contentResolver?.notifyChange(uri, null)
        return uri
    }

    override fun update(
        uri: Uri,
        cv: ContentValues?,
        condition: String?,
        conditionValues: Array<out String>?
    ): Int {
        val count = db.update("ACTABLE", cv, condition, conditionValues)
        context?.contentResolver?.notifyChange(uri, null)
        return count
    }

    override fun delete(uri: Uri, condition: String?, conditionValues: Array<out String>?): Int {
        val count = db.delete("ACTABLE", condition, conditionValues)
        context?.contentResolver?.notifyChange(uri, null)
        return count
    }

    override fun query(
        uri: Uri,
        colums: Array<out String>?,
        condition: String?,
        conditionValues: Array<out String>?,
        order: String?
    ): Cursor? {
        return db.query("ACTABLE", colums, condition, conditionValues, null, null, order)
    }

    //Provider-specific part: vnd.<name>.<type>
    //You supply the <name> and <type>.
    //The <name> value should be globally unique, and the <type> value should be unique to the corresponding URI pattern.
    //A good choice for <name> is your company's name or some part of your application's Android package name.
    //A good choice for the <type> is a string that identifies the table associated with the URI.
    override fun getType(p0: Uri): String? {
        return "vnd.android.cursor.dir/vnd.example.actable"
    }
}