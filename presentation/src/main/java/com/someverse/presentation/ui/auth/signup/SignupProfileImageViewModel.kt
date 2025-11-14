package com.someverse.presentation.ui.auth.signup

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.someverse.domain.usecase.onboarding.SubmitProfileImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

/**
 * 프로필 이미지 업로드 화면 ViewModel
 */
@HiltViewModel
class SignupProfileImageViewModel @Inject constructor(
    private val submitProfileImageUseCase: SubmitProfileImageUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignupProfileImageUiState())
    val uiState: StateFlow<SignupProfileImageUiState> = _uiState.asStateFlow()

    companion object {
        private const val MAX_IMAGE_COUNT = 6
    }

    // 이미지 추가
    fun addImage(uri: Uri) {
        val currentState = _uiState.value
        if (currentState.selectedImageUrLs.size >= MAX_IMAGE_COUNT) {
            _uiState.update { it.copy(errorMessage = "최대 ${MAX_IMAGE_COUNT}장까지만 선택할 수 있습니다.") }
            return
        }

        val updatedUris = currentState.selectedImageUrLs + uri
        _uiState.update { it.copy(selectedImageUrLs = updatedUris, errorMessage = "") }
    }

    // 이미지 제거
    fun removeImage(index: Int) {
        val currentState = _uiState.value
        val updatedUris = currentState.selectedImageUrLs.toMutableList()
        if (index in updatedUris.indices) {
            updatedUris.removeAt(index)
            _uiState.update { it.copy(selectedImageUrLs = updatedUris, errorMessage = "") }
        }
    }

    // 프로필 이미지 업로드
    fun uploadProfileImages() {
        val currentState = _uiState.value
        if (currentState.selectedImageUrLs.isEmpty()) {
            _uiState.update { it.copy(errorMessage = "최소 1장의 이미지를 선택해주세요.") }
            return
        }

        _uiState.update { it.copy(isLoading = true, errorMessage = "") }
        println("📤 프로필 이미지 업로드 시작 (${currentState.selectedImageUrLs.size}장)")

        viewModelScope.launch {
            try {
                // 임시 구현 - MockUseCase 사용 (파일 객체는 사실상 무시됨)
                // 실제 구현에서는 첫 번째 이미지만 업로드하거나 모든 이미지를 업로드
                val file = File("") // 빈 파일 - mockUseCase에서는 사용되지 않음

                submitProfileImageUseCase(file).onSuccess { user ->
                    println("✅ 프로필 이미지 업로드 성공 - 화면 이동 시킨 [canProceed=true]")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            canProceed = true,
                            errorMessage = ""
                        )
                    }
                }.onFailure { exception ->
                    println("❌ 프로필 이미지 업로드 실패: ${exception.message}")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = exception.message ?: "이미지 업로드에 실패했습니다."
                        )
                    }
                }
            } catch (e: Exception) {
                println("❌ 프로필 이미지 업로드 예외 발생: ${e.message}")
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "이미지 업로드 중 오류가 발생했습니다."
                    )
                }
            }
        }
    }

    // 추가 작업 후 canProceed 상태 초기화
    fun resetProceedState() {
        _uiState.update { it.copy(canProceed = false) }
        println("🔄 canProceed 상태 초기화 (false)")
    }
}