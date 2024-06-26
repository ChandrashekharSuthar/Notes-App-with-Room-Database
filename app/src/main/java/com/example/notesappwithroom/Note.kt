package com.example.notesappwithroom

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val timeStamp: String,
    val title: String,
    val description: String,
    val isStarred: Boolean
) : Serializable
