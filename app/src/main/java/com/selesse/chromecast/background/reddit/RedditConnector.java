package com.selesse.chromecast.background.reddit;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import net.dean.jraw.RedditClient;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.http.oauth.Credentials;
import net.dean.jraw.http.oauth.OAuthData;
import net.dean.jraw.http.oauth.OAuthException;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;
import net.dean.jraw.paginators.Sorting;
import net.dean.jraw.paginators.SubredditPaginator;
import net.dean.jraw.paginators.TimePeriod;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class RedditConnector {
    private static final UserAgent SCRIPT_USER_AGENT =
            UserAgent.of("desktop", "com.selesse.chromecast.background", "0.1.0", "selesse");
    private static final String CLIENT_ID = System.getProperty("REDDIT_CLIENT_ID");
    private static final String CLIENT_SECRET = System.getProperty("REDDIT_CLIENT_SECRET");

    private RedditClient redditClient;

    public RedditConnector() {
        this.redditClient = new RedditClient(SCRIPT_USER_AGENT);
    }

    public void login() throws OAuthException {
        Preconditions.checkNotNull(Strings.emptyToNull(CLIENT_ID), "Reddit client ID not provided");
        Preconditions.checkNotNull(Strings.emptyToNull(CLIENT_SECRET), "Reddit client secret not provided");

        Credentials credentials = Credentials.userless(CLIENT_ID, CLIENT_SECRET, UUID.randomUUID());
        OAuthData oAuthData = redditClient.getOAuthHelper().easyAuth(credentials);
        redditClient.authenticate(oAuthData);
    }

    public List<Submission> getPosts(String subredditName) {
        return getDefaultPaginator(subredditName).next();
    }

    public List<Submission> getImgurPosts(String subredditName) {
        SubredditPaginator paginator = getDefaultPaginator(subredditName);
        paginator.setLimit(50);
        Listing<Submission> submissions = paginator.next();

        return submissions.stream()
                .filter(post -> post.getDomain().startsWith("imgur.com") || post.getDomain().startsWith("i.imgur.com"))
                .limit(10)
                .collect(Collectors.toList());
    }

    private SubredditPaginator getDefaultPaginator(String subredditName) {
        SubredditPaginator subredditPaginator = new SubredditPaginator(redditClient, subredditName);
        subredditPaginator.setLimit(10);
        subredditPaginator.setTimePeriod(TimePeriod.DAY);
        subredditPaginator.setSorting(Sorting.TOP);
        return subredditPaginator;
    }

}
