package pt.isel.pdm.battleship.screen

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.pdm.battleship.R
import pt.isel.pdm.battleship.service.AuthType
import pt.isel.pdm.battleship.ui.theme.BattleShipTheme

@Composable
fun AuthScreen(
    authType: AuthType,
    loginUsername: String,
    registerUsername: String,
    onLoginTextUpdate:  (TextFieldValue) -> Unit,
    onRegisterTextUpdate: (TextFieldValue) -> Unit,
    onLoginRequested: () -> Unit,
    onRegisterRequested: () -> Unit,
    changeAuthType: (AuthType) -> Unit
) {
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
                    Column {
                        Row {
                            Text(
                                stringResource(id = R.string.app_auth_login),
                                Modifier.clickable {
                                    if (authType != AuthType.LOGIN) {
                                        changeAuthType(AuthType.LOGIN)
                                    }
                                }
                            )
                            Text("|")
                            Text(
                                stringResource(id = R.string.app_auth_register),
                                Modifier.clickable {
                                    if (authType != AuthType.REGISTER) {
                                        changeAuthType(AuthType.REGISTER)
                                    }
                                }
                            )
                        }
                        Column {
                            if (authType == AuthType.LOGIN) {
                                TextField(
                                    value = TextFieldValue(loginUsername, TextRange(loginUsername.length)),
                                    label = {
                                        Text(text = stringResource(id = R.string.app_auth_username))
                                    },
                                    onValueChange = onLoginTextUpdate,
                                )
                            }
                            else {
                                TextField(
                                    value = TextFieldValue(registerUsername, TextRange(registerUsername.length)),
                                    label = {
                                        Text(text = stringResource(id = R.string.app_auth_username))
                                    },
                                    onValueChange = onRegisterTextUpdate
                                )
                            }
                        }
                        if (authType == AuthType.LOGIN) {
                            Button(onClick = onLoginRequested) {
                                Text(text = stringResource(id = R.string.app_auth_login))
                            }
                        }
                        else {
                            Button(onClick = onRegisterRequested) {
                                Text(text = stringResource(id = R.string.app_auth_register))
                            }
                        }
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
        AuthScreen(
            authType = AuthType.LOGIN,
            loginUsername = "",
            registerUsername = "",
            onLoginTextUpdate = { Log.v("AuthScreen", "Login Text Update") },
            onRegisterTextUpdate = { Log.v("AuthScreen", "Register Text Update") },
            onLoginRequested = { Log.v("AuthScreen", "Login Requested") },
            onRegisterRequested = { Log.v("AuthScreen", "Register Requested") },
            changeAuthType = { Log.v("AuthScreen", "Auth Type Changed") }
        )
    }
}