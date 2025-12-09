package com.someverse.domain.usecase.file

import com.someverse.domain.repository.FileRepository
import javax.inject.Inject

/**
 * Delete File UseCase
 * - Handles file deletion from S3
 * - Validates file URL before deletion
 */
class DeleteFileUseCase @Inject constructor(
    private val fileRepository: FileRepository
) {
    /**
     * Execute file deletion
     *
     * @param fileUrl Full S3 URL of the file to delete
     * @return Result<Unit>
     */
    suspend operator fun invoke(fileUrl: String): Result<Unit> {
        // Business logic: Validate URL
        if (fileUrl.isBlank()) {
            return Result.failure(IllegalArgumentException("File URL is empty"))
        }

        if (!fileUrl.startsWith("http://") && !fileUrl.startsWith("https://")) {
            return Result.failure(IllegalArgumentException("Invalid file URL format"))
        }

        // Delegate to repository
        return fileRepository.deleteFile(fileUrl)
    }
}