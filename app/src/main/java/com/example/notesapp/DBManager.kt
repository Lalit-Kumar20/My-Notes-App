package com.example.notesapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQuery
import android.database.sqlite.SQLiteQueryBuilder
import android.media.projection.MediaProjection
import android.widget.Toast

class DBManager {
    val dbName = "MyNewNotes"
    val dbTable="NNotes"
    val colId = "ID"
    val colTitle = "Title"
    val colDes = "Description"
    val dbVersion = 1
    val sqlCreateTable="CREATE TABLE IF NOT EXISTS "+dbTable+" ("+colId+" INTEGER PRIMARY KEY,"+colTitle+" TEXT, "+colDes+" TEXT);"
    var sqlDB:SQLiteDatabase?=null
    constructor(context: Context){
        val db = DataBaseHelperNotes(context)
        sqlDB = db.writableDatabase
    }
    inner class DataBaseHelperNotes:SQLiteOpenHelper{
        var context:Context?=null
        constructor(context:Context):super(context,dbName,null,dbVersion){
             this.context = context
        }
        override fun onCreate(db: SQLiteDatabase?) {
           db!!.execSQL(sqlCreateTable)
            Toast.makeText(this.context,"Database created",Toast.LENGTH_LONG).show()
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db!!.execSQL("DROP table IF EXISTS "+dbTable);
        }

    }
    fun Insert(values:ContentValues):Long{
        val ID = sqlDB!!.insert(dbTable,"",values)
        return ID
    }
    fun Query(projection: Array<String>,selection:String,selectionArgs:Array<String>,sorOrder:String):Cursor{
         val qb = SQLiteQueryBuilder()
        qb.tables = dbTable
        val cursor = qb.query(sqlDB,projection,selection,selectionArgs,null,null,sorOrder)
        return cursor
    }
    fun Delete(selection:String,selectionArgs:Array<String>):Int{
        val count = sqlDB!!.delete(dbTable,selection,selectionArgs)
        return count
    }
    fun Update(values: ContentValues,selection:String,selectionArgs: Array<String>):Int{
        val count = sqlDB!!.update(dbTable,values,selection,selectionArgs)
        return count
    }

}