package com.example.ui.screens

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BookingStatus
import com.example.data.model.CarReturnBooking
import com.example.ui.components.AgentContactCard
import com.example.ui.components.BookingStatusBadge
import com.example.ui.components.FeeBreakdownCard
import com.example.ui.components.HandoffPinCard
import com.example.ui.components.IndependentServiceLegalNotice
import com.example.ui.components.RentalCompanyBadge
import com.example.ui.components.StatusProgressStepper
import com.example.ui.components.TimeSavingsBanner
import com.example.ui.theme.AeroBlue
import com.example.ui.theme.Navy800
import com.example.ui.theme.Navy900
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate900
import com.example.ui.theme.StatusGreen
import com.example.ui.theme.StatusGreenBg
import com.example.ui.theme.ValetGold

@Composable
fun ActiveDropDetailScreen(
    booking: CarReturnBooking?,
    onAdvanceSimulatedStatus: (CarReturnBooking, BookingStatus) -> Unit,
    onBookNewClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (booking == null) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Slate100),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.DirectionsCar, contentDescription = null, tint = Slate500, modifier = Modifier.size(32.dp))
                }
                Text(
                    text = "No Active Rental Drop",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Slate900
                )
                Text(
                    text = "You don't have an in-progress curbside pickup right now.",
                    fontSize = 13.sp,
                    color = Slate500,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Button(
                    onClick = onBookNewClick,
                    colors = ButtonDefaults.buttonColors(containerColor = ValetGold, contentColor = Navy900),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Book Curbside Return ($49)", fontWeight = FontWeight.Bold)
                }
            }
        }
        return
    }

    val infiniteTransition = rememberInfiniteTransition(label = "radar")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.35f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("active_drop_detail_screen"),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Status & Live Radar Header
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(18.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                if (booking.status.isActive()) {
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .scale(pulseScale)
                                            .clip(CircleShape)
                                            .background(ValetGold.copy(alpha = 0.35f))
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .size(14.dp)
                                        .clip(CircleShape)
                                        .background(if (booking.status == BookingStatus.RETURNED_SUCCESS) StatusGreen else ValetGold)
                                )
                            }
                            Column {
                                Text(
                                    text = booking.bookingCode,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 17.sp,
                                    color = Slate900
                                )
                                Text(
                                    text = "Scheduled: ${booking.scheduledDropTime}",
                                    fontSize = 11.sp,
                                    color = Slate500
                                )
                            }
                        }

                        BookingStatusBadge(status = booking.status)
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = Color(0xFFF1F5F9))
                    Spacer(modifier = Modifier.height(14.dp))

                    StatusProgressStepper(
                        currentStatus = booking.status,
                        showDetailedTimeline = true,
                        onStageSelected = { targetStatus ->
                            onAdvanceSimulatedStatus(booking, targetStatus)
                        }
                    )
                }
            }
        }

        // Handoff PIN Card (Prominent for Curbside Exchange)
        if (booking.status != BookingStatus.RETURNED_SUCCESS && booking.status != BookingStatus.CANCELLED) {
            item {
                HandoffPinCard(
                    pin = booking.handoffPin,
                    status = booking.status,
                    curbsidePillar = booking.curbsidePillar
                )
            }
        }

        // Curbside Terminal Meeting Point Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Curbside Terminal Location",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Slate900
                        )
                        Surface(
                            color = Navy900,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = booking.airportCode,
                                color = ValetGold,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = AeroBlue, modifier = Modifier.size(20.dp))
                        Column {
                            Text(
                                text = "${booking.terminalName} - ${booking.curbsidePillar}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Slate900
                            )
                            Text(
                                text = "Departures Level • Flight: ${booking.airline} (${booking.flightNumber})",
                                fontSize = 12.sp,
                                color = Slate500
                            )
                        }
                    }
                }
            }
        }

        // Vehicle & Rental Company Info
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Rental Vehicle Details",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Slate900
                        )
                        RentalCompanyBadge(name = booking.rentalCompany)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = booking.vehicleMakeModel,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Slate900
                    )
                    Text(
                        text = "Color: ${booking.vehicleColor} • License Plate: ${booking.licensePlate}",
                        fontSize = 12.sp,
                        color = Slate700
                    )
                    Text(
                        text = "Rental Agreement: ${booking.rentalAgreementNumber}",
                        fontSize = 11.sp,
                        color = Slate500
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = Color(0xFFF1F5F9))
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(imageVector = Icons.Default.LocalGasStation, contentDescription = null, tint = Slate500, modifier = Modifier.size(16.dp))
                            Text(text = "Fuel: ${booking.refuelOption.label}", fontSize = 11.sp, color = Slate700)
                        }
                        if (booking.isPriorityRush) {
                            Surface(color = Color(0xFFFFEDD5), shape = RoundedCornerShape(6.dp)) {
                                Text(
                                    text = "Priority Rush",
                                    color = Color(0xFFC2410C),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Assigned Valet Agent
        item {
            AgentContactCard(
                agentName = booking.assignedAgentName,
                phone = booking.assignedAgentPhone
            )
        }

        // Return Completion Receipt (if returned)
        if (booking.status == BookingStatus.RETURNED_SUCCESS) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = StatusGreenBg),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, StatusGreen),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = StatusGreen, modifier = Modifier.size(24.dp))
                            Text(
                                text = "Official Rental Return Verified",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = Color(0xFF065F46)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Your car has been returned to ${booking.rentalCompany}'s return lanes at ${booking.airportCode}.",
                            fontSize = 12.sp,
                            color = Color(0xFF047857)
                        )
                        Text(
                            text = "• Return Bay: ${booking.returnBayNumber.ifBlank { "Bay 3A Rapid Check" }}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF065F46)
                        )
                        Text(
                            text = "• Checked in with Lot Supervisor: ${booking.returnRentalAgentName.ifBlank { "Lot Attendant" }}",
                            fontSize = 12.sp,
                            color = Color(0xFF047857)
                        )
                        Text(
                            text = "• Odometer logged: ${booking.intakeMileage} mi | Fuel: ${booking.intakeFuelPercent}%",
                            fontSize = 12.sp,
                            color = Color(0xFF047857)
                        )
                        Text(
                            text = "• Digital Receipt ID: ${booking.rentalReturnReceiptUrl ?: "H-RET-SUCCESS"}",
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            color = Color(0xFF065F46)
                        )
                    }
                }
            }
        }

        // Fee Breakdown
        item {
            FeeBreakdownCard(
                baseFee = booking.baseFee,
                airportFee = booking.airportFee,
                refuelFee = booking.refuelFee,
                rushFee = booking.rushFee,
                totalFee = booking.totalFee
            )
        }

        // Live Simulation Controls for User / Evaluator
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Slate100),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCBD5E1)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Speed, contentDescription = null, tint = Slate700, modifier = Modifier.size(16.dp))
                        Text(
                            text = "DEMO & INTERACTIVE SIMULATOR",
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = Slate700,
                            letterSpacing = 0.5.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Advance through each phase of the curbside pickup to see the live app state change:",
                        fontSize = 11.sp,
                        color = Slate500
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val nextStatus = when (booking.status) {
                            BookingStatus.CONFIRMED -> BookingStatus.VALET_DISPATCHED
                            BookingStatus.VALET_DISPATCHED -> BookingStatus.AT_CURBSIDE
                            BookingStatus.AT_CURBSIDE -> BookingStatus.INSPECTING_HANDOFF
                            BookingStatus.INSPECTING_HANDOFF -> BookingStatus.EN_ROUTE_TO_RETURN
                            BookingStatus.EN_ROUTE_TO_RETURN -> BookingStatus.RETURNED_SUCCESS
                            BookingStatus.RETURNED_SUCCESS -> BookingStatus.CONFIRMED
                            BookingStatus.CANCELLED -> BookingStatus.CONFIRMED
                        }

                        Button(
                            onClick = { onAdvanceSimulatedStatus(booking, nextStatus) },
                            colors = ButtonDefaults.buttonColors(containerColor = Navy900, contentColor = Color.White),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("simulate_next_status_button")
                        ) {
                            Text(
                                text = "Advance to: ${nextStatus.title}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Legal & Non-Affiliation Compliance Notice
        item {
            IndependentServiceLegalNotice()
        }
    }
}
