package com.example.notesapp

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_notes.*
import java.lang.Exception

class AddNotes : AppCompatActivity() {
    var id = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_notes)


        try {
            var bundle: Bundle?=intent.extras
            id = bundle!!.getInt("ID",0)
            if(id!=0)
            {
                etText.setText(bundle!!.getString("Title").toString())
                etDes.setText(bundle!!.getString("Description").toString())

            }

        }catch (ex:Exception){

        }

    }
    fun buAdd(view: View){
        var dbManager = DBManager(this)
        var values = ContentValues()
        values.put("Title",etText.text.toString())
        values.put("Description",etDes.text.toString())
        if(id==0) {
            val ID = dbManager.Insert(values)

            if (ID > 0) {
                Toast.makeText(this, "Note Added", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Cannot Add", Toast.LENGTH_LONG).show()
            }
        }
        else {
            var selectionArgs = arrayOf(id.toString())
            val ID = dbManager.Update(values,"ID=?",selectionArgs)
            if (ID > 0) {
                Toast.makeText(this, "Note Updated", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Cannot Update", Toast.LENGTH_LONG).show()
            }
        }
        finish()
    }
}