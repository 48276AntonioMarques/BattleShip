package pt.isel.pdm.battleship.common

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

val JsonMediaType = "application/json".toMediaType()

suspend fun <T> api(
    client: OkHttpClient,
    requestBuilder: Request.Builder.() -> Unit,
    onFailure: (Call, IOException, Continuation<T>) -> Unit = {_, e, c -> c.resumeWithException(e) },
    onResponse: Continuation<T>.(Call, Response) -> T
): T {
    return suspendCoroutine { continuation ->
        val request = Request.Builder().apply(requestBuilder).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) = onFailure(call, e, continuation)
            override fun onResponse(call: Call, response: Response) {
                continuation.resume(continuation.onResponse(call, response))
            }
        })
    }
}

fun validateResponse(response: Response): Response? {
    val contentType = response.body?.contentType()
    val isSuccess = response.isSuccessful && contentType != null
    return if (isSuccess && contentType == SirenMediaType) response else null
}


fun Request.Builder.createGet(url: String) = url(url)
fun Request.Builder.createPost(url: String, body: String?, mediaType: MediaType) = apply {
    url(url)
    method("POST", body?.toRequestBody(mediaType))
}