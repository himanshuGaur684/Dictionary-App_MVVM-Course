package gaur.himanshu.dictionaryapp.data.remote_model

data class Definition(
    val antonyms: List<String>,
    val definition: String,
    val example: String?,
    val synonyms: List<String>
)