package com.example.ecommerce.models;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tours")
public class Tour {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private int numerOfDays;
  private String[] vaccinations;
  private String description;
  private BigDecimal price;
  private int rating;
  private boolean isActive;
}
