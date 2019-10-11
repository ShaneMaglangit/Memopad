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
import com.example.memopad.R
import com.example.memopad.database.NoteDatabase
import com.example.memopad.databinding.HomeFragmentBinding

class HomeFragment : Fragment() {

    private lateinit var binding: HomeFragmentBinding
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(this.activity).application
        val dataSource = NoteDatabase.getInstance(application).noteDatabaseDao
        val viewModelFactory = HomeViewModelFactory(dataSource, application)
        val adapter = NoteAdapter()

        binding =
            DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false)

        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel::class.java)

        binding.homeViewModel = viewModel
        binding.recyclerNotesList.adapter = adapter
        binding.setLifecycleOwner(this)


        binding.floatingActionButton.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_noteFragment)
        }

        viewModel.allNotes.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        return binding.root
    }
}
