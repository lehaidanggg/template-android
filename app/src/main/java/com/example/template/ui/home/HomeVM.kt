package com.example.template.ui.home

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.template.base.viewmodel.BaseViewModel
import com.example.template.base.viewmodel.safeLaunch
import com.example.template.common.resource.ResourcesProvider
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

class HomeVM @Inject constructor(
    private val resProvider: ResourcesProvider
): BaseViewModel() {
    private val _onEvent = Channel<Event>(Channel.BUFFERED)
    val onEvent = _onEvent.receiveAsFlow()

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asLiveData()


    fun openSetting() {
        viewModelScope.safeLaunch {
            _onEvent.send(Event.OpenSetting)
        }
    }


    data class  UiState(
        val titleButton: String = ""
    )

    sealed class Event {
        object OpenSetting: Event()
        data class OpenDialogResult(val countFile: Int) : Event()
    }

}