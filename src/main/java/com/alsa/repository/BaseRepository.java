package com.alsa.repository;

import com.alsa.domain.Base;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by alsa on 16.11.2016.
 */
public interface BaseRepository extends JpaRepository<Base, Long> {
}
