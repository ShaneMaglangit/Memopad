package com.example.memopad.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.memopad.R
import com.example.memopad.SwipeToDeleteCallback
import com.example.memopad.database.NoteDatabase
import com.example.memopad.databinding.HomeFragmentBinding

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val application = requireNotNull(this.activity).application
        val dataSource = NoteDatabase.getInstance(application).noteDatabaseDao
        val viewModelFactory = HomeViewModelFactory(dataSource, application)

        val viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel::class.java)

        val adapter = NoteAdapter( NoteListener {
            viewModel.openNote(it)
        })

        val binding: HomeFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false)


        val swipeHandler = object: SwipeToDeleteCallback(application.applicationContext) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val note = adapter.getItem(viewHolder.adapterPosition)

                viewModel.removeNote(note)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.recyclerNotesList)

        binding.homeViewModel = viewModel
        binding.recyclerNotesList.adapter = adapter
        binding.setLifecycleOwner(this)

        viewModel.allNotes.observe(this, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        viewModel.navigateToNoteFragment.observe(this, Observer {note ->
            note?.let{
                this.findNavController().navigate(
                    HomeFragmentDirections
                        .actionHomeFragmentToNoteFragment(note.noteId)
                )

                viewModel.doneNavigating()
            }
        })

        return binding.root
    }
}
