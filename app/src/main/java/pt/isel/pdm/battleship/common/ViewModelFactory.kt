package pt.isel.pdm.battleship.common

import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider

open class KotlinActivity : ComponentActivity() {
    @Suppress("UNCHECKED_CAST")
    inline fun <reified VM : ViewModel> ComponentActivity.viewModels(noinline block: () -> VM): Lazy<VM>  {
        val factoryPromise: () -> ViewModelProvider.Factory = {
            object : ViewModelProvider.Factory {
                override fun <VM : ViewModel> create(modelClass: Class<VM>): VM {
                    return block() as VM
                }
            }
        }
        return ViewModelLazy(VM::class, { viewModelStore }, factoryPromise)
    }
}
