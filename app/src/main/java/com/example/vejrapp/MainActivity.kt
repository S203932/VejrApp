package com.example.vejrapp

import android.annotation.SuppressLint
import android.content.pm.ModuleInfo
import android.media.Image
import android.os.Bundle
import android.text.style.BackgroundColorSpan
import android.widget.ImageButton
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState.Companion.Saver
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonDefaults.outlinedButtonBorder
import androidx.compose.material3.ButtonDefaults.shape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.SemanticsProperties.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
//import com.example.vejrapp.SearchInputStateImpl.Companion.Saver
import com.example.vejrapp.ui.theme.VejrAppTheme
import java.nio.file.Files.size

class MainActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VejrAppTheme {
                val viewModel = viewModel<SearchViewModel>()
                val searchText by viewModel.searchText.collectAsState()
                val cities by viewModel.cities.collectAsState()
                val isSearching by viewModel.isSearching.collectAsState()

                //Search mode belongs to the top bar
                var searchMode = false
                var displayText by remember {
                    mutableStateOf("Copenhagen")
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    val scrollBeavior = TopAppBarDefaults.pinnedScrollBehavior()

                    Scaffold(
                        modifier = Modifier
                            .fillMaxSize()
                            .nestedScroll(scrollBeavior.nestedScrollConnection),
                        topBar = {
                            TopAppBar(
                                title = {
                                    TextField(
                                        value = searchText,
                                        onValueChange = viewModel::onSearchTextChange,
                                        modifier = Modifier.fillMaxWidth(),
                                        placeholder = { Text(text = displayText) },
                                        leadingIcon = {
                                            Icon(
                                                Icons.Filled.Search, contentDescription = null,
                                                modifier = Modifier
                                                    .height(20.dp)
                                                    .width(20.dp)
                                            )
                                        },
                                        shape = RoundedCornerShape(28.dp)
                                    )
                                },
                                navigationIcon = {
                                    IconButton(

                                        onClick = { /*TODO*/ },
                                        modifier = Modifier.alpha(if (searchText.isNotBlank()) 1f else 0f)

                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ArrowBack,
                                            contentDescription = "Go back"
                                        )

                                    }
                                },

                                actions = {
                                    IconButton(onClick = { /*TODO*/ }) {
                                        Icon(
                                            imageVector = Icons.Default.Settings,
                                            contentDescription = "Open Settings"
                                        )

                                    }/*
                                    IconButton(onClick = { /*TODO*/ }) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Edit notes"
                                        )

                                    }*/
                                },
                                scrollBehavior = scrollBeavior
                            )
                        }
                    ) { values ->

                        //Insert lazycolumn here
                        if (isSearching) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                            )
                            {
                                CircularProgressIndicator(
                                    modifier = Modifier.align(Alignment.Center)
                                )

                            }
                        } else if (!searchText.isBlank()) {

                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(values),
                            ) {
                                items(cities) { city ->
                                    Text(
                                        text = "${city.name}",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 16.dp)
                                            .selectable(
                                                selected = city.name == displayText,
                                                onClick = {
                                                    displayText = city.name
                                                }
                                            )
                                    )

                                    //Add a button at the end with a favourite icon

                                }
                            }
                        }
                        /*
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(values)
                        ) {
                            items(100) {
                                Text(
                                    text = "Item$it",
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }

                         */


                    }


                    //SearchBar(viewModel, searchText, cities, isSearching)


                }


            }
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun Search(
    onSearch: (String) -> Unit,
    string: String,
    onValueChange: (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    TextField(

        value = string,
        onValueChange = onValueChange,
        modifier = Modifier
            .width(350.dp),
        //label = { Text("Copenhagen") },
        leadingIcon = {
            Icon(
                Icons.Filled.Search, contentDescription = null,
                modifier = Modifier
                    .height(20.dp)
                    .width(20.dp)
            )
        },

        shape = RoundedCornerShape(28.dp),
        keyboardOptions = KeyboardOptions(imeAction = androidx.compose.ui.text.input.ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
            onSearch(string)
            // Hide the keyboard after submitting the search
            keyboardController?.hide()
            //or hide keyboard
            focusManager.clearFocus()

        }
        )
    )


}


@Composable
fun Settings() {
    IconButton(

        onClick = {
            /*TODO*/
        }
    ) {
        Icon(

            modifier = Modifier
                .padding(2.dp, 2.dp)
                .size(30.dp)
                .background(color = Color.Transparent, shape = CircleShape),


            imageVector = Icons.Default.Settings,
            contentDescription = null
        )

    }
    /*
    Button(
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 8.dp,
            disabledElevation = 0.dp
        ),
        onClick = {
            /*TODO*/
        },
        modifier = Modifier
            .background(color = Color.Transparent, shape = CircleShape)
            .padding(12.dp, 12.dp)
            .size(30.dp)

            .border(width = 1.dp, color = Color.Black),

        colors = ButtonDefaults.buttonColors(
            containerColor = Color.LightGray,
            contentColor = Color.LightGray


        )

    ) {
    }

     */


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    viewModel: SearchViewModel,
    searchText: String,
    cities: List<City>,
    isSearching: Boolean
) {

    TextField(
        value = searchText,
        onValueChange = viewModel::onSearchTextChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = "Copenhagen") },
        leadingIcon = {
            Icon(
                Icons.Filled.Search, contentDescription = null,
                modifier = Modifier
                    .height(20.dp)
                    .width(20.dp)
            )
        },
        shape = RoundedCornerShape(28.dp)
    )

    Spacer(modifier = Modifier.height(16.dp))
    if (isSearching) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        )
        {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )

        }
    } else if (!searchText.isBlank()) {

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            items(cities) { city ->
                Text(
                    text = "${city.name}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                )

            }
        }
    }


}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    VejrAppTheme {
        //SearchBar()
    }
}


// One of my attempts, tried some peculiar state hoisting


/*

@Stable
//Make interface for state hoisting of the search
//Think the search input should be generated at a higher level for api to reach and thus
// state hoisting is needed
interface SearchInputState {
    var search: String
}

//Defining a default implementation search input, for the searchbar
class SearchInputStateImpl(
    search: String = "",
) : SearchInputState {
    override var search: String by mutableStateOf(search)

    companion object {
        val Saver = Saver<SearchInputStateImpl, List<Any>>(
            save = { listOf(it.toString()) },
            restore = {
                SearchInputStateImpl(
                    search = it[0] as String,
                )
            }
        )
    }
}

@Composable
fun rememberSearchInputState(): SearchInputState = rememberSaveable(
    saver = SearchInputStateImpl.Saver
) {
    SearchInputStateImpl("")

}

 */





