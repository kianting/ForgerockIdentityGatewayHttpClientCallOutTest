
/*
 * Groovy script for OpenAM JWK URI
 *
 * This script requires these arguments: userId, password, openamUrl
 */
import org.forgerock.http.protocol.Request
import org.forgerock.http.protocol.Response
import org.forgerock.http.protocol.Status
import org.forgerock.util.AsyncFunction
import org.forgerock.http.protocol.Status
import static org.forgerock.http.protocol.Response.newResponsePromise


def getUnauthorizedError() {
    logger.info("Returning Unauthorized error")
    Response errResponse = new Response(Status.UNAUTHORIZED)
    errResponse.entity.json = [code: 401, reason: "Unauthorized", message: "Authentication Failed"];

    // Need to wrap the response object in a promise
    return newResponsePromise(errResponse)
}

logger.info("Starting Test HTTP Call out from IG")

// Invoke OpenAM authentication
Request req = new Request()
req.uri = "${testurl}nn${testPath}"
req.headers['accept'] = "application/json"
req.method = "GET"

logger.info("Getting Payload from test endpoint endpoint: ${req.uri}")

return http.send(context, req)
// when there will be a response available ...
// Need to use 'thenAsync' instead of 'then' because we'll return another promise, not directly a response
.thenAsync(testResponse -> {
    if(testResponse != null && !testResponse.entity.isRawContentEmpty()) {
            logger.info("Test Response : ${testResponse.entity.toString()}")
            if(Status.OK == testResponse.status) {
            logger.info("Success response: " + testResponse.entity.toString())
            }else {
            logger.info("Failure response: " + testResponse.entity.toString())
            }
        response.close()                 
    }else{
        logger.info("Response is empty!!!")
    } 
    return next.handle(context, request)   
}
, throwableEx -> {
    logger.info(">>>>>>A Throwable exception Occured !!! {} ", throwableEx.getMessage())
    return getUnauthorizedError()
}, runtimeEx -> {
    logger.info(">>>>>>A Runtime exception Occured !!! {} ", runtimeEx.getMessage())
    return getUnauthorizedError()
}
)


//return next.handle(context, request)  
    

