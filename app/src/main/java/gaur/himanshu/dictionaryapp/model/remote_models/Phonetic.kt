package gaur.himanshu.dictionaryapp.model.remote_models

data class Phonetic(
    val audio: String,
    val license: License,
    val sourceUrl: String,
    val text: String
)