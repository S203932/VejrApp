@file:OptIn(UiToolingDataApi::class)

package com.example.vejrapp

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.data.UiToolingDataApi
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


/*data class Week : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.week) // Link to your XML layout file
    }


    // findViewById<TextView>(R.id.temperature).text=tem
    val maxTemperature: TextView = findViewById<EditText>(R.id.maxtemperature)
    val minTemperature: TextView = findViewById<EditText>(R.id.mintemperature)
    val weatherIcon: ImageView = findViewById(R.id.weather)
    val rainIcon: ImageView = findViewById(R.id.umbrella)
    val rainChance: TextView = findViewById(R.id.rain)
    val dayOfTheWeek: TextView = findViewById(R.id.dayoftheweek)
    val date: TextView = findViewById(R.id.date)

    @Composable
    fun WeekWeather(

    ) {

    }

}*/

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeekWeather(
    @DrawableRes background: Int,
    modifier: Modifier = Modifier
) {

    Scaffold(
        modifier = Modifier
            .background(Color(R.drawable.forweek))
    ) {
        TopAppBar(title = { "Search Bar" })

        Spacer(modifier = Modifier.height(5.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(15.dp),

            ) {
            Card(modifier = Modifier.align(CenterHorizontally)) {
                Text(
                    text = "Monday   October 23th",
                    /*  text = " ${DateFormat.DAY_OF_WEEK_FIELD} ${DateFormat.DATE_FIELD}"*/
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.height(5.dp))

//Monday
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(10.dp))
                    .background(Color.Blue)
            ) {
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = "20°",
                    modifier = Modifier
                        .padding(3.dp, 3.dp)
                        .align(CenterVertically),
                    color = Color.White,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.width(7.dp))
                Column(modifier = Modifier.align(CenterVertically)) {
                    Text(text = "22°", color = Color.White, fontSize = 13.sp)
                    Text(text = "16°", color = Color.White, fontSize = 13.sp)
                }
                Spacer(modifier = Modifier.width(7.dp))
                Image(
                    painter = painterResource(id = R.drawable.clouds),
                    contentDescription = null,
                    modifier = Modifier.align(CenterVertically)
                )
                Spacer(modifier = Modifier.width(7.dp))
                Image(
                    painter = painterResource(id = R.drawable.umbrella),
                    contentDescription = null,
                    modifier = Modifier.align(CenterVertically),
                )
                Spacer(modifier = Modifier.width(1.dp))
                Text(
                    text = "30%",
                    modifier = Modifier.align(CenterVertically),
                    color = Color.White,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.width(150.dp))

                Column(modifier = Modifier.align(CenterVertically)) {
                    Text(
                        text = "Monday"
                        /*   text = "${DateFormat.DAY_OF_WEEK_FIELD}"*/,
                        color = Color.White,
                        fontSize = 10.sp
                    )
                    Text(
                        text = "23/10"
                        /* text = "${DateFormat.DATE_FIELD}"*/,
                        color = Color.White,
                        fontSize = 10.sp
                    )
                }


            }

            Spacer(modifier = Modifier.height(5.dp))
//Tuesday
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(10.dp))
                    .background(Color.Blue)
            ) {
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = "20°",
                    modifier = Modifier
                        .padding(3.dp, 3.dp)
                        .align(CenterVertically),
                    color = Color.White,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.width(7.dp))
                Column(modifier = Modifier.align(CenterVertically)) {
                    Text(text = "22°", color = Color.White, fontSize = 13.sp)
                    Text(text = "16°", color = Color.White, fontSize = 13.sp)
                }
                Spacer(modifier = Modifier.width(7.dp))
                Image(
                    painter = painterResource(id = R.drawable.clouds),
                    contentDescription = null,
                    modifier = Modifier.align(CenterVertically)
                )
                Spacer(modifier = Modifier.width(7.dp))
                Image(
                    painter = painterResource(id = R.drawable.umbrella),
                    contentDescription = null,
                    modifier = Modifier.align(CenterVertically),
                )
                Spacer(modifier = Modifier.width(1.dp))
                Text(
                    text = "30%",
                    modifier = Modifier.align(CenterVertically),
                    color = Color.White,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.width(150.dp))

                Column(modifier = Modifier.align(CenterVertically)) {
                    Text(
                        text = "Tuesday"
                        /*   text = "${DateFormat.DAY_OF_WEEK_FIELD}"*/,
                        color = Color.White,
                        fontSize = 10.sp
                    )
                    Text(
                        text = "24/10"
                        /* text = "${DateFormat.DATE_FIELD}"*/,
                        color = Color.White,
                        fontSize = 10.sp
                    )
                }


            }

            Spacer(modifier = Modifier.height(5.dp))

