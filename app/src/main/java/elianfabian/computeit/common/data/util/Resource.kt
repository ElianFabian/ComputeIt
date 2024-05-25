package elianfabian.computeit.common.data.util

sealed interface Resource<out D, out E> {
	class Success<out D>(val data: D) : Resource<D, Nothing>
	class Error<out E>(val error: E) : Resource<Nothing, E>

	companion object
}
