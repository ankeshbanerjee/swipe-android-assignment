package com.example.swipeassignment.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import java.io.File

enum class UiState {
    LOADING,
    SUCCESS,
    ERROR,
    REFRESHING,
    IDLE,
}


fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}


fun getFileFromUri(contentResolver: ContentResolver, uri: Uri): File? {
    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
    val cursor = contentResolver.query(uri, filePathColumn, null, null, null)
    cursor?.moveToFirst()
    val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
    val filePath = cursor?.getString(columnIndex!!)
    cursor?.close()
    return filePath?.let { File(it) }
}