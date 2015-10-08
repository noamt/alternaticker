package org._10ne.alternaticker.service

import org._10ne.alternaticker.model.FeedEntry
import org._10ne.alternaticker.model.TestAverage
import spock.lang.Specification

/**
 * @author Noam Y. Tenne
 */
class DataManagementServiceSpec extends Specification {

    def 'Submit feed entries for a new country'() {
        setup:
        def service = new DataManagementService()

        def feedEntry = new FeedEntry(id: 1, countryCode: 'US', overallScore: 2, testName: 'fire')
        def entries = [feedEntry]

        when:
        service.submitNewEntries(entries)

        then:
        def averages = service.averages
        averages.size() == 1
        averages.get('US').get('fire').currentAverage == 2
        averages.get('US').get('fire').scoresSubmitted == 1
    }

    def 'Submit feed entries for an existing country'() {
        setup:
        def service = new DataManagementService()
        service.submitNewEntries([new FeedEntry(id: 1, countryCode: 'US', overallScore: 100, testName: 'fire')])

        def newEntry = new FeedEntry(id: 2, countryCode: 'US', overallScore: 200, testName: 'fire')
        def newEntries = [newEntry]

        when:
        service.submitNewEntries(newEntries)

        then:
        def averages = service.averages
        averages.size() == 1
        averages.get('US').get('fire').currentAverage == 150
        averages.get('US').get('fire').scoresSubmitted == 2
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
        service.submitNewEntries([new FeedEntry(id: 1, countryCode: 'US', overallScore: 100, testName: 'fire')])
        service.submitNewEntries([new FeedEntry(id: 2, countryCode: 'IL', overallScore: 130, testName: 'earth')])
        service.submitNewEntries([new FeedEntry(id: 3, countryCode: 'US', overallScore: 90, testName: 'wind')])

        when:
        def stats = service.getNumericalStats()

        then:
        stats.scoresSubmitted == 3
        stats.countries == 2
    }

    def 'Get the test score averages of a country with no scores'() {
        setup:
        def service = new DataManagementService()

        expect:
        service.getCountryAverages('US').isEmpty()
    }

    def 'Get the test score averages of a country'() {
        setup:
        def service = new DataManagementService()
        service.submitNewEntries([new FeedEntry(id: 1, countryCode: 'US', overallScore: 100, testName: 'fire')])
        service.submitNewEntries([new FeedEntry(id: 2, countryCode: 'US', overallScore: 90, testName: 'wind')])

        expect:
        def countryAverages = service.getCountryAverages('US')
        countryAverages.get('fire').currentAverage == 100
        countryAverages.get('wind').currentAverage == 90
    }
}
