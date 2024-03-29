package com.elian.computeit.feature_tests.presentation.test_details

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import com.elian.computeit.R
import com.elian.computeit.core.domain.models.OperationInfo
import com.elian.computeit.core.domain.models.TestChartInfo
import com.elian.computeit.core.domain.models.TestStatsInfo
import com.elian.computeit.core.presentation.MainActivity
import com.elian.computeit.core.presentation.adapter.MainLabeledDataAdapter
import com.elian.computeit.core.presentation.model.labelOf
import com.elian.computeit.core.presentation.util.extensions.avoidConflictsWithScroll
import com.elian.computeit.core.presentation.util.extensions.navigate
import com.elian.computeit.core.presentation.util.mp_android_chart.applyDefault
import com.elian.computeit.core.presentation.util.mp_android_chart.lineDataSet
import com.elian.computeit.core.presentation.util.mp_android_chart.toEntries
import com.elian.computeit.core.presentation.util.mp_android_chart.valuesToEntriesWithYValue
import com.elian.computeit.core.presentation.util.viewBinding
import com.elian.computeit.core.util.constants.arguments
import com.elian.computeit.core.util.using
import com.elian.computeit.databinding.FragmentTestDetailsBinding
import com.elian.computeit.feature_tests.domain.args.TestDetailsArgs
import com.elian.computeit.feature_tests.presentation.test_details.adapter.FailedOperationAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TestDetailsFragment : Fragment(R.layout.fragment_test_details) {

	private val binding by viewBinding(FragmentTestDetailsBinding::bind)
	private val args by arguments<TestDetailsArgs>()


	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		(activity as MainActivity).onFragmentViewCreated(binding)

		initializeUi()
	}


	private fun initializeUi() = using(binding) {
		val info = args.testInfo

		initializeTestChart(info.chartInfo)
		initializeTextStats(info.statsInfo)

		info.listOfFailedOperationInfo.also {
			if (it.isEmpty()) {
				tvFailedOperations.isGone = true
				rvFailedOperations.isGone = true
			}
			else initializeFailedOperationsAdapter(it)
		}

		btnContinue.setOnClickListener { navigate(R.id.action_testDetailsFragment_to_homeFragment) }
	}

	private fun initializeTestChart(info: TestChartInfo) {
		val lineDataSets = arrayOf(
			lineDataSet(
				labelResId = R.string.Raw,
				lineAndCirclesColorResId = R.color.chart_secondary,
				entries = info.listOfRawOpmPerSecond.toEntries(),
			),
			lineDataSet(
				labelResId = R.string.OPM__OperationsPerMinute,
				entries = info.listOfOpmPerSecond.toEntries(),
			),
			lineDataSet(
				labelResId = R.string.Errors,
				lineAndCirclesColorResId = R.color.red_500,
				entries = info.errorSeconds.valuesToEntriesWithYValue(info.errorsYValue.toFloat()),
				isDashedLineEnable = false,
			) {
				circleRadius = 2F
			}
		)

		binding.viewTestChart.lineChart.applyDefault(dataSets = lineDataSets) {
			xAxis.granularity = 0.5F
		}

		binding.viewTestChart.lineChart.avoidConflictsWithScroll(binding.root)
	}

	private fun initializeTextStats(info: TestStatsInfo) = using(info) {
		val listOfLabeledData = listOf(
			R.string.Operations labelOf operationCount,
			R.string.TotalTime labelOf "$timeInSeconds s",
			R.string.OPM__OperationsPerMinute labelOf opm,
			R.string.Raw labelOf rawOpm,
			R.string.HighestOPM labelOf maxOpm,
			R.string.HighestRawOPM labelOf maxRawOpm,
			R.string.Errors labelOf errorCount,
		)

		binding.viewTextInfoList.rvLabeledData.adapter = MainLabeledDataAdapter(listOfLabeledData)
	}

	private fun initializeFailedOperationsAdapter(listOfFailedOperationInfo: List<OperationInfo>) {
		binding.rvFailedOperations.adapter = FailedOperationAdapter(listOfFailedOperationInfo)
	}
}