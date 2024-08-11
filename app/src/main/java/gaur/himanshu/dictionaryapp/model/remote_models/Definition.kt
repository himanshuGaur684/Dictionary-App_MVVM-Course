package gaur.himanshu.dictionaryapp.model.remote_models

data class Definition(
    val antonyms: List<String>,
    val definition: String,
    val example: String?,
    val synonyms: List<String>
)