package pt.isel.pdm.battleship.menu

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.pdm.battleship.R
import pt.isel.pdm.battleship.common.User
import pt.isel.pdm.battleship.ui.theme.BattleShipTheme

@Composable
fun MenuScreen(
    onRankingRequested: () -> Unit,
    onAuthorRequested: () -> Unit,
    onAuthRequested: () -> Unit,
    onInvitesRequested: () -> Unit,
    onChallengeRequested: (String) -> Unit,
    onChallengeUpdate: (TextFieldValue) -> Unit,
    receiver: String,
    user: User?,
    onLogoutRequested: () -> Unit
) {

    fun isLogged() = user != null

    BattleShipTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            backgroundColor = MaterialTheme.colors.background,
        ) { innerPadding ->
            Column {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(weight = 1.0f)
                        .padding(innerPadding)
                ) {
                    Text(text = "BattleShip")
                }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        if (isLogged()) {
                            Row {
                                TextField(
                                    value = TextFieldValue(receiver, TextRange(receiver.length)),
                                    label = {
                                        Text(text = stringResource(id = R.string.app_auth_username))
                                    },
                                    onValueChange = onChallengeUpdate,
                                )
                                Button (
                                    onClick = { onChallengeRequested(receiver) }
                                ) {
                                    Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null)
                                }
                            }
                            Button (
                                onClick = onInvitesRequested
                            ) {
                                Text("See Invites")
                            }
                            Button (
                                onClick = onLogoutRequested
                            ) {
                                Text("Logout")
                            }
                        }
                        else {
                            Button (
                                onClick = onAuthRequested
                            ) {
                                Text("Login or Register")
                            }
                        }
                        Button (
                            onClick = onRankingRequested
                        ) {
                            Text("Rank")
                        }
                        Button (
                            onClick = onAuthorRequested
                        ) {
                            Text("Author")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MenuScreenPreview() {
    BattleShipTheme {
        val receiver = remember { mutableStateOf("") }
        MenuScreen(
            onRankingRequested = { Log.v("Menu", "Ranking Requested!") },
            onAuthorRequested = { Log.v("Menu", "Author Requested!") },
            onAuthRequested = { Log.v("Menu", "Auth Requested!") },
            onInvitesRequested = { Log.v("Menu", "Invites Requested!") },
            onChallengeRequested = { Log.v("Menu", "Challenge Requested!") },
            onChallengeUpdate = { field -> receiver.value = field.text },
            receiver = receiver.value,
            null,
            onLogoutRequested = { Log.v("Menu", "Logout Requested!") }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoggedUserMenuScreenPreview() {
    BattleShipTheme {
        val user = User("Henrique", "Bearer 1234fg")
        val receiver = remember { mutableStateOf("") }
        MenuScreen(
            onRankingRequested = { Log.v("Menu", "Ranking Requested!") },
            onAuthorRequested = { Log.v("Menu", "Author Requested!") },
            onAuthRequested = { Log.v("Menu", "Auth Requested!") },
            onInvitesRequested = { Log.v("Menu", "Invites Requested!") },
            onChallengeRequested = { Log.v("Menu", "Challenge Requested!") },
            onChallengeUpdate = { field -> receiver.value = field.text },
            receiver = receiver.value,
            user,
            onLogoutRequested = { Log.v("Menu", "Logout Requested!") }
        )
    }
}
