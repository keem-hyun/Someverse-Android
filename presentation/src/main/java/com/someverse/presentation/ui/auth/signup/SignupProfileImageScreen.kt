package com.someverse.presentation.ui.auth.signup

import android.Manifest
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.someverse.presentation.R
import com.someverse.presentation.components.GradientButton
import com.someverse.presentation.ui.theme.*
import java.io.File

/**
 * 프로필 이미지 업로드 화면
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupProfileImageScreen(
    onNext: () -> Unit,
    viewModel: SignupProfileImageViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // 바텀 시트 상태
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    // 카메라로 찍은 이미지 URI
    val capturedImageUri = remember { mutableStateOf<Uri?>(null) }

    // 이미지 업로드 완료 시 다음 화면으로 이동
    LaunchedEffect(uiState.canProceed) {
        println("📝 LaunchedEffect 실행 (canProceed=${uiState.canProceed})")
        if (uiState.canProceed) {
            println("🚀 화면 이동 시작 - onNext() 함수 호출")
            onNext()
            println("🔄 canProceed 상태 초기화")
            viewModel.resetProceedState()
        }
    }

    // 갤러리 이미지 선택 런처
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            println("📷 이미지 선택됨: $it")
            viewModel.addImage(it)
        }
        showBottomSheet = false
    }

    // 카메라 촬영 런처
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            val uri = capturedImageUri.value
            if (uri != null) {
                println("📷 카메라로 촬영됨: $uri")
                viewModel.addImage(uri)
            }
        }
        showBottomSheet = false
    }

    // 카메라 권한 요청 런처
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // 권한이 승인되면 카메라 실행
            val photoFile = File.createTempFile(
                "profile_${System.currentTimeMillis()}",
                ".jpg",
                context.cacheDir
            )
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                photoFile
            )
            capturedImageUri.value = uri
            cameraLauncher.launch(uri)
        } else {
            // 권한 거부 시 바텀 시트 닫기
            showBottomSheet = false
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

        // 상단 타이틀
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

        // 제목
        Text(
            text = "프로필 사진을 업로드해주세요.",
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

        // 설명
        Text(
            text = "최대 6장까지 등록할 수 있어요.",
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

        Spacer(modifier = Modifier.height(24.dp))

        // 이미지 그리드 (3열)
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimensions.space16)
        ) {
            items(6) { index ->
                ProfileImageSlot(
                    index = index,
                    imageUri = uiState.selectedImageUrLs.getOrNull(index),
                    isRequired = index == 0,
                    onAddClick = {
                        showBottomSheet = true
                    },
                    onRemoveClick = { viewModel.removeImage(index) }
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // 오류 메시지
        if (uiState.errorMessage.isNotEmpty()) {
            Text(
                text = uiState.errorMessage,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
        }

        // 페이지 인디케이터
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(5) { index ->
                Box(
                    modifier = Modifier
                        .size(width = 8.dp, height = 8.dp)
                        .clip(CircleShape)
                        .background(
                            if (index == 4) PrimaryPurple else Color(0xFFE4E8EF)
                        )
                )
                if (index < 4) Spacer(modifier = Modifier.width(8.dp))
            }
        }

        // 다음 버튼
        GradientButton(
            text = "선택했어요!",
            onClick = {
                println("📦 '선택했어요!' 버튼 클릭 -> 프로필 이미지 업로드")
                viewModel.uploadProfileImages()
            },
            enabled = uiState.isNextEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = Dimensions.space8)
        )

        Spacer(modifier = Modifier.height(32.dp))
    }

    // 이미지 선택 바텀 시트
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            containerColor = Color.White,
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
        ) {
            ImagePickerBottomSheet(
                onCameraClick = {
                    // 카메라 권한 확인
                    val permission = Manifest.permission.CAMERA
                    if (context.checkSelfPermission(permission) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                        // 권한이 있으면 카메라 실행
                        val photoFile = File.createTempFile(
                            "profile_${System.currentTimeMillis()}",
                            ".jpg",
                            context.cacheDir
                        )
                        val uri = FileProvider.getUriForFile(
                            context,
                            "${context.packageName}.fileprovider",
                            photoFile
                        )
                        capturedImageUri.value = uri
                        cameraLauncher.launch(uri)
                    } else {
                        // 권한이 없으면 권한 요청
                        cameraPermissionLauncher.launch(permission)
                    }
                },
                onGalleryClick = {
                    imagePickerLauncher.launch("image/*")
                }
            )
        }
    }
}

/**
 * 프로필 이미지 슬롯 - 이미지 추가/표시/삭제
 */
@Composable
fun ProfileImageSlot(
    index: Int,
    imageUri: Uri?,
    isRequired: Boolean,
    onAddClick: () -> Unit,
    onRemoveClick: () -> Unit
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .then(
                if (index == 0) {
                    Modifier.border(
                        width = 2.dp,
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color(0xFF7451C9), Color(0xFFFD71A6))
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                } else {
                    Modifier
                }
            )
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF5F7FA))
            .then(
                if (imageUri == null) {
                    Modifier.clickable(onClick = onAddClick)
                } else {
                    Modifier
                }
            )
    ) {
        if (imageUri != null) {
            // 선택된 이미지 표시
            val bitmap = remember(imageUri) {
                try {
                    context.contentResolver.openInputStream(imageUri)?.use {
                        BitmapFactory.decodeStream(it)
                    }
                } catch (e: Exception) {
                    null
                }
            }

            bitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "Profile image $index",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            // 삭제 버튼
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable(onClick = onRemoveClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_cancel_circle),
                    contentDescription = "Remove image",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        } else {
            // 빈 슬롯 - 플러스 표시
            Icon(
                painter = painterResource(id = R.drawable.ic_add_image),
                contentDescription = "Add image",
                tint = Color(0xFFEBEFF5),
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(28.dp)
            )
        }

        // 필수 태그 (첫 번째 이미지)
        if (isRequired && imageUri == null) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color(0xFF8D66FA), Color(0xFFF48FB1))
                        ),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "필수",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 11.sp,
                        fontFamily = PretendardFontFamily
                    ),
                    color = Color.White
                )
            }
        }
    }
}

/**
 * 이미지 선택 바텀 시트 내용
 */
@Composable
fun ImagePickerBottomSheet(
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp)
    ) {
        // 촬영하기
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onCameraClick)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_camera),
                contentDescription = "Camera",
                tint = Color(0xFF6C7580),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "촬영하기",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    fontFamily = PretendardFontFamily
                ),
                color = Color(0xFF1A1D1F)
            )
        }

        // 구분선
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 24.dp),
            thickness = 1.dp,
            color = Color(0xFFEBEFF5)
        )

        // 앨범에서 선택하기
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onGalleryClick)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_album),
                contentDescription = "Album",
                tint = Color(0xFF6C7580),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "앨범에서 선택하기",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    fontFamily = PretendardFontFamily
                ),
                color = Color(0xFF1A1D1F)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}