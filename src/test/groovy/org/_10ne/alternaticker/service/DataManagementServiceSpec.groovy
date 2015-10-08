package org._10ne.alternaticker.service

import org._10ne.alternaticker.model.FeedEntry
import org._10ne.alternaticker.model.NumericalStats
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
        averages.get('US').currentAverage == 2
        averages.get('US').scoresSubmitted == 1
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
        averages.get('US').currentAverage == 150
        averages.get('US').scoresSubmitted == 2
    }

    def 'Get numerical stats when no scores have been submitted'() {
        setup:
        def service = new DataManagementService()

        when:
        def stats = service.getNumericalStats()

        then:
        stats.scoresSubmitted == 0
        stats.countries == 0
    }

    def 'Get numerical stats'() {
        setup:
        def service = new DataManagementService()
        service.submitNewEntries([new FeedEntry(id: 1, countryCode: 'US', overallScore: 100)])
        service.submitNewEntries([new FeedEntry(id: 2, countryCode: 'IL', overallScore: 130)])
        service.submitNewEntries([new FeedEntry(id: 3, countryCode: 'US', overallScore: 90)])

        when:
        def stats = service.getNumericalStats()

        then:
        stats.scoresSubmitted == 3
        stats.countries == 2
    }
}
