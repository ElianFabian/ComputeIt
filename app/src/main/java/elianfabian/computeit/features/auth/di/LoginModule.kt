package elianfabian.computeit.features.auth.di

import com.zhuinden.simplestack.ServiceBinder
import com.zhuinden.simplestackextensions.servicesktx.add
import elianfabian.computeit.common.util.simplestack.CoroutineScopedServiceModule
import elianfabian.computeit.features.auth.presentation.login.LoginViewModel

data object LoginModule : CoroutineScopedServiceModule() {

	override fun bindModuleServices(serviceBinder: ServiceBinder) {
		val viewModel = LoginViewModel(
			backstack = serviceBinder.backstack,
		)

		with(serviceBinder) {
			add(viewModel)
		}
	}
}
