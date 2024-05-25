package elianfabian.computeit.common.util.simplestack

import com.zhuinden.simplestack.ServiceBinder
import com.zhuinden.simplestackextensions.services.DefaultServiceProvider
import com.zhuinden.simplestackextensions.servicesktx.add

class ServiceProvider : DefaultServiceProvider() {

	@OptIn(ExperimentalStdlibApi::class)
	override fun bindServices(serviceBinder: ServiceBinder) {
		val key = serviceBinder.getKey<Any>()
		if (key is CoroutineScopedFragmentKey) {
			serviceBinder.add(key)
		}

		super.bindServices(serviceBinder)
	}
}
