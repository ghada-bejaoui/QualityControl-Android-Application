package com.example.myapplication.utils

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter


class CustomArrayAdapter(
    context: Context,
    resource: Int,
    private val originalData: List<String>
) : ArrayAdapter<String>(context, resource, originalData) {

    private var filteredData: List<String> = originalData.toList()

    override fun getCount(): Int {
        return filteredData.size
    }

    override fun getItem(position: Int): String? {
        return filteredData[position]
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()
                val filteredList = mutableListOf<String>()

                if (constraint == null || constraint.isEmpty()) {
                    filteredList.addAll(originalData)
                } else {
                    val filterPattern = constraint.toString().toLowerCase().trim()

                    for (item in originalData) {
                        if (item.toLowerCase().contains(filterPattern)) {
                            filteredList.add(item)
                        }
                    }
                }

                results.values = filteredList
                results.count = filteredList.size
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredData = results?.values as List<String>
                notifyDataSetChanged()
            }
        }
    }

    fun resetFilter() {
        filteredData = originalData
        notifyDataSetChanged()
    }
}
