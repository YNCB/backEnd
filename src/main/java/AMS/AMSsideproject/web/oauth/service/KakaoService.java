package AMS.AMSsideproject.web.oauth.service;

import AMS.AMSsideproject.web.oauth.provider.info.KakaoInfo;
import AMS.AMSsideproject.web.oauth.provider.profile.KakaoProfile;
import AMS.AMSsideproject.web.oauth.provider.token.KakaoToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KakaoService {

    @Autowired
    private final WebClient wc;
    private final ObjectMapper ob;

    //카카오 oauth 서버로 부터 엑세스 토큰을 받는 메서드
    public KakaoToken getAccessToken(String code) throws JsonProcessingException {

        //파라미터 세팅 -> MultiValueMap 사용시 "Webclient"에서 자동으로 "application/x-www-form-urlencoded" 설정!!
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", KakaoInfo.grantType);
        params.add("client_id", KakaoInfo.client_id);
        params.add("redirect_uri", KakaoInfo.redirect_uri);
        params.add("code", code);
        params.add("client_secret", KakaoInfo.client_secret);


        System.out.println(params);

        //http 요청 및 응답(non blocking)
        String response = wc.post()
                .uri(KakaoInfo.accessTokenUri)
                .bodyValue(params)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        //json 변환
        KakaoToken kakaoToken = ob.readValue(response, KakaoToken.class);
        return kakaoToken;
    }

    //카카오 oauth 서버로 부터 사용자 정보 받아오기기
    public KakaoProfile getUserProfile(String accessToken) throws JsonProcessingException {

        //필요한 사용자 정보 정의 및 파라미터 설정
        List<String> list  = new ArrayList<>();
        list.add("kakao_account.profile");
        list.add("kakao_account.email");
        list.add("kakao_account.birthday");
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("property_keys",ob.writeValueAsString(list));

        //http 요청 및 응답
        String response = wc.post()
                .uri(KakaoInfo.userInfoUri)
                .header("Authorization", "Bearer " + accessToken)
                .bodyValue(params)
                .retrieve()
                .bodyToMono(String.class)
                .block();


        //json 변환
        KakaoProfile kakaoProfile = ob.readValue(response, KakaoProfile.class);
        return kakaoProfile;
    }


}
