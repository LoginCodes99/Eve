import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;


public class Eve implements EventListener {
    JDA jda;
    EventChannel eventChannel = new EventChannel();
    EventController eventController = new EventController();

    public Eve(String[] args) {
        MessageListener messageListener = new MessageListener(eventController, eventChannel);
        JDABuilder builder = JDABuilder.createLight(args[0],
                GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_MEMBERS)
                .addEventListeners(this)
                .addEventListeners(messageListener)
                .setActivity(Activity.listening("Type !eve"));

        try {
            jda = builder.build();
            messageListener.setJDA(jda);
            eventController.setJDA(jda);
        } catch (LoginException e) {
            System.out.println("Error logging in Eve the event bot");
        }
    }

    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if(event instanceof ReadyEvent) {
            //Creating an instance of the Precheck class
            System.out.println("Eve is starting pre-checks");
            EvePrecheck evePrecheck = new EvePrecheck();

            //Checking for the "events" channel in every guild that Eve is in
            evePrecheck.checkGuildForEventChannel(jda);
        }
    }

    public static void main(String[] args) {
        new Eve(args);
    }
}
