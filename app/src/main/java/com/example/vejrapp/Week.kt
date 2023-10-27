package com.example.vejrapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TopHeadLine() {
    val font = Color.White
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row() {
            Text(
                text = "Monday October 9th",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth(),
                color = font
            )
        }
    }
}

@Composable
fun WeekWeather() {
    val fontColor = Color.White
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.6f)),
        modifier = Modifier
            .fillMaxWidth()
    ) {
//Monday
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(10.dp))
        ) {
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = "20°",
                modifier = Modifier
                    .padding(3.dp, 3.dp)
                    .align(Alignment.CenterVertically),
                color = Color.White,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.width(7.dp))
            Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                Text(text = "22°", color = Color.White, fontSize = 13.sp)
                Text(text = "16°", color = Color.White, fontSize = 13.sp)
            }
            Spacer(modifier = Modifier.width(7.dp))
            Image(
                painter = painterResource(id = R.drawable.clouds),
                contentDescription = null,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.width(7.dp))
            Image(
                painter = painterResource(id = R.drawable.umbrella),
                contentDescription = null,
                modifier = Modifier.align(Alignment.CenterVertically),
            )
            Spacer(modifier = Modifier.width(1.dp))
            Text(
                text = "30%",
                modifier = Modifier.align(Alignment.CenterVertically),
                color = Color.White,
                fontSize = 13.sp
            )
            Spacer(modifier = Modifier.width(150.dp))

            Column(modifier = Modifier.align(Alignment.CenterVertically)) {
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
    }
//Tuesday
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(10.dp))
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


@Preview
@Composable
fun Lazy() {
    LazyColumn(
        modifier = Modifier
            .padding(8.dp)
            .wrapContentSize(Alignment.BottomCenter)
    ) {
        item { // You can change the number of cards as needed
            WeekWeather()
            Spacer(modifier = Modifier.height(8.dp)) // Add spacing between cards
        }
    }
}


