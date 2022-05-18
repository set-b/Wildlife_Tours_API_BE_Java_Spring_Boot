package com.example.ecommerce.services;

import com.example.ecommerce.models.Tour;
import java.util.List;

public interface TourService {

  List<Tour> queryTours(Tour tour);

  Tour getTourById(Long id);

  Tour addTour(Tour tour);

  Tour updateTourById(Long id, Tour tour);

  void deleteTourById(Long id);
}
