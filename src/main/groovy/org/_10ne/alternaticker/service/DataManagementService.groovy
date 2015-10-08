package org._10ne.alternaticker.service

import groovy.util.logging.Slf4j
import org._10ne.alternaticker.model.TestAverage
import org._10ne.alternaticker.model.FeedEntry
import org._10ne.alternaticker.model.NumericalStats

/**
 * @author Noam Y. Tenne
 */
@Slf4j
class DataManagementService {

    private Map<String, Map<String, TestAverage>> testAveragesCache = new HashMap<>()

    void submitNewEntries(List<FeedEntry> feedEntries) {
        log.info "Submitting ${feedEntries.size()} new entries"
        feedEntries.each { FeedEntry feedEntry ->
            Map<String, TestAverage> testAverages = testAveragesCache.get(feedEntry.countryCode)

            if (testAverages) {
                updateExistingCountryAverage(feedEntry, testAverages)
            } else {
                createNewCountryAverage(feedEntry)
            }
        }
    }

    Map<String, Map<String, TestAverage>> getAverages() {
        //Clone the cache so that the client gets an absolute snapshot
        testAveragesCache.clone()
    }

    Map<String, TestAverage> getCountryAverages(String country) {
        if (!testAveragesCache.containsKey(country)) {
            return [:]
        }
        testAveragesCache.get(country).clone()
    }

    NumericalStats getNumericalStats() {
        if (testAveragesCache.isEmpty()) {
            return new NumericalStats(countries: 0, scoresSubmitted: 0)
        }
        def countries = testAveragesCache.keySet().size()
        def scoresSubmitted = testAveragesCache.values().sum { it.values().sum { it.scoresSubmitted} }
        new NumericalStats(countries: countries, scoresSubmitted: scoresSubmitted)
    }

    private void updateExistingCountryAverage(FeedEntry feedEntry, Map<String, TestAverage> testAverages) {
        String countryCode = feedEntry.countryCode
        long overallScore = feedEntry.overallScore
        String testName = feedEntry.testName

        def testAverage = testAverages.get(testName)
        if (testAverage) {
            testAverage.submitNewScore(overallScore)
        } else {
            testAverage = new TestAverage()
            testAverage.submitNewScore(overallScore)
            testAverages.put(testName, testAverage)
        }

        log.info("Update ${testName} test average for $countryCode with score $overallScore and new average ${testAverage.currentAverage}")
    }

    private void createNewCountryAverage(FeedEntry feedEntry) {
        String countryCode = feedEntry.countryCode
        long overallScore = feedEntry.overallScore
        String testName = feedEntry.testName

        def testAverages = [:]

        TestAverage testAverage = new TestAverage()
        testAverage.submitNewScore(overallScore)
        testAverages.put(testName, testAverage)

        testAveragesCache.put(countryCode, testAverages)
        log.info("Creating new ${testName} test average for $countryCode with score $overallScore")
    }
}
