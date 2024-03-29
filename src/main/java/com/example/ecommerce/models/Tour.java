package com.example.ecommerce.models;

import static com.example.ecommerce.constants.StringConstants.REQUIRED_FIELD;

import com.example.ecommerce.validators.Vaccinations;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

@Entity
@Table(name = "tours")
public class Tour {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Positive
  @Min(1)
  private int numberOfDays;
  @Vaccinations
  private String[] vaccinations;
  @NotNull(message = "Description " + REQUIRED_FIELD)
  @Length(min = 7)
  private String description;
  @NotNull(message = "Price " + REQUIRED_FIELD)
  @NumberFormat(style = Style.CURRENCY)
  private BigDecimal price;
  @Min(1)
  @Max(5)
  private int rating;
  //@NotNull()
  private boolean isActive;

  public Tour() {
  }

  public Tour(int numberOfDays, String[] vaccinations, String description, BigDecimal price,
      int rating, boolean isActive) {
    this.numberOfDays = numberOfDays;
    this.vaccinations = vaccinations;
    this.description = description;
    this.price = price;
    this.rating = rating;
    this.isActive = isActive;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public int getNumberOfDays() {
    return numberOfDays;
  }

  public void setNumberOfDays(int numberOfDays) {
    this.numberOfDays = numberOfDays;
  }

  public String[] getVaccinations() {
    return vaccinations;
  }

  public void setVaccinations(String[] vaccinations) {
    this.vaccinations = vaccinations;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public int getRating() {
    return rating;
  }

  public void setRating(int rating) {
    this.rating = rating;
  }

  public boolean isActive() {
    return isActive;
  }

  public void setActive(boolean active) {
    isActive = active;
  }

  @Override
  public String toString() {
    return "Tour{" +
        "id=" + id +
        ", numberOfDays=" + numberOfDays +
        ", vaccinations=" + Arrays.toString(vaccinations) +
        ", description='" + description + '\'' +
        ", price=" + price +
        ", rating=" + rating +
        ", isActive=" + isActive +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Tour tour = (Tour) o;
    return numberOfDays == tour.numberOfDays && rating == tour.rating && isActive == tour.isActive
        && Objects.equals(id, tour.id) && Arrays.equals(vaccinations,
        tour.vaccinations) && Objects.equals(description, tour.description)
        && Objects.equals(price, tour.price);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(id, numberOfDays, description, price, rating, isActive);
    result = 31 * result + Arrays.hashCode(vaccinations);
    return result;
  }

  @JsonIgnore
  public boolean isEmpty() {
    return
        Objects.isNull(id) &&
            Objects.isNull(numberOfDays) &&
            Objects.isNull(description) &&
            Objects.isNull(price) &&
            Objects.isNull(rating) &&
            Objects.isNull(isActive);
  }
}
