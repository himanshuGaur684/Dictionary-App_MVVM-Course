package gaur.himanshu.dictionaryapp.data.remote

import gaur.himanshu.dictionaryapp.data.remote_model.DictionaryResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

// https://api.dictionaryapi.dev/api/v2/entries/en/fdahfhajkfhajkf

interface ApiService {
    @GET("api/v2/entries/en/{word}")
    suspend fun getMeaning(@Path("word") word:String):Response<DictionaryResponse>

}