package gaur.himanshu.dictionaryapp.data.remote_model

data class Phonetic(
    val audio: String,
    val license: License,
    val sourceUrl: String,
    val text: String
)