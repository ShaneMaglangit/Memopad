package com.example.memopad.note

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.memopad.R
import com.example.memopad.database.NoteDatabase
import com.example.memopad.databinding.NoteFragmentBinding

class NoteFragment : Fragment() {

    private lateinit var binding: NoteFragmentBinding
    private lateinit var viewModel: NoteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.note_fragment, container, false)

        binding.setLifecycleOwner(this)

        binding.floatingActionButton.setOnClickListener {
            val title = binding.editTitle.text.toString()
            val description = binding.editDescription.text.toString()
            val text = binding.editText.text.toString()

            viewModel.addNote(title=title, description=description, text=text)

            findNavController().navigate(R.id.action_noteFragment_to_homeFragment)
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val application = requireNotNull(this.activity).application
        val dataSource = NoteDatabase.getInstance(application).noteDatabaseDao
        val viewModelFactory = NoteViewModelFactory(dataSource, application)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(NoteViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
