package com.elian.computeit.core.data.mapper

import com.elian.computeit.core.data.model.OperationData
import com.elian.computeit.core.data.model.TestData
import com.elian.computeit.core.domain.models.OperationInfo
import com.elian.computeit.core.domain.models.OperationType
import com.elian.computeit.core.domain.models.TestChartInfo
import com.elian.computeit.core.domain.models.TestInfo
import com.elian.computeit.core.domain.models.TestListInfo
import com.elian.computeit.core.domain.models.TestStatsInfo
import com.elian.computeit.core.util.constants.defaultFullDateFormat
import com.elian.computeit.core.util.constants.secondsToDhhmmss
import com.elian.computeit.core.util.extensions.getListOfValuePerSecond
import com.elian.computeit.core.util.extensions.isError
import com.elian.computeit.core.util.extensions.orZero
import com.elian.computeit.core.util.extensions.result
import java.util.Date
import kotlin.math.ceil

fun OperationData.toOperationInfo() = OperationInfo(
	pairOfNumbers = pairOfNumbers,
	operationSymbol = OperationType.nameToSymbol[operationName]!!,
	result = result,
	insertedResult = insertedResult,
)

fun TestData.toTestInfo(): TestInfo {
	val listOfOpmPerSecond = this.getListOfValuePerSecond(
		countCondition = { !it.isError },
		getValue = { currentSecond, currentOperationCount ->
			currentOperationCount / currentSecond.toFloat() * 60
		},
	)
	val listOfRawOpmPerSecond = this.getListOfValuePerSecond(
		getValue = { currentSecond, currentOperationCount ->
			currentOperationCount / currentSecond.toFloat() * 60
		},
	)

	val minOpm = listOfOpmPerSecond.values.minOrNull().orZero()
	val minRawOpm = listOfRawOpmPerSecond.values.minOrNull().orZero()
	val maxOpm = listOfOpmPerSecond.values.maxOrNull().orZero()
	val maxRawOpm = listOfRawOpmPerSecond.values.maxOrNull().orZero()

	val errorYValuePercentage = 0.5F

	return TestInfo(
		chartInfo = TestChartInfo(
			listOfOpmPerSecond = listOfOpmPerSecond,
			listOfRawOpmPerSecond = listOfRawOpmPerSecond,
			errorSeconds = listOfOperationData.filter {
				it.millisSinceStart >= 1000 && it.isError
			}.map {
				it.millisSinceStart / 1000F
			},
			errorsYValue = (errorYValuePercentage * (minOf(minOpm, minRawOpm) + maxOf(maxOpm, maxRawOpm))).toInt(),
		),
		statsInfo = TestStatsInfo(
			date = defaultFullDateFormat.format(Date(dateUnix)),
			opm = listOfOpmPerSecond.values.lastOrNull().orZero(),
			rawOpm = listOfRawOpmPerSecond.values.lastOrNull().orZero(),
			minOpm = minOpm,
			maxOpm = maxOpm,
			minRawOpm = minRawOpm,
			maxRawOpm = maxRawOpm,
			timeInSeconds = timeInSeconds,
			operationCount = listOfOperationData.size,
			errorCount = listOfOperationData.count { it.isError },
		),
		listOfFailedOperationInfo = listOfOperationData.filter { it.isError }.map { it.toOperationInfo() },
	)
}

fun List<TestData>.toTestListInfo(): TestListInfo {
	val totalTimeInSeconds = sumOf { it.timeInSeconds }
	val operationsCompletedCount = sumOf { it.listOfOperationData.size }
	val correctOperationsCompletedCount = sumOf { it.listOfOperationData.count { data -> !data.isError } }
	val testsCompletedWithoutErrorsCount = count { it.listOfOperationData.all { operation -> !operation.isError } }

	val listOfTestInfo = map { it.toTestInfo() }
	val listOfOpmPerTest = listOfTestInfo.map { it.statsInfo.opm }
	val listOfRawOpmPerTest = listOfTestInfo.map { it.statsInfo.rawOpm }

	val minOpm = listOfOpmPerTest.minOrNull().orZero()
	val maxOpm = listOfOpmPerTest.maxOrNull().orZero()

	val maxAndMinOpmDifference = (maxOpm - minOpm).toInt()

	val defaultRangeLength = getBarRangeLength(
		barCount = 5,
		maxAndMinOpmDifference = maxAndMinOpmDifference,
	)
	val testsPerSpeedRange = getTestCountPerSpeedRange(
		listOfOpmPerTest = listOfOpmPerTest,
		speedRangeLength = defaultRangeLength,
	)

	return TestListInfo(
		listOfOpmPerTest = listOfOpmPerTest,
		listOfRawOpmPerTest = listOfRawOpmPerTest,
		listOfTestInfo = listOfTestInfo,

		speedRangeLength = defaultRangeLength,
		testsPerSpeedRange = testsPerSpeedRange,
		speedRangeLengthValueFrom = 1,
		speedRangeLengthValueTo = maxAndMinOpmDifference.coerceAtLeast(2),

		testsCompletedCount = this.size,
		testsCompletedWithoutErrorsCount = testsCompletedWithoutErrorsCount,
		testsCompletedWithoutErrorsPercentage = (100F * testsCompletedWithoutErrorsCount / this.size).orZero(),
		formattedTotalTime = secondsToDhhmmss(totalTimeInSeconds),
		operationsCompleted = operationsCompletedCount,
		correctOperationsCompletedCount = correctOperationsCompletedCount,
		correctOperationsCompletedPercentage = (100F * correctOperationsCompletedCount / operationsCompletedCount).orZero(),
		averageOpm = listOfOpmPerTest.average().toFloat().orZero(),
		averageRawOpm = listOfRawOpmPerTest.average().orZero().toFloat(),
		minOpm = minOpm,
		maxOpm = maxOpm,
		minRawOpm = listOfRawOpmPerTest.minOrNull().orZero(),
		maxRawOpm = listOfRawOpmPerTest.maxOrNull().orZero(),
	)
}

fun List<TestData>.toTestCountPerSpeedRange(rangeLength: Int): List<Int> {
	val listOfOpmPerTest = map {
		val correctOperations = it.listOfOperationData.count { operation -> !operation.isError }
		val testOpm = (correctOperations / it.timeInSeconds.toFloat() * 60).orZero()

		testOpm
	}

	return getTestCountPerSpeedRange(
		listOfOpmPerTest = listOfOpmPerTest,
		speedRangeLength = rangeLength,
	)
}


private fun getTestCountPerSpeedRange(
	listOfOpmPerTest: List<Float>,
	speedRangeLength: Int,
): List<Int> {
	val minOpm = listOfOpmPerTest.minOrNull().orZero()
	val maxOpm = listOfOpmPerTest.maxOrNull().orZero()

	val rangeCount = if (maxOpm != 0F) {
		((maxOpm - minOpm) / speedRangeLength.toFloat() + 1).orZero().toInt()
	}
	else 0

	val listOfTestsPerSpeedRange = IntArray(rangeCount)

	listOfOpmPerTest.forEach { testOpm ->

		val testRangePosition = ((testOpm - minOpm) / speedRangeLength).orZero().toInt()

		listOfTestsPerSpeedRange[testRangePosition]++
	}

	return listOfTestsPerSpeedRange.toList()
}

private fun getBarRangeLength(barCount: Int, maxAndMinOpmDifference: Int): Int {
	return ceil(maxAndMinOpmDifference / barCount.toFloat()).toInt() + 1
}