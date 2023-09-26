package no.kristiania.carouselhorse

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CarouselMainScreen(carouselViewModel: CarouselViewModel = viewModel()) {
    val primary = MaterialTheme.colorScheme.primary

    val carouselViewState by carouselViewModel.carouselState.collectAsState()
    var buttonText by remember { mutableStateOf("Manual") }
    var buttonColor by remember { mutableStateOf(primary) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally) {
        HorseView(currentHorse = carouselViewState.currentHorse)
        Row {
            Button(onClick = {
                carouselViewModel.previousHorse()
            }) {
                Text(text = "Backwards")
            }
            Button(onClick = {
                carouselViewModel.nextHorse()
            }) {
                Text(text = "Forward")
            }
        }
        Button(onClick = {
            if (carouselViewModel.toggleAutomatic()) {
                buttonText = "Automatic"
                buttonColor = Color.Red
            } else {
                buttonText = "Manual"
                buttonColor = primary
            }
        }, colors = ButtonDefaults.buttonColors(buttonColor)) {
            Text(text = buttonText)
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
