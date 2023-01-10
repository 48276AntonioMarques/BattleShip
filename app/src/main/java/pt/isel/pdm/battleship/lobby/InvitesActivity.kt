package pt.isel.pdm.battleship.lobby

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import pt.isel.pdm.battleship.DependenciesContainer
import pt.isel.pdm.battleship.common.*

class InvitesActivity : KotlinActivity() {

    private val ls by lazy { (application as DependenciesContainer).lobbyService }
    private val ivm by viewModels { InvitesViewModel(ls) }

    companion object {
        const val USER_EXTRA = "USER_EXTRA"
        fun navigate(origin: Activity, user: User? = null) {
            val intent = Intent(origin, InvitesActivity::class.java)
            if (user != null) intent.putExtra(USER_EXTRA, user.toLocalUserDto())
            origin.startActivity(intent)
        }
    }

    private val user: LocalUserDto?
        get() = intent.getParcelableExtra(USER_EXTRA)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val user = user?.toUser()
        if (user == null) { finish(); return }

        if (ivm.invites.value == null) loadInvites(user)
        setContent {
            val accepted = ivm.accepted.value
            if (accepted != null) {
                // Clear state should be call every time you exit without finishing the activity
                // This prevents errors based on the hold state which must be considered
                // invalid when returning to the activity
                ivm.clearState()
                LobbyActivity.navigate(this, user, accepted)
            }
            InvitesScreen(
                onBackRequested = { finish() },
                invites = ivm.invites.value,
                isLoading = ivm.isLoading.value,
                onAccept = { invite -> ivm.accept(this, invite) },
                onDodge = {invite -> ivm.dodge(this, invite) }
            )
        }
    }

    override fun onResume() {
        super.onResume()
        Log.v("InvitesActivity", "Resuming")
        val user = user?.toUser()
        if (user == null) { finish(); return }

        if (ivm.invites.value == null) loadInvites(user)
    }

    private fun loadInvites(user: User) {
        if (ivm.invites.value == null) {
            ivm.setCredentials(user)
            ivm.subscribeInvitesList(this)
        }
    }
}