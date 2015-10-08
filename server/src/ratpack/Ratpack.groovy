import groovy.json.JsonBuilder
import org._10ne.alternaticker.service.DataManagementService
import org._10ne.alternaticker.service.ServicesModule

import static ratpack.groovy.Groovy.ratpack

ratpack {

    bindings {
        module ServicesModule
    }

    handlers {
        get('hello') { DataManagementService dataManagementService ->
            response.send('application/json', new JsonBuilder(dataManagementService.countryAverages).toPrettyString())
        }
    }
}