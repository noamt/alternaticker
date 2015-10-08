package org._10ne.alternaticker.handlers

import groovy.json.JsonSlurper
import org._10ne.alternaticker.model.TestAverage
import org._10ne.alternaticker.service.DataManagementService
import spock.lang.Specification

import static ratpack.groovy.test.handling.GroovyRequestFixture.handle
import static ratpack.http.Status.OK

/**
 * @author Noam Y. Tenne
 */
class GlobalFeedHandlerSpec extends Specification {

    def 'Get the global country data'() {
        setup:
        def dataManagementService = Mock(DataManagementService)
        def handler = new GlobalFeedHandler()
        handler.dataManagementService = dataManagementService

        when:
        def result = handle(handler) {}

        then:
        1 * dataManagementService.getAverages() >> {
            [US: [earth: new TestAverage(scoresSubmitted: 1, currentAverage: 1),
                  wind : new TestAverage(scoresSubmitted: 2, currentAverage: 2)],

             DE: [earth: new TestAverage(scoresSubmitted: 4, currentAverage: 5)]]
        }
        result.status.code == OK.code
        def resultBody = new JsonSlurper().parse(result.bodyBytes)

        resultBody.map == 'worldLow'
        def areas = resultBody.areas
        areas.any { it.id == 'US' && it.value == 2}
        areas.any { it.id == 'DE' && it.value == 1}
    }
}
