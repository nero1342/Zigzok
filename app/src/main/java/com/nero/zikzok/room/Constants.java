package com.nero.zikzok.room;

public interface Constants {

    // TODO Change it to your web domain
    String WEB_DOMAIN = "zoom.us";

    // TODO change it to your user ID
//    String USER_ID = "nero18@apcs.vn";
//    String USER_ID = "hxnhat18@apcs.vn";

//    public final static String JWT_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOm51bGwsImlzcyI6Ilk1WTdUZlJvUVZPaElhYnQ0aG9zS3ciLCJleHAiOjE3NjYyMjk5MDAsImlhdCI6MTYwODQ1ODEyNX0.zSvQLIN0w-9PMjufhPRSLfmAO7r2fBBsWkZZUCKcrEA";
//    public final static String JWT_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOm51bGwsImlzcyI6ImpFWTV4QU1GUjhTTzBBSXpYOV9Uc0EiLCJleHAiOjE2MTAyODA0ODIsImlhdCI6MTYxMDE5NDA4Mn0.gzvMsNVqGDG44M925rv9z511pb6mQeTIGhasotbxh4I";
    /**
     * We recommend that, you can generate jwttoken on your own server instead of hardcore in the code.
     * We hardcore it here, just to run the demo.
     *
     * You can generate a jwttoken on the https://jwt.io/
     * with this payload:
     * {
     *     "appKey": "string", // app key
     *     "iat": long, // access token issue timestamp
     *     "exp": long, // access token expire time
     *     "tokenExp": long // token expire time
     * }
     */
    public final static String SDK_KEY = "DymeOSFIbfNPCyIMv23EmmyWvYPNFJ44BPkQ";
    public final static String SDK_SECRET = "t191t46bV8VkZqjeQdzfIOzPcYcWWpUFhuNY";
}
