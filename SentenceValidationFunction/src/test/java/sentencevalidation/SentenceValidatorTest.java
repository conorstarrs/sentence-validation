package sentencevalidation;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

public class SentenceValidatorTest {
	SentenceValidator app;
	
	@Before
	public void setUp() throws Exception {
		app = new SentenceValidator();
	}	
	
    @Test
    public void validSentencesTest() {
    	// Build a list of valid sentences, then loop through to make sure they all pass as valid
    	List<String> validSentences = validSentences();
    	for(String valid : validSentences) {
    		APIGatewayProxyRequestEvent input = new APIGatewayProxyRequestEvent().withBody(valid);
    		APIGatewayProxyResponseEvent result = app.handleRequest(input, null);
    		
    		String content = result.getBody();
    		assertNotNull(content);
    		
    		JSONObject jsonObj = new JSONObject(content);
    		assertTrue(isValidSentence(jsonObj));    		
    	}
    }
	
    @Test
    public void invalidSentencesTest() {
    	// Build a list of invalid sentences, then loop through to make sure they all fail as invalid
    	List<String> invalidSentences = invalidSentences();
    	for(String invalid : invalidSentences) {
    		APIGatewayProxyRequestEvent input = new APIGatewayProxyRequestEvent().withBody(invalid);
    		APIGatewayProxyResponseEvent result = app.handleRequest(input, null);
    		
    		String content = result.getBody();
    		assertNotNull(content);
    		
    		JSONObject jsonObj = new JSONObject(content);
    		assertFalse(isValidSentence(jsonObj));    		
    	}
    }   
	
    @Test
    public void successfulResponseTest() {
    	APIGatewayProxyRequestEvent input = new APIGatewayProxyRequestEvent().withBody("The quick brown fox said \"hello Mr lazy dog\".");
	    APIGatewayProxyResponseEvent result = app.handleRequest(input, null);
	    
	    assertEquals(result.getStatusCode().intValue(), 200);
	    assertEquals(result.getHeaders().get("Content-Type"), "application/json");
	    
	    String content = result.getBody();
	    assertNotNull(content);
    }
    
    @Test
    public void isFirstLetterCapitalTest() {
    	APIGatewayProxyRequestEvent input = new APIGatewayProxyRequestEvent().withBody("One lazy dog is too few, thirteen is too many.");
	    APIGatewayProxyResponseEvent result = app.handleRequest(input, null);

	    String content = result.getBody();
	    assertNotNull(content);

	    JSONObject jsonObj = new JSONObject(content);
	    assertTrue((boolean) jsonObj.get("isFirstLetterCapital"));
    }
    
    @Test
    public void isFirstLetterNotCapitalTest() {
    	APIGatewayProxyRequestEvent input = new APIGatewayProxyRequestEvent().withBody("one lazy dog is too few, 13 is too many.");
	    APIGatewayProxyResponseEvent result = app.handleRequest(input, null);

	    String content = result.getBody();
	    assertNotNull(content);

	    JSONObject jsonObj = new JSONObject(content);
	    assertFalse((boolean) jsonObj.get("isFirstLetterCapital"));
    }    
    
    @Test
    public void hasEvenNumberOfQuotesTest() {
    	APIGatewayProxyRequestEvent input = new APIGatewayProxyRequestEvent().withBody("The 'quick' brown \"fox\" said hello Mr lazy dog.");
	    APIGatewayProxyResponseEvent result = app.handleRequest(input, null);

	    String content = result.getBody();
	    assertNotNull(content);

	    JSONObject jsonObj = new JSONObject(content);
	    assertTrue((boolean) jsonObj.get("hasEvenNumberOfQuotes"));
    }
    
    @Test
    public void hasOddNumberOfQuotesTest() {
    	APIGatewayProxyRequestEvent input = new APIGatewayProxyRequestEvent().withBody("Lorem 'ipsum dolor sit amet,\" consectetur adipiscing");
	    APIGatewayProxyResponseEvent result = app.handleRequest(input, null);

	    String content = result.getBody();
	    assertNotNull(content);

	    JSONObject jsonObj = new JSONObject(content);
	    assertFalse((boolean) jsonObj.get("hasEvenNumberOfQuotes"));
    }    
    
