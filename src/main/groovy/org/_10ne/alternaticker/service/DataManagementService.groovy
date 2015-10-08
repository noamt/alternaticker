package org._10ne.alternaticker.service

import groovy.util.logging.Slf4j
import org._10ne.alternaticker.model.CountryAverage
import org._10ne.alternaticker.model.FeedEntry
import org._10ne.alternaticker.model.NumericalStats

/**
 * @author Noam Y. Tenne
 */
@Slf4j
class DataManagementService {

    private Map<String, CountryAverage> countryAveragesCache = new HashMap<>()

    void submitNewEntries(List<FeedEntry> feedEntries) {
        log.info "Submitting ${feedEntries.size()} new entries"
        feedEntries.each { FeedEntry feedEntry ->
            CountryAverage countryAverage = countryAveragesCache.get(feedEntry.countryCode)

            if (countryAverage) {
                updateExistingCountryAverage(feedEntry, countryAverage)
            } else {
                createNewCountryAverage(feedEntry)
            }
        }
    }

    Map<String, CountryAverage> getCountryAverages() {
        //Clone the cache so that the client gets an absolute snapshot
        countryAveragesCache.clone()
    }

    NumericalStats getNumericalStats() {
        if (countryAveragesCache.isEmpty()) {
            return new NumericalStats(countries: 0, scoresSubmitted: 0)
        }
        def countries = countryAveragesCache.keySet().size()
        def scoresSubmitted = countryAveragesCache.values().sum { it.scoresSubmitted }
        new NumericalStats(countries: countries, scoresSubmitted: scoresSubmitted)
    }

    private void updateExistingCountryAverage(FeedEntry feedEntry, CountryAverage countryAverage) {
        String countryCode = feedEntry.countryCode
        long overallScore = feedEntry.overallScore
        countryAverage.submitNewScore(overallScore)
        log.info("Update country average for $countryCode with score $overallScore and new average ${countryAverage.currentAverage}")
    }

    private void createNewCountryAverage(FeedEntry feedEntry) {
        String countryCode = feedEntry.countryCode
        long overallScore = feedEntry.overallScore
        CountryAverage countryAverage = new CountryAverage()
        countryAverage.submitNewScore(overallScore)
        countryAveragesCache.put(countryCode, countryAverage)
        log.info("Creating new country average for $countryCode with score $overallScore")
    }
}
