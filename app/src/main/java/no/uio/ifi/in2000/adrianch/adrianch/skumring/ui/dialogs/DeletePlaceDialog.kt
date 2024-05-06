package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import no.uio.ifi.in2000.adrianch.adrianch.skumring.R
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.theme.SkumringTheme

@Composable
fun DeletePlaceDialog(
    onDismissRequest: () -> Unit,
    onConfirmClick: () -> Unit
) {
    val buttonColors = ButtonDefaults.outlinedButtonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    )

    Dialog(onDismissRequest = onDismissRequest) {
        Card (
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ){
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.delete_place_confirmation),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = stringResource(id = R.string.delete_place_info),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(20.dp))
                Row (
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OutlinedButton(
                        onClick = onDismissRequest,
                        colors = buttonColors
                    ) {
                        Text(text = stringResource(id = R.string.cancel))
                    }
                    Spacer(modifier = Modifier.width(20.dp))

                    OutlinedButton(
                        onClick = onConfirmClick,
                        colors = buttonColors
                    ) {
                        Text(text = stringResource(id = R.string.delete_place_confirm_button))
                    }
                }
            }
        }
    }
}

@Preview(name = "Light mode, norsk")
@Composable
fun PreviewDeletePlaceDialog() {
    SkumringTheme(useDarkTheme = false) {
        Surface {
            DeletePlaceDialog(
                onDismissRequest = {},
                onConfirmClick = {})
        }
    }
}

@Preview(name = "Dark mode, engelsk",
    locale = "en")
@Composable
fun PreviewDeletePlaceDialogDark() {
    SkumringTheme(useDarkTheme = true) {
        Surface {
            DeletePlaceDialog(
                onDismissRequest = {},
                onConfirmClick = {})
        }
    }
}