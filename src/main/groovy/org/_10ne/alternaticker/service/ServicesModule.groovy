package org._10ne.alternaticker.service

import com.google.inject.AbstractModule
import com.google.inject.Scopes

/**
 * @author Noam Y. Tenne
 */
class ServicesModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(RemoteFeedService).in(Scopes.SINGLETON)
        bind(DataManagementService).in(Scopes.SINGLETON)
        bind(FeedUpdateService).in(Scopes.SINGLETON)
    }
}
