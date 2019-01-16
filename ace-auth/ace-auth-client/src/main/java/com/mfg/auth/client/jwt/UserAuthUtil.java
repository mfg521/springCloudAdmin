package com.mfg.auth.client.jwt;

import com.mfg.auth.client.config.UserAuthConfig;
import com.mfg.auth.common.exception.auth.UserTokenException;
import com.mfg.auth.common.util.jwt.CipherHelper;
import com.mfg.auth.common.util.jwt.IJWTInfo;
import com.mfg.auth.common.util.jwt.JWTHelper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * Created by mengfanguang on 2017/9/15.
 */
@Configuration
public class UserAuthUtil {
    @Autowired
    private UserAuthConfig userAuthConfig;
    public IJWTInfo getInfoFromToken(String token) throws Exception {
        try {
            return JWTHelper.getInfoFromToken(token, userAuthConfig.getPubKeyByte());
        }catch (ExpiredJwtException ex){
            throw new UserTokenException("User token expired!");
        }catch (SignatureException ex){
            throw new UserTokenException("User token signature error!");
        }catch (IllegalArgumentException ex){
            throw new UserTokenException("User token is null or empty!");
        }
    }

    public String getInfoFromTokenCipher(String token) throws Exception {
            return CipherHelper.decrypt(token);

    }
}
