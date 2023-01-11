package pt.isel.pdm.battleship.common

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.lang.Exception
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

val JsonMediaType = "application/json".toMediaType()

open class ApiException(message: String?) : Exception(message)
class UnexpectedResponseTypeException(message: String?) : ApiException(message)


suspend fun <T> api(
    client: OkHttpClient,
    requestBuilder: Request.Builder.() -> Unit,
    onResponse: (Response) -> T
): T {
    return suspendCoroutine { continuation ->
        val request = Request.Builder().apply(requestBuilder).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) { continuation.resumeWithException(e) }
            override fun onResponse(call: Call, response: Response) {
                try {
                    continuation.resume(onResponse(response))
                }
                catch (t: Throwable) {
                    continuation.resumeWithException(t)
                }
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
    method("POST", body?.toRequestBody(mediaType)?: "".toRequestBody(mediaType))
}
fun Request.Builder.createDelete(url: String, body: String?, mediaType: MediaType) = apply {
    url(url)
    method("DELETE", body?.toRequestBody(mediaType))
}
fun Request.Builder.authenticate(authorization: String) = addHeader("Authorization", authorization)