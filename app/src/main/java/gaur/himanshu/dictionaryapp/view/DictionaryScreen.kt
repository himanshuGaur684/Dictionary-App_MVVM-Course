package gaur.himanshu.dictionaryapp.view

import android.media.AudioManager
import android.media.MediaPlayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import gaur.himanshu.dictionaryapp.model.remote_models.Definition
import gaur.himanshu.dictionaryapp.model.remote_models.DictionaryResponseItem
import gaur.himanshu.dictionaryapp.model.remote_models.License
import gaur.himanshu.dictionaryapp.model.remote_models.Meaning
import gaur.himanshu.dictionaryapp.model.remote_models.Phonetic
import gaur.himanshu.dictionaryapp.view_model.DictionaryScreen
import gaur.himanshu.dictionaryapp.view_model.DictionaryVIewModel

@Composable
fun DictionaryScreen(modifier: Modifier = Modifier, vIewModel: DictionaryVIewModel) {
    val uiState by vIewModel.uiState.collectAsState()
    Content(modifier = modifier, uiState) { query -> vIewModel.updateQuery(query) }
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    uiState: DictionaryScreen.UiState,
    onQuery: (String) -> Unit
) {
    val query = rememberSaveable { mutableStateOf("") }
    Scaffold(modifier = modifier, topBar = {
        TextField(
            value = query.value, onValueChange = {
                query.value = it
                onQuery.invoke(query.value)
            }, colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ), placeholder = { Text(text = "Search here...") }, modifier = Modifier.fillMaxWidth()
        )
    }) {
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(), contentAlignment = Center
            ) {
                CircularProgressIndicator()
            }
        }

        if (uiState.error.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(), contentAlignment = Center
            ) {
                Text(text = uiState.error)
            }
        }

        uiState.data?.let { dictionaryResponseItems ->
            LazyColumn(
                Modifier
                    .padding(it)
                    .fillMaxSize()
            ) {
                items(dictionaryResponseItems) { item ->
                    ListItem(item)
                }
            }
        }
    }


}

@Composable
fun ListItem(item: DictionaryResponseItem) {
    Column(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp, top = 8.dp)
            .border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = item.word,
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Red
            )
            IconButton(onClick = {
                if (item.phonetics.isNotEmpty()) {
                    playAudio(item.phonetics.get(0).audio)
                }
            }) {
                Image(imageVector = Icons.Default.PlayArrow, contentDescription = null)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Meanings",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.height(12.dp))
        item.meanings.forEach { meaning ->
            Text(
                text = meaning.partOfSpeech,
                modifier = Modifier
                    .padding(top = 12.dp)
                    .clipToBounds()
                    .fillMaxWidth()
                    .background(color = Color.Green, shape = RoundedCornerShape(12.dp))
                    .padding(
                        vertical = 8.dp,
                        horizontal = 16.dp
                    ),

                style = MaterialTheme.typography.headlineSmall,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(12.dp))

            meaning.definitions.forEach {
                Card(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = it.definition,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Black
                    )
                    if (it.synonyms.isNotEmpty())
                        Text(
                            text = "Synonyms: ${it.synonyms.joinToString(",")}",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .fillMaxWidth()
                        )
                    if (it.antonyms.isNotEmpty())
                        Text(
                            text = "Antonyms: ${it.antonyms.joinToString(",")}",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .fillMaxWidth()
                        )

                    if (it.example.isNullOrBlank().not()) {
                        Text(
                            text = "Ex: " + it.example.toString(),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .fillMaxWidth()
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }


        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun ContentPreview(modifier: Modifier = Modifier) {
    Content(uiState = DictionaryScreen.UiState(data = listOf(dictionaryItem))) {

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

private fun playAudio(url: String) {
    try {
        val mediaPlayer = MediaPlayer().apply {
            setAudioStreamType(AudioManager.STREAM_MUSIC)
            setDataSource(url)
            prepare()
            setVolume(1f, 1f)
            isLooping = false
        }
        mediaPlayer.start()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}



