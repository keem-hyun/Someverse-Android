package com.someverse.domain.repository

import java.io.File

/**
 * File Repository Interface
 * - Handles file upload/download operations via Backend API
 * - Uploads files to backend server which stores them in S3
 * - Returns S3 URLs from server
 * - Generic repository for reusability
 * - Implementation will be in data layer
 *
 * Flow:
 * 1. Client (Android) -> Backend API -> S3
 * 2. Backend returns S3 URL
 * 3. Client uses URL for other operations
 */
interface FileRepository {

    /**
     * Upload single image file
     *
     * @param file File to upload
     * @param folder Folder for organizing files (e.g., "profile-images", "post-images")
     * @return Result<String> containing S3 URL
     *
     * API: POST /files/upload
     * Response: { success: true, data: "https://s3.url/path/to/file.jpg" }
     */
    suspend fun uploadImage(
        file: File,
        folder: String = "profile-images"
    ): Result<String>

    /**
     * Upload multiple image files
     *
     * @param files List of files to upload (max 6)
     * @param folder Folder for organizing files (e.g., "profile-images")
     * @return Result<List<String>> containing list of S3 URLs
     *
     * API: POST /files/upload/multiple
     * Response: { success: true, data: ["url1", "url2", ...] }
     */
    suspend fun uploadMultipleImages(
        files: List<File>,
        folder: String = "general"
    ): Result<List<String>>

    /**
     * Delete file from S3
     *
     * @param fileUrl Full S3 URL of the file to delete
     * @return Result<Unit>
     *
     * API: DELETE /files/delete?fileUrl={url}
     * Response: { success: true, message: "파일 삭제에 성공했습니다." }
     */
    suspend fun deleteFile(
        fileUrl: String
    ): Result<Unit>
}