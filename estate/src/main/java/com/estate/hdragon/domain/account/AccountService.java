package com.estate.hdragon.domain.account;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public Optional<Account> getAccountByKakaoUID(Long kakaoUniqueId) {
        Optional<Account> account = accountRepository.findById(kakaoUniqueId);
        return account;
    }

    public void saveAccount(Account account) {
        accountRepository.save(account);
    }

}
