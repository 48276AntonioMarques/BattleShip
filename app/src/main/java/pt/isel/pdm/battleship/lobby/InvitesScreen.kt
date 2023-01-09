package pt.isel.pdm.battleship.lobby

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.pdm.battleship.R
import pt.isel.pdm.battleship.common.Invite
import pt.isel.pdm.battleship.ui.TopBar
import pt.isel.pdm.battleship.ui.theme.BattleShipTheme

@Composable
fun InvitesScreen(
    onBackRequested: () -> Unit,
    invites: List<Invite>?,
    isLoading: Boolean,
    onAccept: (lobbyID: Int) -> Unit,
    onDodge: (lobbyID: Int) -> Unit
) {
    BattleShipTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            backgroundColor = MaterialTheme.colors.background,
            topBar = {
                TopBar(onBackRequested = onBackRequested)
            }
        ) { innerPadding ->
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            ) {
                if (invites != null) {
                    if (invites.isNotEmpty()) {
                        invites.map { invite ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(15.dp)
                            ){
                                Text(
                                    text = invite.senderName,
                                    modifier = Modifier.padding(5.dp)
                                )
                                Button( enabled = !isLoading, onClick = { onAccept(invite.lobbyID) }) {
                                    Icon(imageVector = Icons.Default.Check, contentDescription = null)
                                }
                                Button( enabled = !isLoading, onClick = { onDodge(invite.lobbyID) }) {
                                    Icon(imageVector = Icons.Default.Close, contentDescription = null)
                                }
                            }
                        }
                    }
                    else {
                        Text(text= stringResource(id = R.string.app_invites_none))
                    }
                }
                else {
                    Text(text= stringResource(id = R.string.app_invites_loading))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InvitesScreenPreview() {
    BattleShipTheme {
        InvitesScreen(
            onBackRequested = { Log.v("InviteScreen", "Back Requested") },
            null,
            false,
            onAccept = { Log.v("InviteScreen", "Lobby $it accepted") },
            onDodge = { Log.v("InviteScreen", "Lobby $it dodged") },
        )
    }
}