package com.example.notesappwithroom

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.notesappwithroom.databinding.ActivityMainBinding
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var notesDatabase: NotesDatabase

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        notesDatabase = NotesDatabase.getNotesDatabase(this)

        GlobalScope.launch {
            notesDatabase.noteDao().createNote(
                Note(
                    0,
                    System.currentTimeMillis().toString(),
                    "First Note",
                    "Description",
                    true
                )
            )
        }


    }
}