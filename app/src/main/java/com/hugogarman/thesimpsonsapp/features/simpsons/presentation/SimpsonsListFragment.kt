package com.hugogarman.thesimpsonsapp.features.simpsons.presentation

import SimpsonsListAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hugogarman.thesimpsonsapp.R
import com.hugogarman.thesimpsonsapp.core.presentation.ext.hide
import com.hugogarman.thesimpsonsapp.core.presentation.ext.visible
import com.hugogarman.thesimpsonsapp.databinding.FragmentSimpsonsListBinding
import com.hugogarman.thesimpsonsapp.features.simpsons.domain.Simpson
import org.koin.androidx.viewmodel.ext.android.viewModel

class SimpsonsListFragment : Fragment() {

    private var _binding: FragmentSimpsonsListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SimpsonsListViewModel by viewModel()
    private val adapter = SimpsonsListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSimpsonsListBinding.inflate(inflater, container, false)
        setupRecyclerView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeUiState()
        viewModel.loadSimpsons()
    }

    private fun setupRecyclerView() {
        binding.rvSimpsons.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSimpsons.adapter = adapter

        adapter.setEvent { simpsonId ->
            val action = R.id.action_simpsonsListFragment_to_simpsonDetailFragment
            val bundle = Bundle().apply {
                putInt("simpsonId", simpsonId)
            }
            findNavController().navigate(action, bundle)
        }
    }

    private fun observeUiState() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            when {
                state.isLoading -> {
                    binding.rvSimpsons.hide()
                }
                else -> {
                    binding.rvSimpsons.visible()
                    state.simpsonsList?.let { bindData(it) }
                }
            }
        }
    }

    private fun bindData(simpsons: List<Simpson>) {
        adapter.submitList(simpsons)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}