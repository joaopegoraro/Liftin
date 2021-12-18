package xyz.joaophp.liftin.data.services.storage

import android.net.Uri
import xyz.joaophp.liftin.utils.StorageCallback
import xyz.joaophp.liftin.utils.StorageDownloadCallback

interface StorageService {

    fun upload(path: String, fileUri: Uri, cb: StorageCallback)

    fun download(path: String, cb: StorageDownloadCallback)

    fun delete(path: String, cb: StorageCallback)
}