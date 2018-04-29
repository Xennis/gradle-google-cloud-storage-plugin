# Gradle Plugin - Google Cloud Storage

[![Build Status](https://travis-ci.org/Xennis/gradle-google-cloud-storage-plugin.svg?branch=master)](https://travis-ci.org/Xennis/gradle-google-cloud-storage-plugin)

Plugin to wrap the Google Cloud Storage Java library

## Usage

### Setup authentication

[Setting up authentication according to the documentation](https://cloud.google.com/storage/docs/reference/libraries#setting_up_authentication)
 of the Google Cloud Storage Java library. This plugin uses the environment variable `GOOGLE_APPLICATION_CREDENTIALS`, that
provides authentication credentials.

### Tasks

Task `googleCloudStorageUploading`:

|Variables  |Type   |Description                      |
|-----------|-------|---------------------------------|
|bucketName |String |Name of the bucket upload target |
|inputDir   |File   |Directory to upload              |


#### Exemplary usage

The example can be found in [example/build.gradle](example/build.gradle).

```
buildscript {
    repositories {
        mavenLocal()
    }

    dependencies {
        classpath 'org.xennis:gradle-google-cloud-storage:0.1.0'
    }
}

apply plugin: 'org.xennis.gradle-google-cloud-storage'

task('exampleUploadDirectory', type: org.xennis.gradle.googlecloudstorage.GoogleCloudStorageUploadDirectoryTask) {
    inputDir = file( 'src/main/resources' )
    bucketName = 'example-bucket'
}
```

## Resources

* [Google Cloud Storage Java Client Library documentation](https://cloud.google.com/storage/docs/reference/libraries#client-libraries-install-java)
