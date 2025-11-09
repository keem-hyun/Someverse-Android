package com.someverse.presentation.ui.auth.login

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.someverse.presentation.R
import com.someverse.presentation.ui.components.KakaoLoginButton
import com.someverse.presentation.ui.theme.Dimensions
import com.someverse.presentation.ui.theme.PrimaryPurple
import com.someverse.presentation.ui.theme.SomeverseTheme
import com.someverse.presentation.ui.theme.White
import com.someverse.presentation.ui.theme.Black
import com.someverse.presentation.ui.theme.DescGray

private const val TAG = "LoginScreen"

/**
 * Login Screen with Social Login options
 */
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Navigate on login success
    LaunchedEffect(uiState.isLoginSuccess) {
        if (uiState.isLoginSuccess) {
            onLoginSuccess()
            viewModel.resetLoginState()
        }
    }

    // Show error dialog
    uiState.error?.let { error ->
        AlertDialog(
            onDismissRequest = { viewModel.clearError() },
            title = { Text("Login Failed") },
            text = { Text(error) },
            confirmButton = {
                TextButton(onClick = { viewModel.clearError() }) {
                    Text("OK")
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        // Main login UI
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimensions.screenPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo
            LogoSection()

            Spacer(modifier = Modifier.height(48.dp))

            // Heading Text
            Text(
                text = "당신의 취향이\n연결의 시작",
                style = MaterialTheme.typography.displayLarge,
                textAlign = TextAlign.Center,
                color = Black
            )

            Spacer(modifier = Modifier.height(Dimensions.space12))

            // Subtitle
            Text(
                text = "콘텐츠 취향을 함께 나눌 친구를 만나세요.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = DescGray
            )

            Spacer(modifier = Modifier.weight(1f))

            // Login Buttons
            KakaoLoginButton(
                onClick = {
                    Log.d(TAG, "Kakao login clicked")
                    viewModel.mockLogin()
                }
            )

            Spacer(modifier = Modifier.height(Dimensions.space64))

            // Terms and Conditions
            Text(
                text = "시작하면\n서비스 이용약관, 개인정보 처리방침, 위치정보, 이용약관에\n동의하게 됩니다.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = Color(0xFF9BA3B1)
            )

            TextButton(
                onClick = { /* TODO: Open business info */ },
                modifier = Modifier.padding(bottom = Dimensions.space16)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "사업자 정보",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF9BA3B1)
                    )

                    Icon(
                        painter = painterResource(id = R.drawable.ic_triangle_right),
                        contentDescription = "Arrow Right",
                        tint = Color(0xFF9BA3B1),
                        modifier = Modifier.size(Dimensions.iconSizeSmall)
                    )
                }
            }
        }

        // Loading indicator
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.8f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
private fun LogoSection() {
    Image(
        painter = painterResource(id = R.drawable.ic_login_logo),
        contentDescription = "Someverse Logo",
        modifier = Modifier.size(Dimensions.logoSize)
    )

    Spacer(modifier = Modifier.height(Dimensions.space16))

    // Logo text
    Text(
        text = "SOMEVERSE",
        style = MaterialTheme.typography.titleSmall,
        color = PrimaryPurple
    )
}
