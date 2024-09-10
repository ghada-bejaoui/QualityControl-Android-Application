package com.example.myapplication.QC.ui.fragment

import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.myapplication.QC.data.model.HistoryItem
import com.example.myapplication.QC.data.repository.QcRepository
import com.example.myapplication.QC.ui.viewmodel.QCViewModelFactory
import com.example.myapplication.QC.ui.viewmodel.QcViewModel
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentAddFormBinding
import com.example.myapplication.utils.RetrofitBuilder
import com.example.myapplication.utils.SpinnerUtils
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddFormFragment : Fragment() {

    private val viewModel: QcViewModel by activityViewModels {
        QCViewModelFactory(QcRepository(RetrofitBuilder.qcService))
    }
    private lateinit var mProgressDialog: AlertDialog
    private lateinit var binding: FragmentAddFormBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddFormBinding.inflate(inflater, container, false)

        // binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        initView()
        setupObservers()
        setupSpinners()
        setupButtons()

        return binding.root
    }


    private fun initView() {
        mProgressDialog = ProgressDialog(requireContext()).apply {
            setMessage(getString(R.string.login_progress_msg))
            setCancelable(true)
        }
    }

    private fun setupObservers() {
        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
            mProgressDialog.dismiss()
        }

        viewModel.warningMessage.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
            mProgressDialog.dismiss()
        }

        viewModel.loading.observe(requireActivity()) { loading ->
            if (loading) mProgressDialog.show()
            else mProgressDialog.dismiss()
        }

        viewModel.lines.observe(viewLifecycleOwner, Observer { lines ->
            binding.spinnerLines.isEnabled = true
            lines?.let {
                SpinnerUtils.setupSearchableSpinner(
                    requireContext(),
                    binding.spinnerLines,
                    it.map { line -> line.id },
                    { selectedLine ->
                        viewModel.selectedLine.value = selectedLine
                        binding.spinnerLines.setText(selectedLine)
                        viewModel.loadTeams()
                    }
                )
            }
        })

        viewModel.teams.observe(viewLifecycleOwner, Observer { teams ->
            binding.spinnerTeams.isEnabled = true
            teams?.let {
                SpinnerUtils.setupSearchableSpinner(
                    requireContext(),
                    binding.spinnerTeams,
                    it.map { team -> team.id },
                    { selectedTeam ->
                        viewModel.selectedTeam.value = selectedTeam
                        binding.spinnerTeams.setText(selectedTeam)
                        binding.editTextDefectsCount.isEnabled = true
                    }
                )
            }
        })

        viewModel._saveFormDataResult.observe(viewLifecycleOwner, Observer { result ->
            binding.spinnerLines.setText("")
            binding.spinnerTeams.setText("")
            binding.editTextDefectsCount.setText("")
            binding.spinnerLines.isEnabled = false
            binding.spinnerTeams.isEnabled = false
            binding.editTextDefectsCount.isEnabled = false
            viewModel.selectedLine = MutableLiveData<String>()
            viewModel.selectedTeam = MutableLiveData<String>()
            setupSpinners()
        })

    }

    private fun setupSpinners() {
        // Load data for spinners
        viewModel.loadLines()
    }

    private fun setupButtons() {
        binding.buttonSave.setOnClickListener {
            val line = viewModel.selectedLine.value ?: ""
            val team = viewModel.selectedTeam.value ?: ""
            val defectsCount = binding.editTextDefectsCount.text.toString().toIntOrNull() ?: 0
            val id = generateId() // Assumes you have a method to generate or get the ID
            val date = getCurrentDate() // Assumes you have a method to get the current date

            viewModel.saveFormData(
                HistoryItem(
                    //   id = id,
                    date = date,
                    line = line,
                    team = team,
                    defectsCount = defectsCount
                )
            )
        }

        // Add this click listener to navigate to HistoricFragment
        binding.buttonViewHistory.setOnClickListener {
            findNavController().navigate(R.id.action_addFormFragment_to_historiqueFragment)
        }
    }

    private fun generateId(): String {
        // Generate or get a unique ID
        return "ID_${System.currentTimeMillis()}"
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        return dateFormat.format(Calendar.getInstance().time)
    }

}
