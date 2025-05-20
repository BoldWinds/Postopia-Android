package com.postopia.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.postopia.ui.SharedViewModel
import com.postopia.ui.components.ArrowBack
import com.postopia.ui.components.AuthButton
import com.postopia.ui.components.PasswordTextField
import com.postopia.ui.components.UsernameTextField

@Composable
fun AuthScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel,
    navigateBack : () -> Unit,
){
    val uiState by viewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current
    val colorScheme = MaterialTheme.colorScheme

    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let { message ->
            sharedViewModel.showSnackbar(message)
            viewModel.handleEvent(AuthEvent.SnackbarMessageShown)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 左上角回退键
            ArrowBack { navigateBack() }
            // Logo和标题
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = "Postopia",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = colorScheme.primary
            )

            Spacer(modifier = Modifier.height(30.dp))

            // 用户名输入框
            UsernameTextField(
                value = uiState.username,
                onValueChange = { viewModel.handleEvent(AuthEvent.UsernameChanged(it)) },
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 密码输入框
            PasswordTextField(
                password = uiState.password,
                onPasswordChanged = { viewModel.handleEvent(AuthEvent.PasswordChanged(it)) },
                label = "密码",
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 确认密码输入框
            if(uiState.isRegister) {
                PasswordTextField(
                    password = uiState.confirmPassword,
                    onPasswordChanged = { viewModel.handleEvent(AuthEvent.ConfirmPasswordChanged(it)) },
                    label = "确认密码",
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // 认证按钮
            AuthButton(
                text = if(uiState.isRegister) "注册" else "登陆",
                isLoading = uiState.isLoading,
                onClick = {
                    if(uiState.isRegister){
                        viewModel.handleEvent(AuthEvent.Register)
                    }else{
                        viewModel.handleEvent(AuthEvent.Login)
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 条款和政策信息
            Text(
                text = if(uiState.isRegister) "注册即表示您同意我们的服务条款和隐私政策" else "登陆即表示您同意我们的服务条款和隐私政策",
                fontSize = 12.sp,
                color = colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = if(uiState.isRegister) "已有账户? "  else "没有账户? ",
                    color = colorScheme.onBackground
                )
                Text(
                    text = if(uiState.isRegister) "登录" else "注册",
                    color = colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        viewModel.handleEvent(AuthEvent.ChangeAuth)
                    }
                )
            }
        }
    }
}
