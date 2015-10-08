package org._10ne.alternaticker.model

import groovy.util.logging.Slf4j

/**
 * @author Noam Y. Tenne
 */
@Slf4j
class TestAverage {

    private long scoresSubmitted
    private long currentAverage

    void submitNewScore(long score) {
        scoresSubmitted++
        if (scoresSubmitted == 1) {
            currentAverage = score
            return
        }

        def numberOfScoresBeforeNewSubmission = scoresSubmitted - 1
        currentAverage = ((currentAverage * numberOfScoresBeforeNewSubmission) + score) / scoresSubmitted
    }

    long getCurrentAverage() {
        return currentAverage
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        new TestAverage(scoresSubmitted: scoresSubmitted, currentAverage: currentAverage)
    }
}
