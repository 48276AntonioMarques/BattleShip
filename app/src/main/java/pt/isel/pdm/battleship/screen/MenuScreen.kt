package pt.isel.pdm.battleship.screen

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.pdm.battleship.service.User
import pt.isel.pdm.battleship.ui.theme.BattleShipTheme

@Composable
fun MenuScreen(
    onRankingRequested: () -> Unit,
    onAuthorRequested: () -> Unit,
    onAuthRequested: () -> Unit,
    onInvitesRequested: () -> Unit,
    onChallengeRequested: () -> Unit,
    user: User?,
    invitesCount: Int,
    feedbackText: String
) {

    fun isLogged() = user != null

    BattleShipTheme {
        Log.v("TAG", "QuoteOfDayScreen composed")
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
                            Column {
                                Row {
                                    Button (
                                        onClick = onChallengeRequested
                                    ) {
                                        Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null)
                                    }
                                }
                                if (feedbackText.isNotBlank()) Text(text = feedbackText)
                            }
                            Button (
                                onClick = onInvitesRequested
                            ) {
                                Text("See Invites ($invitesCount)")
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
        MenuScreen(
            onRankingRequested = { Log.v("Menu", "Ranking Requested!") },
            onAuthorRequested = { Log.v("Menu", "Author Requested!") },
            onAuthRequested = { Log.v("Menu", "Auth Requested!") },
            onInvitesRequested = { Log.v("Menu", "Invites Requested!") },
            onChallengeRequested = { Log.v("Menu", "Challenge Requested!") },
            null,
            0,
            ""
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoggedUserMenuScreenPreview() {
    BattleShipTheme {
        val user = User("Henrique", "Bearer 1234fg")
        MenuScreen(
            onRankingRequested = { Log.v("Menu", "Ranking Requested!") },
            onAuthorRequested = { Log.v("Menu", "Author Requested!") },
            onAuthRequested = { Log.v("Menu", "Auth Requested!") },
            onInvitesRequested = { Log.v("Menu", "Invites Requested!") },
            onChallengeRequested = { Log.v("Menu", "Challenge Requested!") },
            user,
            0,
            ""
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoggedUserMenuScreenWithInvitesPreview() {
    BattleShipTheme {
        val user = User("Henrique", "Bearer 1234fg")
        MenuScreen(
            onRankingRequested = { Log.v("Menu", "Ranking Requested!") },
            onAuthorRequested = { Log.v("Menu", "Author Requested!") },
            onAuthRequested = { Log.v("Menu", "Auth Requested!") },
            onInvitesRequested = { Log.v("Menu", "Invites Requested!") },
            onChallengeRequested = { Log.v("Menu", "Challenge Requested!") },
            user,
            (1..99).random(),
            ""
        )
    }
}