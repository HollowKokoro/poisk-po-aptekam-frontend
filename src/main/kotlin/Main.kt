import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

@Composable
@Preview
fun App(consumer: Consumer) {
    var input by remember { mutableStateOf("") }
    val consumerResult = consumer.result.collectAsState()
    MaterialTheme {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            TextField(
                value = input,
                onValueChange = {
                    input = it
                }
            )
            Button(
                onClick = {
                    Producer(input)
                    consumer.run()
                }
            ) {
                Text("Искать")
            }
            Text(text = consumerResult.value.joinToString())
//            TextField(value = consumerResult.value.joinToString(), onValueChange = {
//            })
        }
    }
}

fun main() = application {
    val consumer = Consumer()
    Window(onCloseRequest = ::exitApplication) {
        App(consumer)
    }
}
