package com.example.notesapp

class Note {
    var noteId:Int?=null
    var nodeName:String?=null
    var noteDescription:String?=null
    constructor(noteId:Int,nodeName:String,noteDescription:String){
        this.noteId = noteId
        this.nodeName = nodeName
        this.noteDescription = noteDescription
    }
}