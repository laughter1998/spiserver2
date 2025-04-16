package org.zerock.spiserver2.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.zerock.spiserver2.util.CustomJWTException;
import org.zerock.spiserver2.util.JWTUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@Log4j2
public class APIRefreshController {

    @RequestMapping("/api/member/refresh")
    public Map<String, Object> refresh(@RequestHeader("Authorization") String authHeader, @RequestParam("refreshToken") String refreshToken) {
        // refreshToken이 없는 경우 예외 처리
        if (refreshToken == null) {
            throw new CustomJWTException("NULL_REFRESH");
        }

        // Authorization 헤더 값이 잘못된 경우 예외 처리
        if (authHeader == null || authHeader.length() < 7) {
            throw new CustomJWTException("INVALID_STRING");
        }

        // Authorization 헤더에서 accessToken을 추출
        String accessToken = authHeader.substring(7);

        // accessToken이 아직 유효하다면 그대로 반환
        if (!checkExpiredToken(accessToken)) {
            log.info("Access 토큰이 만료되지 않았습니다.");
            return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
        }

        // refreshToken 검증
        try {
            log.info("Refresh 토큰 검증 중...");
            Map<String, Object> claims = JWTUtil.validateToken(refreshToken);
            log.info("Refresh 토큰의 클레임: " + claims);

            // 새로운 accessToken 생성
            String newAccessToken = JWTUtil.generateToken(claims, 10);

            // refreshToken이 1시간 이내로 만료되었으면 새로운 refreshToken 생성
            String newRefreshToken = checkTime((Integer) claims.get("exp"))
                    ? JWTUtil.generateToken(claims, 60 * 24)  // 24시간으로 갱신
                    : refreshToken;

            log.info("새로운 토큰들을 생성했습니다.");
            return Map.of("accessToken", newAccessToken, "refreshToken", newRefreshToken);

        } catch (CustomJWTException ex) {
            log.error("Refresh 토큰 검증 실패: " + ex.getMessage());
            throw ex;
        }
    }

    // refreshToken이 1시간 이내로 만료될 때 새로운 토큰을 발급하기 위한 확인
    private boolean checkTime(Integer exp) {
        // exp 값을 Date로 변환 (초 단위)
        java.util.Date expDate = new java.util.Date((long) exp * 1000);
        long gap = expDate.getTime() - System.currentTimeMillis();
        long leftMin = gap / (1000 * 60);  // 남은 시간 계산 (분 단위)
        return leftMin < 60;  // 1시간 미만이면 true 반환
    }

    // accessToken이 만료되었는지 확인
    private boolean checkExpiredToken(String token) {
        try {
            JWTUtil.validateToken(token);  // 유효한 토큰이라면 예외 발생하지 않음
        } catch (CustomJWTException ex) {
            if (ex.getMessage().equals("Expired")) {
                log.info("Access 토큰이 만료되었습니다.");
                return true;  // 만료된 토큰이라면 true 반환
            }
        }
        return false;  // 만료되지 않았다면 false
    }
}
