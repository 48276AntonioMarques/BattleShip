package pt.isel.pdm.battleship.menu

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.pdm.battleship.R
import pt.isel.pdm.battleship.ui.TopBar
import pt.isel.pdm.battleship.ui.theme.BattleShipTheme

@Composable
fun AuthorScreen(
    onBackRequested: () -> Unit,
    onSendEmailRequested: () -> Unit
) {
    BattleShipTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            backgroundColor = MaterialTheme.colors.background,
            topBar = {
                TopBar(onBackRequested = onBackRequested)
            },
        ) { innerPadding ->
            Column(
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            ) {
                Author(onSendEmailRequested = onSendEmailRequested)
            }
        }
    }
}

@Composable
fun Author(onSendEmailRequested: () -> Unit = { }) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onSendEmailRequested() }
    ) {
        Text(text = "48276 - António Marques", style = MaterialTheme.typography.h4)
        Icon(imageVector = Icons.Default.Email, contentDescription = null)
    }
}

@Preview(showBackground = true)
@Composable
fun AuthorScreenPreview() {
    BattleShipTheme {
        val onBackRequested: () -> Unit = {
            Log.v("AuthorScreen", "This will take you back.")
        }
        val onSendEmailRequested: () -> Unit = {
            Log.v("AuthorScreen", "Emailing someone!")
        }
        AuthorScreen(onBackRequested, onSendEmailRequested)
    }
}