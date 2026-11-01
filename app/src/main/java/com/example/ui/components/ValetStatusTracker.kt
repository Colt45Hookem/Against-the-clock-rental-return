package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentInd
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
import com.example.ui.theme.StatusGreen
import com.example.ui.theme.StatusGreenBg
import com.example.ui.theme.ValetGold
import com.example.ui.theme.ValetGoldLight

/**
 * Represents the 4 primary stages of the valet return process:
 * 1. 'Assigned'
 * 2. 'Concierge En Route'
 * 3. 'Vehicle Picked Up'
 * 4. 'Returned'
 */
enum class ValetReturnStage(
    val stageIndex: Int,
    val label: String,
    val subtitle: String,
    val targetBookingStatus: BookingStatus,
    val icon: ImageVector
) {
    ASSIGNED(
        stageIndex = 0,
        label = "Assigned",
        subtitle = "Verified valet concierge assigned & monitoring your flight arrival at the curb.",
        targetBookingStatus = BookingStatus.CONFIRMED,
        icon = Icons.Default.AssignmentInd
    ),
    CONCIERGE_EN_ROUTE(
        stageIndex = 1,
        label = "Concierge En Route",
        subtitle = "Your concierge is heading to your departure terminal curbside pillar.",
        targetBookingStatus = BookingStatus.VALET_DISPATCHED,
        icon = Icons.Default.Navigation
    ),
    VEHICLE_PICKED_UP(
        stageIndex = 2,
        label = "Vehicle Picked Up",
        subtitle = "Keys handed off at the curb; vehicle is in transit to the rental agency return bay.",
        targetBookingStatus = BookingStatus.EN_ROUTE_TO_RETURN,
        icon = Icons.Default.Key
    ),
    RETURNED(
        stageIndex = 3,
        label = "Returned",
        subtitle = "Checked in at the rental company return lane with official receipt issued.",
        targetBookingStatus = BookingStatus.RETURNED_SUCCESS,
        icon = Icons.Default.CheckCircle
    );

    companion object {
        fun fromBookingStatus(status: BookingStatus): ValetReturnStage {
            return when (status) {
                BookingStatus.CONFIRMED -> ASSIGNED
                BookingStatus.VALET_DISPATCHED, BookingStatus.AT_CURBSIDE -> CONCIERGE_EN_ROUTE
                BookingStatus.INSPECTING_HANDOFF, BookingStatus.EN_ROUTE_TO_RETURN -> VEHICLE_PICKED_UP
                BookingStatus.RETURNED_SUCCESS -> RETURNED
                BookingStatus.CANCELLED -> ASSIGNED
            }
        }
    }
}

/**
 * Full-featured Status Tracking UI component displaying the 4 stages of the valet return process:
 * 'Assigned', 'Concierge En Route', 'Vehicle Picked Up', and 'Returned'.
 *
 * Includes both a visual 4-node horizontal progress bar and detailed vertical stage timeline rows.
 * Optionally allows tapping any stage to preview/advance the booking status.
 */
