package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.sharedcomponents


import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import no.uio.ifi.in2000.adrianch.adrianch.skumring.R




@Composable
fun WeatherIconPopUp(
    onClose: () -> Unit,
) {
    Dialog(
        onDismissRequest = onClose
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(Icons.Default.Info, contentDescription = "Info", tint = Color.Red, modifier = Modifier.size(30.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Weather conditions",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,)
                }
                Spacer(modifier = Modifier.height(20.dp))

                Row{
                    Column{
                        Text(
                            text = "Excellent",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Divider( // For dividing sunset today info from golden hour and blue hour times
                            modifier = Modifier.padding(start = 2.dp, end = 2.dp, bottom = 5.dp),
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            thickness = 1.dp
                        )
                        Text(
                            "There should be some clouds in the middle to high levels, low moisture and excellent visibility!"
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row{
                    Column{
                        Text(
                            text = "Decent",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Divider( // For dividing sunset today info from golden hour and blue hour times
                            modifier = Modifier.padding(start = 2.dp, end = 2.dp, bottom = 5.dp),
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            thickness = 1.dp
                        )
                        Text(
                            "A little cloudy, but hopefully still makes for a nice trip!"
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row{//verticalAlignment = Alignment.CenterVertically) {
                    Column{
                        Text(
                            text = "Poor",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center

                        )
                        Divider( // For dividing sunset today info from golden hour and blue hour times
                            modifier = Modifier.padding(start = 2.dp, end = 2.dp, bottom = 5.dp),
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            thickness = 1.dp
                        )
                        Text(
                            "The clouds are hanging low, visibility is not good"
                        )
                    }
                }

                            Spacer(modifier = Modifier.height(16.dp))
                            Row(
                                horizontalArrangement = Arrangement.End,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                TextButton(
                                    onClick = onClose
                                ) {
                                    Text("Dismiss",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }



@Preview
@Composable
fun CustomDialogPreview() {
    MaterialTheme {
        WeatherIconPopUp(
            onClose = { /* Handle close action */ },
        )
    }
}

