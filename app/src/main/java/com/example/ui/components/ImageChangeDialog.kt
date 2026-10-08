package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ImageSourceType
import com.example.ui.theme.*

/**
 * Image Management Modal Sheet / Dialog:
 * Supports:
 * 1. 📷 Take Photo (Camera upload flow)
 * 2. 🖼️ Choose from Gallery
 * 3. ✨ Generate Product Image (AI prompt template for representative photo)
 * Includes crop/preview confirmation step before saving.
 */
@Composable
fun ImageChangeDialog(
    cropName: String,
    onDismiss: () -> Unit,
    onImageSelected: (sourceType: ImageSourceType, isAiRepresentative: Boolean) -> Unit
) {
    var selectedAction by remember { mutableStateOf<String?>(null) }
    var isSimulatingCapture by remember { mutableStateOf(false) }
    var previewSourceType by remember { mutableStateOf(ImageSourceType.FARMER_UPLOAD) }
    var previewIsAi by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (selectedAction == null) "Change Product Image" else "Preview & Crop Photo",
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = AgriGreenDark
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                if (selectedAction == null) {
                    Text(
                        text = "Upload real produce photograph or generate a representative visual for $cropName:",
                        fontSize = 13.sp,
                        color = AgriTextSecondary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 1. Take Photo
                    Card(
                        onClick = {
                            selectedAction = "CAMERA"
                            previewSourceType = ImageSourceType.FARMER_UPLOAD
                            previewIsAi = false
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFDCFCE7)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.CameraAlt, contentDescription = null, tint = AgriGreenDark)
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text("Take Photo", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = AgriTextPrimary)
                                Text("Capture real harvest from camera", fontSize = 11.sp, color = AgriTextMuted)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // 2. Choose from Gallery
                    Card(
                        onClick = {
                            selectedAction = "GALLERY"
                            previewSourceType = ImageSourceType.FARMER_UPLOAD
                            previewIsAi = false
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE0F2FE)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.PhotoLibrary, contentDescription = null, tint = Color(0xFF0284C7))
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text("Choose from Gallery", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = AgriTextPrimary)
                                Text("Select high quality farm photo", fontSize = 11.sp, color = AgriTextMuted)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // 3. AI Generate Representative Image
                    Card(
                        onClick = {
                            selectedAction = "AI_GENERATE"
                            previewSourceType = ImageSourceType.AI_REPRESENTATIVE
                            previewIsAi = true
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFEEF2FF)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFC7D2FE)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE0E7FF)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color(0xFF4338CA))
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text("Generate Product Image", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF312E81))
                                Text("Creates realistic representative image (AI)", fontSize = 11.sp, color = Color(0xFF6366F1))
                            }
                        }
                    }
                } else {
                    // Preview & Crop Flow
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        RealisticProductImage(
                            cropName = cropName,
                            height = 160.dp,
                            imageSourceType = previewSourceType,
                            isAiRepresentative = previewIsAi,
                            showSourceBadge = true
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedButton(
                                onClick = { /* Crop adjust */ },
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Default.Crop, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Crop (1:1)", fontSize = 11.sp)
                            }

                            OutlinedButton(
                                onClick = { /* Rotate adjust */ },
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Default.RotateRight, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Rotate 90°", fontSize = 11.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = if (previewIsAi) {
                                "✨ Will be labeled as 'Representative Image' to keep buyer trust transparent."
                            } else {
                                "✓ Real photo compressed to webp (800x800) for fast loading."
                            },
                            fontSize = 11.sp,
                            color = AgriTextSecondary
                        )
                    }
                }
            }
        },
        confirmButton = {
            if (selectedAction != null) {
                Button(
                    onClick = {
                        onImageSelected(previewSourceType, previewIsAi)
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AgriGreenDark)
                ) {
                    Text("Use This Photo")
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    if (selectedAction != null) {
                        selectedAction = null
                    } else {
                        onDismiss()
                    }
                }
            ) {
                Text(if (selectedAction != null) "Back" else "Cancel", color = AgriTextMuted)
            }
        }
    )
}
