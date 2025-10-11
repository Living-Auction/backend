package com.project.livingauction.oauth.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.project.livingauction.oauth.model.CustomOAuth2User;
import com.project.livingauction.user.entity.SocialAccount;
import com.project.livingauction.user.entity.User;
import com.project.livingauction.user.repository.SocialAccountRepository;
import com.project.livingauction.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	private final UserRepository userRepository;
	private final SocialAccountRepository socialAccountRepository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);

		String provider = userRequest.getClientRegistration().getRegistrationId(); // google/kakao
		Map<String, Object> attributes = oAuth2User.getAttributes();

		String providerId;
		String email;
		String name;

		switch (provider) {
		case "google":
			providerId = (String) attributes.get("sub");
			email = (String) attributes.get("email");
			name = (String) attributes.get("name");
			break;
		case "kakao":
			providerId = attributes.get("id").toString();
			Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
			email = (String) kakaoAccount.get("email");
			Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
			name = (String) profile.get("nickname");
			break;
		default:
			throw new OAuth2AuthenticationException("Unsupported provider: " + provider);
		}

		// SocialAccount 존재 여부 확인
		Optional<SocialAccount> socialAccountOpt = socialAccountRepository.findByProviderAndProviderId(provider,
				providerId);

		User user;
		if (socialAccountOpt.isPresent()) {
			user = socialAccountOpt.get().getUser();
		} else {
			user = userRepository.save(User.builder().email(email).nickname(name).build());

			socialAccountRepository
					.save(SocialAccount.builder().user(user).provider(provider).providerId(providerId).build());
		}

		return new CustomOAuth2User(user, attributes);
	}
	
	public User getUserByAuthentication() {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();

        return userRepository.findByEmail(authentication.getName())
                .orElseThrow();
    }
}
