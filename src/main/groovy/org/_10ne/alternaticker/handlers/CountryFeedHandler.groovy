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
class CountryFeedHandler extends GroovyHandler {

    @Inject
    DataManagementService dataManagementService

    @Override
    protected void handle(GroovyContext context) {
        context.byMethod {
            get {
                def selectedCountry = context.pathTokens.get('country')

                Map<String, TestAverage> countryAverages = dataManagementService.getCountryAverages(selectedCountry)
                def data = countryAverages.collect { String testName , TestAverage testAverage ->
                    [name: testName, average: testAverage.currentAverage]
                }
                data.sort(true){ it.average * -1}

                context.response.send('application/json', new JsonBuilder(data).toString())
            }
        }
    }
}
