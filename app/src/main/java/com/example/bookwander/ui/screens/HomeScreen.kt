package com.example.bookwander.ui.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.bookwander.R
import com.example.bookwander.model.Book

/*
* TODO: Apply proper layout and make the images clickable
*  TODO: Find a good workaround in nested scrollable screen
*
*
* */

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    bookViewModel: BookWanderViewModel,
    contentPadding: PaddingValues = PaddingValues(0.dp)
){

    when(val bookUiState = bookViewModel.bookUiState){
        is BookUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        is BookUiState.Success -> BooksListScreen(
            bookUiState.books,
            bookUiState.bookCategory,
            bookViewModel = bookViewModel,
            contentPadding = contentPadding,
            modifier = modifier.fillMaxSize()
        )
        is BookUiState.Error -> ErrorScreen(
            errorMessage = bookUiState.message,
            modifier = modifier.fillMaxSize())

    }

}
@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(id = R.drawable.loading_img),
        contentDescription = stringResource(id = R.string.loading)
    )
}

@Composable
fun BooksListScreen(
    booksTrend: List<Book>,
    booksCategorize: List<Book>,
    bookViewModel: BookWanderViewModel,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
){
    MaterialTheme {
        LazyColumn (
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = contentPadding
        ) {
            item{
                BookTrendList(books = booksTrend, modifier = modifier)
            }
            item{
                BookCategoryList(bookViewModel = bookViewModel, books = booksCategorize, modifier = modifier)
            }

        }
    }
}


@Composable
fun BookTrendList(
    books: List<Book>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(top=16.dp)
    ) {
        Text(
            text = "Trending",
            style = MaterialTheme.typography.headlineSmall,
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
        LazyRow(modifier =
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(items = books, key = { book -> book.id }) { book ->
                BookCard(
                    books = book,
                    modifier = Modifier
                        .height(150.dp)
                        .aspectRatio(0.8f),
                    onClick = { }
                )
            }
        }
    }
}


@Composable
fun BookCategoryList(bookViewModel: BookWanderViewModel, books: List<Book>, modifier: Modifier) {
    Column (
        modifier = modifier.padding(top = 24.dp)
    ) {
        Text(
            text = "Other Categories",
            style = MaterialTheme.typography.headlineSmall,
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
        val bookLabels = listOf("E-book", "Fiction", "Money", "Skills", "War")
        BooksLabelCategories(buttonLabels = bookLabels,
            onButtonClick = {
                bookViewModel.updateBookCategory(it)
                bookViewModel.getBookCategory(it)
                            }, modifier = Modifier)
        LazyVerticalGrid(
            columns = GridCells.Adaptive(150.dp),
            modifier = modifier
                .size(1000.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ){
            items(books, key = {book -> book.id}){
                book -> BookCardWide(book = book, modifier = Modifier
                .padding(vertical = 4.dp)
                .aspectRatio(1f)
                )
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookCard(
    books: Book,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val oldValue = "http"
    val newValue = "https"
    val newImageUrl = books.volumeInfo.imageLinks?.thumbnail?.replace(oldValue, newValue)
//    Log.d("HomeScreen", newImageUrl!!)
      Card (
          modifier = modifier,
          elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
          shape = RoundedCornerShape(10.dp),
          onClick = onClick
      ){
              AsyncImage(model = ImageRequest.Builder(context = LocalContext.current)
                  .data(newImageUrl).crossfade(true).build(),
                  contentDescription = stringResource(id = R.string.book_photo),
                  error = painterResource(id = R.drawable.ic_broken_image),
                  placeholder = painterResource(id = R.drawable.loading_img),
                  contentScale = ContentScale.Crop,
                  modifier = Modifier.fillMaxSize()
              )


      }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookCardWide(
    book: Book,
    modifier: Modifier = Modifier
){
    val numberOfAuthors = book.volumeInfo.authors
    val formattedAuthors = formatAuthors(numberOfAuthors)
    Card(
        modifier = modifier
            .fillMaxSize(),
        elevation = CardDefaults.cardElevation(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        border = BorderStroke(2.dp, Color.LightGray),
        onClick = { }
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            BooksListImageItem(
                modifier = Modifier.size(140.dp),
                book = book
            )
            Text(text = "By $formattedAuthors",
                style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .width(200.dp))
        }

    }
}

@Composable
fun BooksListImageItem(
    book: Book,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ){
        val oldUrlValue = "http"
        val newUrlValue = "https"
        val newImageUrl = book.volumeInfo.imageLinks?.thumbnail?.replace(oldUrlValue, newUrlValue)
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(newImageUrl).build(),
            contentDescription = stringResource(id = R.string.book_photo),
            error = painterResource(id = R.drawable.ic_broken_image),
            placeholder = painterResource(id = R.drawable.loading_img),
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
        )
    }
}


@Composable
fun BooksLabelCategories(
    buttonLabels: List<String>,
    onButtonClick: (String) -> Unit,
    modifier: Modifier = Modifier){

    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(0.dp)
    ){
        items(buttonLabels) { label ->
            Button(
                onClick = { onButtonClick(label) },
                modifier = Modifier,
                shape = RoundedCornerShape(4.dp)
            ){
                Text(label)
            }

        }
    }

}
@Composable
fun ErrorScreen(
    @StringRes errorMessage: Int,
    modifier: Modifier = Modifier) {
    Column (
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(painter = painterResource(id = R.drawable.ic_connection_error),
            contentDescription = stringResource(id = R.string.loading_failed))
        Text(text = stringResource(id = errorMessage), textAlign = TextAlign.Center)
    }
}

fun formatAuthors(authors: List<String>): String{
    return when(authors.size){
        0 -> ""
        1 -> authors[0]
        2 -> authors.joinToString(separator = (" & "))
        else -> authors.dropLast(1).joinToString(separator = (", ")) + " & " + authors.last()
    }


}

