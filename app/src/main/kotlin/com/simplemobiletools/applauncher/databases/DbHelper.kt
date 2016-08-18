package com.simplemobiletools.applauncher.databases

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.text.TextUtils
import com.simplemobiletools.applauncher.R
import com.simplemobiletools.applauncher.models.AppLauncher
import java.util.*

class DbHelper(context: Context) : SQLiteOpenHelper(context, "launchers.db", null, 1) {
    val TABLE = "launchers"
    val CREATE_DB = "CREATE TABLE $TABLE (" +
            "$ID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "$NAME TEXT," +
            "$PKG_NAME TEXT UNIQUE," +
            "$ICON_ID INTEGER, " +
            "$POSITION INTEGER " +
            ")"

    companion object {
        val ID: String = "_id"
        val NAME: String = "name"
        val PKG_NAME: String = "pkgName"
        val ICON_ID: String = "icon"
        val POSITION: String = "position"
    }

    fun addInitialLaunchers(db: SQLiteDatabase) {
        addLauncher("Simple Calculator", "com.simplemobiletools.calculator", R.mipmap.calculator, db)
        addLauncher("Simple Calendar", "com.simplemobiletools.calendar", R.mipmap.calendar, db)
        addLauncher("Simple Camera", "com.simplemobiletools.camera", R.mipmap.camera, db)
        addLauncher("Simple Draw", "com.simplemobiletools.draw", R.mipmap.draw, db)
        addLauncher("Simple File Manager", "com.simplemobiletools.filemanager", R.mipmap.filemanager, db)
        addLauncher("Simple Flashlight", "com.simplemobiletools.flashlight", R.mipmap.flashlight, db)
        addLauncher("Simple Gallery", "com.simplemobiletools.gallery", R.mipmap.gallery, db)
        addLauncher("Simple Music Player", "com.simplemobiletools.musicplayer", R.mipmap.musicplayer, db)
        addLauncher("Simple Notes", "com.simplemobiletools.notes", R.mipmap.notes, db)
    }

    fun addLauncher(name: String, pkgName: String, iconId: Int = 0, db: SQLiteDatabase = writableDatabase) {
        val contentValues = ContentValues()
        contentValues.put(NAME, name)
        contentValues.put(PKG_NAME, pkgName)
        contentValues.put(ICON_ID, iconId)
        db.insert(TABLE, null, contentValues)
    }

    fun deleteLaunchers(ids: ArrayList<String>) {
        val args = TextUtils.join(", ", ids.toArray())
        writableDatabase.delete(TABLE, "$ID IN ($args)", null)
    }

    fun updateLauncherName(id: Int, newName: String): Int {
        val values = ContentValues()
        values.put(NAME, newName)
        val selection = ID + " = ?"
        val selectionArgs = Array(1) { id.toString() }
        return writableDatabase.update(TABLE, values, selection, selectionArgs)
    }

    fun getLaunchers(): ArrayList<AppLauncher> {
        val launchers = ArrayList<AppLauncher>()
        val cursor = readableDatabase.query(TABLE, arrayOf(ID, NAME, PKG_NAME, ICON_ID), null, null, null, null, NAME)
        try {
            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndex(DbHelper.ID))
                val name = cursor.getString(cursor.getColumnIndex(DbHelper.NAME))
                val pkgName = cursor.getString(cursor.getColumnIndex(DbHelper.PKG_NAME))
                val icon = cursor.getInt(cursor.getColumnIndex(DbHelper.ICON_ID))
                launchers.add(AppLauncher(id, name, pkgName, icon))
            }
        } finally {
            cursor.close()
        }
        return launchers
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_DB)
        addInitialLaunchers(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}
