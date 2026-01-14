package com.smartpad.cabessa.modules.drugs.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartpad.cabessa.modules.drugs.data.entities.DrugEntity
import com.smartpad.cabessa.modules.drugs.data.repositories.DrugRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

/**
 * ViewModel for the Drugs module.
 *
 * Technical basis:
 * - Exposes StateFlow for UI state (list of drugs).
 * - Provides functions for CRUD that run in viewModelScope.
 * - Uses a small UI event SharedFlow to communicate one-shot events (e.g., show toast, navigation).
 */
class DrugViewModel(
    private val repository: DrugRepository
) : ViewModel() {

    // UI state: list of drugs
    private val _drugs = MutableStateFlow<List<DrugEntity>>(emptyList())
    val drugs: StateFlow<List<DrugEntity>> = _drugs.asStateFlow()

    // UI events for one-shot messages/actions
    private val _events = MutableSharedFlow<DrugUiEvent>()
    val events = _events.asSharedFlow()

    init {
        observeDrugs()
    }

    private fun observeDrugs() {
        viewModelScope.launch {
            repository.observeActiveDrugs()
                .catch { e ->
                    _events.emit(DrugUiEvent.Error("Error loading drugs: ${e.message}"))
                }
                .collect { list ->
                    _drugs.value = list
                }
        }
    }

    fun search(query: String) {
        viewModelScope.launch {
            repository.searchActiveDrugs(query)
                .collect { list -> _drugs.value = list }
        }
    }

    fun createDrug(drug: DrugEntity) {
        viewModelScope.launch {
            try {
                val id = repository.createDrug(drug)
                _events.emit(DrugUiEvent.Created(id))
            } catch (e: Exception) {
                _events.emit(DrugUiEvent.Error("Create failed: ${e.message}"))
            }
        }
    }

    fun updateDrug(drug: DrugEntity) {
        viewModelScope.launch {
            try {
                repository.updateDrug(drug)
                _events.emit(DrugUiEvent.Updated(drug.id))
            } catch (e: Exception) {
                _events.emit(DrugUiEvent.Error("Update failed: ${e.message}"))
            }
        }
    }

    fun softDeleteDrug(id: Long) {
        viewModelScope.launch {
            try {
                repository.softDeleteDrug(id)
                _events.emit(DrugUiEvent.Deleted(id))
            } catch (e: Exception) {
                _events.emit(DrugUiEvent.Error("Delete failed: ${e.message}"))
            }
        }
    }
}

/**
 * Events emitted by the DrugViewModel for one-shot UI actions.
 */
sealed class DrugUiEvent {
    data class Created(val id: Long) : DrugUiEvent()
    data class Updated(val id: Long) : DrugUiEvent()
    data class Deleted(val id: Long) : DrugUiEvent()
    data class Error(val message: String) : DrugUiEvent()
}