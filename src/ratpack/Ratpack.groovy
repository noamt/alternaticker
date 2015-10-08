import org._10ne.alternaticker.handlers.CountryFeedHandler
import org._10ne.alternaticker.handlers.GlobalFeedHandler
import org._10ne.alternaticker.model.NumericalStats
import org._10ne.alternaticker.service.DataManagementService
import org._10ne.alternaticker.service.HandlersModule
import org._10ne.alternaticker.service.ServicesModule

import static ratpack.groovy.Groovy.ratpack
import static ratpack.jackson.Jackson.json

ratpack {

    bindings {
        module ServicesModule
        module HandlersModule
    }

    handlers {
        get {
            redirect('index.html')
        }
        get('globalFeed', GlobalFeedHandler)
        get('countryFeed/:country', CountryFeedHandler)
        get('numericalStats') { DataManagementService dataManagementService ->
            NumericalStats stats = dataManagementService.numericalStats
            render(json(stats))
        }
        fileSystem('assets') { f ->
            f.files()
        }
    }
}