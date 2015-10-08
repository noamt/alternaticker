import groovy.json.JsonBuilder
import org._10ne.alternaticker.model.CountryAverage
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
        get('feed') { DataManagementService dataManagementService ->
            def data = [:]

            Map<String, CountryAverage> countryAverages = dataManagementService.countryAverages
            def areas = countryAverages.collect { String countryCode , CountryAverage countryAverage ->
                [id: countryCode, value: countryAverage.currentAverage]
            }

            data.map = 'worldLow'
            data.areas = areas
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