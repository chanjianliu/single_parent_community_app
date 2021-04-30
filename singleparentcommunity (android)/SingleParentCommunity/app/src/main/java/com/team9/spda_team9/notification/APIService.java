package com.team9.spda_team9.notification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAEbzgaJM:APA91bFcH9N11AbtB0icrWr6qbcZkdDTYSvrGNKgwicgJE7kCRgPZ5WDwEjX_THmAdp9MlEnCulsmN9Xiiuo_dPacq-ersMa2PF_qb7gJdxVnedT0qkjFim9Cn9yzbiNEh8EYbC0Dumb" // Your server key refer to video for finding your server key
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotifcation(@Body NotificationSender body);
}
