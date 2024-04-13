package com.example.notesappwithroom

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NoteAdapter(val context: Context, private var notes: List<Note>) :
    RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.bind(note)

    }

    override fun getItemCount(): Int {
        return notes.size
    }

    fun submitList(newNotes: List<Note>) {
        notes = newNotes
        notifyDataSetChanged()
    }

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        private val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        private val starredIcon: MaterialButton = itemView.findViewById(R.id.starredIcon)

        fun bind(note: Note) {
            titleTextView.text = note.title
            descriptionTextView.text = note.description
            dateTextView.text = formatDateFromLong(note.timeStamp)
            starredIcon.setIconResource(if (note.isStarred) R.drawable.round_star_24 else R.drawable.round_star_outline_24)
            starredIcon.setOnClickListener {
                updateNote(note)
            }

        }

    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun updateNote(note: Note) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                val noteDatabase = NotesDatabase.getNotesDatabase(context)
                noteDatabase.noteDao().updateNote(
                    Note(
                        note.id,
                        note.timeStamp,
                        note.title,
                        note.description,
                        !note.isStarred
                    )
                )
            }
        }
    }


    // Function to convert a string representing a long value to a Date object
    private fun formatDateFromLong(longString: String): String {
        val longValue = longString.toLong()
        val date = Date(longValue)
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        return sdf.format(date)
    }


}
