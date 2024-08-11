package gaur.himanshu.dictionaryapp.model.repository

import com.google.gson.Gson
import gaur.himanshu.dictionaryapp.model.remote.ApiService
import gaur.himanshu.dictionaryapp.model.remote_models.DictionaryResponse
import gaur.himanshu.dictionaryapp.model.remote_models.error.ErrorMessage
import javax.inject.Inject

class DictionaryRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getMeaning(word: String): Result<DictionaryResponse> {
        return try {
            val response = apiService.getMeaning(word)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorMessage = response.errorBody()?.string()
                val error = Gson().fromJson<ErrorMessage>(errorMessage, ErrorMessage::class.java)
                Result.failure(Exception(error.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


}