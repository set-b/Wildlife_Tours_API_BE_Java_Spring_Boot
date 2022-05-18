package com.example.ecommerce.models;

import static com.example.ecommerce.constants.StringConstants.REQUIRED_FIELD;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.OptBoolean;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.CreditCardNumber;

@Entity
@Table(name = "tour_bookings")
public class TourBooking {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @NotNull(message = "Contact " + REQUIRED_FIELD)
  private Contact contact;
  @NotBlank(message = "Payment method must include card number")
//  @Pattern(regexp =
//      "^(?:4[0-9]{12}(?:[0-9]{3})?|[25][1-7][0-9]{14}|6(?:011|5[0-9][0-9])[0-9]{12}|3[47][0-9]{13}|3(?:0[0-5]|[68][0-9])[0-9]{11}|(?:2131|1800|35\\d{3})\\d{11})$",
//      message = "Payment method must be a valid card number")
  @CreditCardNumber // todo test this
  private String cardNo;
  @NotNull(message = "Reserved date " + REQUIRED_FIELD)
  @JsonFormat(pattern = "yyyy-MM-dd", lenient = OptBoolean.FALSE)// todo test this
  //@DateTimeFormat(pattern = "yyyy-MM-dd") //
  private Date reservedDate;
  @NotNull(message = "Tour id " + REQUIRED_FIELD)
  @Valid
  private Long tourId;

  public TourBooking() {
  }

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
