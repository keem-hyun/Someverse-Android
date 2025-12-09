package com.someverse.domain.usecase.file

import com.someverse.domain.repository.FileRepository
import java.io.File
import javax.inject.Inject

/**
 * Upload Single Image UseCase
 * - Handles single image upload
 * - Validates file before upload
 * - Returns S3 URL
 */
class UploadImageUseCase @Inject constructor(
    private val fileRepository: FileRepository
) {
    /**
     * Execute single image upload
     *
     * @param file File to upload
     * @param folder Folder category (e.g., "profile-images", "post-images")
     * @return Result<String> containing S3 URL
     */
    suspend operator fun invoke(
        file: File,
        folder: String = "profile-images"
    ): Result<String> {
        // Business logic: Validate file
        if (!file.exists()) {
            return Result.failure(IllegalArgumentException("File does not exist"))
        }

        if (!file.isFile) {
            return Result.failure(IllegalArgumentException("Path is not a file"))
        }

        // Max size: 10MB
        val maxSizeBytes = 10 * 1024 * 1024L
        if (file.length() > maxSizeBytes) {
            return Result.failure(IllegalArgumentException("File size exceeds 10MB limit"))
        }

        // Delegate to repository
        return fileRepository.uploadImage(file, folder)
    }
}