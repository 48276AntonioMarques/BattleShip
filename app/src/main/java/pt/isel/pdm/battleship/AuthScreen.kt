package pt.isel.pdm.battleship

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.pdm.battleship.ui.theme.BattleShipTheme

@Composable
fun AuthScreen() {
    BattleShipTheme {
        Log.v("TAG", "AuthScreen composed")
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
                    Column {
                        Row {
                            Text("Login")
                            Text("|")
                            Text("Register")
                        }
                        Column {
                            TextField(
                                value = TextFieldValue("username"),
                                label = {
                                    Text(text = "Username")
                                },
                                onValueChange = {

                                }
                            )
                            TextField(
                                value = TextFieldValue("password"),
                                label = {
                                    Text(text = "Password")
                                },
                                onValueChange = {

                                }
                            )
                        }
                    }
                    Button(onClick = { /*TODO*/ }) {
                        Text(text = "Login")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AuthScreenPreview() {
    BattleShipTheme {
        AuthScreen()
    }
}