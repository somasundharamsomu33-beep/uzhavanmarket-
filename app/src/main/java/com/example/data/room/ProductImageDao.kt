package com.example.data.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductImageDao {

    @Query("SELECT * FROM product_images WHERE product_id = :productId ORDER BY is_primary DESC, created_at DESC")
    fun getImagesForProduct(productId: String): Flow<List<ProductImageEntity>>

    @Query("SELECT * FROM product_images WHERE listing_id = :listingId ORDER BY is_primary DESC, created_at DESC")
    fun getImagesForListing(listingId: String): Flow<List<ProductImageEntity>>

    @Query("SELECT * FROM product_images WHERE product_id = :productId AND is_primary = 1 LIMIT 1")
    fun getPrimaryImageForProduct(productId: String): Flow<ProductImageEntity?>

    @Query("SELECT * FROM product_images WHERE id = :id LIMIT 1")
    suspend fun getImageById(id: String): ProductImageEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImage(image: ProductImageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImages(images: List<ProductImageEntity>)

    @Update
    suspend fun updateImage(image: ProductImageEntity)

    @Query("UPDATE product_images SET is_primary = 0 WHERE product_id = :productId")
    suspend fun clearPrimaryStatusForProduct(productId: String)

    @Transaction
    suspend fun setPrimaryImage(productId: String, imageId: String) {
        clearPrimaryStatusForProduct(productId)
        markAsPrimary(imageId)
    }

    @Query("UPDATE product_images SET is_primary = 1 WHERE id = :imageId")
    suspend fun markAsPrimary(imageId: String)

    @Query("DELETE FROM product_images WHERE id = :id")
    suspend fun deleteImageById(id: String)

    @Query("DELETE FROM product_images WHERE product_id = :productId")
    suspend fun deleteImagesForProduct(productId: String)
}
