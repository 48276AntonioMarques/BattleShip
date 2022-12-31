package pt.isel.pdm.battleship.service

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.pdm.battleship.R
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
                            Text(stringResource(id = R.string.app_auth_login))
                            Text("|")
                            Text(stringResource(id = R.string.app_auth_register))
                        }
                        Column {
                            TextField(
                                value = TextFieldValue(stringResource(id = R.string.app_auth_username)),
                                label = {
                                    Text(text = stringResource(id = R.string.app_auth_username))
                                },
                                onValueChange = {

                                }
                            )
                            TextField(
                                value = TextFieldValue(stringResource(id = R.string.app_auth_password)),
                                label = {
                                    Text(text = stringResource(id = R.string.app_auth_password))
                                },
                                onValueChange = {

                                }
                            )
                        }
                    }
                    Button(onClick = { /*TODO*/ }) {
                        Text(text = stringResource(id = R.string.app_auth_login))
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