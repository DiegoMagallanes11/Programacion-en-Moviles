package com.tecsup.avisoscampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tecsup.avisoscampus.data.AvisosRepository
import com.tecsup.avisoscampus.data.AvisosRepositoryMemoria

class AvisosViewModelFactory(
    private val repo: AvisosRepository = AvisosRepositoryMemoria()
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass == AvisosViewModel::class.java)
        @Suppress("UNCHECKED_CAST")
        return AvisosViewModel(repo) as T
    }
}
