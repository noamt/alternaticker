package org._10ne.alternaticker.service.remote

import org._10ne.alternaticker.model.FeedEntry
import org._10ne.alternaticker.service.DataManagementService
import org._10ne.alternaticker.service.RemoteFeedService
import spock.lang.Specification

/**
 * @author Noam Y. Tenne
 */
class FeedUpdateSpec extends Specification {

    def 'Run the feed update thread'() {
        setup:
        def dataManagementService = Mock(DataManagementService)
        def remoteFeedService = Mock(RemoteFeedService)
        def feedUpdate = new FeedUpdate(dataManagementService, remoteFeedService)

        when:
        feedUpdate.run()

        then:
        1 * remoteFeedService.getFeedAsJson(0) >> {
            [
                    [id: '1', benchmarkRunModel: [countryCode: 'DE', overallScore: 1000]],
                    [id: '2', benchmarkRunModel: [countryCode: 'US', overallScore: 900]]
            ]
        }
        1 * dataManagementService.submitNewEntries(_ as List) >> {List<FeedEntry> feedEntries ->
            def entries = feedEntries.first()
            assert entries.any { it.id == 1 && it.countryCode == 'DE' && it.overallScore == 1000}
            assert entries.any { it.id == 2 && it.countryCode == 'US' && it.overallScore == 900}
        }
        feedUpdate.nextEventStartId == 2
    }
}
