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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.bookwander.R
import com.example.bookwander.model.Book

/*
* TODO: Apply proper layout and make the images clickable
*  TODO: Find a good workaround in nested scrollable screen
* TODO: Implement my design to all Screen Sizes
*  TODO: Do unit testing
*
* */

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    bookViewModel: BookWanderViewModel,
    onClick: (Book) -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp)
){

    when(val bookUiState = bookViewModel.bookUiState){
        is BookUiState.Loading -> LoadingScreen()
        is BookUiState.Success -> BooksListScreen(
            bookUiState.books,
            bookUiState.bookCategory,
            onClick = onClick,
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
    Box (modifier = modifier, contentAlignment = Alignment.Center){
        Image(
            modifier = Modifier.size(150.dp),
            painter = painterResource(id = R.drawable.loading_img),
            contentDescription = stringResource(id = R.string.loading),
        )
    }

}

@Composable
fun BooksListScreen(
    booksTrend: List<Book>,
    booksCategorize: List<Book>,
    onClick: (Book) -> Unit,
    bookViewModel: BookWanderViewModel,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
){
    LazyColumn (
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = contentPadding
    ) {
        item{
            BookTrendList(onClick = onClick, books = booksTrend, modifier = modifier)
        }
        item{
            BookCategoryList(onClick = onClick, bookViewModel = bookViewModel, books = booksCategorize, modifier = modifier)
        }

    }

}


@Composable
fun BookTrendList(
    books: List<Book>,
    modifier: Modifier = Modifier,
    onClick: (Book) -> Unit,
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
                    book = book,
                    modifier = Modifier
                        .height(dimensionResource(id = R.dimen.book_trending_card_size))
                        .aspectRatio(0.8f),
                    onClick = onClick
                )
            }
        }
    }
}


@Composable
fun BookCategoryList(
    bookViewModel: BookWanderViewModel,
    books: List<Book>, modifier: Modifier,
    onClick: (Book) -> Unit,
) {
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
                            }, 
            modifier = Modifier
                .padding(vertical = dimensionResource(id = R.dimen.padding_small))
        )
        LazyVerticalGrid(
            columns = GridCells.Adaptive(150.dp),
            modifier = Modifier
                .fillMaxWidth()
                .size(700.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            items(books, key = {book -> book.id}){
                book -> BookCardWide(
                book = book,
                modifier = Modifier
                    .aspectRatio(0.8f),
                onClick = onClick
                )
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookCard(
    book: Book,
    modifier: Modifier = Modifier,
    onClick: (Book) -> Unit,
) {
      Card(
          modifier = modifier,
          elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
          shape = RoundedCornerShape(10.dp),
          onClick = { onClick(book) }
      ){
          val newImageUrl = book.volumeInfo.imageLinks?.thumbnail?.replace("http", "https")
          val context = LocalContext.current
          val imagePainter = rememberAsyncImagePainter(
              model = ImageRequest.Builder(context)
                  .data(newImageUrl)
                  .crossfade(true)
                  .size(coil.size.Size.ORIGINAL)
                  .build()
          )
          Box(
              modifier = modifier.size(128.dp),
              contentAlignment = Alignment.Center
          ) {
              when (imagePainter.state) {
                  is AsyncImagePainter.State.Loading -> {
                      Image(
                          painter = painterResource(id = R.drawable.loading_img),
                          contentDescription = stringResource(id = R.string.loading),
                          modifier = Modifier.size(64.dp) // Adjust the size as needed
                      )
                  }
                  is AsyncImagePainter.State.Error -> {
                      Image(
                          painter = painterResource(id = R.drawable.ic_broken_image),
                          contentDescription = stringResource(id = R.string.loading_failed),
                          modifier = Modifier.size(64.dp) // Adjust the size as needed
                      )
                  }
                  else -> {
                      AsyncImage(
                          model = newImageUrl,
                          contentDescription = stringResource(id = R.string.book_photo),
                          modifier = Modifier.fillMaxSize(),
                          contentScale = ContentScale.Crop
                      )
                  }
              }
          }



      }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookCardWide(
    book: Book,
    modifier: Modifier = Modifier,
    onClick: (Book) -> Unit
){
    val numberOfAuthors = book.volumeInfo.authors
    val formattedAuthors = formatAuthors(numberOfAuthors)
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        border = BorderStroke(2.dp, Color.LightGray),
        onClick = { onClick(book) }
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.padding_small)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            BooksListImageItem(
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.book_category_card_size)),
                book = book
            )
            Text(text = "By $formattedAuthors",
                style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .width(200.dp)
                    .padding(top = dimensionResource(id = R.dimen.padding_small)))
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
            contentDescription = stringResource(id = R.string.loading_failed),
            modifier = Modifier.size(150.dp))
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

