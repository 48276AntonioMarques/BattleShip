package pt.isel.pdm.battleship.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import pt.isel.pdm.battleship.screen.AuthorScreen

class AuthorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AuthorScreen {
                Log.v("AuthorActivity", "Getting back. I hoped...")
            }
        }
    }
}