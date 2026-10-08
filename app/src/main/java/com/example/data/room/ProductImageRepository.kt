package com.example.data.room

import kotlinx.coroutines.flow.Flow

class ProductImageRepository(private val dao: ProductImageDao) {

    fun getImagesForProduct(productId: String): Flow<List<ProductImageEntity>> {
        return dao.getImagesForProduct(productId)
    }

    fun getPrimaryImageForProduct(productId: String): Flow<ProductImageEntity?> {
        return dao.getPrimaryImageForProduct(productId)
    }

    suspend fun saveImage(image: ProductImageEntity) {
        if (image.isPrimary) {
            dao.setPrimaryImage(image.productId, image.id)
        }
        dao.insertImage(image)
    }

    suspend fun setPrimaryImage(productId: String, imageId: String) {
        dao.setPrimaryImage(productId, imageId)
    }

    suspend fun deleteImage(imageId: String) {
        dao.deleteImageById(imageId)
    }
}
