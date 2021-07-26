package com.example.notesapp

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.icu.text.CaseMap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.SearchView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.ticket.view.*

class MainActivity : AppCompatActivity() {
   var listNotes = ArrayList<Note>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
           //add dummy data
       // listNotes.add(Note(1,"First","this is first"))
        //listNotes.add(Note(2,"Second","this is second"))
        //listNotes.add(Note(3,"Third","this is third"))


        // load from database
       LoadQuery("%")

    }

    override fun onResume() {
        super.onResume()
        LoadQuery("%")
     }
    fun LoadQuery(title:String){
        var dbManager = DBManager(this)
        val projection = arrayOf("ID","Title","Description")
        val selectionArgs = arrayOf(title)
        val cursor = dbManager.Query(projection,"Title like ?",selectionArgs,"Title")
        listNotes.clear()
        if(cursor.moveToFirst()){
              do {
                 val ID = cursor.getInt(cursor.getColumnIndex("ID"))
                val Title = cursor.getString(cursor.getColumnIndex("Title"))

                val Description = cursor.getString(cursor.getColumnIndex("Description"))
                listNotes.add(Note(ID,Title,Description))

            }while(cursor.moveToNext());
        }
        var adapter = NoteAdapter(this,listNotes)
        lView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        val sv: SearchView = menu!!.findItem(R.id.search_note).actionView as SearchView

        val sm= getSystemService(Context.SEARCH_SERVICE) as SearchManager
        sv.setSearchableInfo(sm.getSearchableInfo(componentName))
        sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                Toast.makeText(applicationContext, query, Toast.LENGTH_LONG).show()
                // to search database
                LoadQuery("%" + query + "%")
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item!=null) {
            when (item.itemId) {
                R.id.add_note -> {
                   //go to add page
                   var intent = Intent(this,AddNotes::class.java)
                    startActivity(intent)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
    inner class NoteAdapter: BaseAdapter {
        var listNotes = ArrayList<Note>()
        var context:Context?=null
        constructor(context: Context,listNotes:ArrayList<Note>):super(){
          this.listNotes = listNotes
            this.context = context
        }
        override fun getCount(): Int {
             return listNotes.size
        }

        override fun getItem(position: Int): Any {
            return listNotes[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
             var myView = layoutInflater.inflate(R.layout.ticket,null)
            var myNote = listNotes[position]
            myView.tView.text = myNote.nodeName
            myView.dView.text = myNote.noteDescription
            myView.ivDelete.setOnClickListener {
                var dbManager = DBManager(this.context!!)
                var selectionArgs = arrayOf(myNote.noteId.toString())
                dbManager.Delete("ID=?",selectionArgs)
                LoadQuery("%")

            }
            myView.ivUpdate.setOnClickListener {
                GoToUpdate(myNote)
            }
            return myView
        }

    }
    fun GoToUpdate(note:Note){
        var intent = Intent(this,AddNotes::class.java)
        intent.putExtra("ID",note.noteId)
        intent.putExtra("Title",note.nodeName)
        intent.putExtra("Description",note.noteDescription)
        startActivity(intent)
    }
}