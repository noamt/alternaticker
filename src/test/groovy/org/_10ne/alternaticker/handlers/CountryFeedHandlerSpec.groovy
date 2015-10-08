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
class CountryFeedHandlerSpec extends Specification {

    def 'Get the score feed of a country'() {
        setup:
        def dataManagementService = Mock(DataManagementService)
        def handler = new CountryFeedHandler()
        handler.dataManagementService = dataManagementService

        when:
        def result = handle(handler) {
            pathBinding(country: 'US')
        }

        then:
        1 * dataManagementService.getCountryAverages('US') >> {
            [earth: new TestAverage(scoresSubmitted: 1, currentAverage: 1),
             wind : new TestAverage(scoresSubmitted: 1, currentAverage: 2)]
        }
        result.status.code == OK.code
        def resultBody = new JsonSlurper().parse(result.bodyBytes)
        def first = resultBody.first()
        first.name == 'wind'
        first.average == 2

        def last = resultBody.last()
        first.name == 'earth'
        first.average == 1
    }
}
