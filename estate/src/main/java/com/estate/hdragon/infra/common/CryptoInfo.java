package com.estate.hdragon.infra.common;

import com.estate.hdragon.infra.util.AESCryptoUtil;
import lombok.Getter;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;


public class CryptoInfo {

    private @Getter static SecretKey key;
    private @Getter static IvParameterSpec ivParameterSpec;

    private CryptoInfo() {
        {
            try {
                key = AESCryptoUtil.getKey();
                ivParameterSpec = AESCryptoUtil.getIv();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static CryptoInfo getInstance() {
        return LazyHolder.INSTANCE;
    }

    private static class LazyHolder {
        private static final CryptoInfo INSTANCE = new CryptoInfo();
    }
}
