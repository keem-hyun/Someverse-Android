package com.someverse.presentation.ui.auth.signup

import android.net.Uri

/**
 * 프로필 이미지 업로드 화면 UI 상태
 */
data class SignupProfileImageUiState(
    val selectedImageUrLs: List<Uri> = emptyList(),  // 선택된 이미지 URI 리스트 (최대 6개)
    val imageUrls: List<String> = emptyList(),  // 업로드 후 받을 이미지 URL 리스트
    val isLoading: Boolean = false,
    val canProceed: Boolean = false,
    val errorMessage: String = ""
) {
    /**
     * 다음 버튼 활성화 여부
     */
    val isNextEnabled: Boolean
        get() = selectedImageUrLs.isNotEmpty() && !isLoading
}