
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import com.tecknobit.mantis.Mantis
import java.util.*

@Composable
fun UpdateDialog(
    locale: Locale = Locale.getDefault(),
    currentVersion: String,
    accessToken: String,
    owner: String,
    repo: String
) {
    val mantis = Mantis(locale)
    var show by remember { mutableStateOf(true) }
    val kduChecker = KDUChecker(accessToken, owner, repo)
    if(kduChecker.canBeUpdated(currentVersion)) {
        if(show) {
            AlertDialog(
                onDismissRequest = { show = false },
                title = {
                    Text("prova")
                },
                dismissButton = {
                    TextButton(
                        onClick = { show = false }
                    ) {
                        Text(mantis.getResource("no_update_key"))
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            show = false
                            // TO-DO: DOWNLOAD THE CORRECT ASSET
                        }
                    ) {
                        Text(mantis.getResource("update_key"))
                    }
                }
            )
        }
    }
}