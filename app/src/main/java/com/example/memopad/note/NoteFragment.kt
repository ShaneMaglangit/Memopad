package com.example.memopad.note

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.memopad.R
import com.example.memopad.database.NoteDatabase
import com.example.memopad.databinding.NoteFragmentBinding
import kotlinx.android.synthetic.main.note_fragment.*

class NoteFragment : Fragment() {

    private lateinit var binding: NoteFragmentBinding
    private lateinit var viewModel: NoteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val arguments: NoteFragmentArgs by navArgs()
        val application = requireNotNull(this.activity).application
        val dataSource = NoteDatabase.getInstance(application).noteDatabaseDao
        val viewModelFactory = NoteViewModelFactory(arguments.noteKey, dataSource)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(NoteViewModel::class.java)

        binding =
            DataBindingUtil.inflate(inflater, R.layout.note_fragment, container, false)

        binding.noteViewModel = viewModel
        binding.setLifecycleOwner(this)

        binding.floatingActionButton.setOnClickListener {
            val title = edit_title.text.toString()
            val description = edit_description.text.toString()
            val text = edit_text.text.toString()

            viewModel.saveChanges(title, description, text)
        }

        viewModel.navigateToHome.observe(this, Observer {
            if(it) {
                findNavController().navigate(R.id.action_noteFragment_to_homeFragment)
                viewModel.doneNavigating()
            }
        })

        return binding.root
    }
}
