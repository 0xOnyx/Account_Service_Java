package account.security;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import jakarta.servlet.http.HttpServletRequest;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class LoginAttemptService {
    public static final int                 MAX_ATTEMPT = 5;
    private final LoadingCache<String, Integer>     attemptsCache;
    private final HttpServletRequest                request;

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginAttemptService.class);

    @Autowired
    public LoginAttemptService(
            HttpServletRequest request){
        super();
        this.request = request;
        this.attemptsCache = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.DAYS).build(new CacheLoader<String, Integer>() {
            @NotNull
            @Override
            public Integer load(@NotNull String key) {
                return 0;
            }
        });
    }

    public void loginSucceeded(String key) {
        attemptsCache.invalidate(key);
    }

    public void loginFailed(String key) {
        int attempts;

        try {
            attempts = attemptsCache.get(key);
        }
        catch (Exception e) {
            attempts = 0;
        }
        attempts++;
        attemptsCache.put(key, attempts);
        LOGGER.info("Login failed for user " + key + " attempt " + attempts + " size " + attemptsCache.size());
    }

    public boolean isBlocked(String key) {
        try {
            return attemptsCache.get(key) >= MAX_ATTEMPT;
        }
        catch (final ExecutionException e) {
            return false;
        }
    }

    public String getClientIP() {
        final String xfHeader = request.getHeader("X-Forwarded-For");

        if (xfHeader == null || xfHeader.isEmpty() || !xfHeader.contains(request.getRemoteAddr())) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
