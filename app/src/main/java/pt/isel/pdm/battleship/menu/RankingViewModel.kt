package pt.isel.pdm.battleship.menu

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.isel.pdm.battleship.R
import pt.isel.pdm.battleship.common.Leaderboard
import pt.isel.pdm.battleship.service.GeneralService
import java.lang.Exception

class RankingViewModel(
    private val generalService: GeneralService
): ViewModel() {

    private val _leaderboard = mutableStateOf<Leaderboard?>(null)
    val leaderboard: State<Leaderboard?>
        get() = _leaderboard

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean>
        get() = _isLoading

    fun fetchLeaderboard(context: Context) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _leaderboard.value = generalService.getLeaderboard()
            }
            catch (e: Exception) {
                Log.e("RankingViewModel", "${e.message}\n${e.localizedMessage}")
                Toast.makeText(context, context.getString(R.string.app_rank_error), Toast.LENGTH_LONG).show()
                throw e
            }
            finally {
                _isLoading.value = false
            }
        }
    }
}