//Wednesday
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(10.dp))
                    .background(Color.Blue)
            ) {
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = "20°",
                    modifier = Modifier
                        .padding(3.dp, 3.dp)
                        .align(CenterVertically),
                    color = Color.White,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.width(7.dp))
                Column(modifier = Modifier.align(CenterVertically)) {
                    Text(text = "22°", color = Color.White, fontSize = 13.sp)
                    Text(text = "16°", color = Color.White, fontSize = 13.sp)
                }
                Spacer(modifier = Modifier.width(7.dp))
                Image(
                    painter = painterResource(id = R.drawable.clouds),
                    contentDescription = null,
                    modifier = Modifier.align(CenterVertically)
                )
                Spacer(modifier = Modifier.width(7.dp))
                Image(
                    painter = painterResource(id = R.drawable.umbrella),
                    contentDescription = null,
                    modifier = Modifier.align(CenterVertically),
                )
                Spacer(modifier = Modifier.width(1.dp))
                Text(
                    text = "30%",
                    modifier = Modifier.align(CenterVertically),
                    color = Color.White,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.width(150.dp))

                Column(modifier = Modifier.align(CenterVertically)) {
                    Text(
                        text = "Wednesday"
                        /*   text = "${DateFormat.DAY_OF_WEEK_FIELD}"*/,
                        color = Color.White,
                        fontSize = 10.sp
                    )
                    Text(
                        text = "25/10"
                        /* text = "${DateFormat.DATE_FIELD}"*/,
                        color = Color.White,
                        fontSize = 10.sp
                    )
                }


            }

            Spacer(modifier = Modifier.height(5.dp))

//Thursday
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(10.dp))
                    .background(Color.Blue)
            ) {
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = "20°",
                    modifier = Modifier
                        .padding(3.dp, 3.dp)
                        .align(CenterVertically),
                    color = Color.White,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.width(7.dp))
                Column(modifier = Modifier.align(CenterVertically)) {
                    Text(text = "22°", color = Color.White, fontSize = 13.sp)
                    Text(text = "16°", color = Color.White, fontSize = 13.sp)
                }
                Spacer(modifier = Modifier.width(7.dp))
                Image(
                    painter = painterResource(id = R.drawable.clouds),
                    contentDescription = null,
                    modifier = Modifier.align(CenterVertically)
                )
                Spacer(modifier = Modifier.width(7.dp))
                Image(
                    painter = painterResource(id = R.drawable.umbrella),
                    contentDescription = null,
                    modifier = Modifier.align(CenterVertically),
                )
                Spacer(modifier = Modifier.width(1.dp))
                Text(
                    text = "30%",
                    modifier = Modifier.align(CenterVertically),
                    color = Color.White,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.width(150.dp))

                Column(modifier = Modifier.align(CenterVertically)) {
                    Text(
                        text = "Thursday"
                        /*   text = "${DateFormat.DAY_OF_WEEK_FIELD}"*/,
                        color = Color.White,
                        fontSize = 10.sp
                    )
                    Text(
                        text = "26/10"
                        /* text = "${DateFormat.DATE_FIELD}"*/,
                        color = Color.White,
                        fontSize = 10.sp
                    )
                }


            }

            Spacer(modifier = Modifier.height(5.dp))
