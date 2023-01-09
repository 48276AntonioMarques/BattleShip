package pt.isel.pdm.battleship.lobby

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import pt.isel.pdm.battleship.DependenciesContainer
import pt.isel.pdm.battleship.common.*
import pt.isel.pdm.battleship.menu.MenuActivity

class InvitesActivity : KotlinActivity() {

    private val ls by lazy { (application as DependenciesContainer).lobbyService }
    private val lvm by viewModels { InvitesViewModel(ls) }

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
        if (user == null) { finish() }

        if (lvm.invites.value == null && user != null) {
            lvm.setCredentials(user)
            lvm.fetchInvites(this)
        }
        setContent {
            InvitesScreen(
                onBackRequested = { finish() },
                invites = lvm.invites.value,
                isLoading = lvm.isLoading.value,
                onAccept = { invite -> lvm.accept(this, invite) },
                onDodge = {invite -> lvm.dodge(this, invite) }
            )
        }
    }
}