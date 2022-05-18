package com.example.ecommerce.services;

import static com.example.ecommerce.constants.StringConstants.NOT_FOUND;

import com.example.ecommerce.exceptions.BadDataResponse;
import com.example.ecommerce.exceptions.ResourceNotFound;
import com.example.ecommerce.exceptions.ServiceUnavailable;
import com.example.ecommerce.models.Tour;
import com.example.ecommerce.repositories.TourRepository;
import java.util.Comparator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

@Service
public class TourServiceImpl implements TourService {

  private final Logger logger = LoggerFactory.getLogger(TourService.class);

  @Autowired
  private TourRepository tourRepository;

  @Override
  public List<Tour> queryTours(Tour tour) {
    try {
      if (tour.isEmpty()) {
        List<Tour> tours = tourRepository.findAll();
        tours.sort(Comparator.comparing(Tour::getId));
        return tours;
      } else {
        Example<Tour> tourExample = Example.of(tour);
        return tourRepository.findAll(tourExample);
      }
    } catch (Exception e) {
      logger.error(e.getMessage());
      throw new ServiceUnavailable(e);
    }
  }

  @Override
  public Tour getTourById(Long id) {
    if (id < 1) {
      throw new BadDataResponse("id must be positive and cannot be zero");
    }
    Tour tourLookUpResult;
    try {
      tourLookUpResult = tourRepository.findById(id).orElse(null);
      if (tourLookUpResult != null) {
        return tourLookUpResult;
      }
    } catch (Exception e) {
      throw new ServiceUnavailable(e.getMessage());
    }
    throw new ResourceNotFound(NOT_FOUND + " tour with id " + id);
  }

  @Override
  public Tour addTour(Tour tour) {
    try {
      return tourRepository.save(tour);
    } catch (Exception e) {
      throw new ServiceUnavailable(e);
    }
  }

  @Override
  public Tour updateTourById(Long id, Tour tour) {
    if (id < 1) {
      throw new BadDataResponse("id must be positive and cannot be zero");
    }
    Tour updatedTour = null;
    if (!tourRepository.existsById(id)) {
      throw new ResourceNotFound(NOT_FOUND + "tour with id " + id);
    }
    try {
      tour.setId(id);
      updatedTour = tourRepository.save(tour);
    } catch (Exception e) {
      throw new ServiceUnavailable(e);
    }
    return updatedTour;
  }

  @Override
  public void deleteTourById(Long id) {
    if (id < 1) {
      throw new BadDataResponse("id must be positive and cannot be zero");
    }
    getTourById(id);
    try {
      tourRepository.deleteById(id);
    } catch (Exception e) {
      throw new ServiceUnavailable("Something went wrong");
    }
  }
}
