package com.system.design.ui.main


import android.widget.ImageView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.edit
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.system.design.R

/* -----------------------------
   Data Model
-------------------------------- */

data class OnboardingPage(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val rawResId: Int? = null
)

/* -----------------------------
   Static Onboarding Content
-------------------------------- */

private val onboardingPages = listOf(
    OnboardingPage(
        title = "Schedule quest and observe your growth",
        description = "View patient records, history, and reports in one place.",
        icon = Icons.Filled.Person,
        rawResId = R.raw.first
    ),
    OnboardingPage(
        title = "Smart Appointments",
        description = "Track schedules and manage consultations efficiently.",
        icon = Icons.Filled.Person,
        rawResId = R.raw.second
    ),
    OnboardingPage(
        title = "Secure & Reliable",
        description = "All medical data is encrypted and securely stored.",
        icon = Icons.Filled.Person,
        rawResId = R.raw.third
    )
)

/* -----------------------------
   Main Onboarding Screen
-------------------------------- */

@Composable
fun OnboardingScreen(
    onFinished: () -> Unit
) {
    val context = LocalContext.current
    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = false // ❗ white icons
        )
    }

    var currentPage by rememberSaveable { mutableIntStateOf(0) }
    val scrollState = rememberScrollState()

    Box {
        Column(
            modifier = Modifier
                .background(color = Color(0xFF0E3A3A))
                .fillMaxSize()
                .padding(15.dp)
        ) {

            // Scrollable content area (takes remaining space)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                OnboardingPageItem(
                    page = onboardingPages[currentPage]
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                // Previous button
                TextButton(
                    onClick = {
                        if (currentPage > 0) currentPage--
                    }
                ) {
                    Text(
                        text = if (currentPage == 0) "" else "Previous",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 15.sp),
                    )
                }

                // Next / Get Started
                Button(
                    onClick = {
                        if (currentPage == onboardingPages.lastIndex) {
                            onFinished()
                        } else {
                            currentPage++
                        }
                    }
                ) {
                    Text(
                        text = if (currentPage == onboardingPages.lastIndex) "Get Started" else "Next",
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 15.sp),
                    )
                }
            }
        }
    }
}

/* -----------------------------
   Onboarding Page UI
-------------------------------- */

@Composable
private fun OnboardingPageItem(
    page: OnboardingPage
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 0.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // If the page has a raw resource, show a GIF from raw resources
        if (page.rawResId != null) {
            GifView(rawResId = page.rawResId)
        } else {
            Icon(
                imageVector = page.icon,
                contentDescription = "onBoarding Icon",
                modifier = Modifier.size(220.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = page.title,
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Text(
            text = "Organize your goals, stay consistent, and grow step by step.\n",
            style = MaterialTheme.typography.headlineSmall.copy(fontSize = 15.sp),
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 1.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

    }
}

@Composable
private fun GifView(rawResId: Int, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    // Build the raw resource Uri using KTX toUri
    val gifUri = "android.resource://${context.packageName}/$rawResId".toUri()

    AndroidView(factory = { ctx ->
        ImageView(ctx).apply {
            layoutParams = android.view.ViewGroup.LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT
            )
            scaleType = ImageView.ScaleType.FIT_CENTER
        }
    }, update = { imageView ->
        Glide.with(imageView.context)
            .asGif()
            .load(gifUri)
            .into(imageView)
    }, modifier = modifier.fillMaxWidth().height(750.dp))
}

/* -----------------------------
   Persistence Helpers
-------------------------------- */


