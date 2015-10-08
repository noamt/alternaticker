package org._10ne.alternaticker.handlers

import com.google.inject.Inject
import com.google.inject.Singleton
import groovy.json.JsonBuilder
import org._10ne.alternaticker.model.TestAverage
import org._10ne.alternaticker.service.DataManagementService
import ratpack.groovy.handling.GroovyContext
import ratpack.groovy.handling.GroovyHandler

/**
 * @author Noam Y. Tenne
 */
@Singleton
class GlobalFeedHandler extends GroovyHandler {

    @Inject
    DataManagementService dataManagementService

    @Override
    protected void handle(GroovyContext context) {
        context.byMethod {
            get {
                def data = [:]

                Map<String, Map<String, TestAverage>> allAverages = dataManagementService.averages
                def areas = allAverages.collect { String countryCode, Map<String, TestAverage> countryAverages ->
                    def numberOfTests = countryAverages.size()
                    [id: countryCode, value: numberOfTests]
                }

                data.map = 'worldLow'
                data.areas = areas
                context.response.send('application/json', new JsonBuilder(data).toString())
            }
        }
    }
}
