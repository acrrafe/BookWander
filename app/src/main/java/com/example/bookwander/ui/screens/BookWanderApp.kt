package com.example.bookwander.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bookwander.R
import com.example.bookwander.model.Screen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookshelfApp(
    navController: NavHostController = rememberNavController()
){
   MaterialTheme {
       val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
       Scaffold (
           modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
           topBar = { BookTopAppBar(scrollBehavior = scrollBehavior) }
       ) {innerPadding ->
           Surface (modifier = Modifier.fillMaxSize()){
               /**
                * Use the Factory that we created inside the viewModel
                * The factory will ensure that our repository will be consider in the
                * ViewModel constructor
                * **/
               val bookWanderViewModel: BookWanderViewModel =
                   viewModel(factory = BookWanderViewModel.Factory)

               NavHost(
                   navController = navController,
                   startDestination = Screen.Start.name,
                   modifier = Modifier
               ){
                   composable(route = Screen.Start.name){
                       HomeScreen(
                           bookViewModel = bookWanderViewModel,
                           contentPadding = innerPadding,
                           onClick = {
                                     bookWanderViewModel.updateSelectedBook(it)
                                     navController.navigate(Screen.Book.name)
                           },
                           modifier = Modifier
                       )
                   }
                   composable(route = Screen.Book.name){
                       BookDetailsScreen(
                           bookWanderViewModel = bookWanderViewModel,
                           contentPadding = innerPadding,
                           modifier = Modifier)
                   }

               }


           }

       }
   }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier
){
    CenterAlignedTopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.headlineSmall
            )
        },

//        colors = TopAppBarDefaults.topAppBarColors(
//            containerColor = Color.Transparent,
//            titleContentColor = MaterialTheme.colorScheme.primary,
//            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
//            actionIconContentColor = MaterialTheme.colorScheme.onSecondary
//        ),

        modifier = modifier.shadow(elevation = 5.dp)

    )
}