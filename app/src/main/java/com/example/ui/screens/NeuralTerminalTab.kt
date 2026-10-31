package com.example.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.viewmodel.KarenOSViewModel
import com.example.viewmodel.KarenOsUiState

/**
 * NeuralTerminalTab serves as the primary host for the Centralized AI Chat component
 * connecting users to the Shadow Supporter (RAVI) assistant with Gemini API integration
 * across all hubs (Study Hub, Arduino Studio, Music Room, and Command Center).
 */
@Composable
fun NeuralTerminalTab(
    viewModel: KarenOSViewModel,
    uiState: KarenOsUiState,
    modifier: Modifier = Modifier
) {
    CentralizedAiChatComponent(
        viewModel = viewModel,
        uiState = uiState,
        modifier = modifier
    )
}
