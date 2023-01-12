package pt.isel.pdm.battleship.ui

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import pt.isel.pdm.battleship.R

@Composable
fun TopBar(
    onBackRequested: (() -> Unit)? = null,
    title: String = ""
) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            if(onBackRequested != null) {
                IconButton(onClick = onBackRequested) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null)
                }
            }
        }
    )
}