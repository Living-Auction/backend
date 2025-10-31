package com.project.livingauction.user.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.livingauction.user.dto.LoginRequestDto;
import com.project.livingauction.user.entity.SocialAccount;
import com.project.livingauction.user.entity.User;
import com.project.livingauction.user.repository.SocialAccountRepository;
import com.project.livingauction.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final SocialAccountRepository socialAccountRepository;

    @Transactional
    public User handleOAuthLogin(LoginRequestDto request) {
        Optional<SocialAccount> existingAccount =
                socialAccountRepository.findByProviderAndProviderId(
                        request.getProvider(), request.getProviderId()
                );

        if (existingAccount.isPresent()) {
            return existingAccount.get().getUser();
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseGet(() -> registerNewUser(request));

        SocialAccount account = SocialAccount.builder()
                .user(user)
                .provider(request.getProvider())
                .providerId(request.getProviderId())
                .build();
        socialAccountRepository.save(account);

        return user;
    }

    private User registerNewUser(LoginRequestDto request) {
        String nickname = generateUniqueNicknameFromEmailLocalPart(request.getEmail());

        User user = User.builder()
                .email(request.getEmail())
                .nickname(nickname)
                .profileImg(request.getPicture())
                .isVerified(true)
                .build();

        return userRepository.save(user);
    }

    private String generateUniqueNicknameFromEmailLocalPart(String email) {
        String base = email.split("@")[0];
        String candidate = base;
        int suffix = 0;
        while (userRepository.findByEmail(email).isPresent() == false
                && nicknameExists(candidate)) {
            suffix++;
            candidate = base + "_" + suffix;
        }
        return candidate;
    }

    private boolean nicknameExists(String nickname) {
        // UserRepository에 boolean existsByNickname(String nickname); 추가 권장
        // 임시 구현을 위해 findByEmail로 못합니다. 실제로는 아래 라인을 쓰세요:
        // return userRepository.existsByNickname(nickname);
        return false;
    }
}