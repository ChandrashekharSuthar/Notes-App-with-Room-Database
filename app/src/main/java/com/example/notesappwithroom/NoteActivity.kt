package com.example.notesappwithroom

import android.os.Bundle
import android.text.Editable
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.notesappwithroom.databinding.ActivityNoteBinding
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NoteActivity : AppCompatActivity() {


    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityNoteBinding.inflate(layoutInflater)
    }

    private var note: Note? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarCreateNote)
        binding.toolbarCreateNote.setNavigationOnClickListener { finish() }

        val intent = intent
        if (intent != null) {
            note = intent.getSerializableExtra("Note") as? Note
            binding.titleEt.text = Editable.Factory.getInstance().newEditable(note?.title ?: "")
            binding.descriptionEt.text =
                Editable.Factory.getInstance().newEditable(note?.description ?: "")

        }

        binding.saveNoteBtn.setOnClickListener {
            note?.let { saveNote(it) }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.view_edit_note_toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.edit_note -> {
                // Handle edit action
                binding.titleEt.isEnabled = true
                binding.descriptionEt.isEnabled = true
                true
            }

            R.id.save_note -> {
                // Handle delete action
                note?.let { saveNote(it) }
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun saveNote(note: Note) {
        val title = binding.titleEt.text.toString().trim()
        val description = binding.descriptionEt.text.toString().trim()

        if (title.isNotEmpty() && description.isNotEmpty()) {

            val updatedNote = Note(
                note.id,
                note.timeStamp,
                title,
                description,
                note.isStarred
            )

            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    val noteDatabase = NotesDatabase.getNotesDatabase(this@NoteActivity)
                    noteDatabase.noteDao().updateNote(updatedNote)
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


}