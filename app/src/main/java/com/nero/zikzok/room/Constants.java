package com.nero.zikzok.room;

public interface Constants {

	// TODO Change it to your web domain
    String WEB_DOMAIN = "zoom.us";

	// TODO change it to your user ID
    String USER_ID = "nero18@apcs.vn";
	
	// TODO change it to your token
    String ZOOM_ACCESS_TOKEN = "eyJ6bV9za20iOiJ6bV9vMm0iLCJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJjbGllbnQiLCJ1aWQiOiJfZ2hoMFlhalJOdVYtY2lKOUpjaVZRIiwiaXNzIjoid2ViIiwic3R5IjoxLCJ3Y2QiOiJhdzEiLCJjbHQiOjAsInN0ayI6ImwxOUZXLUNxTkVVRTIzOGV3d05RaDNiajdCcTgzUW04bVZxX3VzZndQYzQuQUcuZlRXVGhRcndiXzEyYmd6NDQ2ZjRCVzFmZHYyUV9DMTJXQ2dGYi1Fa2JfT2oycW4xVENHZ3FpbnZtTjZqZ2dfdlIzZWFGRG9ZMURwVVJmcy5MTEg2bndfZ3ZWaVNiUkRLSmhYMkNRLjM2NS1CNExMeUd4cndtQl8iLCJleHAiOjE2MDg0NzU2NjMsImlhdCI6MTYwODQ2ODQ2MywiYWlkIjoiOGJ3TGNfMUVUUGFkeWU5ZF9XZFJ1USIsImNpZCI6IiJ9.SgA6BzsjHtIi1-SaVKtKUayJqEGNSXVIBDqm9dFILKk";
	
	// TODO Change it to your exist meeting ID to start meeting
    String MEETING_ID = "6308912138";

    String MEETING_PASSWORD = "789656";

    public  final static String JWT_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOm51bGwsImlzcyI6Ilk1WTdUZlJvUVZPaElhYnQ0aG9zS3ciLCJleHAiOjE3NjYyMjk5MDAsImlhdCI6MTYwODQ1ODEyNX0.zSvQLIN0w-9PMjufhPRSLfmAO7r2fBBsWkZZUCKcrEA";
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
