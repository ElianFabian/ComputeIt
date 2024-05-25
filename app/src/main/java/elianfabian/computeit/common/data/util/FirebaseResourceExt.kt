package elianfabian.computeit.common.data.util

//suspend inline fun <D> Resource.Companion.safeFirestoreCall(
//	crossinline call: suspend () -> D,
//): Resource<D, Nothing?> {
//	try {
//		val response = call()
//
//		return Resource.Success(response)
//	}
//	catch (e: CancellationException) {
//		throw e
//	}
//	catch (e: FirebaseFirestoreException) {
//		e.printStackTrace()
//		e.code
//		return Resource.Error(NetworkRequestError.IoError)
//	}
//	catch (e: Exception) {
//		e.printStackTrace()
//		return Resource.Error(NetworkRequestError.UnknownError)
//	}
//}