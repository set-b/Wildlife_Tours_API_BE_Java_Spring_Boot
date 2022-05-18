package com.example.ecommerce.services;

import static com.example.ecommerce.constants.StringConstants.NOT_FOUND;

import com.example.ecommerce.exceptions.BadDataResponse;
import com.example.ecommerce.exceptions.ResourceNotFound;
import com.example.ecommerce.exceptions.ServiceUnavailable;
import com.example.ecommerce.models.TourBooking;
import com.example.ecommerce.repositories.TourBookingRepository;
import java.util.Comparator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

@Service
public class TourBookingServiceImpl implements TourBookingService {

  private final Logger logger = LoggerFactory.getLogger(TourBookingService.class);

  @Autowired
  private TourBookingRepository tourBookingRepository;

  @Override
  public List<TourBooking> queryTourBookings(TourBooking tourBooking) {
    try {
      if (tourBooking.isEmpty()) {
        List<TourBooking> tourBookings = tourBookingRepository.findAll();
        tourBookings.sort(Comparator.comparing(TourBooking::getId));
        return tourBookings;
      } else {
        Example<TourBooking> tourBookingExample = Example.of(tourBooking);
        return tourBookingRepository.findAll(tourBookingExample);
      }
    } catch (Exception e) {
      logger.error(e.getMessage());
      throw new ServiceUnavailable(e);
    }
  }

  @Override
  public TourBooking getTourBookingById(Long id) {
    if (id < 1) {
      throw new BadDataResponse("id must be positive and cannot be zero");
    }
    TourBooking tourBookingLookUpResult;
    try {
      tourBookingLookUpResult = tourBookingRepository.findById(id).orElse(null);
      if (tourBookingLookUpResult != null) {
        return tourBookingLookUpResult;
      }
    } catch (Exception e) {
      throw new ServiceUnavailable(e.getMessage());
    }
    throw new ResourceNotFound(NOT_FOUND + " tourBooking with id " + id);
  }

  @Override
  public TourBooking addTourBooking(TourBooking tourBooking) {
    try {
      return tourBookingRepository.save(tourBooking);
    } catch (Exception e) {
      throw new ServiceUnavailable(e);
    }
  }

  @Override
  public TourBooking updateTourBookingById(Long id, TourBooking tourBooking) {
    if (id < 1) {
      throw new BadDataResponse("id must be positive and cannot be zero");
    }
    TourBooking updatedTourBooking = null;
    if (!tourBookingRepository.existsById(id)) {
      throw new ResourceNotFound(NOT_FOUND + "tourBooking with id " + id);
    }
    try {
      tourBooking.setId(id);
      updatedTourBooking = tourBookingRepository.save(tourBooking);
    } catch (Exception e) {
      throw new ServiceUnavailable(e);
    }
    return updatedTourBooking;
  }

  @Override
  public void deleteTourBookingById(Long id) {
    if (id < 1) {
      throw new BadDataResponse("id must be positive and cannot be zero");
    }
    getTourBookingById(id);
    try {
      tourBookingRepository.deleteById(id);
    } catch (Exception e) {
      throw new ServiceUnavailable("Something went wrong");
    }
  }
}
