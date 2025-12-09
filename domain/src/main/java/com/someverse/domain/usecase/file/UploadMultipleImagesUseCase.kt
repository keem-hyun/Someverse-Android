package com.someverse.domain.usecase.file

import com.someverse.domain.repository.FileRepository
import java.io.File
import javax.inject.Inject

/**
 * Upload Multiple Images UseCase
 * - Handles multiple image upload (max 6 files)
 * - Validates files before upload
 * - Returns list of S3 URLs
 */
class UploadMultipleImagesUseCase @Inject constructor(
    private val fileRepository: FileRepository
) {
    /**
     * Execute multiple image upload
     *
     * @param files List of files to upload (max 6)
     * @param folder Folder category (e.g., "profile-images")
     * @return Result<List<String>> containing list of S3 URLs
     */
    suspend operator fun invoke(
        files: List<File>,
        folder: String = "profile-images"
    ): Result<List<String>> {
        // Business logic: Validate files
        if (files.isEmpty()) {
            return Result.failure(IllegalArgumentException("File list is empty"))
        }

        if (files.size > 6) {
            return Result.failure(IllegalArgumentException("Maximum 6 files allowed"))
        }

        val maxSizeBytes = 10 * 1024 * 1024L

        files.forEach { file ->
            if (!file.exists()) {
                return Result.failure(IllegalArgumentException("File does not exist: ${file.name}"))
            }

            if (!file.isFile) {
                return Result.failure(IllegalArgumentException("Path is not a file: ${file.name}"))
            }

            if (file.length() > maxSizeBytes) {
                return Result.failure(IllegalArgumentException("File size exceeds 10MB limit: ${file.name}"))
            }
        }

        // Delegate to repository
        return fileRepository.uploadMultipleImages(files, folder)
    }
}