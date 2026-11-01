package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Radar
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.screens.ActiveDropDetailScreen
import com.example.ui.screens.AirportGuideScreen
import com.example.ui.screens.BookDropOffScreen
import com.example.ui.screens.OperatorDashboardScreen
import com.example.ui.screens.TravelerHistoryScreen
import com.example.ui.screens.TravelerHomeScreen
import com.example.ui.theme.AeroBlue
import com.example.ui.theme.Navy800
import com.example.ui.theme.Navy900
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate300
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate900
import com.example.ui.theme.StatusGreen
import com.example.ui.theme.ValetGold
import com.example.ui.viewmodel.AppRole
import com.example.ui.viewmodel.TerminalDropViewModel
import com.example.ui.viewmodel.TravelerTab

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: TerminalDropViewModel,
    modifier: Modifier = Modifier
) {
    val role by viewModel.role.collectAsState()
    val travelerTab by viewModel.travelerTab.collectAsState()
    val isBookingWizardOpen by viewModel.isBookingWizardOpen.collectAsState()
    val bookingDraft by viewModel.bookingDraft.collectAsState()
    val currentActiveBooking by viewModel.currentActiveBooking.collectAsState()
    val activeBookings by viewModel.activeBookings.collectAsState()
    val pastBookings by viewModel.pastBookings.collectAsState()
    val allBookings by viewModel.allBookings.collectAsState()
    val operatorStats by viewModel.operatorStats.collectAsState()

    // Handle back button for sub-screens
    if (isBookingWizardOpen) {
        BackHandler { viewModel.closeBookingWizard() }
    } else if (travelerTab != TravelerTab.HOME && role == AppRole.TRAVELER) {
        BackHandler { viewModel.setTravelerTab(TravelerTab.HOME) }
    }

    if (isBookingWizardOpen) {
        BookDropOffScreen(
            draft = bookingDraft,
            onDraftChange = { transform -> viewModel.updateBookingDraft(transform) },
            onSubmit = { viewModel.submitBookingDraft() },
            onCancel = { viewModel.closeBookingWizard() }
        )
        return
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            color = ValetGold,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.size(34.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.DirectionsCar,
                                    contentDescription = null,
                                    tint = Navy900,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "TerminalDrop",
                                    color = Color.White,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 17.sp
                                )
                                Text(
                                    text = "VALET",
                                    color = ValetGold,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp,
                                    letterSpacing = 1.sp
                                )
                            }
                            Text(
                                text = if (role == AppRole.TRAVELER) "Airport Curbside Return" else "Business Operator Mode",
                                color = Slate300,
                                fontSize = 11.sp
                            )
                        }
                    }
                },
                actions = {
                    // Role Toggle Pill: Traveler vs Business Operator
                    Surface(
                        color = if (role == AppRole.BUSINESS_OPERATOR) ValetGold else Navy800,
                        shape = RoundedCornerShape(20.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (role == AppRole.BUSINESS_OPERATOR) ValetGold else Color(0xFF1E3A5F)
                        ),
                        modifier = Modifier
                            .clickable {
                                if (role == AppRole.TRAVELER) {
                                    viewModel.setRole(AppRole.BUSINESS_OPERATOR)
                                } else {
                                    viewModel.setRole(AppRole.TRAVELER)
                                }
                            }
                            .testTag("role_toggle_button")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = if (role == AppRole.BUSINESS_OPERATOR) Icons.Default.BusinessCenter else Icons.Default.Person,
                                contentDescription = null,
                                tint = if (role == AppRole.BUSINESS_OPERATOR) Navy900 else Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = if (role == AppRole.BUSINESS_OPERATOR) "Operator Mode" else "Business Portal",
                                color = if (role == AppRole.BUSINESS_OPERATOR) Navy900 else Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Navy900,
                    titleContentColor = Color.White
                ),
                modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
            )
        },
        floatingActionButton = {
            if (role == AppRole.TRAVELER) {
                ExtendedFloatingActionButton(
                    onClick = { viewModel.openBookingWizard() },
                    icon = { Icon(imageVector = Icons.Default.Key, contentDescription = null, tint = Navy900) },
                    text = { Text("Book Drop ($49)", fontWeight = FontWeight.Bold, color = Navy900) },
                    containerColor = ValetGold,
                    modifier = Modifier
                        .windowInsetsPadding(WindowInsets.navigationBars)
                        .testTag("fab_book_drop")
                )
            } else {
                ExtendedFloatingActionButton(
                    onClick = { viewModel.openBookingWizard() },
                    icon = { Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = Color.White) },
                    text = { Text("Quick Curbside Intake", fontWeight = FontWeight.Bold, color = Color.White) },
                    containerColor = Navy900,
                    modifier = Modifier
                        .windowInsetsPadding(WindowInsets.navigationBars)
                        .testTag("fab_quick_intake")
                )
            }
        },
        bottomBar = {
            if (role == AppRole.TRAVELER) {
                NavigationBar(
                    containerColor = Color.White,
                    tonalElevation = 8.dp,
                    modifier = Modifier
                        .windowInsetsPadding(WindowInsets.navigationBars)
                        .testTag("traveler_nav_bar")
                ) {
                    NavigationBarItem(
                        selected = travelerTab == TravelerTab.HOME,
                        onClick = { viewModel.setTravelerTab(TravelerTab.HOME) },
                        icon = {
                            Icon(
                                imageVector = if (travelerTab == TravelerTab.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                                contentDescription = "Home"
                            )
                        },
                        label = { Text("Home", fontSize = 11.sp, fontWeight = FontWeight.Medium) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Navy900,
                            indicatorColor = ValetGold.copy(alpha = 0.25f)
                        ),
                        modifier = Modifier.testTag("nav_home")
                    )

                    NavigationBarItem(
                        selected = travelerTab == TravelerTab.ACTIVE_RADAR,
                        onClick = { viewModel.setTravelerTab(TravelerTab.ACTIVE_RADAR) },
                        icon = {
                            if (activeBookings.isNotEmpty()) {
                                BadgedBox(badge = { Badge { Text(activeBookings.size.toString()) } }) {
                                    Icon(
                                        imageVector = if (travelerTab == TravelerTab.ACTIVE_RADAR) Icons.Filled.Radar else Icons.Outlined.Radar,
                                        contentDescription = "Radar"
                                    )
                                }
                            } else {
                                Icon(
                                    imageVector = if (travelerTab == TravelerTab.ACTIVE_RADAR) Icons.Filled.Radar else Icons.Outlined.Radar,
                                    contentDescription = "Radar"
                                )
                            }
                        },
                        label = { Text("Live Drop", fontSize = 11.sp, fontWeight = FontWeight.Medium) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Navy900,
                            indicatorColor = ValetGold.copy(alpha = 0.25f)
                        ),
                        modifier = Modifier.testTag("nav_radar")
                    )

                    NavigationBarItem(
                        selected = travelerTab == TravelerTab.MY_DROPS,
                        onClick = { viewModel.setTravelerTab(TravelerTab.MY_DROPS) },
                        icon = {
                            Icon(
                                imageVector = if (travelerTab == TravelerTab.MY_DROPS) Icons.Filled.History else Icons.Outlined.History,
                                contentDescription = "History"
                            )
                        },
                        label = { Text("Receipts", fontSize = 11.sp, fontWeight = FontWeight.Medium) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Navy900,
                            indicatorColor = ValetGold.copy(alpha = 0.25f)
                        ),
                        modifier = Modifier.testTag("nav_receipts")
                    )

                    NavigationBarItem(
                        selected = travelerTab == TravelerTab.AIRPORT_GUIDE,
                        onClick = { viewModel.setTravelerTab(TravelerTab.AIRPORT_GUIDE) },
                        icon = {
                            Icon(
                                imageVector = if (travelerTab == TravelerTab.AIRPORT_GUIDE) Icons.Filled.Info else Icons.Outlined.Info,
                                contentDescription = "Guide"
                            )
                        },
                        label = { Text("Guide", fontSize = 11.sp, fontWeight = FontWeight.Medium) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Navy900,
                            indicatorColor = ValetGold.copy(alpha = 0.25f)
                        ),
                        modifier = Modifier.testTag("nav_guide")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (role == AppRole.BUSINESS_OPERATOR) {
                OperatorDashboardScreen(
                    bookings = allBookings,
                    stats = operatorStats,
                    onAdvanceStatus = { booking, next -> viewModel.advanceBookingStatus(booking, next) },
                    onCompleteIntake = { booking, miles, fuel, notes -> viewModel.completeIntakeChecklist(booking, miles, fuel, notes) },
                    onFinalizeReturn = { booking, bay, agent -> viewModel.finalizeRentalReturn(booking, bay, agent) }
                )
            } else {
                when (travelerTab) {
                    TravelerTab.HOME -> {
                        TravelerHomeScreen(
                            activeBooking = currentActiveBooking,
                            onBookClick = { viewModel.openBookingWizard() },
                            onViewActiveBookingClick = { id -> viewModel.selectBooking(id) },
                            onSelectAirportGuide = { viewModel.setTravelerTab(TravelerTab.AIRPORT_GUIDE) }
                        )
                    }
                    TravelerTab.ACTIVE_RADAR -> {
                        ActiveDropDetailScreen(
                            booking = currentActiveBooking,
                            onAdvanceSimulatedStatus = { booking, next -> viewModel.advanceBookingStatus(booking, next) },
                            onBookNewClick = { viewModel.openBookingWizard() }
                        )
                    }
                    TravelerTab.MY_DROPS -> {
                        TravelerHistoryScreen(
                            bookings = allBookings,
                            onSelectBooking = { id -> viewModel.selectBooking(id) }
                        )
                    }
                    TravelerTab.AIRPORT_GUIDE -> {
                        AirportGuideScreen()
                    }
                }
            }
        }
    }
}
