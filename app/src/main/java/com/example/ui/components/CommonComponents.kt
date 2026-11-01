package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BookingStatus
import com.example.ui.theme.AeroBlue
import com.example.ui.theme.Navy800
import com.example.ui.theme.Navy900
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate300
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate900
import com.example.ui.theme.StatusBlue
import com.example.ui.theme.StatusBlueBg
import com.example.ui.theme.StatusGreen
import com.example.ui.theme.StatusGreenBg
import com.example.ui.theme.StatusOrange
import com.example.ui.theme.StatusOrangeBg
import com.example.ui.theme.ValetGold
import com.example.ui.theme.ValetGoldLight

@Composable
fun BookingStatusBadge(
    status: BookingStatus,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor) = when (status) {
        BookingStatus.CONFIRMED -> StatusBlueBg to StatusBlue
        BookingStatus.VALET_DISPATCHED -> StatusOrangeBg to StatusOrange
        BookingStatus.AT_CURBSIDE -> ValetGoldLight to Color(0xFFB45309)
        BookingStatus.INSPECTING_HANDOFF -> StatusBlueBg to StatusBlue
        BookingStatus.EN_ROUTE_TO_RETURN -> Color(0xFFF3E8FF) to Color(0xFF7E22CE)
        BookingStatus.RETURNED_SUCCESS -> StatusGreenBg to StatusGreen
        BookingStatus.CANCELLED -> Slate100 to Slate500
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.testTag("status_badge_${status.name}")
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(textColor)
            )
            Text(
                text = status.title,
                color = textColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun RentalCompanyBadge(
    name: String,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Slate100,
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate300),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(7.dp)
                    .clip(CircleShape)
                    .background(Navy800)
            )
            Text(
                text = "Dest: $name",
                color = Slate900,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun HandoffPinCard(
    pin: String,
    status: BookingStatus,
    curbsidePillar: String,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Navy900),
        shape = RoundedCornerShape(18.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("handoff_pin_card")
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = "Security",
                    tint = ValetGold,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "BAILMENT CUSTODY HANDOFF PIN",
                    color = ValetGold,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 4-digit PIN display
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                pin.forEach { digit ->
                    Surface(
                        color = Navy800,
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, ValetGold.copy(alpha = 0.6f)),
                        modifier = Modifier.size(54.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = digit.toString(),
                                color = Color.White,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.ExtraBold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Provide this 4-digit PIN to your TerminalDrop courier at $curbsidePillar to authorize custody transfer & insured transit.",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 12.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
fun StatusProgressStepper(
    currentStatus: BookingStatus,
    modifier: Modifier = Modifier,
    showDetailedTimeline: Boolean = true,
    onStageSelected: ((BookingStatus) -> Unit)? = null
) {
    ValetReturnStatusTracker(
        currentStatus = currentStatus,
        modifier = modifier.testTag("status_stepper"),
        showDetailedTimeline = showDetailedTimeline,
        onStageSelected = onStageSelected
    )
}

@Composable
fun TimeSavingsBanner(
    minutesSaved: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        color = StatusGreenBg,
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, StatusGreen.copy(alpha = 0.3f)),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(StatusGreen),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Saved ~$minutesSaved Minutes Shuttle Delay",
                    color = Color(0xFF065F46),
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
                Text(
                    text = "Dropped at departure door. Zero off-airport bus transit.",
                    color = Color(0xFF047857),
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
fun FeeBreakdownCard(
    baseFee: Double,
    airportFee: Double,
    refuelFee: Double,
    rushFee: Double,
    totalFee: Double,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Slate100),
        shape = RoundedCornerShape(14.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = "Service Fee Breakdown",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Slate900
            )

            Spacer(modifier = Modifier.height(8.dp))

            FeeRow(label = "Independent Curbside Courier Return", amount = baseFee)
            FeeRow(label = "Airport Commercial Access & Dispatch", amount = airportFee)
            if (refuelFee > 0.0) {
                FeeRow(label = "Courier Refuel Assist (Plus Pump Receipt)", amount = refuelFee)
            }
            if (rushFee > 0.0) {
                FeeRow(label = "Priority Rush Dispatch (<30 min)", amount = rushFee)
            }

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = Color(0xFFCBD5E1))
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total Service Fee",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp,
                    color = Slate900
                )
                Text(
                    text = "$${String.format("%.2f", totalFee)}",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    color = Navy900
                )
            }
        }
    }
}

@Composable
private fun FeeRow(label: String, amount: Double) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 12.sp, color = Slate700)
        Text(text = "$${String.format("%.2f", amount)}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Slate900)
    }
}

@Composable
fun AgentContactCard(
    agentName: String,
    phone: String,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Navy800),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.DirectionsCar,
                    contentDescription = null,
                    tint = ValetGold,
                    modifier = Modifier.size(24.dp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Assigned Independent Valet Concierge",
                    fontSize = 11.sp,
                    color = Slate500,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = agentName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate900
                )
                Text(
                    text = phone,
                    fontSize = 12.sp,
                    color = AeroBlue,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun IndependentServiceLegalNotice(
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate300),
        modifier = modifier
            .fillMaxWidth()
            .testTag("independent_service_legal_notice")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.VerifiedUser,
                    contentDescription = null,
                    tint = Navy800,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "Independent Courier & Legal Compliance Notice",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = Slate900
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "TerminalDrop is an independent, customer-authorized vehicle return courier service and is NOT affiliated with, endorsed by, or sponsored by any rental car company, airline, or airport authority. All third-party company and airport names are used strictly for nominative destination identification. All transfers operate under a signed Customer Bailment & Limited Return Agency Agreement backed by Commercial Garagekeepers Liability insurance.",
                fontSize = 11.sp,
                color = Slate700,
                lineHeight = 15.sp
            )
        }
    }
}