@Composable
fun ValetReturnStatusTracker(
    currentStatus: BookingStatus,
    modifier: Modifier = Modifier,
    showDetailedTimeline: Boolean = true,
    onStageSelected: ((BookingStatus) -> Unit)? = null
) {
    val activeStage = ValetReturnStage.fromBookingStatus(currentStatus)
    val stages = ValetReturnStage.entries

    val infiniteTransition = rememberInfiniteTransition(label = "valet_stage_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.28f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "stage_pulse_scale"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("valet_return_status_tracker")
    ) {
        // Header row showing current stage pill & progress fraction
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "VALET RETURN PROGRESS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Slate500,
                letterSpacing = 0.8.sp
            )
            Surface(
                color = if (activeStage == ValetReturnStage.RETURNED) StatusGreenBg else ValetGoldLight,
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "Stage ${activeStage.stageIndex + 1} of ${stages.size}: ${activeStage.label}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (activeStage == ValetReturnStage.RETURNED) Color(0xFF065F46) else Color(0xFFB45309),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 4-Stage Horizontal Step Bar with labels:
        // 'Assigned', 'Concierge En Route', 'Vehicle Picked Up', 'Returned'
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            stages.forEachIndexed { index, stage ->
                val isCompleted = activeStage.stageIndex > index || activeStage == ValetReturnStage.RETURNED
                val isCurrent = activeStage.stageIndex == index && activeStage != ValetReturnStage.RETURNED

                val nodeBgColor by animateColorAsState(
                    targetValue = when {
                        isCompleted -> StatusGreen
                        isCurrent -> ValetGold
                        else -> Slate100
                    },
                    label = "node_bg_$index"
                )

                val nodeContentColor = when {
                    isCompleted -> Color.White
                    isCurrent -> Navy900
                    else -> Slate500
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("valet_stage_node_${stage.name}")
                        .then(
                            if (onStageSelected != null) {
                                Modifier.clickable { onStageSelected(stage.targetBookingStatus) }
                            } else {
                                Modifier
                            }
                        )
                ) {
                    // Node Circle + Connector Row
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.height(40.dp)
                    ) {
                        if (isCurrent) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .scale(pulseScale)
                                    .clip(CircleShape)
                                    .background(ValetGold.copy(alpha = 0.3f))
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(nodeBgColor)
                                .border(
                                    width = if (isCurrent) 2.dp else 1.dp,
                                    color = when {
                                        isCompleted -> StatusGreen
                                        isCurrent -> Navy900
                                        else -> Slate300
                                    },
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isCompleted) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "${stage.label} Completed",
                                    tint = nodeContentColor,
                                    modifier = Modifier.size(18.dp)
                                )
                            } else {
                                Icon(
                                    imageVector = stage.icon,
                                    contentDescription = stage.label,
                                    tint = nodeContentColor,
                                    modifier = Modifier.size(17.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = stage.label,
                        fontSize = 11.sp,
                        fontWeight = if (isCurrent || isCompleted) FontWeight.Bold else FontWeight.Medium,
                        color = when {
                            isCurrent -> Navy900
                            isCompleted -> Color(0xFF065F46)
                            else -> Slate500
                        },
                        textAlign = TextAlign.Center,
                        lineHeight = 13.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(horizontal = 2.dp)
                    )
                }
            }
        }

        // Segmented Progress Bar connecting the 4 stages
        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            stages.forEachIndexed { index, _ ->
                val isSegmentFilled = activeStage.stageIndex >= index
                val segmentColor by animateColorAsState(
                    targetValue = when {
                        activeStage == ValetReturnStage.RETURNED -> StatusGreen
                        activeStage.stageIndex > index -> StatusGreen
                        activeStage.stageIndex == index -> ValetGold
                        else -> Slate100
                    },
                    label = "segment_$index"
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(5.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(segmentColor)
                )
            }
        }

        if (showDetailedTimeline) {
            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = Slate100)
            Spacer(modifier = Modifier.height(10.dp))

            // Vertical Stage Timeline Cards
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                stages.forEachIndexed { index, stage ->
                    val isCompleted = activeStage.stageIndex > index || activeStage == ValetReturnStage.RETURNED
                    val isCurrent = activeStage.stageIndex == index && activeStage != ValetReturnStage.RETURNED

                    val rowBgColor = when {
                        isCurrent -> ValetGoldLight.copy(alpha = 0.45f)
                        isCompleted -> StatusGreenBg.copy(alpha = 0.35f)
                        else -> Slate100.copy(alpha = 0.55f)
                    }

                    val borderColor = when {
                        isCurrent -> ValetGold
                        isCompleted -> StatusGreen.copy(alpha = 0.4f)
                        else -> Color.Transparent
                    }

                    Surface(
                        color = rowBgColor,
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("valet_stage_row_${stage.name}")
                            .then(
                                if (onStageSelected != null) {
                                    Modifier.clickable { onStageSelected(stage.targetBookingStatus) }
                                } else {
                                    Modifier
                                }
                            )
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            isCompleted -> StatusGreen
                                            isCurrent -> Navy900
                                            else -> Slate300
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isCompleted) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(15.dp)
                                    )
                                } else {
                                    Icon(
                                        imageVector = stage.icon,
                                        contentDescription = null,
                                        tint = if (isCurrent) ValetGold else Slate700,
                                        modifier = Modifier.size(15.dp)
                                    )
                                }
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = stage.label,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isCurrent || isCompleted) Slate900 else Slate500
                                    )
                                    Text(
                                        text = when {
                                            isCompleted -> "Completed"
                                            isCurrent -> "Active Now"
                                            else -> "Pending"
                                        },
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = when {
                                            isCompleted -> StatusGreen
                                            isCurrent -> Color(0xFFB45309)
                                            else -> Slate500
                                        }
                                    )
                                }
                                Text(
                                    text = if (isCurrent && currentStatus == BookingStatus.AT_CURBSIDE) {
                                        "Concierge has arrived at your terminal curb with hazard lights on."
                                    } else {
                                        stage.subtitle
                                    },
                                    fontSize = 11.sp,
                                    color = if (isCurrent || isCompleted) Slate700 else Slate500,
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
