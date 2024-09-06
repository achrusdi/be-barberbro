package com.enigmacamp.barbershop.service.Impl;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import com.enigmacamp.barbershop.constant.BookingStatus;
import com.enigmacamp.barbershop.constant.PaymentStatus;
import com.enigmacamp.barbershop.model.dto.request.MidtransRequest;
import com.enigmacamp.barbershop.model.entity.Barbers;
import com.enigmacamp.barbershop.model.entity.Booking;
import com.enigmacamp.barbershop.model.entity.Customer;
import com.enigmacamp.barbershop.repository.BookingRepository;
import com.enigmacamp.barbershop.service.BarberService;
import com.enigmacamp.barbershop.service.BookingService;

@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final BarberService barberService;
    private final RestClient restClient;
    private final String MIDTRANS_KEY;
    private final String MIDTRANS_URL;

    public BookingServiceImpl(BookingRepository bookingRepository,
            @Value("${midtrans.api.server-key}") String secretKey, @Value("${midtrans.api.snap-url}") String snapUrl,
            RestClient restClient, BarberService barberService) {
        this.barberService = barberService;
        this.restClient = restClient;
        this.MIDTRANS_URL = snapUrl;
        this.bookingRepository = bookingRepository;
        this.MIDTRANS_KEY = secretKey;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Booking create(Booking booking) {
        try {

            List<MidtransRequest.ItemDetails> servicesMidtrans = booking.getServices().stream()
                    .map(service -> MidtransRequest.ItemDetails.builder()
                            .id(service.getService_id())
                            .price((double) Math.round(service.getPrice()))
                            .quantity(1)
                            .name(service.getService_name())
                            .build())
                    .toList();

            Double amount = servicesMidtrans.stream().mapToDouble(s -> s.getPrice()).sum();

            // List<com.enigmacamp.barbershop.model.entity.Service> services =
            // booking.getServices();

            // Double totalPrice =
            // services.stream().mapToDouble(com.enigmacamp.barbershop.model.entity.Service::getPrice)
            // .sum();

            booking.setTotalPrice(amount);

            booking.setStatus(BookingStatus.Pending.name());

            if (booking.getServices().isEmpty()) {
                throw new RuntimeException("Please add at least one service");
            }

            bookingRepository.save(booking);

            Customer customer = booking.getCustomerId();

            String base64EncodedKey = Base64.getEncoder().encodeToString((MIDTRANS_KEY + ":").getBytes());

            MidtransRequest midtransRequest = MidtransRequest.builder()
                    .transaction_details(MidtransRequest.TransactionDetails.builder()
                            .orderId(booking.getBookingId())
                            .grossAmount(amount)
                            .build())
                    .creditCard(MidtransRequest.CreditCard.builder()
                            .secure(true)
                            .build())
                    .customerDetails(MidtransRequest.CustomerDetails.builder()
                            .firstName(customer.getFirstName())
                            .lastName(customer.getSurname())
                            .email(customer.getUserId().getEmail())
                            .phone(customer.getPhone())
                            .build())
                    .itemDetails(servicesMidtrans)
                    .build();

            ResponseEntity<Map<String, String>> response = restClient.post()
                    .uri(MIDTRANS_URL)
                    .body(midtransRequest)
                    .header(HttpHeaders.AUTHORIZATION, "Basic " + base64EncodedKey)
                    .retrieve()
                    .toEntity(new ParameterizedTypeReference<Map<String, String>>() {
                    });

            if (response.getStatusCode().equals(HttpStatus.CREATED)) {
                booking.setMidtransPaymentUrl(response.getBody().get("redirect_url"));
            }

            return bookingRepository.save(booking);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Booking getById(String bookingId) {
        try {
            return bookingRepository.findById(bookingId).orElse(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Booking update(Booking booking) {
        try {
            return bookingRepository.save(booking);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Booking> getByCustomer(Customer customer) {
        try {
            return bookingRepository.findByCustomerId(customer).orElse(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Booking> getByBarber(Barbers barber) {
        try {
            return bookingRepository.findByBarberId(barber).orElse(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Booking cancel(Barbers barbers, String bookingId) {
        try {
            Booking booking = bookingRepository.findByBarberIdAndBookingId(barbers, bookingId).orElse(null);

            if (booking == null) {
                throw new RuntimeException();
            }

            booking.setStatus(BookingStatus.Canceled.name());

            return bookingRepository.save(booking);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Booking completed(Barbers barbers, String bookingId) {
        try {
            Booking booking = bookingRepository.findByBarberIdAndBookingId(barbers, bookingId).orElse(null);

            if (booking == null) {
                throw new RuntimeException();
            }

            booking.setStatus(BookingStatus.Completed.name());

            return bookingRepository.save(booking);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Booking> getAll() {
        return bookingRepository.findAll();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Booking getMidtransUrl(Booking booking) {
        try {

            // if (booking.getMidtransPaymentUrl() != null) {
            // return booking;
            // }

            // Customer customer = booking.getCustomerId();

            // List<MidtransRequest.ItemDetails> services = booking.getServices().stream()
            // .map(service -> MidtransRequest.ItemDetails.builder()
            // .id(service.getService_id())
            // .price((double) Math.round(service.getPrice()))
            // .quantity(1)
            // .name(service.getService_name())
            // .build())
            // .toList();

            // Double amount = services.stream().mapToDouble(s -> s.getPrice()).sum();

            // String base64EncodedKey = Base64.getEncoder().encodeToString((MIDTRANS_KEY +
            // ":").getBytes());

            // MidtransRequest midtransRequest = MidtransRequest.builder()
            // .transaction_details(MidtransRequest.TransactionDetails.builder()
            // .orderId(booking.getBookingId())
            // .grossAmount(amount)
            // .build())
            // .creditCard(MidtransRequest.CreditCard.builder()
            // .secure(true)
            // .build())
            // .customerDetails(MidtransRequest.CustomerDetails.builder()
            // .firstName(customer.getFirstName())
            // .lastName(customer.getSurname())
            // .email(customer.getUserId().getEmail())
            // .phone(customer.getPhone())
            // .build())
            // .itemDetails(services)
            // .build();

            // ResponseEntity<Map<String, String>> response = restClient.post()
            // .uri(MIDTRANS_URL)
            // .body(midtransRequest)
            // .header(HttpHeaders.AUTHORIZATION, "Basic " + base64EncodedKey)
            // .retrieve()
            // .toEntity(new ParameterizedTypeReference<Map<String, String>>() {
            // });

            // if (response.getStatusCode().equals(HttpStatus.CREATED)) {
            // booking.setMidtransPaymentUrl(response.getBody().get("redirect_url"));
            // }

            // return bookingRepository.saveAndFlush(booking);

            return null;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Booking updateBookingStatus(Booking booking) {
        try {
            if (!booking.getStatus().equals(BookingStatus.Pending.name())) {
                return booking;
            }

            Barbers barber = booking.getBarberId();

            String base64EncodedKey = Base64.getEncoder().encodeToString((MIDTRANS_KEY + ":").getBytes());

            ResponseEntity<Map<String, String>> response = restClient.get()
                    .uri("https://api.sandbox.midtrans.com/v2/" + booking.getBookingId() + "/status")
                    .header(HttpHeaders.AUTHORIZATION, "Basic " + base64EncodedKey)
                    .retrieve()
                    .toEntity(new ParameterizedTypeReference<Map<String, String>>() {
                    });

            if (response.getStatusCode().equals(HttpStatus.OK)
                    && !response.getBody().get("status_code").equals("404")) {
                if (response.getBody().get("transaction_status").equals("settlement")) {
                    booking.setStatus(BookingStatus.Confirmed.name());
                    // payment.setPaymentStatus(PaymentStatus.COMPLETED.name());
                    booking.setStatus(BookingStatus.Settlement.name());
                    barber.setBalance((float) (booking.getTotalPrice() + barber.getBalance()));
                    barberService.update(barber);
                } else {
                    booking.setStatus(PaymentStatus.PENDING.name());
                }

                bookingRepository.saveAndFlush(booking);
            }

            return booking;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Booking> getAllByBarberAndDate(Barbers barber, Long date) {
        try {

            LocalDate localDate = getLocalDateFromEpochMillis(date);
            Long start = getStartOfDayEpochMillis(localDate);
            Long end = getEndOfDayEpochMillis(localDate);

            return bookingRepository.findByBarberIdAndBookingDateRange(barber, start, end);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private LocalDate getLocalDateFromEpochMillis(Long timestampNow) {
        Instant instant = Instant.ofEpochMilli(timestampNow);
          ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
          
          return zdt.toLocalDate();
    }

    public Long getStartOfDayEpochMillis(LocalDate date) {
        return date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public Long getEndOfDayEpochMillis(LocalDate date) {
        return date.atTime(23, 59, 59, 999_999_999)
                   .atZone(ZoneId.systemDefault())
                   .toInstant()
                   .toEpochMilli();
    }
}