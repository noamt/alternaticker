package org._10ne.alternaticker.service

import com.squareup.okhttp.Call
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.Response
import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j
import ratpack.http.internal.HttpHeaderConstants

/**
 * @author Noam Y. Tenne
 */
@Slf4j
class RemoteFeedService {

    public Collection getFeedAsJson(long nextEventStartId) throws Throwable {
        log.info("Requesting a new feed with starting at ${nextEventStartId}")
        Request request = buildRequest(nextEventStartId)
        OkHttpClient okHttpClient = new OkHttpClient()
        Call call = okHttpClient.newCall(request)
        Response response = call.execute()
        if (!response.isSuccessful()) {
            throw new Exception("Request for a new feed has failed - ${response.code()}:${response.message()}")
        }
        readResponseBody(response)
    }

    private Request buildRequest(long nextEventStartId) {
        new Request.Builder()
                .get()
                .addHeader(HttpHeaderConstants.ACCEPT.toString(), HttpHeaderConstants.JSON.toString())
                .url("http://www.3dmark.com/proxycon/resultevent/?eventIdStart=${nextEventStartId}&eventCountMax=10")
                .build()
    }

    private Collection readResponseBody(Response response) {
        def rawJson
        try {
            response.body().source().inputStream().withStream {
                rawJson = new JsonSlurper().parse(it)
            }
        } catch (Throwable t) {
            throw new Exception("Failed to read the response body after requesting a new feed", t)
        }
        rawJson
    }
}
