package no.kristiania.carouselhorse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CarouselMainScreen()
        }
    }
}

@Composable
fun CarouselMainScreen() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally) {
        HorseView(currentHorse = R.drawable.horse12)
        Row {
            Button(onClick = {
            }) {
                Text(text = "Backwards")
            }
            Button(onClick = {
            }) {
                Text(text = "Forward")
            }
        }
        Button(onClick = {
        }) {
            Text(text = "Automatic")
        }
    }
}

@Composable
fun HorseView(currentHorse: Int) {
    Image(
        modifier = Modifier.fillMaxWidth(0.8f),
        painter = painterResource(id = currentHorse),
        contentScale = ContentScale.Fit,
        contentDescription = stringResource(id = R.string.horse_frame_description)
    )
}

@Preview(showBackground = true)
@Composable
fun CarouselMainScreenPreview() {
    CarouselMainScreen()
}
