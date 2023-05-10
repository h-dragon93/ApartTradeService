package com.estate.hdragon.domain.account.kakao;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class KakaoAccessToken {

    private String id;
    private int expires_in;
    private int expiresInMillis;
    private int app_id;
    private int appId;
}
