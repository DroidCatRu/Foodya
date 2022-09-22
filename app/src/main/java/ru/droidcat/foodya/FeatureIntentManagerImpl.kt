package ru.droidcat.foodya

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ru.droidcat.core_utils.FeatureIntent
import ru.droidcat.core_utils.FeatureIntentManager
import ru.droidcat.core_utils.MainImmediateScope
import javax.inject.Inject

class FeatureIntentManagerImpl @Inject constructor(
    @MainImmediateScope private val externalMainImmediateScope: CoroutineScope
) : FeatureIntentManager {

    private val featureIntentChannel = Channel<FeatureIntent>(Channel.BUFFERED)
    override val featureIntent = featureIntentChannel.receiveAsFlow()

    override fun sendIntent(intent: FeatureIntent) {
        externalMainImmediateScope.launch {
            featureIntentChannel.send(intent)
        }
    }
}