    @Test
    public void endsWithAPeriodTest() {
    	APIGatewayProxyRequestEvent input = new APIGatewayProxyRequestEvent().withBody("Lorem ipsum dolor sit amet, consectetur adipiscing elit.");
	    APIGatewayProxyResponseEvent result = app.handleRequest(input, null);

	    String content = result.getBody();
	    assertNotNull(content);

	    JSONObject jsonObj = new JSONObject(content);
	    assertTrue((boolean) jsonObj.get("endsWithAPeriod"));
    }   
    
    @Test
    public void endsWithoutAPeriodTest() {
    	APIGatewayProxyRequestEvent input = new APIGatewayProxyRequestEvent().withBody("Excepteur sint occaecat cupidatat non proident");
	    APIGatewayProxyResponseEvent result = app.handleRequest(input, null);

	    String content = result.getBody();
	    assertNotNull(content);

	    JSONObject jsonObj = new JSONObject(content);
	    assertFalse((boolean) jsonObj.get("endsWithAPeriod"));
    }    
    
    @Test
    public void hasNoPeriodOtherThanLastCharacterTest() {
    	APIGatewayProxyRequestEvent input = new APIGatewayProxyRequestEvent().withBody("The 'quick brown fox\" said hello Mr lazy dog.");
	    APIGatewayProxyResponseEvent result = app.handleRequest(input, null);

	    String content = result.getBody();
	    assertNotNull(content);

	    JSONObject jsonObj = new JSONObject(content);
	    assertTrue((boolean) jsonObj.get("hasNoPeriodOtherThanLastCharacter"));
    }     
    
    @Test
    public void hasPeriodOtherThanLastCharacterTest() {
    	APIGatewayProxyRequestEvent input = new APIGatewayProxyRequestEvent().withBody("The 'quick brown fox\" said. hello Mr lazy dog.");
	    APIGatewayProxyResponseEvent result = app.handleRequest(input, null);

	    String content = result.getBody();
	    assertNotNull(content);

	    JSONObject jsonObj = new JSONObject(content);
	    assertFalse((boolean) jsonObj.get("hasNoPeriodOtherThanLastCharacter"));
    }     
    
    @Test
    public void hasNoIntegersBelowThirteenTest() {
    	APIGatewayProxyRequestEvent input = new APIGatewayProxyRequestEvent().withBody("The 'quick 13 16brown fox\" said hello Mr lazy dog.");
	    APIGatewayProxyResponseEvent result = app.handleRequest(input, null);

	    String content = result.getBody();
	    assertNotNull(content);

	    JSONObject jsonObj = new JSONObject(content);
	    assertTrue((boolean) jsonObj.get("hasNoIntegersBelowThirteen"));
    }     
    
    @Test
    public void hasIntegersBelowThirteenTest() {
    	APIGatewayProxyRequestEvent input = new APIGatewayProxyRequestEvent().withBody("The 2 3 8 16 'quick brown fox\" said. hello Mr lazy dog.");
	    APIGatewayProxyResponseEvent result = app.handleRequest(input, null);

	    String content = result.getBody();
	    assertNotNull(content);

	    JSONObject jsonObj = new JSONObject(content);
	    assertFalse((boolean) jsonObj.get("hasNoIntegersBelowThirteen"));
    }     

	private List<String> validSentences() {
		List<String> invalidSentences = new ArrayList<>();
		
		invalidSentences.add("The quick brown fox said \"hello Mr lazy dog\".");
		invalidSentences.add("The quick brown fox said hello Mr lazy dog.");
		invalidSentences.add("One lazy dog is too few, 13 is too many.");
		invalidSentences.add("One lazy dog is too few, thirteen is too many.");

		return invalidSentences;
	}    

	private List<String> invalidSentences() {
		List<String> invalidSentences = new ArrayList<>();
		
		invalidSentences.add("The quick brown fox said \"hello Mr. lazy dog\".");
		invalidSentences.add("the quick brown fox said \"hello Mr lazy dog\".");
		invalidSentences.add("\"The quick brown fox said \"hello Mr lazy dog.\"");
		invalidSentences.add("One lazy dog is too few, 12 is too many.");

		return invalidSentences;
	}

	private boolean isValidSentence(JSONObject jsonObj) {
		return (boolean) jsonObj.get("isFirstLetterCapital") && 
			   (boolean) jsonObj.get("hasEvenNumberOfQuotes") &&
			   (boolean) jsonObj.get("endsWithAPeriod") &&
			   (boolean) jsonObj.get("hasNoPeriodOtherThanLastCharacter") &&
			   (boolean) jsonObj.get("hasNoIntegersBelowThirteen");
	}
}
