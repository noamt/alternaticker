package org._10ne.alternaticker.service

import org._10ne.alternaticker.model.FeedEntry
import spock.lang.Specification

/**
 * @author Noam Y. Tenne
 */
class DataManagementServiceSpec extends Specification {

    def 'Submit feed entries for a new country'() {
        setup:
        def service = new DataManagementService()

        def feedEntry = new FeedEntry(id: 1, countryCode: 'US', overallScore: 2)
        def entries = [feedEntry]

        when:
        service.submitNewEntries(entries)

        then:
        def averages = service.countryAverages
        averages.size() == 1
        averages.get('US') == 2
    }

    def 'Submit feed entries for an existing country'() {
        setup:
        def service = new DataManagementService()
        service.submitNewEntries([new FeedEntry(id: 1, countryCode: 'US', overallScore: 100)])

        def newEntry = new FeedEntry(id: 2, countryCode: 'US', overallScore: 200)
        def newEntries = [newEntry]

        when:
        service.submitNewEntries(newEntries)

        then:
        def averages = service.countryAverages
        averages.size() == 1
        averages.get('US') == 150
    }
}
