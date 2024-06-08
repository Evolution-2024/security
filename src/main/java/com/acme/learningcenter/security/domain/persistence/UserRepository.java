package com.acme.learningcenter.security.domain.persistence;

import com.acme.learningcenter.security.domain.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Boolean existsByUsername(String username);
  Boolean existsByEmail(String email);

  Optional<User> findByEmail(String username);

  @Query(value = "select distinct c from User c " +
    "left join c.roles cr " +
    "where (c.id = :id or :id is null) " +
    "and (cr.id = :roleId or :roleId is null)"
  )
  List<User> findByFilter(
    @Param("id") Long id,
    @Param("roleId") Long roleId
  );
}
