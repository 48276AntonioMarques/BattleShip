package pt.isel.pdm.battleship.menu

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.isel.pdm.battleship.common.Leaderboard
import pt.isel.pdm.battleship.service.GeneralService

class MenuViewModel(
    private val generalService: GeneralService
): ViewModel() {

    private val _leaderboard = mutableStateOf<Leaderboard?>(null)
    val leaderboard: State<Leaderboard?>
        get() = _leaderboard

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean>
        get() = _isLoading

    fun fetchLeaderboard() {
        viewModelScope.launch {
            _leaderboard.value = generalService.getLeaderboard()
        }
    }
}