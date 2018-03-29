/*
 * Copyright 2018 Xennis
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.xennis.gradle.googlecloudstorage

import com.google.cloud.storage.Blob
import com.google.cloud.storage.BlobId
import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.TaskAction

import java.nio.file.Files

/**
 * Task to upload a directory to Google Cloud Storage.
 */
class GoogleCloudStorageUploadDirectoryTask extends DefaultTask {

    /**
     * Directory to upload.
     */
    @InputDirectory
    File inputDir

    /**
     * Name of the bucket upload target.
     */
    @Input
    String bucketName


    @TaskAction
    def taskAction() {
        if (!inputDir.isDirectory()) {
            throw new GradleException("Not a directory: " + inputDir)
        }

        String credentialsFile = System.getenv("GOOGLE_APPLICATION_CREDENTIALS")
        if (credentialsFile == null) {
            throw new GradleException("The environment variable GOOGLE_APPLICATION_CREDENTIALS is not set.\n" +
                    "\n" +
                    "The variable should contain the path to the JSON file, which contains the credentials\n" +
                    "of the service account to access the cloud storage. Details can be found at:\n" +
                    "https://cloud.google.com/storage/docs/reference/libraries#client-libraries-usage-java")
        }

        uploadDirectory(bucketName, inputDir)
    }

    static def uploadDirectory(String bucketName, File directory) {
        directory.traverse {
            if (it.isFile()) {
                String blobName = relativeFilePathString(directory, it)
                String contentType = Files.probeContentType(it.toPath())
                uploadFile(bucketName, blobName, contentType, it)
            }
        }
    }

    static def relativeFilePathString(File directory, File file) {
        return directory.getName() + file.getPath().minus(directory.toString())
    }

    static def uploadFile(String bucketName, String blobName, String contentType, File file) {
        Storage storage = StorageOptions.getDefaultInstance().getService()

        BlobId blobId = BlobId.of(bucketName, blobName)
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(contentType).build()
        Blob blob = storage.create(blobInfo, file.newInputStream());
    }
}

