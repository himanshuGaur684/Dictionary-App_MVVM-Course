package gaur.himanshu.dictionaryapp.model.remote

import gaur.himanshu.dictionaryapp.model.remote_models.DictionaryResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    // https://api.dictionaryapi.dev/api/v2/entries/en/hello

    @GET("api/v2/entries/en/{word}")
    suspend fun getMeaning(
        @Path("word") word: String
    ): Response<DictionaryResponse>


}