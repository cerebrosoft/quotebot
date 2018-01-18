package cerebrosoft.quotebot;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.speechlet.SpeechletV2;
import com.amazon.speech.ui.Image;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.StandardCard;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This speechlet speaks Ayn Rand quotes on demand
 */
public class QuoteBotSpeechlet implements SpeechletV2 {
    private static final Logger log = LoggerFactory.getLogger(QuoteBotSpeechlet.class);
    private static String MAIN_INTENT = "QuoteBotIntent";
    private static String HELP_INTENT = "AMAZON.HelpIntent";
    private static String CARD_TITLE = "QuoteBot";

    private List<String> quoteList;

    /**
     * Constructor
     */
    public QuoteBotSpeechlet() {
        URL quoteURL = getClass().getResource("/quotes.json");
        ObjectMapper mapper = new ObjectMapper();
        try {
            quoteList = mapper.readValue(quoteURL, QuoteList.class).getQuoteList();
        }
        catch (Exception e) {
            quoteList = new ArrayList<>();
        }
    }

    @Override
    public void onSessionStarted(SpeechletRequestEnvelope<SessionStartedRequest> requestEnvelope) {
    }

    @Override
    public SpeechletResponse onLaunch(SpeechletRequestEnvelope<LaunchRequest> requestEnvelope) {
        String speechText = "Welcome to QuoteBot, you can ask for a quote.";
        return getAskResponse(CARD_TITLE, speechText);
    }

    @Override
    public SpeechletResponse onIntent(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
        IntentRequest request = requestEnvelope.getRequest();

        Intent intent = request.getIntent();
        String intentName = (intent != null) ? intent.getName() : null;

        if (MAIN_INTENT.equals(intentName)) {
            return getQuoteResponse();
        }
        else if (HELP_INTENT.equals(intentName)) {
            String speechText = "You can ask me for a quote!";
            return getAskResponse(CARD_TITLE, speechText);
        }
        else {
            return getAskResponse(CARD_TITLE, "This is unsupported.  Please try something else.");
        }
    }

    @Override
    public void onSessionEnded(SpeechletRequestEnvelope<SessionEndedRequest> requestEnvelope) {
    }

    /**
     * Creates a {@code SpeechletResponse} for the hello intent.
     *
     * @return SpeechletResponse spoken and visual response for the given intent
     */
    private SpeechletResponse getQuoteResponse() {
        Random rand = new Random();

        String speechText = quoteList.get(rand.nextInt(quoteList.size()));

        // Create the standard card content.
        StandardCard card = getStandardCard(CARD_TITLE, speechText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        return SpeechletResponse.newTellResponse(speech, card);
    }

    /**
     * Helper method that creates a card object.
     * 
     * @param title title of the card
     * @param content body of the card
     * @return StandardCard the display card to be sent along with the voice response.
     */
    private StandardCard getStandardCard(String title, String content) {
        StandardCard card = new StandardCard();
        card.setTitle(title);
        card.setText(content);

        Image image = new Image();
        image.setLargeImageUrl("https://s3.amazonaws.com/alexa-public/rand.jpg");
        image.setSmallImageUrl("https://s3.amazonaws.com/alexa-public/rand.jpg");
        card.setImage(image);

        return card;
    }

    /**
     * Helper method for retrieving an Ask response with a simple card and reprompt included.
     * 
     * @param cardTitle Title of the card that you want displayed.
     * @param speechText speech text that will be spoken to the user.
     * @return the resulting card and speech text.
     */
    private SpeechletResponse getAskResponse(String cardTitle, String speechText) {
        StandardCard card = getStandardCard(cardTitle, speechText);

        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }

}
