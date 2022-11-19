package com.beautyline.polimi.repository;

import com.beautyline.polimi.entity.AccountEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

	Optional<AccountEntity> findByReferenceTypeAndEmailAndPassword(AccountEntity.Type type, String email, String password);

	Optional<AccountEntity> findByReferenceTypeAndEmail(AccountEntity.Type type, String email);

	Optional<AccountEntity> findByReferenceId(Long id);

	Page<AccountEntity> findAllByReferenceType(AccountEntity.Type type, Pageable pageable);

	Optional<AccountEntity> findByEmailAndPassword(String email, String password);

	Optional<AccountEntity> findByEmail(String email);
}
