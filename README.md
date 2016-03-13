# Chromecast Background

This application is based on [someone else's idea][1]: put shower thoughts on
pretty Earth images and configure your Chromecast to display them.

## Setup

In order to connect to Reddit (the source), an "application" must be created
on a Reddit account. Visit <https://reddit.com/prefs/apps>, create a "script"
application. Take note of the client ID and client secret.

Create a file in `gradle/config/secrets.gradle` that has the following
structure:

```groovy
reddit {
    client_id = 'the client ID from above'
    client_secret = 'the client secret from above'
}
```

## Running It

```bash
./gradlew run
```

Once the secrets are configured, running it is as simple as `gradlew run`.

[1]: https://www.reddit.com/r/raspberry_pi/comments/4a1zpq/i_took_the_idea_of_putting_showerthoughts_on/ "The source, which was probably inspired by another..."
