package com.example.myapplication.QC.ui.fragment

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.QC.ui.viewmodel.HistoriqueViewModel
import com.example.myapplication.QC.ui.adapter.HistoricAdapter
import com.example.myapplication.QC.data.repository.QcRepository
import com.example.myapplication.QC.ui.viewmodel.HistoriqueViewModelFactory
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentHistoricBinding
import com.example.myapplication.utils.ExcelUtils
import com.example.myapplication.utils.RetrofitBuilder
import java.io.File
import java.util.Calendar

class HistoricFragment : Fragment() {

    private lateinit var binding: FragmentHistoricBinding
    private lateinit var mProgressDialog: AlertDialog

    private val viewModel: HistoriqueViewModel by activityViewModels {
        HistoriqueViewModelFactory(QcRepository(RetrofitBuilder.qcService))
    }
    private lateinit var adapter: HistoricAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoricBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialization of adapter for RecyclerView
        adapter = HistoricAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        mProgressDialog = ProgressDialog(requireContext()).apply {
            setMessage(getString(R.string.login_progress_msg))
            setCancelable(true)
        }

        // Buttons for selecting dates
        binding.buttonDateDebut.setOnClickListener {
            showDatePicker { selectedDate ->
                binding.textDateDebut.text = selectedDate // Displays the selected date
            }
        }

        binding.buttonDateFin.setOnClickListener {
            showDatePicker { selectedDate ->
                binding.textDateFin.text = selectedDate // Displays the selected date
            }
        }

        binding.generateExcelButton.setOnClickListener {
            generateExcelFile()
        }

        // Button to apply filter
        binding.applyFilterButton.setOnClickListener {
            val dateDebut = binding.textDateDebut.text.toString().trim()
            val dateFin = binding.textDateFin.text.toString().trim()

            if (dateDebut.isNotEmpty() && dateFin.isNotEmpty()) {
                // Make sure the dates are in the format yyyy-MM-dd
                if (isValidDateFormat(dateDebut) && isValidDateFormat(dateFin)) {
                    viewModel.loadHistory(dateDebut, dateFin)
                } else {
                    Toast.makeText(requireContext(), "Invalid date format.", Toast.LENGTH_LONG)
                        .show()
                }
            } else {
                Toast.makeText(requireContext(), "Please select dates.", Toast.LENGTH_LONG).show()
            }
        }


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

        // Observe ViewModel data
        viewModel.history.observe(viewLifecycleOwner, Observer { historyItems ->
            if (historyItems.isNullOrEmpty()) {
                binding.noDataTextView.visibility = View.VISIBLE
            } else {
                binding.noDataTextView.visibility = View.GONE
                adapter.submitList(historyItems)
                binding.generateExcelButton.visibility = View.VISIBLE
            }
        })

        // Manage errors
        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        })

        // Manage warnings
        viewModel.warningMessage.observe(viewLifecycleOwner, Observer { warning ->
            warning?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun showDatePicker(onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                // Date formatting in yyyy-MM-dd with a zero for single digit months and days
                val formattedMonth = String.format("%02d", selectedMonth + 1)
                val formattedDay = String.format("%02d", selectedDay)
                val selectedDate = "$selectedYear-$formattedMonth-$formattedDay"
                onDateSelected(selectedDate)
            }, year, month, day)

        datePickerDialog.show()
    }


    private fun generateExcelFile() {
        val historyItems = viewModel.history.value ?: return

        val file = ExcelUtils.createExcelFile(requireContext(), historyItems)
        if (file != null) {
            sendEmailWithAttachment(file)
        } else {
            Toast.makeText(
                requireContext(),
                "Error while creating the Excel file.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun sendEmailWithAttachment(file: File) {
        val uri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.provider",
            file
        )
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/vnd.ms-excel"
            putExtra(Intent.EXTRA_SUBJECT, "Data history")
            putExtra(
                Intent.EXTRA_TEXT,
                "Please find the Excel file with historical data attached."
            )
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(
                Intent.EXTRA_EMAIL,
                arrayOf("gbejaoui08@gmail.com")
            ) // Add the email address here
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(Intent.createChooser(intent, "Send email"))
    }

    // Function to check if the date format is valid
    private fun isValidDateFormat(date: String): Boolean {
        return date.matches(Regex("\\d{4}-\\d{2}-\\d{2}"))
    }

}
