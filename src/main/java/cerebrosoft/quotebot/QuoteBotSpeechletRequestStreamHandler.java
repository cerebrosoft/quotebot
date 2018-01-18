package cerebrosoft.quotebot;

import java.util.HashSet;
import java.util.Set;

import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;

/**
 * This is the handler for an AWS Lambda function that powers an Alexa skill
 */
public final class QuoteBotSpeechletRequestStreamHandler extends SpeechletRequestStreamHandler {
    private static final Set<String> supportedApplicationIds;
    static {
        supportedApplicationIds = new HashSet<String>();
        // add relevant Alexa skill ID
        supportedApplicationIds.add("amzn1.ask.skill.b795ec2a-fd71-4968-ac17-3e4d9f5ce243");
    }

    public QuoteBotSpeechletRequestStreamHandler() {
        super(new QuoteBotSpeechlet(), supportedApplicationIds);
    }
}
