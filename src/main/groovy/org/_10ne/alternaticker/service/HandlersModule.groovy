package org._10ne.alternaticker.service

import com.google.inject.AbstractModule
import org._10ne.alternaticker.handlers.CountryFeedHandler
import org._10ne.alternaticker.handlers.GlobalFeedHandler

/**
 * @author Noam Y. Tenne
 */
class HandlersModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(CountryFeedHandler)
        bind(GlobalFeedHandler)
    }
}
