package gaur.himanshu.dictionaryapp.data.remote_model

data class DictionaryResponseItem(
    val license: License,
    val meanings: List<Meaning>,
    val phonetics: List<Phonetic>,
    val sourceUrls: List<String>,
    val word: String
)