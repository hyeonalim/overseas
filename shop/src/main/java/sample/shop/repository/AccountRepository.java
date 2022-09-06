package sample.shop.repository;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import sample.shop.domain.Account;

@Repository
@RequiredArgsConstructor
public class AccountRepository {

    @Autowired
    public EntityManager em;

    public Long save(Account account) {
        em.persist(account);
        return account.getNo();
    }
}
