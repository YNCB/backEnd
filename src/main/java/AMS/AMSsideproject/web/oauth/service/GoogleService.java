package AMS.AMSsideproject.web.oauth.service;

import AMS.AMSsideproject.web.oauth.provider.info.GoogleInfo;
import AMS.AMSsideproject.web.oauth.provider.info.KakaoInfo;
import AMS.AMSsideproject.web.oauth.provider.profile.GoogleProfile;
import AMS.AMSsideproject.web.oauth.provider.profile.KakaoProfile;
import AMS.AMSsideproject.web.oauth.provider.token.GoogleToken;
import AMS.AMSsideproject.web.oauth.provider.token.KakaoToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GoogleService {

    private final WebClient wc;
    private final ObjectMapper ob;

    //카카오 oauth 서버로 부터 엑세스 토큰을 받는 메서드
    public GoogleToken getAccessToken(String code) throws JsonProcessingException {

        //파라미터 세팅
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", GoogleInfo.client_id);
        params.add("client_secret", GoogleInfo.client_secret);
        params.add("redirect_uri", GoogleInfo.redirect_uri);
        params.add("grant_type", GoogleInfo.grantType);

        //http 요청 및 응답
        String response = wc.post()
                .uri(GoogleInfo.accessTokenUri)
                .bodyValue(params)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        //json 변환
        GoogleToken googleToken = ob.readValue(response, GoogleToken.class);


        return googleToken;
    }

    //카카오 oauth 서버로 부터 사용자 정보 받아오기기
    public GoogleProfile getUserProfile(String accessToken) throws JsonProcessingException {

        //http 요청 및 응답
        String response = wc.get()
                .uri(GoogleInfo.userInfoUri)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        //json 변환
        GoogleProfile googleProfile = ob.readValue(response, GoogleProfile.class);

        return googleProfile;
    }


}
