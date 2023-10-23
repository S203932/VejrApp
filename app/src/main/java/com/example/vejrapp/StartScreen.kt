package com.example.vejrapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vejrapp.data.SearchViewModel

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun StartScreen(
    onNextButtonClicked: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel
) {
    val searchText by viewModel.searchText.collectAsState()
    val cities by viewModel.cities.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()

    //Search mode belongs to the top bar
    var searchMode by remember {
        mutableStateOf(true)
    }
    var displayText by remember {
        mutableStateOf("Copenhagen")
    }

    //This is for hiding the keyboard
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current


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
                            modifier = Modifier
                                .fillMaxWidth()
                                .motionEventSpy {
                                    //searchMode = !searchMode
                                }
                                .onFocusEvent {
                                    searchMode = !searchMode
                                },
                            placeholder = { Text(text = displayText) },
                            leadingIcon = {
                                Icon(
                                    Icons.Filled.Search, contentDescription = null,
                                    modifier = Modifier
                                        .height(20.dp)
                                        .width(20.dp)
                                )
                            },
                            shape = RoundedCornerShape(28.dp),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Done,
                                keyboardType = KeyboardType.Text
                            ),
                            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                        )

                    },
                    navigationIcon = {
                        IconButton(

                            onClick = {
                                keyboardController?.hide()
                                //or hide keyboard
                                focusManager.clearFocus()
                                searchMode = !searchMode
                            },
                            modifier = Modifier.alpha(if (searchMode) 1f else 0f)

                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Go back"
                            )

                        }
                    },

                    actions = {
                        IconButton(onClick = onNextButtonClicked) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Open Settings"
                            )

                        }
                    },
                    scrollBehavior = scrollBeavior
                )
            }
        ) { values ->

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
            } else if (searchMode) {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(values),
                ) {
                    items(cities) { city ->
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        )
                        {
                            Text(
                                text = "${city.name}",
                                modifier = Modifier
                                    .align(alignment = Alignment.Top)
                                    .fillMaxWidth(0.85f)
                                    .padding(vertical = 16.dp)
                                    .selectable(
                                        selected = city.name == displayText,
                                        onClick = {
                                            displayText = city.name
                                            keyboardController?.hide()
                                            focusManager.clearFocus()
                                        }
                                    )
                            )
                            var iconChange by remember {
                                mutableStateOf(city.favorite)
                            }
                            IconButton(
                                modifier = Modifier.align(alignment = Alignment.Bottom),

                                onClick = {
                                    viewModel.favoriteUpdate(city)
                                    iconChange = city.favorite

                                }

                            ) {
                                if (iconChange) {
                                    Icon(
                                        imageVector = Icons.Outlined.Favorite,
                                        contentDescription = "Favourited this city"
                                    )


                                } else {
                                    Icon(
                                        imageVector = Icons.Outlined.FavoriteBorder,
                                        contentDescription = "Favourite this city",
                                    )

                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun StartOrderPreview() {
    val viewModel = viewModel<SearchViewModel>()
    StartScreen(
        viewModel = viewModel,

        onNextButtonClicked = {},
    )

}


