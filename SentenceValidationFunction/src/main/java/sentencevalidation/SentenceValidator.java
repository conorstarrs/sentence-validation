package sentencevalidation;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.json.JSONObject;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

/**
 * Conor Starrs - Sentence Validation Assignment.
 * 18th October 2020.
 * 
 * Handler for requests to AWS Lambda function through API Gateway.
 * https://assignment-api.conorstarrs.dev/sentence-validation (HTTP POST)
 */
public class SentenceValidator implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
	
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");
        headers.put("Access-Control-Allow-Origin", "*");
        
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                .withHeaders(headers);
        
        // If the body isn't empty, validate the sentence
        String output = input.getBody() != null ? validateSentence(input.getBody().trim()) : "";
        
		return response
				.withStatusCode(200)
		        .withBody(output);
    }
    
	private String validateSentence(String sentence) {
		// Return a JSON object with results from each validation for displaying details on the UI
		String output = new JSONObject()
		    .put("isFirstLetterCapital", isFirstLetterCapital(sentence))
		    .put("hasEvenNumberOfQuotes", isCountOfQuotationMarksEven(sentence))
		    .put("endsWithAPeriod", endsWithAPeriod(sentence))
		    .put("hasNoPeriodOtherThanLastCharacter", hasNoPeriodOtherThanLastCharacter(sentence))
		    .put("hasNoIntegersBelowThirteen", hasNoIntegersBelowThirteen(sentence))
		    .toString();
		return output;
	}

	private boolean hasNoIntegersBelowThirteen(String sentence) {
		// Find all digits in the sentence
		Pattern p = Pattern.compile("-?\\d+");
		Matcher m = p.matcher(sentence);
		while (m.find()) {
			// Check each digit and return false if any are less than 13
			if(Integer.parseInt(m.group()) < 13) {
				return false;
			}
		}
		return true;
	}

	private boolean hasNoPeriodOtherThanLastCharacter(String sentence) {
		// If no periods present, or, period present and index is that of last character.
		return sentence.indexOf(".") == -1 
				|| (sentence.indexOf(".") == (sentence.length() - 1));
	}

	private boolean endsWithAPeriod(String sentence) {
		return sentence.endsWith(".");
	}

	private boolean isFirstLetterCapital(String sentence) {
		return Character.isUpperCase(sentence.charAt(0));
	}

	private boolean isCountOfQuotationMarksEven(String sentence) {
		// Remove quotes from sentence and deduct from total length to check if even number. 
		int doubleQuoteCount = sentence.length() - sentence.replace("\"", "").length();
		int singleQuoteCount = sentence.length() - sentence.replace("\'", "").length();
		return isEven(doubleQuoteCount) && isEven(singleQuoteCount);
	}

	private static boolean isEven(int count) {
		return ((count % 2) == 0); 
	}    
}
