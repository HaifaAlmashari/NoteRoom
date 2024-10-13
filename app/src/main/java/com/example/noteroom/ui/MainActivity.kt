package com.example.noteroom.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.LEFT
import androidx.recyclerview.widget.ItemTouchHelper.RIGHT
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noteroom.R
import com.example.noteroom.data.Note
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


const val ADD_NOTE_REQUEST = 1
const val EDIT_NOTE_REQUEST = 2

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {
    private lateinit var vm: NoteViewModel
    private lateinit var noteAdapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpRecyclerView()

        setUpListeners()

        vm = ViewModelProviders.of(this)[NoteViewModel::class.java]

        vm.getAllNotes().observe(this, Observer {
            Log.i("Notes observed", "$it")

            noteAdapter.submitList(it)
        })
    }

    private fun setUpListeners() {
        button_add_note.setOnClickListener {
            val intent = Intent(this, AddEditNoteActivity::class.java)
            startActivityForResult(intent, ADD_NOTE_REQUEST)
        }

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, LEFT or RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val note = noteAdapter.getNoteAt(viewHolder.adapterPosition)
                GlobalScope.launch {
                    vm.delete(note)
                }

            }

        }).attachToRecyclerView(recycler_view)
    }

    private fun setUpRecyclerView() {
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)

        noteAdapter = NoteAdapter { clickedNote ->
            val intent = Intent(this, AddEditNoteActivity::class.java)
            intent.putExtra(EXTRA_ID, clickedNote.id)
            intent.putExtra(EXTRA_TITLE, clickedNote.title)
            intent.putExtra(EXTRA_DESCRIPTION, clickedNote.description)
            startActivityForResult(intent, EDIT_NOTE_REQUEST)
        }
        recycler_view.adapter = noteAdapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(data != null && requestCode == ADD_NOTE_REQUEST && resultCode == Activity.RESULT_OK) {
            val title: String? = data.getStringExtra(EXTRA_TITLE)
            val description: String? = data.getStringExtra(EXTRA_DESCRIPTION)
            title?.let { description?.let { it1 -> Note(it, it1) } }?.let { GlobalScope.launch { vm.insert(it) }}
            Toast.makeText(this, "Note inserted!", Toast.LENGTH_SHORT).show()

        } else if(data != null && requestCode == EDIT_NOTE_REQUEST && resultCode == Activity.RESULT_OK) {
            val id = data.getIntExtra(EXTRA_ID, -1)
            if(id == -1) {
                Toast.makeText(this, "Note couldn't be updated!", Toast.LENGTH_SHORT).show()
                return
            }
            val title: String? = data.getStringExtra(EXTRA_TITLE)
            val description: String? =
                data.getStringExtra(EXTRA_DESCRIPTION)
            title?.let { description?.let { it1 -> Note(it, it1, id) } }?.let { GlobalScope.launch { vm.update(it) }}
            Toast.makeText(this, "Note updated!", Toast.LENGTH_SHORT).show()

        } else {
            Toast.makeText(this, "Note not saved!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.main_menu, menu)

        val search = menu?.findItem(R.id.menu_search)
        val searchView = search?.actionView as? SearchView
//        searchView?.isSubmitButtonEnabled = false
        searchView?.setOnQueryTextListener(this)

        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if(query != null){
            searchDatabase(query)
        }
        return true
    }

    private fun searchDatabase(searchText: String) {
        var searchText = searchText
        searchText = "%$searchText%"

        vm.searchDatabase(searchQuery = searchText).observe(this@MainActivity, Observer { list ->
            list?.let {

                noteAdapter.submitList(it)
                Log.e("List = ", list.toString())
            }

        })

    }
}
