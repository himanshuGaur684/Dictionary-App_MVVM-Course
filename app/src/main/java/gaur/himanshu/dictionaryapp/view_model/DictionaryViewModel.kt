package gaur.himanshu.dictionaryapp.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gaur.himanshu.dictionaryapp.data.remote_model.DictionaryResponse
import gaur.himanshu.dictionaryapp.data.repository.DictionaryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DictionaryViewModel @Inject constructor(private val repository: DictionaryRepository) :
    ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val _query = MutableStateFlow("")

    init {
        viewModelScope.launch {
            _query.debounce(1000)
                .filter { it.isNotBlank() }
                .collectLatest {
                    getMeaning(it)
                }
        }
    }

    fun updateQuery(word: String) {
        _query.update { word }
    }

    fun getMeaning(word: String) = viewModelScope.launch {
        _uiState.update { UiState(isLoading = true) }
        val response = repository.getMeaning(word)
        if (response.isSuccess) {
            _uiState.update { UiState(data = response.getOrThrow()) }
        } else {
            _uiState.update { UiState(error = response.exceptionOrNull()?.message.toString()) }
        }
    }
}

data class UiState(
    val isLoading: Boolean = false,
    val error: String = "",
    val data: DictionaryResponse? = null
)

