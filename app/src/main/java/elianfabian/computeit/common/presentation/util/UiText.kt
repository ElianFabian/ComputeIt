@file:Suppress("DEPRECATION", "DEPRECATION_ERROR")

package elianfabian.computeit.common.presentation.util

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.BoolRes
import androidx.annotation.IntegerRes
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import java.io.Serializable

sealed interface UiText : Parcelable {
	fun asString(context: Context): String
}

fun UiText(value: String): UiText = when {
	value.isEmpty() -> EmptyUiText
	else -> DynamicString(value)
}

fun UiText(
	@StringRes
	resId: Int,
	args: UiTextArgList = EmptyUiArgs,
): UiText = StringResource(
	resId = resId,
	args = args.rawList,
)

fun UiText(
	@PluralsRes
	resId: Int,
	quantity: Int,
	args: UiTextArgList = EmptyUiArgs,
): UiText = PluralsResource(
	resId = resId,
	quantity = quantity,
	args = args.rawList,
)

fun uiTextArgsOf(arg0: Any?, vararg args: Any?): UiTextArgList {
	val uiArgs = buildList {
		add(arg0?.asUiArg())
		for (arg in args) {
			add(arg?.asUiArg())
		}
	}
	return UiTextArgListImpl(uiArgs)
}

fun stringResArg(
	@StringRes
	resId: Int,
	args: UiTextArgList = EmptyUiArgs,
): UiTextArg = StringResourceArg(resId, args.rawList)

fun pluralsResArg(
	@PluralsRes
	resId: Int,
	quantity: Int,
	args: UiTextArgList = EmptyUiArgs,
): UiTextArg = PluralsResourceArg(resId, quantity, args.rawList)

fun integerResArg(@IntegerRes resId: Int): UiTextArg = IntegerResourceArg(resId)

fun booleanResArg(@BoolRes resId: Int): UiTextArg = BooleanResourceArg(resId)


private val EmptyUiText: UiText = DynamicString("")

private data class DynamicString(val value: String) : UiText {

	override fun asString(context: Context) = value

	override fun writeToParcel(dest: Parcel, flags: Int) {
		dest.writeString(value)
	}

	override fun describeContents() = 0


	companion object CREATOR : Parcelable.Creator<DynamicString> {
		override fun createFromParcel(parcel: Parcel) = DynamicString(parcel.readString().orEmpty())
		override fun newArray(size: Int): Array<DynamicString?> = arrayOfNulls(size)
	}
}

private data class StringResource(
	@StringRes
	val resId: Int,
	val args: List<UiTextArg?>,
) : UiText {

	override fun asString(context: Context): String {
		val arguments = args.map { arg ->
			arg?.getValue(context)
		}.toTypedArray()

		return String.format(
			format = context.getString(resId),
			args = arguments,
		)
	}

	override fun writeToParcel(dest: Parcel, flags: Int) {
		dest.writeInt(resId)
		dest.writeList(args)
	}

	override fun describeContents() = 0


	companion object CREATOR : Parcelable.Creator<StringResource> {
		override fun createFromParcel(parcel: Parcel) = StringResource(
			resId = parcel.readInt(),
			args = parcel.readList(),
		)

		override fun newArray(size: Int): Array<StringResource?> = arrayOfNulls(size)
	}
}

private data class PluralsResource(
	@PluralsRes
	val resId: Int,
	val quantity: Int,
	val args: List<UiTextArg?>,
) : UiText {

	override fun asString(context: Context): String {
		val arguments = args.map { arg ->
			arg?.getValue(context)
		}.toTypedArray()

		return String.format(
			format = context.resources.getQuantityString(resId, quantity),
			args = arguments,
		)
	}

	override fun writeToParcel(dest: Parcel, flags: Int) {
		dest.writeInt(resId)
		dest.writeInt(quantity)
		dest.writeList(args)
	}

	override fun describeContents() = 0


	companion object CREATOR : Parcelable.Creator<PluralsResource> {
		override fun createFromParcel(parcel: Parcel) = PluralsResource(
			resId = parcel.readInt(),
			quantity = parcel.readInt(),
			args = parcel.readList(),
		)

		override fun newArray(size: Int): Array<PluralsResource?> = arrayOfNulls(size)
	}
}

@Deprecated("Hidden from intellisense", level = DeprecationLevel.HIDDEN)
sealed interface UiTextArgList : Parcelable {
	private companion object
}

private class UiTextArgListImpl(
	val sourceList: List<UiTextArg?>,
) : UiTextArgList, List<UiTextArg?> by sourceList {

	override fun toString(): String = sourceList.toString()

	override fun hashCode(): Int = sourceList.hashCode()

	override fun equals(other: Any?): Boolean {
		if (this === other) {
			return true
		}
		if (other is UiTextArgListImpl) {
			return sourceList == other.sourceList
		}
		return false
	}

	override fun writeToParcel(dest: Parcel, flags: Int) {
		dest.writeList(sourceList)
	}

	override fun describeContents() = 0


	companion object CREATOR : Parcelable.Creator<UiTextArgListImpl> {
		override fun createFromParcel(parcel: Parcel) = UiTextArgListImpl(parcel.readList())
		override fun newArray(size: Int): Array<UiTextArgListImpl?> = arrayOfNulls(size)
	}
}

