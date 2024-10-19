package gaur.himanshu.dictionaryapp.view.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import gaur.himanshu.dictionaryapp.data.remote_model.Definition
import gaur.himanshu.dictionaryapp.data.remote_model.DictionaryResponseItem
import gaur.himanshu.dictionaryapp.data.remote_model.License
import gaur.himanshu.dictionaryapp.data.remote_model.Meaning
import gaur.himanshu.dictionaryapp.data.remote_model.Phonetic
import gaur.himanshu.dictionaryapp.view_model.DictionaryViewModel

@Composable
fun DictionaryScreen(modifier: Modifier = Modifier, viewModel: DictionaryViewModel) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var query by rememberSaveable {
        mutableStateOf("")
    }

    Scaffold(topBar = {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = query, onValueChange = {
                query = it
                viewModel.updateQuery(query)
             }, colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ), placeholder = { Text(text = "Search....")}
        )
    }) {

        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        if (uiState.error.isNotBlank()) {
            Box(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Text(text = uiState.error)
            }
        }
        uiState.data?.let { response ->
            LazyColumn(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
            ) {

                items(response.toList()) { item: DictionaryResponseItem ->
                    ListItem(dictionaryResponseItem = item)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListItem(
    modifier: Modifier = Modifier,
    dictionaryResponseItem: DictionaryResponseItem = dictionaryItem
) {

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Text(
            text = dictionaryResponseItem.word,
            style = MaterialTheme.typography.displayMedium.copy(
                color = Color.Red,
                fontWeight = FontWeight.Bold
            )
        )

        dictionaryResponseItem.meanings.forEach { meaning: Meaning ->
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = meaning.partOfSpeech,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.Green, shape = RoundedCornerShape(12.dp))
                    .padding(8.dp),
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(12.dp))

            meaning.definitions.forEach { definition: Definition ->

                Card(modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)) {
                    Column(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                    ) {

                        Text(
                            text = definition.definition,
                            style = MaterialTheme.typography.bodyMedium
                        )

                        if (definition.synonyms.isNotEmpty()) {
                            Text(
                                text = "Synonyms: ${definition.synonyms.joinToString(",")}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        if (definition.antonyms.isNotEmpty()) {
                            Text(
                                text = "Synonyms: ${definition.antonyms.joinToString(",")}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        if (definition.example.isNullOrBlank().not()) {
                            Text(
                                text = "Ex." + definition.example,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

            }

        }


    }


}


val dictionaryItem = DictionaryResponseItem(
    license = License(name = "license", url = ""),
    meanings = listOf(
        Meaning(
            antonyms = listOf("first"),
            definitions = listOf(
                Definition(
                    antonyms = listOf("first antonynms"),
                    definition = "this is definition",
                    example = "this is example",
                    synonyms = listOf("synonyms")
                )
            ), partOfSpeech = "this is part of speech",
            synonyms = listOf("", "")
        )
    ),
    phonetics = listOf(
        Phonetic(
            audio = "",
            license = License(name = "name", url = "this is license url"),
            sourceUrl = "",
            text = ""
        )
    ),
    sourceUrls = listOf(""),
    word = "hello"
)
