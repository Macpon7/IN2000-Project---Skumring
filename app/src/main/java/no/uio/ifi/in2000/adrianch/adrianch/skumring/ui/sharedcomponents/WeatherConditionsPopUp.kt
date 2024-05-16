package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.sharedcomponents


import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
                    Icon(
                        Icons.Default.Info,
                        contentDescription = stringResource(id = R.string.information),
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(30.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(id = R.string.weather_condition),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))

                Row {
                    Column {
                        Text(
                            text = stringResource(R.string.conditions_excellent),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Divider(
                            modifier = Modifier.padding(start = 2.dp, end = 2.dp, bottom = 5.dp),
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            thickness = 1.dp
                        )
                        Text(
                            stringResource(id = R.string.weather_conditions_excellent_explanation),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row {
                    Column {
                        Text(
                            text = stringResource(R.string.conditions_decent),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Divider(
                            modifier = Modifier.padding(start = 2.dp, end = 2.dp, bottom = 5.dp),
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            thickness = 1.dp
                        )
                        Text(
                            stringResource(id = R.string.weather_conditions_decent_explanation),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row {
                    Column {
                        Text(
                            text = stringResource(R.string.conditions_poor),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center

                        )
                        Divider(
                            modifier = Modifier.padding(start = 2.dp, end = 2.dp, bottom = 5.dp),
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            thickness = 1.dp
                        )
                        Text(
                            stringResource(id = R.string.weather_conditions_poor_explanation),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(
                        onClick = onClose
                    ) {
                        Text(
                            stringResource(id =  R.string.close),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
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
            onClose = {},
        )
    }
}

