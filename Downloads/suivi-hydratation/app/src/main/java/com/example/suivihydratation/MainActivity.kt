package com.example.suivihydratation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.suivihydratation.ui.WaterViewModel
import com.example.ui.theme.*

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        var selectedTab by remember { mutableStateOf(0) }

        Scaffold(
          modifier = Modifier
            .fillMaxSize()
            .testTag("app_scaffold"),
          topBar = {
            ElegantTopBar()
          },
          bottomBar = {
            ElegantBottomNavigation(
              selectedTab = selectedTab,
              onTabSelected = { selectedTab = it }
            )
          },
          contentWindowInsets = WindowInsets.safeDrawing
        ) { innerPadding ->
          HydrationTrackerScreen(
            modifier = Modifier.padding(innerPadding)
          )
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ElegantTopBar() {
  TopAppBar(
    title = {
      Text(
        text = "Hydratation",
        style = MaterialTheme.typography.titleLarge.copy(
          fontWeight = FontWeight.Normal,
          color = OnDarkBackground
        )
      )
    },
    navigationIcon = {
      IconButton(
        onClick = { /* Menu action */ },
        modifier = Modifier.size(48.dp)
      ) {
        Icon(
          imageVector = Icons.Rounded.Menu,
          contentDescription = "Menu",
          tint = OnDarkBackground
        )
      }
    },
    actions = {
      // User Profile Indicator with a gorgeous turquoise gradient ring
      Box(
        modifier = Modifier
          .padding(end = 16.dp)
          .size(40.dp)
          .clip(CircleShape)
          .background(DarkSurfaceVariant)
          .border(
            width = 2.dp,
            brush = Brush.linearGradient(listOf(TurquoisePrimary, TurquoiseSecondary)),
            shape = CircleShape
          )
          .padding(4.dp)
      ) {
        Box(
          modifier = Modifier
            .fillMaxSize()
            .clip(CircleShape)
            .background(Brush.linearGradient(listOf(TurquoisePrimary, TurquoiseSecondary)))
        )
      }
    },
    colors = TopAppBarDefaults.topAppBarColors(
      containerColor = DarkBackground,
      titleContentColor = OnDarkBackground
    )
  )
}

@Composable
fun ElegantBottomNavigation(
  selectedTab: Int,
  onTabSelected: (Int) -> Unit,
  modifier: Modifier = Modifier
) {
  NavigationBar(
    containerColor = DarkSurface,
    tonalElevation = 8.dp,
    modifier = modifier.windowInsetsPadding(WindowInsets.navigationBars)
  ) {
    NavigationBarItem(
      selected = selectedTab == 0,
      onClick = { onTabSelected(0) },
      icon = {
        Icon(
          imageVector = Icons.Rounded.Home,
          contentDescription = "Accueil"
        )
      },
      label = { Text("Accueil") },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = ActivePillText,
        selectedTextColor = OnDarkBackground,
        indicatorColor = ActivePillBg,
        unselectedIconColor = TextSecondary,
        unselectedTextColor = TextSecondary
      )
    )
    NavigationBarItem(
      selected = selectedTab == 1,
      onClick = { onTabSelected(1) },
      icon = {
        Icon(
          imageVector = Icons.Rounded.BarChart,
          contentDescription = "Stats"
        )
      },
      label = { Text("Stats") },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = ActivePillText,
        selectedTextColor = OnDarkBackground,
        indicatorColor = ActivePillBg,
        unselectedIconColor = TextSecondary,
        unselectedTextColor = TextSecondary
      )
    )
    NavigationBarItem(
      selected = selectedTab == 2,
      onClick = { onTabSelected(2) },
      icon = {
        Icon(
          imageVector = Icons.Rounded.Person,
          contentDescription = "Profil"
        )
      },
      label = { Text("Profil") },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = ActivePillText,
        selectedTextColor = OnDarkBackground,
        indicatorColor = ActivePillBg,
        unselectedIconColor = TextSecondary,
        unselectedTextColor = TextSecondary
      )
    )
  }
}

