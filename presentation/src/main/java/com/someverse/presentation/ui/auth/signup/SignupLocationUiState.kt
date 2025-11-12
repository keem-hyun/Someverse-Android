package com.someverse.presentation.ui.auth.signup

import com.someverse.domain.model.Location

/**
 * 위치 정보 입력 화면 UI 상태
 */
data class SignupLocationUiState(
    val selectedLocations: List<Location> = emptyList(),
    val isDropdownExpanded: Boolean = false,
    val selectedCity: String? = null,  // Currently selected city
    val selectionStep: SelectionStep = SelectionStep.CITY, // Current selection step

    // 주요 지역 목록 (시/도)
    val regions: List<String> = emptyList(),

    // 지역별 세부 구/군 정보
    val districtMap: Map<String, List<String>> = emptyMap(),

    val isLoading: Boolean = false,
    val canProceed: Boolean = false,
    val errorMessage: String = ""
) {
    // 최대 선택 가능 개수 (2개)
    val maxLocationSelection: Int = 2
}

/**
 * 위치 선택 단계
 */
enum class SelectionStep {
    CITY,       // 시/도 선택 단계
    DISTRICT    // 구/군 선택 단계
}