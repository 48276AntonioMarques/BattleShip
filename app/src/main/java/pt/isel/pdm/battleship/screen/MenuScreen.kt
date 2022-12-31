package pt.isel.pdm.battleship

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.pdm.battleship.ui.theme.BattleShipTheme

@Composable
fun MenuScreen() {
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
                    Button (
                        onClick = { Log.v("TAG", "Clicked!") }
                            ) {
                        Text("Login")
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
        MenuScreen()
    }
}