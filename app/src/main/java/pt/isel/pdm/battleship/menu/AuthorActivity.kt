package pt.isel.pdm.battleship.menu

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import pt.isel.pdm.battleship.R
import pt.isel.pdm.battleship.auth.AuthActivity

class AuthorActivity : ComponentActivity() {

    companion object {
        fun navigate(origin: Context) {
            val intent = Intent(origin, AuthorActivity::class.java)
            origin.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AuthorScreen (
                onBackRequested = { finish() },
                onSendEmailRequested = { launchEmailingIntent() }
            )
        }
    }

    private fun launchEmailingIntent() {
        try {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf("a48276@alunos.isel.pt"))
                putExtra(Intent.EXTRA_SUBJECT, R.string.app_author_email_subject)
            }
            startActivity(intent)
        }
        catch (e: ActivityNotFoundException) {
            Log.e("Author Activity", "Failed to send email", e)
            Toast
                .makeText(
                    this,
                    R.string.app_author_email_app_not_found,
                    Toast.LENGTH_LONG
                )
                .show()
        }
    }
}