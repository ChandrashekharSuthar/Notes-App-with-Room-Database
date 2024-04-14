package com.example.notesappwithroom

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notesappwithroom.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var notesDatabase: NotesDatabase
    private lateinit var adapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Get the database Instance
        notesDatabase = NotesDatabase.getNotesDatabase(this)

        // Initialize with an empty list
        adapter = NoteAdapter(this, emptyList())
        // Setup Recycler View
        binding.notesRv.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.notesRv.adapter = adapter

        getAllNotes() // Fetch Notes from Database

        binding.createNoteBtn.setOnClickListener {
            // Navigate to create Note Activity
            val intent = Intent(this@MainActivity, CreateNoteActivity::class.java)
            startActivity(intent)
        }


        // Scroll Effect for Fab button
        binding.notesRv.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY > oldScrollY) {
                // Scrolling down
                binding.createNoteBtn.shrink() // Hide FAB
            } else {
                // Scrolling up or not scrolling
                binding.createNoteBtn.extend() // Show FAB
            }
        }


    }

    private fun getAllNotes() {
        notesDatabase.noteDao().getAllNotes().observe(this) { notes ->
            // Update UI or perform any actions with the received notes
            adapter.submitList(notes)
            if (notes.isNotEmpty()) binding.noNoteText.visibility =
                View.GONE else binding.noNoteText.visibility = View.VISIBLE
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_developer_info -> {
                // Handle edit action
                Toast.makeText(this@MainActivity, "Feature Not Available yet", Toast.LENGTH_SHORT)
                    .show()
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