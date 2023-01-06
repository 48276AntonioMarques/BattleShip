package pt.isel.pdm.battleship.activity

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.res.stringResource
import pt.isel.pdm.battleship.R
import pt.isel.pdm.battleship.screen.AuthorScreen

class AuthorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AuthorScreen (
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