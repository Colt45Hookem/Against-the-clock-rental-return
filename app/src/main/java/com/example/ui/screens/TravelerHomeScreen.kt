package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AirportShuttle
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AirportData
import com.example.data.model.CarReturnBooking
import com.example.ui.components.BookingStatusBadge
import com.example.ui.components.IndependentServiceLegalNotice
import com.example.ui.components.RentalCompanyBadge
import com.example.ui.components.TimeSavingsBanner
import com.example.ui.components.ValetReturnStatusTracker
import com.example.ui.theme.AeroBlue
import com.example.ui.theme.Navy700
import com.example.ui.theme.Navy800
import com.example.ui.theme.Navy900
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate900
import com.example.ui.theme.StatusGreen
import com.example.ui.theme.StatusGreenBg
import com.example.ui.theme.ValetGold
import com.example.ui.theme.ValetGoldDark
import com.example.ui.theme.ValetGoldLight

@Composable
fun TravelerHomeScreen(
    activeBooking: CarReturnBooking?,
    onBookClick: () -> Unit,
    onViewActiveBookingClick: (Long) -> Unit,
    onSelectAirportGuide: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("traveler_home_screen"),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 96.dp)
    ) {
        // Hero Section
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(Navy900, Navy800, Color(0xFF1E3A5F))
                        )
                    )
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = ValetGold.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(20.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, ValetGold.copy(alpha = 0.5f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FlightTakeoff,
                                    contentDescription = null,
                                    tint = ValetGold,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "AIRPORT VALET CAR RETURN",
                                    color = ValetGold,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }

                        Surface(
                            color = Color.White.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "Save 40+ Mins",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Skip the rental return shuttle.",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        lineHeight = 30.sp
                    )
                    Text(
                        text = "Drop your car directly at the terminal curb.",
                        color = ValetGold,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 28.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Running late for your flight? Hand your keys to our verified airport valet right outside your departure door. We take it back to the rental company and text you the receipt.",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 13.sp,
                        lineHeight = 19.sp
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Button(
                        onClick = onBookClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ValetGold,
                            contentColor = Navy900
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("book_curbside_button")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Key, contentDescription = null, modifier = Modifier.size(20.dp))
                            Text(
                                text = "Book Curbside Return - from $49",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            }
        }

        // Active Booking Spotlight (if active)
        if (activeBooking != null) {
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                    Text(
                        text = "CURRENT ACTIVE DROP-OFF",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate500,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(18.dp),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, ValetGold),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onViewActiveBookingClick(activeBooking.id) }
                            .testTag("active_booking_card")
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Surface(
                                        color = Navy900,
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(
                                            text = activeBooking.airportCode,
                                            color = ValetGold,
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 14.sp,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                    Column {
                                        Text(
                                            text = activeBooking.terminalName,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp,
                                            color = Slate900
                                        )
                                        Text(
                                            text = activeBooking.curbsidePillar,
                                            fontSize = 12.sp,
                                            color = Slate500
                                        )
                                    }
                                }

                                BookingStatusBadge(status = activeBooking.status)
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = activeBooking.vehicleMakeModel,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 14.sp,
                                        color = Slate900
                                    )
                                    Text(
                                        text = "Plate: ${activeBooking.licensePlate} • ${activeBooking.vehicleColor}",
                                        fontSize = 12.sp,
                                        color = Slate700
                                    )
                                }
                                RentalCompanyBadge(name = activeBooking.rentalCompany)
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            ValetReturnStatusTracker(
                                currentStatus = activeBooking.status,
                                showDetailedTimeline = false
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Button(
                                onClick = { onViewActiveBookingClick(activeBooking.id) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Navy900,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Shield, contentDescription = null, tint = ValetGold, modifier = Modifier.size(16.dp))
                                    Text("Show Curbside PIN (${activeBooking.handoffPin}) & Live Radar")
                                    Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }
            }
        }

        // How it Works: 3-Step 60-Second Process
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                Text(
                    text = "HOW TERMINALDROP WORKS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate500,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        StepRow(
                            stepNumber = "1",
                            title = "Drive Straight to Your Terminal Door",
                            desc = "Put your departure terminal curb in your GPS. Avoid the remote rental car center entirely."
                        )
                        StepRow(
                            stepNumber = "2",
                            title = "60-Second Handshake & PIN Check",
                            desc = "Our vetted valet concierge meets you right at your curbside pillar. Verify with your 4-digit PIN."
                        )
                        StepRow(
                            stepNumber = "3",
                            title = "Walk to Your Gate - We Handle the Rest",
                            desc = "Walk through TSA security relaxed. We drive the car to your rental agency and send your return receipt."
                        )
                    }
                }
            }
        }

        // Comparison: Shuttle Hassle vs TerminalDrop
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
                Text(
                    text = "TIME & STRESS COMPARISON",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate500,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    colors = CardDefaults.cardColors(containerColor = Slate100),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            // Shuttle Column
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Rental Shuttle",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Color(0xFFDC2626)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                ComparisonBullet("Drive 4-6 mi off airport")
                                ComparisonBullet("Wait 15+ mins for bus")
                                ComparisonBullet("Haul luggage 2x")
                                ComparisonBullet("Crowded shuttle ride")
                                ComparisonBullet("Total: 40-55 mins!")
                            }

                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(140.dp)
                                    .background(Color(0xFFCBD5E1))
                            )

                            // TerminalDrop Column
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 12.dp)
                            ) {
                                Text(
                                    text = "TerminalDrop",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = StatusGreen
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                ComparisonBullet("Drive direct to terminal")
                                ComparisonBullet("Meet valet at pillar")
                                ComparisonBullet("60-second handoff")
                                ComparisonBullet("Walk directly to TSA")
                                ComparisonBullet("Total: 1-2 mins!", highlight = true)
                            }
                        }
                    }
                }
            }
        }

        // Supported Airports
        item {
            Column(modifier = Modifier.padding(vertical = 10.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "SUPPORTED AIRPORTS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate500,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "View All Guides",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = AeroBlue,
                        modifier = Modifier.clickable { onSelectAirportGuide() }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(AirportData.AIRPORTS) { airport ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(14.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                            modifier = Modifier
                                .width(200.dp)
                                .clickable { onSelectAirportGuide() }
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Surface(
                                        color = Navy900,
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(
                                            text = airport.code,
                                            color = ValetGold,
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 14.sp,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                    Text(
                                        text = airport.city,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Slate900,
                                        maxLines = 1
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "Saves ~${airport.averageShuttleMinutesSaved} mins",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = StatusGreen
                                )
                                Text(
                                    text = "${airport.terminals.size} terminals covered",
                                    fontSize = 11.sp,
                                    color = Slate500
                                )
                            }
                        }
                    }
                }
            }
        }

        // Legal & Non-Affiliation Compliance Notice
        item {
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                IndependentServiceLegalNotice()
            }
        }
    }
}

@Composable
private fun StepRow(stepNumber: String, title: String, desc: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(Navy800),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stepNumber,
                color = ValetGold,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
        }
        Column {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Slate900
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = desc,
                fontSize = 12.sp,
                color = Slate700,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
private fun ComparisonBullet(text: String, highlight: Boolean = false) {
    Row(
        modifier = Modifier.padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = if (highlight) "★" else "•",
            color = if (highlight) StatusGreen else Slate500,
            fontSize = 12.sp
        )
        Text(
            text = text,
            fontSize = 11.sp,
            color = if (highlight) Color(0xFF047857) else Slate700,
            fontWeight = if (highlight) FontWeight.Bold else FontWeight.Normal
        )
    }
}
