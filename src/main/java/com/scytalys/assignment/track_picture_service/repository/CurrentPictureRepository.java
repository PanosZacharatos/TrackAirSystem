package com.scytalys.assignment.track_picture_service.repository;

import com.scytalys.assignment.track_picture_service.model.CurrentPicture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrentPictureRepository extends JpaRepository<CurrentPicture, String> {
}
