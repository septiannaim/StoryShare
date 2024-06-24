package com.example.storyshare.utils

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment
import com.example.storyshare.R
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

private const val FILENAME_PATTERN = "dd-MMM-yyyy"

val currentTimestamp: String = SimpleDateFormat(
    FILENAME_PATTERN,
    Locale.US
).format(System.currentTimeMillis())

fun generateTemporaryFile(context: Context): File {
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(currentTimestamp, ".jpg", storageDir)
}

fun generateFile(application: Application): File {
    val mediaDir = application.externalMediaDirs.firstOrNull()?.let {
        File(it, application.resources.getString(R.string.app_name)).apply { mkdirs() }
    }
    val outputDirectory = if (mediaDir != null && mediaDir.exists()) mediaDir else application.filesDir
    return File(outputDirectory, "$currentTimestamp.jpg")
}

fun adjustBitmapOrientation(bitmap: Bitmap, isBackCamera: Boolean = false): Bitmap {
    val matrix = Matrix()
    return if (isBackCamera) {
        matrix.postRotate(90f)
        Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    } else {
        matrix.postRotate(-90f)
        matrix.postScale(1f, 1f, bitmap.width / 2f, bitmap.height / 2f)
        Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
}

fun convertUriToFile(selectedImg: Uri, context: Context): File {
    val contentResolver: ContentResolver = context.contentResolver
    val tempFile = generateTemporaryFile(context)

    val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
    val outputStream: OutputStream = FileOutputStream(tempFile)
    val buffer = ByteArray(1024)
    var length: Int
    while (inputStream.read(buffer).also { length = it } > 0) outputStream.write(buffer, 0, length)
    outputStream.close()
    inputStream.close()

    return tempFile
}

fun optimizeFileImage(file: File): File {
    val bitmap = BitmapFactory.decodeFile(file.path)
    var compressionQuality = 100
    var streamSize: Int

    do {
        val bmpStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressionQuality, bmpStream)
        val bmpPicByteArray = bmpStream.toByteArray()
        streamSize = bmpPicByteArray.size
        compressionQuality -= 5
    } while (streamSize > 1000000)
    bitmap.compress(Bitmap.CompressFormat.JPEG, compressionQuality, FileOutputStream(file))
    return file
}
