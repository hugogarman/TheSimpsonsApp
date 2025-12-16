package com.hugogarman.thesimpsonsapp.features.simpsons.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hugogarman.thesimpsonsapp.core.presentation.ext.loadUrl
import com.hugogarman.thesimpsonsapp.databinding.FragmentSimpsonsDetailBinding
import com.hugogarman.thesimpsonsapp.features.simpsons.domain.Simpson
import org.koin.androidx.viewmodel.ext.android.viewModel

class SimpsonDetailFragment : Fragment() {

    private var _binding: FragmentSimpsonsDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SimpsonsListViewModel by viewModel()
    private var simpsonId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSimpsonsDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btBack.setOnClickListener {
            findNavController().navigateUp()
        }

        simpsonId = arguments?.getInt("simpsonId")

        observeUiState()
        viewModel.loadSimpsons()
    }

    private fun observeUiState() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            state.simpsonsList?.find { it.id == simpsonId }?.let { simpson ->
                bindData(simpson)
            }
        }
    }

    private fun bindData(simpson: Simpson) {
        binding.apply {
            ivSimpsonDetail.loadUrl(simpson.portraitPath)
            tvSimpsonNameDetail.text = simpson.name
            tvSimpsonAgeDetail.text = if (simpson.age > 0) {
                "Age: ${simpson.age}"
            } else {
                "Age: Unknown"
            }
            tvSimpsonOccupationDetail.text = "Occupation: ${simpson.occupation}"
            tvPhrases.text = simpson.phrases.joinToString("\n") { "- \"$it\"" }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}