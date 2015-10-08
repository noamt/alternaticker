package org._10ne.alternaticker.service.remote

import groovy.util.logging.Slf4j
import org._10ne.alternaticker.model.FeedEntry
import org._10ne.alternaticker.service.DataManagementService
import org._10ne.alternaticker.service.RemoteFeedService

/**
 * @author Noam Y. Tenne
 */
@Slf4j
class FeedUpdate implements Runnable {

    private DataManagementService dataManagementService
    private RemoteFeedService remoteFeedService

    FeedUpdate(DataManagementService dataManagementService, RemoteFeedService remoteFeedService) {
        this.dataManagementService = dataManagementService
        this.remoteFeedService = remoteFeedService
    }

    private long nextEventStartId = 0

    @Override
    void run() {
        Collection rawJsonFeed = feedAsJson()
        List<FeedEntry> feedEntries = jsonAsFeedModel(rawJsonFeed)
        updateNextEventStartId(feedEntries)
        submitEntries(feedEntries)
    }

    private Collection feedAsJson() throws Throwable {
        remoteFeedService.getFeedAsJson(nextEventStartId)
    }

    private List<FeedEntry> jsonAsFeedModel(Collection rawJsonFeed) {
        rawJsonFeed.collect { Map jsonEntry ->
            def id = jsonEntry.id.toLong()
            def countryCode = jsonEntry.benchmarkRunModel.countryCode
            def overallScore = jsonEntry.benchmarkRunModel.overallScore
            new FeedEntry(id: id, countryCode: countryCode, overallScore: overallScore)
        }
    }

    private void updateNextEventStartId(List<FeedEntry> feedEntries) {
        def max = feedEntries.max { FeedEntry entry -> entry.id }
        if (max) {
            nextEventStartId = max.id
        }
    }

    private void submitEntries(List<FeedEntry> feedEntries) {
        dataManagementService.submitNewEntries(feedEntries)
    }
}
