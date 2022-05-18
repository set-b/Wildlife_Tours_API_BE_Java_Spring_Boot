package com.example.ecommerce.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import java.util.Objects;
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
  // required and cannot be null 
  private Contact contact;
  // debit/credit card regex?
  private String cardNo;
  // date format pattern yyyy-mm-dd
  private Date reservedDate;
  private Long tourId;

  public TourBooking(){}

  public TourBooking(Contact contact, String cardNo, Date reservedDate, Long tourId) {
    this.contact = contact;
    this.cardNo = cardNo;
    this.reservedDate = reservedDate;
    this.tourId = tourId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Contact getContact() {
    return contact;
  }

  public void setContact(Contact contact) {
    this.contact = contact;
  }

  public String getCardNo() {
    return cardNo;
  }

  public void setCardNo(String cardNo) {
    this.cardNo = cardNo;
  }

  public Date getReservedDate() {
    return reservedDate;
  }

  public void setReservedDate(Date reservedDate) {
    this.reservedDate = reservedDate;
  }

  public Long getTourId() {
    return tourId;
  }

  public void setTourId(Long tourId) {
    this.tourId = tourId;
  }

  @Override
  public String toString() {
    return "TourBooking{" +
        "id=" + id +
        ", contact=" + contact +
        ", cardNo='" + cardNo + '\'' +
        ", reservedDate=" + reservedDate +
        ", tourId=" + tourId +
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
    TourBooking that = (TourBooking) o;
    return Objects.equals(id, that.id) && Objects.equals(contact, that.contact)
        && Objects.equals(cardNo, that.cardNo) && Objects.equals(reservedDate,
        that.reservedDate) && Objects.equals(tourId, that.tourId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, contact, cardNo, reservedDate, tourId);
  }

  @JsonIgnore
  public boolean isEmpty() {
    return
        Objects.isNull(id) &&
            Objects.isNull(contact) &&
            Objects.isNull(cardNo) &&
            Objects.isNull(reservedDate) &&
            Objects.isNull(tourId);
  }
}
