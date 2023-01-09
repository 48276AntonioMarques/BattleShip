package pt.isel.pdm.battleship.auth

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
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
import pt.isel.pdm.battleship.service.AuthType
import pt.isel.pdm.battleship.ui.TopBar
import pt.isel.pdm.battleship.ui.theme.BattleShipTheme

@Composable
fun AuthScreen(
    onBackRequested: () -> Unit,
    authType: AuthType,
    username: String,
    onLoginTextUpdate:  (TextFieldValue) -> Unit,
    onRegisterTextUpdate: (TextFieldValue) -> Unit,
    onLoginRequested: () -> Unit,
    onRegisterRequested: () -> Unit,
    changeAuthType: (AuthType) -> Unit,
    isAuthenticating: Boolean
) {
    BattleShipTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            backgroundColor = MaterialTheme.colors.background,
            topBar = {
                TopBar(onBackRequested = onBackRequested)
            }
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
                                    value = TextFieldValue(username, TextRange(username.length)),
                                    label = {
                                        Text(text = stringResource(id = R.string.app_auth_username))
                                    },
                                    onValueChange = onLoginTextUpdate,
                                )
                            }
                            else {
                                TextField(
                                    value = TextFieldValue(username, TextRange(username.length)),
                                    label = {
                                        Text(text = stringResource(id = R.string.app_auth_username))
                                    },
                                    onValueChange = onRegisterTextUpdate
                                )
                            }
                        }
                        if (authType == AuthType.LOGIN) {
                            Button(onClick = onLoginRequested, enabled = !isAuthenticating) {
                                if (isAuthenticating)
                                    Text(text = stringResource(id = R.string.app_auth_loading) + "...")
                                else
                                    Text(text = stringResource(id = R.string.app_auth_login))
                            }
                        }
                        else {
                            Button(onClick = onRegisterRequested, enabled = !isAuthenticating) {
                                if (isAuthenticating)
                                    Text(text = stringResource(id = R.string.app_auth_loading) + "...")
                                else
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
fun LoginAuthScreenPreview() {
    BattleShipTheme {
        AuthScreen(
            onBackRequested = { Log.v("AuthScreen", "This will take you back.") },
            authType = AuthType.LOGIN,
            username = "",
            onLoginTextUpdate = { Log.v("AuthScreen", "Login Text Update") },
            onRegisterTextUpdate = { Log.v("AuthScreen", "Register Text Update") },
            onLoginRequested = { Log.v("AuthScreen", "Login Requested") },
            onRegisterRequested = { Log.v("AuthScreen", "Register Requested") },
            changeAuthType = { Log.v("AuthScreen", "Auth Type Changed") },
            false
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterAuthScreenPreview() {
    BattleShipTheme {
        AuthScreen(
            onBackRequested = { Log.v("AuthScreen", "This will take you back.") },
            authType = AuthType.REGISTER,
            username = "",
            onLoginTextUpdate = { Log.v("AuthScreen", "Login Text Update") },
            onRegisterTextUpdate = { Log.v("AuthScreen", "Register Text Update") },
            onLoginRequested = { Log.v("AuthScreen", "Login Requested") },
            onRegisterRequested = { Log.v("AuthScreen", "Register Requested") },
            changeAuthType = { Log.v("AuthScreen", "Auth Type Changed") },
            false
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingAuthScreenPreview() {
    BattleShipTheme {
        AuthScreen(
            onBackRequested = { Log.v("AuthScreen", "This will take you back.") },
            authType = AuthType.LOGIN,
            username = "",
            onLoginTextUpdate = { Log.v("AuthScreen", "Login Text Update") },
            onRegisterTextUpdate = { Log.v("AuthScreen", "Register Text Update") },
            onLoginRequested = { Log.v("AuthScreen", "Login Requested") },
            onRegisterRequested = { Log.v("AuthScreen", "Register Requested") },
            changeAuthType = { Log.v("AuthScreen", "Auth Type Changed") },
            true
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AuthScreenPreview() {
    val authType = remember { mutableStateOf(AuthType.LOGIN) }
    val isAuthenticating = remember { mutableStateOf(false) }
    val username = remember { mutableStateOf("") }
    BattleShipTheme {
        AuthScreen(
            onBackRequested = { Log.v("AuthScreen", "This will take you back.") },
            authType = authType.value,
            username = username.value,
            onLoginTextUpdate = { Log.v("AuthScreen", "Login Text Update") },
            onRegisterTextUpdate = { field -> username.value = field.text },
            onLoginRequested = { isAuthenticating.value = true },
            onRegisterRequested = { isAuthenticating.value = true },
            changeAuthType = { newAuthType -> authType.value = newAuthType },
            isAuthenticating.value
        )
    }
}