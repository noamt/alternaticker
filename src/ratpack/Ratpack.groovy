import groovy.json.JsonBuilder
import org._10ne.alternaticker.model.TestAverage
import org._10ne.alternaticker.model.NumericalStats
import org._10ne.alternaticker.service.DataManagementService
import org._10ne.alternaticker.service.ServicesModule

import static ratpack.groovy.Groovy.ratpack

ratpack {

    bindings {
        module ServicesModule
    }

    handlers {
        get {
            redirect('index.html')
        }
        get('globalFeed') { DataManagementService dataManagementService ->
            def data = [:]

            Map<String, Map<String, TestAverage>> allAverages = dataManagementService.averages
            def areas = allAverages.collect { String countryCode , Map<String, TestAverage> countryAverages ->
                def numberOfTests = countryAverages.size()
                [id: countryCode, value: numberOfTests]
            }

            data.map = 'worldLow'
            data.areas = areas
            response.send('application/json', new JsonBuilder(data).toString())
        }
        get('countryFeed/:country') { DataManagementService dataManagementService ->
            def selectedCountry = pathTokens.get('country')

            Map<String, TestAverage> countryAverages = dataManagementService.getCountryAverages(selectedCountry)
            def data = countryAverages.collect { String testName , TestAverage testAverage ->
                [name: testName, average: testAverage.currentAverage]
            }
            data.sort(true){ it.average * -1}

            response.send('application/json', new JsonBuilder(data).toString())
        }
        get('numericalStats') { DataManagementService dataManagementService ->
            NumericalStats stats = dataManagementService.numericalStats
            response.send('application/json', new JsonBuilder(stats).toString())
        }
        fileSystem('assets') { f ->
            f.files()
        }
    }
}