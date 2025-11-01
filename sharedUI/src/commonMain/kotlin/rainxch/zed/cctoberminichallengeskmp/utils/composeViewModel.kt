package rainxch.zed.cctoberminichallengeskmp.utils

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.reflect.KClass

@Composable
inline fun <reified VM : ViewModel> composeViewModel(
    crossinline creator: () -> VM
): VM {
    val factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
            @Suppress("UNCHECKED_CAST")
            return creator() as T
        }
    }
    return viewModel(factory = factory)
}