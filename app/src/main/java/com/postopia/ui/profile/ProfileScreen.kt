package com.postopia.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.postopia.ui.SharedViewModel
import com.postopia.ui.components.LoadingContainer
import com.postopia.ui.components.StatItem
import com.postopia.ui.components.TabSection
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    sharedViewModel : SharedViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let { message ->
            sharedViewModel.showSnackbar(message)
            viewModel.handleEvent(ProfileEvent.SnackbarMessageShown)
        }
    }

    LoadingContainer(uiState.isLoading) {
        ProfileContent(viewModel)
    }
}

@Composable
private fun ProfileContent(viewModel : ProfileViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val userDetail = uiState.userDetail
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1B))
    ) {
        item {
            // Header Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF272729))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    // Avatar and Basic Info
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(userDetail?.avatar)
                                .crossfade(true)
                                .build(),
                            contentDescription = "User Avatar",
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF4FC3F7)),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Text(
                                text = userDetail?.nickname ?: "Unknown User",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "u/${userDetail?.username}",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                            Text(
                                text = formatDate(userDetail?.createdAt ?: "0"),
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }

                    // Edit Button
                    OutlinedButton(
                        onClick = { /* TODO: Handle edit profile */ },
                        modifier = Modifier
                            .height(36.dp)
                            .padding(start = 8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.White
                        ),
                        border = ButtonDefaults.outlinedButtonBorder(enabled = true)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Edit", fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Stats Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatItem(
                        value = userDetail?.postCount.toString(),
                        label = "Posts"
                    )
                    StatItem(
                        value = userDetail?.commentCount.toString(),
                        label = "Comments"
                    )
                    StatItem(
                        value = userDetail?.credit.toString(),
                        label = "Credit"
                    )
                }

                // Introduction
                if (userDetail?.introduction.toString().isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF1A1A1B)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Text(
                                text = "About",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = userDetail?.introduction.toString(),
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }

                // Email (if shown)
                if (userDetail?.email != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Email",
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = userDetail.email.toString(),
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Tab Section (Posts, Comments, About)
        item {
            TabSection()
        }

        // Content based on selected tab would go here
        // For now, showing placeholder
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color(0xFF272729))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Content will be loaded here based on selected tab",
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}



private fun formatDate(dateString: String): String {
    return try {
        // Assuming the date format from API, adjust as needed
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        date?.let { outputFormat.format(it) } ?: dateString
    } catch (e: Exception) {
        dateString
    }
}