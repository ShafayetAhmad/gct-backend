# Greenwich Community Theatre API Documentation

## Base URL

`http://localhost:8080/api`

## Authentication Endpoints

### Register User

```http
POST /auth/register
```

**Request Body:**

```json
{
  "email": "string",
  "password": "string",
  "fullName": "string",
  "role": "CUSTOMER" // Optional, defaults to CUSTOMER
}
```

**Response:** `200 OK`

```json
{
  "status": 200,
  "data": {
    "token": "string"
  }
}
```

### Login

```http
POST /auth/login
```

**Request Body:**

```json
{
  "email": "string",
  "password": "string"
}
```

**Response:** `200 OK`

```json
{
  "status": 200,
  "data": {
    "token": "string"
  }
}
```

## Performance Management

### List All Performances

```http
GET /performances
```

**Response:** `200 OK`

```json
{
  "status": 200,
  "data": [
    {
      "id": "long",
      "title": "string",
      "description": "string",
      "dateTime": "datetime",
      "basePrice": "decimal",
      "availableSeats": "integer"
    }
  ]
}
```

### Get Performance Details

```http
GET /performances/{id}
```

**Response:** `200 OK`

```json
{
  "status": 200,
  "data": {
    "id": "long",
    "title": "string",
    "description": "string",
    "dateTime": "datetime",
    "basePrice": "decimal",
    "seatMap": {
      "rows": "integer",
      "seatsPerRow": "integer",
      "availableSeats": [
        {
          "id": "long",
          "row": "string",
          "number": "integer",
          "band": "string",
          "status": "string"
        }
      ]
    }
  }
}
```

### Create Performance (Admin/Staff Only)

```http
POST /performances
```

**Request Body:**

```json
{
  "title": "string",
  "description": "string",
  "dateTime": "datetime",
  "basePrice": "decimal"
}
```

## Booking Management

### Create Booking

```http
POST /bookings
```

**Request Body:**

```json
{
  "performanceId": "long",
  "seats": [
    {
      "seatId": "long",
      "discountType": "string" // NONE, CHILD, SENIOR, SOCIAL_CLUB
    }
  ]
}
```

**Response:** `200 OK`

```json
{
  "status": 200,
  "data": {
    "bookingId": "long",
    "totalPrice": "decimal",
    "seats": [
      {
        "seatLocation": "string",
        "price": "decimal",
        "discountType": "string"
      }
    ],
    "status": "string"
  }
}
```

### Get User's Bookings

```http
GET /bookings/me
```

### Cancel Booking

```http
POST /bookings/{id}/cancel
```

## Payment Processing

### Process Payment

```http
POST /payments/process
```

**Request Body:**

```json
{
  "bookingId": "long",
  "cardNumber": "string",
  "expiryDate": "string",
  "cvv": "string"
}
```

**Response:** `200 OK`

```json
{
  "status": 200,
  "data": {
    "transactionId": "string",
    "status": "string",
    "amount": "decimal"
  }
}
```

### Request Refund (Admin/Staff Only)

```http
POST /payments/{bookingId}/refund
```

## Review Management

### Submit Review

```http
POST /reviews
```

**Request Body:**

```json
{
  "playId": "long",
  "rating": "integer",
  "comment": "string"
}
```

### Get Play Reviews

```http
GET /reviews/play/{playId}
```

### Moderate Review (Admin/Staff Only)

```http
PUT /reviews/{id}/status
```

**Request Body:**

```json
{
  "status": "string" // APPROVED, REJECTED
}
```

## Theatre Package Management

### Get User's Package Status

```http
GET /packages/me
```

**Response:** `200 OK`

```json
{
  "status": 200,
  "data": {
    "playsBooked": "integer",
    "freeTicketsEarned": "integer",
    "isActive": "boolean"
  }
}
```

## Seat Management

### Get Seat Map

```http
GET /performances/{id}/seats
```

**Response:** `200 OK`

```json
{
  "status": 200,
  "data": {
    "rows": "integer",
    "seatsPerRow": "integer",
    "seats": [
      {
        "id": "long",
        "row": "string",
        "number": "integer",
        "band": "string", // A, B, C
        "status": "string" // AVAILABLE, BOOKED
      }
    ]
  }
}
```

## Error Responses

### 400 Bad Request

```json
{
  "status": 400,
  "message": "Invalid request parameters",
  "errors": {
    "field": "error message"
  }
}
```

### 401 Unauthorized

```json
{
  "status": 401,
  "message": "Authentication required"
}
```

### 403 Forbidden

```json
{
  "status": 403,
  "message": "Access denied"
}
```

### 404 Not Found

```json
{
  "status": 404,
  "message": "Resource not found"
}
```

### 409 Conflict

```json
{
  "status": 409,
  "message": "Resource already exists"
}
```

## Notes

1. All requests except `/auth/register` and `/auth/login` require JWT authentication
2. JWT token should be included in the Authorization header: `Bearer <token>`
3. Discount Types:
   - NONE (no discount)
   - CHILD (25% off)
   - SENIOR (25% off)
   - SOCIAL_CLUB (5% off, additional 5% for 20+ tickets)
4. Additional discounts:
   - Weekday Special: 10% off Monday-Thursday
   - Last Hour: 10% off in the last hour before performance
   - Theatre Package: Free ticket after booking 4 plays
5. Seat Bands:
   - Band A: Full price
   - Band B: 80% of full price
   - Band C: 60% of full price
