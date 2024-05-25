package elianfabian.computeit.common

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentActivity
import com.zhuinden.simplestack.BackHandlingModel
import com.zhuinden.simplestack.SimpleStateChanger
import com.zhuinden.simplestack.navigator.Navigator
import com.zhuinden.simplestackextensions.navigatorktx.backstack
import com.zhuinden.simplestackextensions.services.DefaultServiceProvider
import elianfabian.computeit.R
import elianfabian.computeit.common.di.GlobalServiceProvider
import elianfabian.computeit.common.util.callback.OnMainBackstackIsInitializedCallback
import elianfabian.computeit.common.util.simplestack.FragmentStateChanger
import elianfabian.computeit.common.util.simplestack.ProcessDeathKeyFilter
import elianfabian.computeit.common.util.simplestack.forEachServiceOfType
import elianfabian.computeit.ui.theme.ComputeItTheme

class MainActivity : FragmentActivity() {

	private val backPressedCallback = object : OnBackPressedCallback(false) {
		override fun handleOnBackPressed() {
			backstack.goBack()
		}
	}

	/**
	 * This FrameLayout is the container used to implement Fragment based
	 * navigation in Simple-Stack with Compose based UI.
	 */
	private val fragmentContainerView by lazy {
		InnerPaddingFrameLayout(this).apply {
			id = R.id.MainFragmentContainerView
		}
	}


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()

		onBackPressedDispatcher.addCallback(backPressedCallback)

		setContent {
			ComputeItTheme {
				ComputeItApp { innerPadding ->
					FragmentScreenContainer(
						innerPadding = innerPadding,
						modifier = Modifier
							.fillMaxSize()
					)
				}
			}
		}

		val fragmentStateChanger = FragmentStateChanger(supportFragmentManager, fragmentContainerView.id)

		Navigator.configure()
			.setBackHandlingModel(BackHandlingModel.AHEAD_OF_TIME)
			.setStateChanger(
				SimpleStateChanger { stateChange ->
					fragmentStateChanger.handleStateChange(stateChange)
				}
			)
			.setScopedServices(DefaultServiceProvider())
			.setGlobalServices(GlobalServiceProvider(application))
			.setKeyFilter(ProcessDeathKeyFilter())
//			.install(
//				this,
//				fragmentContainerView,
//				History.single(TODO("Set initial key")),
//			)
//
//		backPressedCallback.isEnabled = backstack.willHandleAheadOfTimeBack()
//		backstack.observeAheadOfTimeWillHandleBackChanged(this) { willHandleBack ->
//			backPressedCallback.isEnabled = willHandleBack
//		}
//
//		if (savedInstanceState == null) {
//			initialSetup()
//		}
	}

	@Suppress("UNCHECKED_CAST")
	override fun <T : View?> findViewById(id: Int): T {
		if (id == fragmentContainerView.id) {
			return fragmentContainerView as T
		}
		return super.findViewById(id)
	}


	private fun initialSetup() {
		backstack.forEachServiceOfType<OnMainBackstackIsInitializedCallback> { service ->
			service.onMainBackstackIsInitialized(backstack)
		}
	}


	@Composable
	private fun FragmentScreenContainer(
		innerPadding: PaddingValues,
		modifier: Modifier = Modifier,
	) {
		AndroidView(
			factory = { fragmentContainerView },
			update = { container ->
				container.innerPadding.value = innerPadding
			},
			modifier = modifier
		)
	}
}


/**
 * FrameLayout that contains the inner padding provided by a Scaffold.
 *
 * This is to allow Fragments access this value.
 */
private class InnerPaddingFrameLayout(context: Context) : FrameLayout(context), InnerPaddingProvider {
	override var innerPadding = mutableStateOf(PaddingValues())
}

/**
 * Interface that allows to access the inner padding from the App's Scaffold.
 */
interface InnerPaddingProvider {
	val innerPadding: State<PaddingValues>
}