private val UiTextArgList.rawList: List<UiTextArg?>
	get() {
		this as UiTextArgListImpl
		return sourceList
	}

private val EmptyUiArgs: UiTextArgList = UiTextArgListImpl(emptyList())


private data class SerializableArg(val value: Serializable) : UiTextArg {

	override fun writeToParcel(dest: Parcel, flags: Int) {
		dest.writeSerializable(value)
	}

	override fun describeContents() = 0

	override fun toString() = value.toString()


	companion object CREATOR : Parcelable.Creator<SerializableArg> {
		override fun createFromParcel(parcel: Parcel) = SerializableArg(parcel.readSerializable() as Serializable)
		override fun newArray(size: Int): Array<SerializableArg?> = arrayOfNulls(size)
	}
}


private data class ParcelableArg(val value: Parcelable) : UiTextArg {

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeParcelable(value, flags)
	}

	override fun describeContents() = 0

	override fun toString() = value.toString()


	companion object CREATOR : Parcelable.Creator<ParcelableArg> {
		override fun createFromParcel(parcel: Parcel) = ParcelableArg(parcel.readParcelable(Parcelable::class.java.classLoader)!!)
		override fun newArray(size: Int): Array<ParcelableArg?> = arrayOfNulls(size)
	}
}

@Deprecated("Hidden from intellisense", level = DeprecationLevel.HIDDEN)
sealed interface UiTextArg : Parcelable {
	private companion object
}

private data class BooleanResourceArg(@BoolRes val resId: Int) : UiTextArg {

	override fun writeToParcel(dest: Parcel, flags: Int) {
		dest.writeInt(resId)
	}

	override fun describeContents() = 0

	override fun toString() = "booleanRes(id=$resId)"


	companion object CREATOR : Parcelable.Creator<BooleanResourceArg> {
		override fun createFromParcel(parcel: Parcel) = BooleanResourceArg(parcel.readInt())
		override fun newArray(size: Int): Array<BooleanResourceArg?> = arrayOfNulls(size)
	}
}

private data class IntegerResourceArg(@IntegerRes val resId: Int) : UiTextArg {

	override fun writeToParcel(dest: Parcel, flags: Int) {
		dest.writeInt(resId)
	}

	override fun describeContents() = 0

	override fun toString() = "integerRes(id=$resId)"


	companion object CREATOR : Parcelable.Creator<IntegerResourceArg> {
		override fun createFromParcel(parcel: Parcel) = IntegerResourceArg(parcel.readInt())

		override fun newArray(size: Int): Array<IntegerResourceArg?> = arrayOfNulls(size)
	}
}

private data class StringResourceArg(
	@StringRes
	val resId: Int,
	val args: List<UiTextArg?>,
) : UiTextArg {

	override fun writeToParcel(dest: Parcel, flags: Int) {
		dest.writeInt(resId)
		dest.writeList(args)
	}

	override fun describeContents() = 0

	override fun toString() = "stringRes(id=$resId, args=$args)"


	companion object CREATOR : Parcelable.Creator<StringResourceArg> {
		override fun createFromParcel(parcel: Parcel) = StringResourceArg(
			resId = parcel.readInt(),
			args = parcel.readList(),
		)

		override fun newArray(size: Int): Array<StringResourceArg?> = arrayOfNulls(size)
	}
}

private data class PluralsResourceArg(
	@PluralsRes
	val resId: Int,
	val quantity: Int,
	val args: List<UiTextArg?>,
) : UiTextArg {

	override fun writeToParcel(dest: Parcel, flags: Int) {
		dest.writeInt(resId)
		dest.writeInt(quantity)
		dest.writeList(args)
	}

	override fun describeContents() = 0

	override fun toString() = "pluralsRes(id=$resId, quantity=$quantity, args=$args)"


	companion object CREATOR : Parcelable.Creator<PluralsResourceArg> {
		override fun createFromParcel(parcel: Parcel) = PluralsResourceArg(
			resId = parcel.readInt(),
			quantity = parcel.readInt(),
			args = parcel.readList(),
		)

		override fun newArray(size: Int): Array<PluralsResourceArg?> = arrayOfNulls(size)
	}
}

private fun UiTextArg.getValue(context: Context): Any {
	val resources = context.resources

	return when (this) {
		is SerializableArg -> value
		is ParcelableArg -> value
		is BooleanResourceArg -> resources.getBoolean(resId)
		is IntegerResourceArg -> resources.getInteger(resId)
		is StringResourceArg -> {
			String.format(
				format = resources.getString(resId),
				args = args.map { arg -> arg?.getValue(context) }.toTypedArray(),
			)
		}
		is PluralsResourceArg -> {
			String.format(
				format = resources.getQuantityString(resId, quantity),
				args = args.map { arg -> arg?.getValue(context) }.toTypedArray(),
			)
		}
	}
}


private fun Any.asUiArg(): UiTextArg = when (this) {
	is UiTextArg -> this
	is Parcelable -> ParcelableArg(this)
	is Serializable -> SerializableArg(this)
	else -> throw IllegalArgumentException("Unsupported type: ${this::class}. Only Serializable and Parcelable types are supported.")
}


private inline fun <reified T> Parcel.readList(): List<T> {
	val outList = mutableListOf<T>()

	readList(outList, T::class.java.classLoader)

	return outList
}
