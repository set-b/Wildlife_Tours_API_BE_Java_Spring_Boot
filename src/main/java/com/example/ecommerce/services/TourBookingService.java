package com.example.ecommerce.services;

import com.example.ecommerce.models.TourBooking;
import java.util.List;

public interface TourBookingService {

  List<TourBooking> queryTourBookings(TourBooking tourBooking);

  TourBooking getTourBookingById(Long id);

  TourBooking addTourBooking(TourBooking tourBooking);

  TourBooking updateTourBookingById(Long id, TourBooking tourBooking);
  TourBooking getTourBookingByTourCode(String tourCode);
  void deleteTourBookingById(Long id);
}
