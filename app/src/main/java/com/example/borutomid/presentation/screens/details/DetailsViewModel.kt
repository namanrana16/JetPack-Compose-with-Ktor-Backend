package com.example.borutomid.presentation.screens.details

import android.content.Context
import android.provider.CalendarContract
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.borutomid.domain.model.Hero
import com.example.borutomid.domain.use_cases.UseCases
import com.example.borutomid.util.Constants.DETAILS_ARGUMENT_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val useCases:UseCases,savedStateHandle:SavedStateHandle)
    :ViewModel() {

    private val _selectedHero: MutableStateFlow<Hero?> = MutableStateFlow(null)
    val selectedHero: StateFlow<Hero?> = _selectedHero



        init {

            viewModelScope.launch (Dispatchers.IO){
                val heroId=savedStateHandle.get<Int>(DETAILS_ARGUMENT_KEY)

                _selectedHero.value=heroId?.let { useCases.getSelectedHeroUseCase(heroId = heroId) }

                _selectedHero.value?.name?.let { Log.d("Hero Name",it) }
            }
        }


    private val _uiEvent= MutableSharedFlow<UiEvent>()
    val uiEvent:SharedFlow<UiEvent> = _uiEvent.asSharedFlow()

    private val _colorPalette:MutableState<Map<String,String>> = mutableStateOf(mapOf())
    val colorPalette: State<Map<String,String>> = _colorPalette

    fun generateColorPalette()
    {
        viewModelScope.launch {
            _uiEvent.emit( UiEvent.GenerateColorPalette )
        }
    }


    fun     setColorPalette(colors: Map<String,String>)
    {

        _colorPalette.value=colors
    }


}


sealed class UiEvent{
    object GenerateColorPalette:UiEvent()
}