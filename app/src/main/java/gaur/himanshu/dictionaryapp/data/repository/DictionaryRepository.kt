package gaur.himanshu.dictionaryapp.data.repository

import com.google.gson.Gson
import gaur.himanshu.dictionaryapp.data.remote.ApiService
import gaur.himanshu.dictionaryapp.data.remote_model.DictionaryResponse
import gaur.himanshu.dictionaryapp.data.remote_model.error.ErrorMessage

class DictionaryRepository(
    private val apiService: ApiService
) {

    suspend fun getMeaning(word: String): Result<DictionaryResponse> {
        val response = apiService.getMeaning(word)
        if (response.isSuccessful) {
            return Result.success(response.body()!!)
        } else {
            val errorMessage = response.errorBody()?.string()
            val obj = Gson().fromJson(errorMessage, ErrorMessage::class.java)
            return Result.failure(Exception(obj.message))
        }
    }

}