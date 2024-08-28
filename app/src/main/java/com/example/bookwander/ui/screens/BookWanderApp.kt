package com.example.bookwander.ui.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.bookwander.model.BookContentType
import com.example.bookwander.model.Screen
import com.example.bookwander.ui.BookWanderViewModel


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookshelfApp(
    windowSizeClass: WindowWidthSizeClass,
    navController: NavHostController = rememberNavController()
){
    // Initialize our back stack by collecting the currBackStackEntryAsState
    val backStack by navController.currentBackStackEntryAsState()
    // Store the route in our backState to observe changes in navigation
    val currScreen = Screen.valueOf(
        backStack?.destination?.route ?: Screen.Start.name
    )
    val bookContentType: BookContentType = when(windowSizeClass){
        WindowWidthSizeClass.Compact -> {
            BookContentType.List
        }
        WindowWidthSizeClass.Medium -> {
            BookContentType.List
        }
        WindowWidthSizeClass.Expanded -> {
            BookContentType.ListAndDetails
        }else -> BookContentType.List
    }

   val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
   val currentBackStackEntry by navController.currentBackStackEntryAsState()
   Scaffold (
       modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
       topBar = { BookTopAppBar(
           currentScreen = currScreen,
           scrollBehavior = scrollBehavior,
           canNavigateBack = currentBackStackEntry?.destination?.route != Screen.Start.name,
           onBack = { navController.navigateUp() }
           ) }
   ) {innerPadding ->
       Surface (modifier = Modifier.fillMaxSize()){
           /**
            * Use the Factory that we created inside the viewModel
            * The factory will ensure that our repository will be consider in the
            * ViewModel constructor
            * **/
           val bookWanderViewModel = hiltViewModel<BookWanderViewModel>()

           val bookCategoryUiState = bookWanderViewModel.bookCategoryUiState.collectAsStateWithLifecycle().value
           val bookTrendingFlow = bookWanderViewModel.bookTrendingFlow.collectAsLazyPagingItems()

           NavHost(
               navController = navController,
               startDestination = Screen.Start.name,
               modifier = Modifier
           ){
               composable(route = Screen.Start.name){
                   HomeScreen(
                       bookContentType = bookContentType,
                       contentPadding = innerPadding,
                       bookUiState = bookWanderViewModel.bookUiState,
                       bookCategoryUiState = bookCategoryUiState,
                       bookTrendingFlow = bookTrendingFlow,
                       onClick = {
                           if(bookContentType != BookContentType.ListAndDetails){
                               bookWanderViewModel.updateSelectedBook(it)
                               navController.navigate(Screen.Book.name)
                           }else {
                               bookWanderViewModel.updateSelectedBook(it)
                           }
                       },
                       onBookCategoryClick = {
                           bookWanderViewModel.updateBookCategory(it)
                           bookWanderViewModel.getBookCategory(it)
                       },
                       modifier = Modifier
                   )
               }
               composable(route = Screen.Book.name){
                   if(bookContentType != BookContentType.ListAndDetails){
                       BookDetailsScreen(
                           bookCategoryUiState = bookCategoryUiState,
                           contentPadding = innerPadding,
                           modifier = Modifier)
                   }else{
                       navController.navigate(Screen.Start.name)
                   }
               }


           }
       }
   }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookTopAppBar(
    currentScreen: Screen,
    scrollBehavior: TopAppBarScrollBehavior,
    canNavigateBack: Boolean,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
){
    CenterAlignedTopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
            Text(
                text = stringResource(id = currentScreen.route),
                style = MaterialTheme.typography.headlineSmall
            )
        },

//        colors = TopAppBarDefaults.topAppBarColors(
//            containerColor = Color.Transparent,
//            titleContentColor = MaterialTheme.colorScheme.primary,
//            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
//            actionIconContentColor = MaterialTheme.colorScheme.onSecondary
//        ),
        navigationIcon = {
            if(canNavigateBack){
                IconButton(onClick = onBack) {
                    Icon(imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.primary)
                }
            }
        },
        modifier = modifier.shadow(elevation = 5.dp)

    )
}