//Friday
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(10.dp))
                    .background(Color.Blue)
            ) {
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = "20°",
                    modifier = Modifier
                        .padding(3.dp, 3.dp)
                        .align(CenterVertically),
                    color = Color.White,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.width(7.dp))
                Column(modifier = Modifier.align(CenterVertically)) {
                    Text(text = "22°", color = Color.White, fontSize = 13.sp)
                    Text(text = "16°", color = Color.White, fontSize = 13.sp)
                }
                Spacer(modifier = Modifier.width(7.dp))
                Image(
                    painter = painterResource(id = R.drawable.clouds),
                    contentDescription = null,
                    modifier = Modifier.align(CenterVertically)
                )
                Spacer(modifier = Modifier.width(7.dp))
                Image(
                    painter = painterResource(id = R.drawable.umbrella),
                    contentDescription = null,
                    modifier = Modifier.align(CenterVertically),
                )
                Spacer(modifier = Modifier.width(1.dp))
                Text(
                    text = "30%",
                    modifier = Modifier.align(CenterVertically),
                    color = Color.White,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.width(150.dp))

                Column(modifier = Modifier.align(CenterVertically)) {
                    Text(
                        text = "Friday"
                        /*   text = "${DateFormat.DAY_OF_WEEK_FIELD}"*/,
                        color = Color.White,
                        fontSize = 10.sp
                    )
                    Text(
                        text = "27/10"
                        /* text = "${DateFormat.DATE_FIELD}"*/,
                        color = Color.White,
                        fontSize = 10.sp
                    )
                }


            }

            Spacer(modifier = Modifier.height(5.dp))
//Saturday
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(10.dp))
                    .background(Color.Blue)
            ) {
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = "20°",
                    modifier = Modifier
                        .padding(3.dp, 3.dp)
                        .align(CenterVertically),
                    color = Color.White,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.width(7.dp))
                Column(modifier = Modifier.align(CenterVertically)) {
                    Text(text = "22°", color = Color.White, fontSize = 13.sp)
                    Text(text = "16°", color = Color.White, fontSize = 13.sp)
                }
                Spacer(modifier = Modifier.width(7.dp))
                Image(
                    painter = painterResource(id = R.drawable.clouds),
                    contentDescription = null,
                    modifier = Modifier.align(CenterVertically)
                )
                Spacer(modifier = Modifier.width(7.dp))
                Image(
                    painter = painterResource(id = R.drawable.umbrella),
                    contentDescription = null,
                    modifier = Modifier.align(CenterVertically),
                )
                Spacer(modifier = Modifier.width(1.dp))
                Text(
                    text = "30%",
                    modifier = Modifier.align(CenterVertically),
                    color = Color.White,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.width(150.dp))

                Column(modifier = Modifier.align(CenterVertically)) {
                    Text(
                        text = "Saturday"
                        /*   text = "${DateFormat.DAY_OF_WEEK_FIELD}"*/,
                        color = Color.White,
                        fontSize = 10.sp
                    )
                    Text(
                        text = "28/10"
                        /* text = "${DateFormat.DATE_FIELD}"*/,
                        color = Color.White,
                        fontSize = 10.sp
                    )
                }


            }

            Spacer(modifier = Modifier.height(5.dp))
//Sunday
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(10.dp))
                    .background(Color.Blue)
            ) {
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = "20°",
                    modifier = Modifier
                        .padding(3.dp, 3.dp)
                        .align(CenterVertically),
                    color = Color.White,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.width(7.dp))
                Column(modifier = Modifier.align(CenterVertically)) {
                    Text(text = "22°", color = Color.White, fontSize = 13.sp)
                    Text(text = "16°", color = Color.White, fontSize = 13.sp)
                }
                Spacer(modifier = Modifier.width(7.dp))
                Image(
                    painter = painterResource(id = R.drawable.clouds),
                    contentDescription = null,
                    modifier = Modifier.align(CenterVertically)
                )
                Spacer(modifier = Modifier.width(7.dp))
                Image(
                    painter = painterResource(id = R.drawable.umbrella),
                    contentDescription = null,
                    modifier = Modifier.align(CenterVertically),
                )
                Spacer(modifier = Modifier.width(1.dp))
                Text(
                    text = "30%",
                    modifier = Modifier.align(CenterVertically),
                    color = Color.White,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.width(150.dp))

                Column(modifier = Modifier.align(CenterVertically)) {
                    Text(
                        text = "Sunday"
                        /*   text = "${DateFormat.DAY_OF_WEEK_FIELD}"*/,
                        color = Color.White,
                        fontSize = 10.sp
                    )
                    Text(
                        text = "29/10"
                        /* text = "${DateFormat.DATE_FIELD}"*/,
                        color = Color.White,
                        fontSize = 10.sp
                    )
                }


            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Showing() {
    Card {
        WeekWeather(background = R.drawable.forweek)
    }
}







