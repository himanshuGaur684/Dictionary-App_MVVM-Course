package gaur.himanshu.dictionaryapp.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gaur.himanshu.dictionaryapp.model.remote_models.DictionaryResponseItem
import gaur.himanshu.dictionaryapp.model.repository.DictionaryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DictionaryVIewModel @Inject constructor(private val repository: DictionaryRepository) :
    ViewModel() {

    private val _query = MutableStateFlow("")

    private val _uiState = MutableStateFlow(DictionaryScreen.UiState())
    val uiState: StateFlow<DictionaryScreen.UiState> get() = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _query.debounce(500)
                .filter { it.isNotEmpty() }
                .collectLatest {
                    getMeaning(it)
                }
        }
    }

    fun updateQuery(word: String) {
        _query.update { word }
    }

    private fun getMeaning(word: String) = viewModelScope.launch {
        _uiState.update { DictionaryScreen.UiState(isLoading = true) }
        val response = repository.getMeaning(word)
        if (response.isSuccess) {
            _uiState.update { DictionaryScreen.UiState(data = response.getOrThrow()) }
        } else {
            _uiState.update { DictionaryScreen.UiState(error = response.exceptionOrNull()?.message.toString()) }
        }
    }
}

object DictionaryScreen {

    data class UiState(
        val isLoading: Boolean = false,
        val error: String = "",
        val data: List<DictionaryResponseItem>? = null
    )

}

