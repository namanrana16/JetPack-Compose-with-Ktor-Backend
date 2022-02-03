package com.example.borutomid.presentation.screens.search

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import coil.annotation.ExperimentalCoilApi
import com.example.borutomid.navigation.Screen
import com.example.borutomid.presentation.common.ListContent
import com.example.borutomid.ui.theme.statusBarColor
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@ExperimentalCoilApi
@Composable
fun SearchScreen(navController:NavHostController,
searchViewModel: SearchViewModel= hiltViewModel())
{

    val searchQuery by searchViewModel.searchQuery
    val heroes= searchViewModel.searchHeroes.collectAsLazyPagingItems()



    val systemUiController = rememberSystemUiController()
    val systemBarcolor= MaterialTheme.colors.statusBarColor

    SideEffect {
        systemUiController.setStatusBarColor(color = systemBarcolor)
    }


Scaffold(topBar = { SearchTopBar(text = searchQuery,

    onTextChange = {searchViewModel.updateSearchQuery(query = it)}
,
    onSearchClicked = {
                      searchViewModel.searchHeroes(query = it)
    },
    onCloseClicked = {
        navController.popBackStack()
    }
    )},
    content={ ListContent(heroes = heroes, navController =navController )}

)
    

}