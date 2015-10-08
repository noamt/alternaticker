package org._10ne.alternaticker.model

import spock.lang.Specification

/**
 * @author Noam Y. Tenne
 */
class CountryAverageSpec extends Specification {

    def 'Submit and initial score that should be set rather than averaged'() {
        given:
        def average = new TestAverage()

        when:
        average.submitNewScore(200)

        then:
        average.scoresSubmitted == 1
        average.currentAverage == 200
    }

    def 'Submit a new score to an existing average that should be incrementally updated'() {
        given:
        def average = new TestAverage()
        average.submitNewScore(200)
        average.submitNewScore(100)

        when:
        average.submitNewScore(170)

        then:
        average.scoresSubmitted == 3
        average.currentAverage == 156
    }
}
