package com.alsa.repository;

import com.alsa.domain.Base;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * Created by alsa on 16.11.2016.
 */
@PreAuthorize("hasRole('ROLE_USER')")
public interface BaseRepository extends PagingAndSortingRepository<Base, Long> {
    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    <S extends Base> S save(S s);

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    void delete(Base base);

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    void deleteById(Long aLong);

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    void deleteAll(Iterable<? extends Base> iterable);

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    void deleteAll();
}
