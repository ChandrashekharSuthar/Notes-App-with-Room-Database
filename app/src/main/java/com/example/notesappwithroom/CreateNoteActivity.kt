package com.example.notesappwithroom

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.notesappwithroom.databinding.ActivityCreateNoteBinding
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateNoteActivity : AppCompatActivity() {

    private lateinit var notesDatabase: NotesDatabase

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityCreateNoteBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarCreateNote)
        binding.toolbarCreateNote.setNavigationOnClickListener { finish() }

        // Get the database Instance
        notesDatabase = NotesDatabase.getNotesDatabase(this)

        // Scroll Effect for Fab button
        binding.descriptionEt.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY > oldScrollY) {
                // Scrolling down
                binding.saveNoteBtn.shrink() // Hide FAB
            } else {
                // Scrolling up or not scrolling
                binding.saveNoteBtn.extend() // Show FAB
            }
        }

        binding.saveNoteBtn.setOnClickListener {
            saveNote()
        }

    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun saveNote() {
        val title = binding.titleEt.text.toString().trim()
        val description = binding.descriptionEt.text.toString().trim()

        if (title.isNotEmpty() && description.isNotEmpty()) {
            val note = Note(
                id = 0,
                timeStamp = System.currentTimeMillis().toString(),
                title = title,
                description = description,
                isStarred = false
            )

            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    notesDatabase.noteDao().createNote(note)
                }
                // Finish the activity upon completion
                Toast.makeText(applicationContext, "Note Saved", Toast.LENGTH_SHORT).show()
                finish()
            }
        } else {
            if (title.isEmpty()) binding.titleLayout.error = "Enter a note title"
            if (description.isEmpty()) binding.descriptionLayout.error = "Write a note"
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.create_note_toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save_note -> {
                // Handle Save Note
                saveNote()
                true
            }
//            R.id.action_delete -> {
//                // Handle delete action
//                true
//            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}