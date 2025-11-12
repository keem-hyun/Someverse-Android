package com.someverse.presentation.ui.auth.signup

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.someverse.presentation.R
import com.someverse.presentation.components.CityList
import com.someverse.presentation.components.DistrictList
import com.someverse.presentation.components.GradientButton
import com.someverse.presentation.components.LocationChip
import com.someverse.presentation.ui.theme.*

/**
 * 위치 정보 입력 화면
 */
@Composable
fun SignupLocationScreen(
    onNext: () -> Unit,
    viewModel: SignupLocationViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // 진행할 수 있는지에 대한 상태가 변경될 때마다 LaunchedEffect가 실행됩니다.
    LaunchedEffect(uiState.canProceed) {
        println("📦 LaunchedEffect 실행 (canProceed=${uiState.canProceed})")
        if (uiState.canProceed) {
            println("📦 선택한 지역 저장 성공! -> 다음 화면으로 이동")
            onNext()
            println("📝 canProceed 상태 초기화")
            viewModel.resetProceedState()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(Dimensions.screenPadding),
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.height(Dimensions.space12))

        Text(
            text = "회원가입",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                lineHeight = 32.sp,
                textAlign = TextAlign.Center,
                fontFamily = PretendardFontFamily
            ).withLetterSpacingPercent(-2.5f),
            color = DescGray,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "주 활동 지역을 알려주세요.",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 21.sp,
                fontFamily = PretendardFontFamily
            ).withLineHeightPercent(150f).withLetterSpacingPercent(-2.5f),
            textAlign = TextAlign.Start,
            color = Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimensions.space16)
        )

        Spacer(modifier = Modifier.height(Dimensions.space4))

        Text(
            text = "최대 2개 지역까지 선택할 수 있어요!",
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Normal,
                lineHeight = 22.sp,
                fontFamily = PretendardFontFamily
            ).withLetterSpacingPercent(-2.5f),
            textAlign = TextAlign.Start,
            color = DescGray,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimensions.space16)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 드롭다운 선택
        LocationSelectionSection(
            expanded = uiState.isDropdownExpanded,
            onExpandedChange = { viewModel.toggleDropdown() },
            regions = uiState.regions,
            districts = uiState.selectedCity?.let { uiState.districtMap[it] } ?: emptyList(),
            onCitySelected = { viewModel.selectItem(it) },
            onDistrictSelected = { viewModel.selectItem(it) },
            selectedCity = uiState.selectedCity,
            selectionStep = uiState.selectionStep
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 선택된 지역 표시 - 칩 형태
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            uiState.selectedLocations.forEach { selection ->
                LocationChip(
                    city = selection.city,
                    district = selection.district,
                    onRemove = { viewModel.removeLocation(selection) }
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // 페이지 인디케이터
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(5) { index ->
                PageIndicator(isActive = index == 3)
                Spacer(modifier = Modifier.width(8.dp))
            }
        }

        // 다음 버튼 (그라데이션 배경)
        GradientButton(
            text = "선택했어요!",
            onClick = {
                println("📦 '선택했어요!' 버튼 클릭 -> 위치 정보 제출")
                viewModel.submitLocation()
            },
            enabled = uiState.selectedLocations.isNotEmpty() && !uiState.isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = Dimensions.space8)
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}

/**
 * 위치 선택 섹션 - 도시와 구/군 선택 영역을 포함
 */
@Composable
fun LocationSelectionSection(
    expanded: Boolean,
    onExpandedChange: () -> Unit,
    regions: List<String>,
    districts: List<String>,
    onCitySelected: (String) -> Unit,
    onDistrictSelected: (String) -> Unit,
    selectedCity: String?,
    selectionStep: SelectionStep
) {
    val arrowRotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = "arrow rotation"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimensions.screenPadding)
    ) {
        // 드롭다운 헤더 - 항상 "지역을 검색하세요" 표시
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .zIndex(1f)
                .clip(RoundedCornerShape(16.dp))
                .height(56.dp)
                .background(Color(0xFFEBEFF5))
                .clickable(onClick = onExpandedChange),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimensions.space12),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "지역을 검색하세요.",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color(0xFF9098A6)
                )

                Icon(
                    painter = painterResource(id = R.drawable.ic_triangle_down),
                    contentDescription = "Toggle dropdown",
                    modifier = Modifier.rotate(arrowRotation),
                    tint = Color(0xFF9098A6)
                )
            }
        }

        // 드롭다운 콘텐츠
        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically(),
            exit = shrinkVertically(),
            modifier = Modifier.offset(y = (-16).dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 240.dp)
                    .border(
                        width = 1.dp,
                        color = Color(0xFFEBEFF5).copy(alpha = 0.5f)
                    )
                    .background(Color(0xFFEBEFF5).copy(alpha = 0.5f))
            ) {
                // 도시 및 구/군 선택 영역을 Row로 배치
                Row(modifier = Modifier.fillMaxWidth()) {
                    // 도시 목록 (항상 표시)
                    if (selectionStep == SelectionStep.DISTRICT) {
                        // 도시 선택 완료 - 선택된 도시 표시
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .background(Color(0xFFF5F5F5))
                                .padding(vertical = 24.dp, horizontal = 16.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(
                                text = selectedCity ?: "",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    fontFamily = PretendardFontFamily,
                                    lineHeight = 22.sp
                                ).withLetterSpacingPercent(-2.5f),
                                color = PrimaryPurple
                            )
                        }
                    } else {
                        // 도시 목록
                        CityList(
                            cities = regions,
                            selectedCity = selectedCity,
                            onCitySelected = onCitySelected,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // 세로 구분선
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(1.dp)
                            .background(Color(0xFFE4E8EF))
                    )

                    // 구/군 목록 (도시가 선택된 경우에만 표시)
                    Box(
                        modifier = Modifier.weight(1f)
                    ) {
                        androidx.compose.animation.AnimatedVisibility(
                            visible = selectionStep == SelectionStep.DISTRICT
                        ) {
                            Column {

                                // 구/군 목록
                                DistrictList(
                                    districts = districts,
                                    onDistrictSelected = onDistrictSelected
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PageIndicator(isActive: Boolean) {
    Box(
        modifier = Modifier
            .size(width = 8.dp, height = 8.dp)
            .clip(CircleShape)
            .background(
                if (isActive) PrimaryPurple else Color(0xFFE4E8EF)
            )
    )
}