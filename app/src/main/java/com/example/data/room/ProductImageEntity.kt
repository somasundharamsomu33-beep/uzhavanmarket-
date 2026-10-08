package com.example.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room Entity representing the 'product_images' table.
 * Supports the agricultural product image management system with fields for:
 * - source (e.g. farmer_upload, ai_generation, admin_upload)
 * - image_type (e.g. product_photo, ai_generated, representative, quality_photo)
 * - is_primary status flag
 */
@Entity(tableName = "product_images")
data class ProductImageEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "product_id")
    val productId: String,

    @ColumnInfo(name = "listing_id")
    val listingId: String,

    @ColumnInfo(name = "image_url")
    val imageUrl: String,

    @ColumnInfo(name = "thumbnail_url")
    val thumbnailUrl: String = "",

    @ColumnInfo(name = "medium_url")
    val mediumUrl: String = "",

    /**
     * Type of image: "product_photo", "ai_generated", "representative", "quality_photo"
     */
    @ColumnInfo(name = "image_type")
    val imageType: String = "product_photo",

    /**
     * Source of image: "farmer_upload", "ai_generation", "admin_upload"
     */
    @ColumnInfo(name = "source")
    val source: String = "farmer_upload",

    /**
     * Primary display status flag
     */
    @ColumnInfo(name = "is_primary")
    val isPrimary: Boolean = false,

    @ColumnInfo(name = "caption")
    val caption: String = "",

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = System.currentTimeMillis()
)
