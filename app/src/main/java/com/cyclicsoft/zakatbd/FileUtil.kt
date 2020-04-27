package com.cyclicsoft.zakatbd

import android.content.Context
import android.util.Log
import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream


object FileUtil {
    val TAG = FileUtil::class.java.simpleName
    fun writeJson(context: Context, jsonString: String, fileName: String) {
        Log.d(TAG, "saveJson( context: $context, jsonString: $jsonString, fileName: $fileName")
        if (jsonString.isBlank() || fileName.isBlank()) {
            return
        }
        try {
            //Get FilePath and use it to create  File
            val filePath = if (fileName.contains(".json", true)) {
                context.filesDir.toString() + "/" + fileName + ".json"
            } else {
                context.filesDir.toString() + "/" + fileName
            }
            val file = File(filePath)
            //Create FileOutputStream, File is part of the constructor
            val fileOutputStream = FileOutputStream(file)
            //Convert JSON String to Bytes and write() it
            fileOutputStream.write(jsonString.toByteArray())
            //Finally flush and close FileOutputStream
            fileOutputStream.flush()
            fileOutputStream.close()
        } catch (exception: Exception) {
            Log.e(
                TAG,
                "writeJson( context: $context, jsonString: $jsonString, fileName: $fileName" + "Exception${exception.localizedMessage}"
            )
        }
    }

    fun readJson(context: Context, fileName: String): String {
        Log.d(TAG, "readJson( context: $context, fileName: $fileName")
        var jsonResult = Errors.UNKNOWN
        if (fileName.isBlank() || fileName == Errors.UNKNOWN) {
            return jsonResult
        }
        try {
            //Make  FilePath and File 
            val filePath = if (fileName.contains(".json", true)) {
                context.filesDir.toString() + "/" + fileName
            } else {
                context.filesDir.toString() + "/" + fileName + ".json"
            }
            Log.d(TAG, "readJson( )>>>  Path: $filePath")
            val file = File(filePath)
            //Make an InputStream with  File in the constructor
            val inputStream: InputStream = FileInputStream(file)
            val stringBuilder = StringBuilder()
            //Check to see if  inputStream is null
            //If it isn't use the inputStream to make a InputStreamReader
            val inputStreamReader = InputStreamReader(inputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            var receiveString: String?
            //Use a while loop to append the lines from the Buffered reader
            while (bufferedReader.readLine().also { receiveString = it } != null) {
                stringBuilder.append(receiveString)
            }
            //Close  InputStream and save stringBuilder as a String
            inputStream.close()
            jsonResult = stringBuilder.toString()
        } catch (exception: FileNotFoundException) {
            Log.e(
                TAG,
                "readJson( context: $context, fileName: $fileName" + "Exception${exception.localizedMessage}"
            )
        } catch (exception: Exception) {
            Log.e(
                TAG,
                "readJson( context: $context, fileName: $fileName" + "Exception${exception.localizedMessage}"
            )
        }
        return jsonResult
    }

    fun unzipJsonFromRaw(context: Context, fileName: String): String {
        Log.d(TAG, "readJsonFromRaw( context: $context, fileName: $fileName")
        var savedFileName = Errors.UNKNOWN
        if (fileName.isBlank()) {
            return savedFileName
        }
        val location = context.filesDir.toString()
        Log.d(TAG, "readJsonFromRaw( )>>>  Path: $location")
        try {
            val ins: InputStream = context.resources.openRawResource(
                context.resources.getIdentifier(
                    fileName,
                    "raw", context.packageName
                )
            )
            ZipInputStream(ins).use { zin ->
                var ze: ZipEntry? = zin.nextEntry
                val fileName = ze?.name
                if (!fileName.isNullOrBlank()) savedFileName = fileName
                while (ze != null) {
                    if (ze.isDirectory) {
                        val f = File(location, ze.name)
                        if (!f.exists()) if (!f.isDirectory) f.mkdirs()
                    } else {
                        FileOutputStream(File(location, ze.name)).use { fout ->
                            val buffer = ByteArray(8192)
                            var len = zin.read(buffer)
                            while (len != -1) {
                                fout.write(buffer, 0, len)
                                len = zin.read(buffer)
                            }
                            zin.closeEntry()
                            fout.close()
                        }

                    }

                    ze = zin.nextEntry
                }
                zin.close()
            }
            ins.close()
        } catch (exception: FileNotFoundException) {
            Log.e(
                TAG,
                "readJsonFromRaw( context: $context, fileName: $fileName" + "Exception${exception.localizedMessage}"
            )
        } catch (exception: IOException) {
            Log.e(
                TAG,
                "readJsonFromRaw( context: $context, fileName: $fileName" + "Exception${exception.localizedMessage}"
            )
        } catch (exception: Exception) {
            Log.e(
                TAG,
                "readJsonFromRaw( context: $context, fileName: $fileName" + "Exception${exception.localizedMessage}"
            )
        }
        return savedFileName
    }
}