import java.util.ArrayList;
import java.util.List;

/*
TYPE ERASURE: instanceOf checks don't work with parameterized types in Java because Java 
 does not store the type parameters after compile time is done. For example, List<String> would 
not store String past compiling.
*/

interface Observer<T> {
    void update(Notification<T> notification);
}

class NotificationManager<T> {
    private final List<Observer<? super T>> observers = new ArrayList<>();

    public void addObserver(Observer<? super T> observer) {
        observers.add(observer);
    }
    
    public void notifyObservers(Notification<T> notification) {
        for (Observer<? super T> observer : observers) {
            observer.update(notification);
        }
    }
}

class EmailObserver implements Observer<String> {
    public void update(Notification<String> emailNotification) {
        System.out.println("New Email: " + emailNotification.getContent());
    }
}

class SMSObserver implements Observer<String> {
    public void update(Notification<String> SMSNotification) {
        System.out.println("New SMS: " + SMSNotification.getContent());
    }
}

class Notification<T> {
    private T content;
    private T platform;
    public Notification(T content, T platform) { this.content = content; this.platform = platform;}
    public T getContent() { return content; }
}

class EmailNotification extends Notification<String> {
    public EmailNotification(String content, String platform) {
        super(content, platform);
    }
}

class SMSNotification extends Notification<String> {
    public SMSNotification(String content, String platform) {
        super(content, platform);
    }
}

class NotificationBuilder<T> {
    protected T content = null;
    protected T platform = null;

    public void buildContent(T content) {
        this.content = content;
    }

    public void buildPlatform(T platform) {
        this.platform = platform;
    }
}

class EmailNotificationBuilder extends NotificationBuilder<String> {
    public EmailNotification buildEmailNotification() {
        return new EmailNotification(super.content, super.platform);
    }
}

class SMSNotificationBuilder extends NotificationBuilder<String> {
    public SMSNotification buildSMSNotification() {
        return new SMSNotification(super.content, super.platform);
    }
}

interface NotificationFactory<T> {
    Notification<T> createNotification(T content);
}

class EmailNotificationFactory implements NotificationFactory<String> {
    private NotificationManager<String> emailManager = new NotificationManager<>();

    public Notification<String> createNotification(String content) {
        EmailNotificationBuilder emailNotificationBuilder = new EmailNotificationBuilder();
        emailNotificationBuilder.buildContent(content);
        EmailNotification emailNotification = emailNotificationBuilder.buildEmailNotification();

        emailManager.notifyObservers(emailNotification);
        return emailNotification;
    }

    public void addObserver(Observer<String> observer) {
        emailManager.addObserver(observer);
    }
}

class SMSNotificationFactory implements NotificationFactory<String> {
    private NotificationManager<String> SMSManager = new NotificationManager<>();

    public Notification<String> createNotification(String content) {
        SMSNotificationBuilder smsNotificationBuilder = new SMSNotificationBuilder();
        smsNotificationBuilder.buildContent(content);
        SMSNotification smsNotification = smsNotificationBuilder.buildSMSNotification();

        SMSManager.notifyObservers(smsNotification);
        return smsNotification;
    }

    public void addObserver(Observer<String> observer) {
        SMSManager.addObserver(observer);
    }
}

public class NotificationTest {
    public static void main(String[] args) {

        // Factories instantiation
        EmailNotificationFactory emailFactory = new EmailNotificationFactory();
        SMSNotificationFactory SMSFactory = new SMSNotificationFactory();


        // Add observers
        emailFactory.addObserver(new EmailObserver());
        emailFactory.addObserver(new EmailObserver());
        SMSFactory.addObserver(new SMSObserver());

        // Notifications instantiation
        Notification<String> email1 = emailFactory.createNotification("Bye from MarketBridge!");
        Notification<String> SMS1 = SMSFactory.createNotification("Hello from MarketBridge!");

        // Generic Restriction
        /*
        List<String> strings = new List<String>[10];
        returns error because generic types cannot be put onto arrays.
        Allowing this could cause problems because of type erasure.
        That is, the program cannot guarantee that the List is full of only strings.
        */
        
    }
}