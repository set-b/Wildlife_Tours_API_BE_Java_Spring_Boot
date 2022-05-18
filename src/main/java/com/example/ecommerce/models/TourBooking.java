package com.example.ecommerce.models;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.metamodel.model.domain.IdentifiableDomainType;

@Entity
@Table(name = "tour_bookings")
public class TourBooking {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Contact contact;
  private String cardNo;
  private Date reservedDate;
  private Long tourId;
}
