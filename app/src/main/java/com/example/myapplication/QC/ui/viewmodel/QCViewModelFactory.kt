package com.example.myapplication.QC.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.QC.data.repository.QcRepository

class QCViewModelFactory constructor(
    private val qcRepository: QcRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(QcViewModel::class.java)) {
            QcViewModel(this.qcRepository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}
