package com.nero.zigzok.room;

public interface Constants {

	// TODO Change it to your web domain
    String WEB_DOMAIN = "zoom.us";

	// TODO change it to your user ID
    String USER_ID = "nero18@apcs.vn";
	
	// TODO change it to your token
    String ZOOM_ACCESS_TOKEN = "eyJ6bV9za20iOiJ6bV9vMm0iLCJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJjbGllbnQiLCJ1aWQiOiJfZ2hoMFlhalJOdVYtY2lKOUpjaVZRIiwiaXNzIjoid2ViIiwic3R5IjoxLCJ3Y2QiOiJhdzEiLCJjbHQiOjAsInN0ayI6InpSTG1ScXRsb2E3Qk81al9LTHlGcWUwMTBfS003U0tpZWJpdEtEMkZGMVEuQUcuVER0bDBSc2VXalpVdGFONGlQendTbDlzQkg0QVJPY1p6SWVjMWNDb1JPSG9YeWxyM016ZDVRQjlxNTdJOWxsNFBNYTk0dlduMkpXVERsQS5vNU9tdHVTbGZyWk1xOWxxQVJWRUtRLkw4SjR2QUFNU0NXN3VFQVciLCJleHAiOjE2MDgyMjg5NTEsImlhdCI6MTYwODIyMTc1MSwiYWlkIjoiOGJ3TGNfMUVUUGFkeWU5ZF9XZFJ1USIsImNpZCI6IiJ9.v1a5caPjAcPjo3Wr51E82_JziZRwYFdbK0ViFTfrrgk";
	
	// TODO Change it to your exist meeting ID to start meeting
    String MEETING_ID = "6308912138";

    String MEETING_PASSWORD = "789656";
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
