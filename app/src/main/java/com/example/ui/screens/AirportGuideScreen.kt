package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AirportShuttle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AirportData
import com.example.data.model.AirportInfo
import com.example.ui.components.TimeSavingsBanner
import com.example.ui.theme.AeroBlue
import com.example.ui.theme.Navy800
import com.example.ui.theme.Navy900
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate300
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate900
import com.example.ui.theme.StatusGreen
import com.example.ui.theme.StatusGreenBg
import com.example.ui.theme.ValetGold

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AirportGuideScreen(
    modifier: Modifier = Modifier
) {
    var selectedAirportCode by remember { mutableStateOf("LAX") }
    val airport = remember(selectedAirportCode) { AirportData.findAirport(selectedAirportCode) }

    // Fuel Calculator State
    var gallonsNeeded by remember { mutableFloatStateOf(8f) }
    val agencyRatePerGallon = 11.99
    val marketRatePerGallon = 4.45
    val valetServiceFee = 15.0

    val agencyFuelTotal = gallonsNeeded * agencyRatePerGallon
    val terminalDropFuelTotal = (gallonsNeeded * marketRatePerGallon) + valetServiceFee
    val fuelSaved = agencyFuelTotal - terminalDropFuelTotal

    Surface(
        color = Color(0xFFF8FAFC),
        modifier = modifier
            .fillMaxSize()
            .testTag("airport_guide_screen")
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(start = 16.dp, end = 16.dp, top = 14.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            item {
                Column {
                    Text(
                        text = "Airport Drop Zones & Guide",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Slate900
                    )
                    Text(
                        text = "Curbside valet locations, shuttle time delays, and fuel savings calculator.",
                        fontSize = 12.sp,
                        color = Slate500
                    )
                }
            }

            // Airport Selector Chips
            item {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AirportData.AIRPORTS.forEach { item ->
                        val isSelected = selectedAirportCode == item.code
                        Surface(
                            color = if (isSelected) Navy900 else Color.White,
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) Navy900 else Color(0xFFE2E8F0)),
                            modifier = Modifier.clickable { selectedAirportCode = item.code }
                        ) {
                            Text(
                                text = "${item.code} (${item.city.split(",").first()})",
                                color = if (isSelected) ValetGold else Slate700,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }

            // Selected Airport Details Card
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
                            Column {
                                Text(
                                    text = airport.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = Slate900
                                )
                                Text(
                                    text = airport.city,
                                    fontSize = 12.sp,
                                    color = Slate500
                                )
                            }
                            Surface(color = Navy900, shape = RoundedCornerShape(8.dp)) {
                                Text(
                                    text = airport.code,
                                    color = ValetGold,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 15.sp,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        TimeSavingsBanner(minutesSaved = airport.averageShuttleMinutesSaved)

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = airport.description,
                            fontSize = 12.sp,
                            color = Slate700,
                            lineHeight = 17.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))
                        HorizontalDivider(color = Color(0xFFF1F5F9))
                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Covered Terminal Curbside Drop Points:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Slate900
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        airport.defaultPillars.forEach { pillar ->
                            Row(
                                modifier = Modifier.padding(vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = AeroBlue, modifier = Modifier.size(14.dp))
                                Text(text = pillar, fontSize = 12.sp, color = Slate700)
                            }
                        }
                    }
                }
            }

            // Interactive Fuel Penalty vs Valet Refuel Calculator
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier.size(32.dp).clip(CircleShape).background(Color(0xFFFEF3C7)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(imageVector = Icons.Default.LocalGasStation, contentDescription = null, tint = ValetGold, modifier = Modifier.size(18.dp))
                            }
                            Column {
                                Text(
                                    text = "Rental Agency Fuel Penalty Calculator",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Slate900
                                )
                                Text(
                                    text = "See why our valet refuel service saves you big money",
                                    fontSize = 11.sp,
                                    color = Slate500
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Gallons Needed to Top Off Tank:", fontSize = 12.sp, color = Slate700)
                            Text(text = "${gallonsNeeded.toInt()} Gallons", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Slate900)
                        }

                        Slider(
                            value = gallonsNeeded,
                            onValueChange = { gallonsNeeded = it },
                            valueRange = 2f..18f,
                            steps = 15,
                            colors = SliderDefaults.colors(thumbColor = ValetGold, activeTrackColor = ValetGold)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Comparison Box
                        Surface(
                            color = Slate100,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = "Rental Agency Gas Charge ($11.99/gal):", fontSize = 12.sp, color = Color(0xFFDC2626))
                                    Text(text = "$${String.format("%.2f", agencyFuelTotal)}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFDC2626))
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = "TerminalDrop (Pump Gas + $15 Valet Fee):", fontSize = 12.sp, color = Color(0xFF065F46))
                                    Text(text = "$${String.format("%.2f", terminalDropFuelTotal)}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF065F46))
                                }

                                HorizontalDivider(color = Color(0xFFCBD5E1))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = "Net Traveler Savings:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF065F46))
                                    Text(text = "Save $${String.format("%.2f", fuelSaved)}", fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, color = StatusGreen)
                                }
                            }
                        }
                    }
                }
            }

            // Legal, Insurance & IP Non-Infringement Framework
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("legal_compliance_framework_card")
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Column {
                            Text(
                                text = "Legal, Insurance & IP Non-Infringement Framework",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = Slate900
                            )
                            Text(
                                text = "How TerminalDrop operates as a legitimate, independent third-party courier service without trademark or copyright conflict:",
                                fontSize = 12.sp,
                                color = Slate500
                            )
                        }

                        AirportData.LEGAL_COMPLIANCE_PILLARS.forEach { pillar ->
                            Surface(
                                color = Slate100,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = pillar.title,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = Navy900
                                    )
                                    Text(
                                        text = pillar.summary,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 11.sp,
                                        color = Color(0xFF047857)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = pillar.details,
                                        fontSize = 11.sp,
                                        color = Slate700,
                                        lineHeight = 15.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
