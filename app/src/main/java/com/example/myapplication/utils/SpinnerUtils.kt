package com.example.myapplication.utils

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.PopupWindow
import android.widget.TextView
import com.example.myapplication.R

object SpinnerUtils {

    @SuppressLint("MissingInflatedId")
    fun showPopup(
        context: Context,
        spinner: TextView,
        data: List<String>,
        onItemSelected: (String) -> Unit
    ) {
        val builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.custom_searchable_spinner, null)
        val searchEditText: EditText = view.findViewById(R.id.search_view)
        val listView: ListView = view.findViewById(R.id.list_view)

        val adapter = CustomArrayAdapter(context, android.R.layout.simple_list_item_1, data)
        listView.adapter = adapter

        builder.setView(view)
        val dialog = builder.create()
        dialog.show()

        listView.setOnItemClickListener { parent, _, position, _ ->
            val selectedItem = parent.getItemAtPosition(position).toString()
            onItemSelected(selectedItem)
            dialog.dismiss()
        }

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filter.filter(s)
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }


    fun setupSearchableSpinner(
        context: Context,
        spinner: TextView,
        data: List<String>,
        onItemSelected: (String) -> Unit
    ) {
        spinner.setOnClickListener {
            showPopup(context, spinner, data, onItemSelected)
        }
    }
}
