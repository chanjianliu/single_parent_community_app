package com.team9.spda_team9.sa;

import java.util.HashMap;

public class Score {
	
	
	
	public Score() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	//Map<String, Object> data = new HashMap<String, Object>();
    //data.put( "name", "Mars" );
    //data.put( "age", 32 );
   // data.put( "city", "NY" );
    //JSONObject json = new JSONObject();
    //json.putAll( data );
    //System.out.printf( "JSON: %s", json.toString(2) );
	
//	public Score(String userId, float neg, float neu, float pos) {
//		super();
//		this.userId = userId;
//		this.neg = neg;
//		this.neu = neu;
//		this.pos = pos;
//		HashMap<String, HashMap<String,Float>> map = new HashMap<>();
//		HashMap<String,Float> scores = new HashMap<String,Float>();
//		scores.put("neg", this.neg);
//		scores.put("neu", this.neu);
//		scores.put("pos", this.pos);
//		map.put(this.userId, scores);
//		//map.put("scores", scores);
//		
//	}


	//private String userId;;
	
	private float neg;
	private float neu;
	private float pos;
	private String timestamp;
//	public String getUserId() {
//		return userId;
//	}
//
//
//	public void setUserId(String userId) {
//		this.userId = userId;
//	}


	public String getTimestamp() {
		return timestamp;
	}


	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}


	public float getNeg() {
		return neg;
	}


	public void setNeg(float neg) {
		this.neg = neg;
	}


	public float getNeu() {
		return neu;
	}


	public void setNeu(float neu) {
		this.neu = neu;
	}


	public float getPos() {
		return pos;
	}


	public void setPos(float pos) {
		this.pos = pos;
	}

}