@Composable
fun HydrationTrackerScreen(
  modifier: Modifier = Modifier,
  viewModel: WaterViewModel = viewModel()
) {
  val todayIntakeState by viewModel.todayIntake.collectAsStateWithLifecycle()
  val currentMl = todayIntakeState.amountMl
  val targetMl = viewModel.targetMl

  var isLoaded by remember { mutableStateOf(false) }
  LaunchedEffect(Unit) {
    isLoaded = true
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .verticalScroll(rememberScrollState())
  ) {
    // Top Hero Banner Card - Version sans dépendance d'image manquante
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(140.dp)
        .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
        .background(
          Brush.linearGradient(
            colors = listOf(
              TurquoisePrimary.copy(alpha = 0.3f),
              DarkSurfaceVariant,
              DarkBackground
            )
          )
        )
    ) {
      Column(
        modifier = Modifier
          .align(Alignment.BottomStart)
          .padding(24.dp)
      ) {
        Text(
          text = "Objectif quotidien : 2,0 L",
          style = MaterialTheme.typography.bodyLarge.copy(
            color = TurquoisePrimary,
            fontWeight = FontWeight.SemiBold
          )
        )
      }
    }

    // Main Content
    AnimatedVisibility(
      visible = isLoaded,
      enter = fadeIn(animationSpec = spring(stiffness = Spring.StiffnessVeryLow)) +
              slideInVertically(initialOffsetY = { 50 }, animationSpec = spring(stiffness = Spring.StiffnessLow))
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Water Progress Circle
        WaterProgressCircle(
          currentMl = currentMl,
          targetMl = targetMl,
          modifier = Modifier
            .padding(vertical = 12.dp)
            .testTag("water_progress_circle")
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Stats summary row
        StatsSummaryRow(
          currentMl = currentMl,
          targetMl = targetMl,
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Dynamic Motivation Message Card
        MotivationCard(
          currentMl = currentMl,
          targetMl = targetMl,
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Action Buttons Row
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
          // Add 250ml Button
          Button(
            onClick = { viewModel.addWater(250) },
            modifier = Modifier
              .weight(1f)
              .height(56.dp)
              .testTag("add_250ml_button"),
            colors = ButtonDefaults.buttonColors(
              containerColor = TurquoisePrimary,
              contentColor = OnTurquoisePrimary
            ),
            shape = RoundedCornerShape(28.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
          ) {
            Icon(
              imageVector = Icons.Rounded.Add,
              contentDescription = null,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "250 ml",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
          }

          // Add 500ml Button
          Button(
            onClick = { viewModel.addWater(500) },
            modifier = Modifier
              .weight(1f)
              .height(56.dp)
              .testTag("add_500ml_button"),
            colors = ButtonDefaults.buttonColors(
              containerColor = DarkSurfaceVariant,
              contentColor = TurquoisePrimary
            ),
            shape = RoundedCornerShape(28.dp),
            border = ButtonDefaults.outlinedButtonBorder.copy(
              brush = Brush.linearGradient(listOf(TurquoisePrimary, TurquoiseSecondary))
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
          ) {
            Icon(
              imageVector = Icons.Rounded.WaterDrop,
              contentDescription = null,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "500 ml",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Reset Button
        TextButton(
          onClick = { viewModel.resetWater() },
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("reset_button"),
          colors = ButtonDefaults.textButtonColors(
            contentColor = TurquoisePrimary
          ),
          shape = RoundedCornerShape(24.dp)
        ) {
          Icon(
            imageVector = Icons.Rounded.Refresh,
            contentDescription = "Réinitialiser la progression",
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Réinitialiser",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Medium,
              letterSpacing = 0.5.sp
            )
          )
        }
      }
    }
  }
}
@Composable
fun StatsSummaryRow(
  currentMl: Int,
  targetMl: Int,
  modifier: Modifier = Modifier
) {
  val percentage = if (targetMl > 0) (currentMl.toFloat() / targetMl.toFloat() * 100).toInt() else 0
  val glasses = currentMl / 250
  val remainingL = ((targetMl - currentMl).coerceAtLeast(0).toFloat() / 1000f)

  Row(
    modifier = modifier
      .fillMaxWidth()
      .padding(vertical = 8.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceAround
  ) {
    // Percentage Column
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      Text(
        text = "$percentage%",
        style = MaterialTheme.typography.titleLarge.copy(
          fontWeight = FontWeight.Bold,
          color = OnDarkBackground
        )
      )
      Text(
        text = "Quotidien",
        style = MaterialTheme.typography.bodySmall.copy(
          color = TextSecondary
        )
      )
    }

    // Divider
    Box(
      modifier = Modifier
        .width(1.dp)
        .height(32.dp)
        .background(ElegantBorder)
    )

    // Glasses Column
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      Text(
        text = "$glasses",
        style = MaterialTheme.typography.titleLarge.copy(
          fontWeight = FontWeight.Bold,
          color = OnDarkBackground
        )
      )
      Text(
        text = "Verres",
        style = MaterialTheme.typography.bodySmall.copy(
          color = TextSecondary
        )
      )
    }

    // Divider
    Box(
      modifier = Modifier
        .width(1.dp)
        .height(32.dp)
        .background(ElegantBorder)
    )

    // Remaining Column
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      Text(
        text = String.format("%.2f L", remainingL),
        style = MaterialTheme.typography.titleLarge.copy(
          fontWeight = FontWeight.Bold,
          color = OnDarkBackground
        )
      )
      Text(
        text = "Restant",
        style = MaterialTheme.typography.bodySmall.copy(
          color = TextSecondary
        )
      )
    }
  }
}

@Composable
fun WaterProgressCircle(
  currentMl: Int,
  targetMl: Int,
  modifier: Modifier = Modifier
) {
  val progressFraction = (currentMl.toFloat() / targetMl.toFloat()).coerceIn(0f, 1f)

  val animatedProgress by animateFloatAsState(
    targetValue = progressFraction,
    animationSpec = spring(
      dampingRatio = Spring.DampingRatioMediumBouncy,
      stiffness = Spring.StiffnessLow
    ),
    label = "WaterProgress"
  )

  Box(
    modifier = modifier
      .size(240.dp)
      .shadow(24.dp, CircleShape, spotColor = TurquoisePrimary)
      .background(DarkSurfaceVariant, CircleShape)
      .border(
        width = 4.dp,
        brush = Brush.sweepGradient(
          colors = listOf(
            TurquoisePrimary,
            TurquoiseSecondary,
            TurquoiseTertiary,
            TurquoisePrimary
          )
        ),
        shape = CircleShape
      )
      .padding(8.dp),
    contentAlignment = Alignment.Center
  ) {
    // Wave Canvas
    Canvas(
      modifier = Modifier
        .fillMaxSize()
        .clip(CircleShape)
    ) {
      val width = size.width
      val height = size.height

      // Subtle water background
      drawRect(
        color = TurquoisePrimary.copy(alpha = 0.05f),
        size = size
      )

      val waterLevelY = height * (1f - animatedProgress)
      val waveHeight = if (animatedProgress > 0f && animatedProgress < 1f) 12.dp.toPx() else 0f

      val path = Path().apply {
        moveTo(0f, height)
        lineTo(0f, waterLevelY)

        if (waveHeight > 0f) {
          // Draw wave curves
          cubicTo(
            width / 3f, waterLevelY - waveHeight,
            width * 2f / 3f, waterLevelY + waveHeight,
            width, waterLevelY
          )
        } else {
          lineTo(width, waterLevelY)
        }

        lineTo(width, height)
        close()
      }

      drawPath(
        path = path,
        brush = Brush.verticalGradient(
          colors = listOf(
            TurquoisePrimary.copy(alpha = 0.85f),
            TurquoiseSecondary
          )
        )
      )
    }

    // Glass reflection overlay ring
    Box(
      modifier = Modifier
        .fillMaxSize()
        .border(1.5.dp, TurquoisePrimary.copy(alpha = 0.25f), CircleShape)
    )

    // Inner details (Adaptive text contrast)
    val textContrastColor = if (animatedProgress > 0.62f) OnTurquoisePrimary else OnDarkBackground

    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier.padding(16.dp)
    ) {
      Icon(
        imageVector = Icons.Rounded.WaterDrop,
        contentDescription = null,
        tint = if (animatedProgress > 0.45f) OnTurquoisePrimary.copy(alpha = 0.7f) else TurquoisePrimary,
        modifier = Modifier.size(28.dp)
      )

      Spacer(modifier = Modifier.height(4.dp))

      val progressLitres = currentMl.toFloat() / 1000f
      Text(
        text = String.format("%.2f L", progressLitres),
        style = MaterialTheme.typography.displayMedium.copy(
          fontWeight = FontWeight.Light,
          color = textContrastColor,
          letterSpacing = (-1).sp
        )
      )

      Text(
        text = "OBJECTIF 2,0 L",
        style = MaterialTheme.typography.labelMedium.copy(
          fontWeight = FontWeight.Medium,
          color = if (animatedProgress > 0.65f) OnTurquoisePrimary.copy(alpha = 0.8f) else TextSecondary,
          letterSpacing = 1.5.sp
        )
      )
    }
  }
}

@Composable
fun MotivationCard(
  currentMl: Int,
  targetMl: Int,
  modifier: Modifier = Modifier
) {
  val ratio = currentMl.toFloat() / targetMl.toFloat()

  val (title, description, progressIcon) = when {
    ratio <= 0f -> Triple(
      "Commencez fort !",
      "Buvez votre premier verre d'eau de la journée pour éveiller votre métabolisme.",
      "💧"
    )
    ratio < 0.25f -> Triple(
      "Excellent début !",
      "L'hydratation est essentielle. Continuez comme ça !",
      "🌱"
    )
    ratio < 0.5f -> Triple(
      "Bonne progression !",
      "Vous faites du bien à votre corps. Restez régulier.",
      "☀️"
    )
    ratio < 0.75f -> Triple(
      "À mi-chemin !",
      "Déjà plus de la moitié de l'objectif accomplie. Sensationnel !",
      "⚡"
    )
    ratio < 1f -> Triple(
      "Presque au bout !",
      "Encore un petit effort et l'objectif de la journée sera atteint.",
      "🚀"
    )
    else -> Triple(
      "Objectif Atteint !",
      "Félicitations ! Votre corps est parfaitement hydraté aujourd'hui.",
      "🏆"
    )
  }

  Card(
    modifier = modifier,
    shape = RoundedCornerShape(24.dp),
    colors = CardDefaults.cardColors(containerColor = DarkSurface),
    border = CardDefaults.outlinedCardBorder().copy(
      brush = Brush.linearGradient(
        colors = if (ratio >= 1f) listOf(TurquoisePrimary, TurquoiseSecondary) else listOf(ElegantBorder, Color.Transparent)
      )
    )
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = progressIcon,
        fontSize = 36.sp,
        modifier = Modifier.padding(end = 16.dp)
      )

      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = title,
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            color = if (ratio >= 1f) TurquoisePrimary else OnDarkBackground
          )
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = description,
          style = MaterialTheme.typography.bodyMedium.copy(
            color = TextSecondary,
            lineHeight = 20.sp
          )
        )
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun HydrationTrackerPreview() {
  MyApplicationTheme {
    Scaffold { padding ->
      Column(
        modifier = Modifier
          .padding(padding)
          .fillMaxSize()
          .background(DarkBackground)
      ) {
        WaterProgressCircle(currentMl = 750, targetMl = 2000)
      }
    }
  }
}
