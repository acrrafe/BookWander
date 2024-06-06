package com.example.bookwander.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.bookwander.R
import com.example.bookwander.model.Book


@Composable
fun BookDetailsScreen(
    bookWanderViewModel: BookWanderViewModel,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
){
    val uiState = bookWanderViewModel.bookCategoryUiState.collectAsState()

   LazyColumn (
       modifier = modifier.fillMaxSize(),
       contentPadding = contentPadding
   ) {
       item{
           uiState.value.currentSelectedBook?.let { BookContent(book = it) }
       }
   }
}


@Composable
fun BookContent(
    book: Book,
    modifier: Modifier = Modifier
){
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp), // Add padding if necessary
        contentAlignment = Alignment.Center
    ) {
        BookImageWithAuthor(book = book)
    }

    Text(text = book.volumeInfo.description)

}

@Composable
fun BookImageWithAuthor(
    book: Book,
    modifier: Modifier = Modifier
){
    val numberOfAuthors = book.volumeInfo.authors
    val formattedAuthors = formatAuthors(numberOfAuthors)

    Card (
        modifier = modifier
            .wrapContentWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(Color.Transparent)
    ) {
        Column (
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
        ){
            Box(modifier = Modifier.size(140.dp)){
                val oldValue = "http"
                val newValue = "https"
                val newImageUrl = book.volumeInfo.imageLinks?.thumbnail?.replace(oldValue,newValue)
                AsyncImage(
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data(newImageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = stringResource(id = R.string.book_photo),
                    modifier = Modifier.fillMaxSize(),
                )
            }

            Text(text = "By $formattedAuthors",
                style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .width(200.dp))
            Text(text = book.volumeInfo.publishedDate,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier)
        }
        }

}

