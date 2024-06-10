package com.example.bookwander.ui.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.bookwander.R
import com.example.bookwander.model.Book
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BookDetailsScreen(
    bookWanderViewModel: BookWanderViewModel,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
){
    val uiState = bookWanderViewModel.bookCategoryUiState.collectAsState()

   LazyColumn (
       modifier = modifier
           .fillMaxSize()
           .padding(dimensionResource(id = R.dimen.padding_medium)),
       contentPadding = contentPadding
   ) {
       item{
           uiState.value.currentSelectedBook?.let { BookContent(book = it) }
       }
   }
}



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BookContent(
    book: Book,
    modifier: Modifier = Modifier
){
    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        BookImageWithAuthor(book = book)
    }

    BookExtraDetails(
        modifier = Modifier
            .fillMaxWidth()
            .size(100.dp),
        book = book
    )

    Text(text = "Description",
        style = MaterialTheme.typography.bodyLarge,
        textAlign = TextAlign.Justify,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        modifier = Modifier.padding(top = dimensionResource(id = R.dimen.padding_medium))
    )

    Text(text = book.volumeInfo.description,
        textAlign = TextAlign.Justify,
        modifier = Modifier.padding(top = dimensionResource(id = R.dimen.padding_small)))

}

@RequiresApi(Build.VERSION_CODES.O)
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
            Box(modifier = Modifier.size(dimensionResource(id = R.dimen.book_details_card_size))){
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
                    .width(200.dp)
                    .padding(top = dimensionResource(id = R.dimen.padding_small)))
            val formattedDate = formatDate(book.volumeInfo.publishedDate)
            Text(text = formattedDate,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier)
        }
        }

}

@Composable
fun BookExtraDetails(
    book: Book,
    modifier:Modifier = Modifier
){
    Card (
        modifier = modifier,
        elevation = CardDefaults.cardElevation(),
    ){
        Row (
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ){
            Row (
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = dimensionResource(id = R.dimen.padding_small))
            ){
                Icon(
                    painter = painterResource(id = R.drawable.published_icon),
                    contentDescription = stringResource(id = R.string.publisher_icon),
                    modifier = Modifier.size(32.dp).padding(end = dimensionResource(id = R.dimen.padding_small))
                )
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        ) {
                            append("${book.volumeInfo.publisher}")
                        }
                    },
                )
            }
            Divider(color = Color.Gray, modifier = Modifier
                .height(50.dp)
                .width(1.dp)
                )
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp)) {
                        append("${book.volumeInfo.pageCount}")
                    }
                    append(" pages")
                },
                modifier = Modifier
                    .weight(0.75f)
                    .padding(start = dimensionResource(id = R.dimen.padding_medium)))
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
fun formatDate(dateString: String): String{

    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)
    val outputFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH)

    return try{
        val date = LocalDate.parse(dateString, inputFormatter)
        date.format(outputFormatter)
    }catch (e: DateTimeParseException){
        Log.e("BOOKDETAILS", "Invalid Date Format: $dateString", e)
        if(!dateString.isEmpty()) dateString else "Invalid Date"
    }

}
