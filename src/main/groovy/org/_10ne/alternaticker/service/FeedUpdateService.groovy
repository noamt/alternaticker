package org._10ne.alternaticker.service

import com.google.inject.Inject
import groovy.util.logging.Slf4j
import org._10ne.alternaticker.service.remote.FeedUpdate
import ratpack.server.Service
import ratpack.server.StartEvent
import ratpack.server.StopEvent

import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * @author Noam Y. Tenne
 */
@Slf4j
class FeedUpdateService implements Service {

    @Inject
    DataManagementService dataManagementService

    @Inject
    RemoteFeedService remoteFeedService

    private ScheduledThreadPoolExecutor executor

    @Override
    void onStart(StartEvent event) throws Exception {
        log.info('Starting feed update worker')
        executor = new ScheduledThreadPoolExecutor(1)
        def fetcher = new FeedUpdate(dataManagementService, remoteFeedService)
        executor.scheduleAtFixedRate(fetcher, 5, 5, TimeUnit.SECONDS)
    }

    @Override
    void onStop(StopEvent event) throws Exception {
        log.info('Shutting down feed update worker')
        executor.shutdownNow()
    }
}
