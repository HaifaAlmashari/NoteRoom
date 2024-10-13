package com.example.noteroom.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.noteroom.R
import kotlinx.android.synthetic.main.activity_add_edit_note.*

const val EXTRA_ID = "com.example.noteroom.EXTRA_ID"
const val EXTRA_TITLE = "com.example.noteroom.EXTRA_TITLE"
const val EXTRA_DESCRIPTION = "com.example.noteroom.EXTRA_DESCRIPTION"

class AddEditNoteActivity : AppCompatActivity() {

    private lateinit var mode : Mode

    private var noteId: Int = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_note)

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_close_24)

        noteId = intent.getIntExtra(EXTRA_ID, -1)
        mode = if(noteId == -1) Mode.AddNote
        else Mode.EditNote

        when(mode) {
            Mode.AddNote -> title = "Add Note"
            Mode.EditNote -> {
                title = "Edit Note"
                et_title.setText(intent.getStringExtra(EXTRA_TITLE))
                et_desc.setText(intent.getStringExtra(EXTRA_DESCRIPTION))
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.add_note_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save_note -> {
                saveNote()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveNote() {
        val title = et_title.text.toString()
        val desc = et_desc.text.toString()

        if(title.isEmpty() || desc.isEmpty()) {
            Toast.makeText(this, "please insert title and description", Toast.LENGTH_SHORT).show()
            return
        }

        val data = Intent()
        if(noteId != -1)
            data.putExtra(EXTRA_ID, noteId)
        data.putExtra(EXTRA_TITLE, title)
        data.putExtra(EXTRA_DESCRIPTION, desc)

        setResult(Activity.RESULT_OK, data)
        finish()
    }

    private sealed class Mode {
        object AddNote : Mode()
        object EditNote : Mode()
    }
}