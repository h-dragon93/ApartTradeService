package com.estate.hdragon.domain.account;

import com.estate.hdragon.domain.account.kakao.KakaoConfig;
import com.estate.hdragon.domain.account.kakao.KakaoProfile;
import com.estate.hdragon.domain.account.kakao.KakaoToken;
import com.estate.hdragon.domain.account.kakao.refreshToken.RefreshToken;
import com.estate.hdragon.domain.account.kakao.refreshToken.RefreshTokenRedisRepository;
import com.estate.hdragon.infra.common.CommonConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/login")
@RequiredArgsConstructor
public class AccountController {

    private final KakaoConfig kakaoConfig;
    private final AccountProvider accountProvider;
    private final AccountService accountService;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;

    @Value("${kakao.apiKey}")
    private String API_KEY;

    @GetMapping("")
    public String login() {
        return "user/login";
    }

    @GetMapping("/loginForm")
    public String loginForm() { return "user/login"; }

    @GetMapping("/signin")
    public String signIn() { return "user/form"; }

    // 카카오로그인 인가코드 받기
    @GetMapping("/getKakaoAuthCode")
    public @ResponseBody String getKakaoAuthUrl(HttpServletRequest request) throws Exception {

        final String responseType = "&response_type=code";
        final String redirectUri = "&redirect_uri=";
        String reqURI = kakaoConfig.getAuthUrl() + kakaoConfig.getUri() + API_KEY + redirectUri + kakaoConfig.getRedirectUrl() + responseType;

        return reqURI;
    }

    // 카카오 연동정보 조회
    // 로그인 세션이 있는 경우 /getKakaoAuthCode를 안거치고 여기로 바로 들어온다
    @GetMapping(value = "/oauth/kakao")
    public String oauthKakao(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {


        ifErrorRedirectLogin(request);  // 카카오로그인 페이지에서 에러 발생한 경우 리다이렉트
        KakaoToken kakaoToken = accountProvider.getKakaoToken(request, request.getParameter("code")); // 카카오로그인 토큰 get
        KakaoProfile kakaoProfile = accountProvider.getKakaoProfile(kakaoToken.getAccess_token());  // 카카오로그인 계정정보 get
        Long kakaoUniqueId= kakaoProfile.getId();

        if (kakaoUniqueId != null && !kakaoUniqueId.equals("")) {

            Optional<Account> account =  accountService.getAccountByKakaoUID(kakaoUniqueId);        // DB 회원정보 조회
            if (!account.isPresent()) { // DB에 kakao UID가 없으면 자동 회원가입
                Account newAccount = makeNewAccount(kakaoProfile, kakaoUniqueId);
                accountService.saveAccount(newAccount); //builder로 save 하는 부분은 나중에 리팩토링하자
                account =  accountService.getAccountByKakaoUID(kakaoUniqueId);
            }
            else {
                // To-Do 이미 등록된 kakao unique ID 이면
            }

            RefreshToken refreshToken = new RefreshToken(kakaoToken.getRefresh_token(), kakaoUniqueId);     // 리프레시 토큰 생성
            refreshTokenRedisRepository.save(refreshToken);                                                 // Redis 저장

            session.setAttribute(CommonConfig.USER_SESSION_ID, kakaoToken.getRefresh_token());

            System.out.println("session id : " + session.getId());
            System.out.println("session : " + session.getAttribute(CommonConfig.USER_SESSION_ID));// 세션에 재발급 토큰을 저장
            System.out.println(refreshToken.toString());
            System.out.println(kakaoToken.toString());
            System.out.println(account);

            return "redirect:/";

        }   else {
            //  To-Do 카카오톡 정보조회 실패했을경우
            // throw new ErrorMessage("카카오톡 정보조회에 실패했습니다.");
            return "error";
        }
    }

    private Account makeNewAccount(KakaoProfile kakaoProfile, Long kakaoUniqueId) {

        String kakaoEmail = kakaoProfile.getKakao_account().getEmail();
        String kakaoNickname = kakaoProfile.getProperties().getNickname();
        Account newAccount = Account.builder()
                .id(kakaoUniqueId)
                .email(kakaoEmail)
                .nickname(kakaoNickname)
                .createdDate(LocalDate.now())
                .build();

        return newAccount;
    }

    private String ifErrorRedirectLogin(HttpServletRequest request) {

        String error = request.getParameter("error");
        if (error != null && error.equals("access_denied")) {
            return "redirect:/login";
        }
        return "";
    }

}

