package com.example.hs2booking.service.feign;

import com.example.hs2booking.controller.exceptions.fallback.DubControllerException;
import com.example.hs2booking.controller.exceptions.fallback.ServiceUnavailableException;
import com.example.hs2booking.controller.exceptions.not_found.PlayerNotFoundException;
import com.example.hs2booking.controller.exceptions.not_found.PlaygroundNotFoundException;
import com.example.hs2booking.model.dto.PlayerDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ActorsClientForPlayerWrapper {

    private final ActorsClient actorsClient;

    @CircuitBreaker(name = "playersCircuitBreaker", fallbackMethod = "getFallbackPlayerDTO")
    public PlayerDTO getPlayerDTO(long playerId) {
        return actorsClient.findPlayerById(playerId);
    }

    public PlayerDTO getFallbackPlayerDTO(Throwable exception) throws ServiceUnavailableException {

        String errorMessage = exception.getMessage();

        System.out.println("[errorMessage] " + errorMessage);
        System.out.println("[cause] " + exception.getCause());

        if (null != exception.getCause()
                && exception.getCause().toString().equals("java.net.SocketTimeoutException: Connect timed out")) {
            throw new ServiceUnavailableException("Actors service is temporarily unavailable" +
                    " --- " + exception.getMessage());

        } else {
            int statusCode = Integer.parseInt(errorMessage.substring(
                    errorMessage.indexOf("[") + 1,
                    errorMessage.indexOf("]")
            ));
            String error;
            String message;

            if (errorMessage.contains("error") && errorMessage.contains("message")) {
                error = getField("error", errorMessage);
                message = "[" + statusCode + "] " + getField("message", errorMessage);
            } else {
                error = errorMessage.substring(errorMessage.lastIndexOf("[") + 1, errorMessage.lastIndexOf("]"));
                message = "---";
            }

            System.out.println("[statusCode] " + statusCode);
            System.out.println("[error] " + error);
            System.out.println("[message] " + message);

            switch (error) {
                case "Element Not Found":
                    throw new PlayerNotFoundException(message);
                default:
                    throw new DubControllerException(statusCode, error, message);
            }
        }


    }

    private static String getField(String field, String message) {
        String fullField = "\"" + field + "\":";
        int start = message.indexOf(fullField) + fullField.length() + 1;
        String postText = message.substring(start);
        int end = postText.indexOf("\"");
        return postText.substring(0, end);
    }
}
