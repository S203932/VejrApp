package com.example.vejrapp.presentation.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.vejrapp.R

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    onNextButtonClicked: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel,
    navController: NavController,
) {

    val searchText by viewModel.searchText.collectAsState()
    val cities = viewModel.cities.collectAsState()
    val searchMode by viewModel.searchMode.collectAsState()
    val currentCity by viewModel.currentCity.collectAsState()

    //There is added a font color, that is the same for all the text within
    val fontColor = Color.Black

    //This is for hiding the keyboard
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
//    var navIconVisible by remember { mutableStateOf(false) }


    Column(modifier = modifier.padding(8.dp)) {
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
        val visible by viewModel.searchMode.collectAsState()

        TopAppBar(
            title = {
                TextField(
                    value = searchText,
                    onValueChange =
                    {
                        viewModel.onSearchTextChange(it)
                        if (it.isBlank()) {
                            viewModel.updateSearchMode(false)

                        } else {
                            viewModel.updateSearchMode(true)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .motionEventSpy {}
                        .onFocusEvent {},
                    placeholder = { Text(text = currentCity.name, color = fontColor) },
                    leadingIcon = {
                        Icon(
                            Icons.Filled.Search, contentDescription = null,
                            modifier = Modifier
                                .height(20.dp)
                                .width(20.dp)
                        )
                    },
                    shape = RoundedCornerShape(25.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Text
                    ),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    textStyle = TextStyle(color = fontColor, fontSize = 20.sp),
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.Gray,
                        disabledTextColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        containerColor = Color.White.copy(alpha = 0.8f)
                    ),
                )
            },
            navigationIcon = {
                val density = LocalDensity.current
                AnimatedVisibility(
                    visible = visible,
                    enter = slideInHorizontally {
                        // Slide in from 40 dp from the side.
                        with(density) { -10.dp.roundToPx() }
                    } + expandHorizontally(
                        // Expand from the side.
                        expandFrom = Alignment.Start
                    ) + fadeIn(
                        // Fade in with the initial alpha of 0.3f.
                        initialAlpha = 0.3f
                    ),
                    exit = slideOutHorizontally() + shrinkHorizontally() + fadeOut()
                ) {
                    IconButton(
                        onClick = {
                            keyboardController?.hide()
                            //or hide keyboard
                            focusManager.clearFocus()
                            viewModel.updateSearchMode(!searchMode)
                            viewModel.onSearchTextChange("")
                        },
                        // modifier = Modifier.alpha(if (searchMode) 1f else 0f)

                    ) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_arrow_back_24),
                            contentDescription = "Back Button",
                            tint = Color.Black
                        )
                    }
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
            scrollBehavior = scrollBehavior,
            colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Transparent)
        )

        if (searchMode) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
            ) {
                items(cities.value) { city ->
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    )
                    {
                        Text(
                            text = city.name,
                            modifier = Modifier
                                .align(alignment = Alignment.Top)
                                .fillMaxWidth(0.85f)
                                .padding(vertical = 16.dp)
                                .selectable(
                                    selected = city.name == currentCity.name,
                                    onClick = {
                                        keyboardController?.hide()
                                        focusManager.clearFocus()
                                        viewModel.onSearchTextChange("")
                                        viewModel.updateSearchMode(false)
                                        viewModel.updateCurrentCity(city)
                                    }
                                ),
                            color = fontColor
                        )
                        IconButton(
                            modifier = Modifier.align(alignment = Alignment.Bottom),
                            onClick = { viewModel.updateFavorite(city) }

                        ) {
                            Icon(
                                imageVector = city.favoriteIcon(),
                                contentDescription = stringResource(city.favoriteDescriptionId())
                            )
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
    var viewModel = viewModel<SearchViewModel>()
    val navController = rememberNavController()
    SearchBar(
        viewModel = viewModel,
        navController = navController,
        onNextButtonClicked = {},
    )
}

