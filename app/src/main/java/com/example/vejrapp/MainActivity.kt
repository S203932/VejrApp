package com.example.vejrapp

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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonDefaults.outlinedButtonBorder
import androidx.compose.material3.ButtonDefaults.shape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.SemanticsProperties.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
//import com.example.vejrapp.SearchInputStateImpl.Companion.Saver
import com.example.vejrapp.ui.theme.VejrAppTheme
import java.nio.file.Files.size

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VejrAppTheme {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.End
                ) {
                    SearchBar()


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
fun SearchBar() {
    var search by rememberSaveable {
        mutableStateOf("")
    }


    Row(
        modifier = Modifier.padding(8.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.End
    ) {
        Search(string = search, onValueChange = { search = it }, onSearch = {})
        Settings()

    }


}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    VejrAppTheme {
        SearchBar()
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





