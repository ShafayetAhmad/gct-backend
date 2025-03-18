# Theatre Management System API Documentation

## Base URL

```
http://localhost:8080/api
```

## URL Conventions

- All endpoints use plural nouns (e.g., `/performances`, `/bookings`, `/reviews`)
- Resource IDs are specified in the URL path (e.g., `/performances/{id}`)
- Nested resources use the parent resource's ID (e.g., `/reviews/play/{playId}`)
- Query parameters are used for filtering and pagination
- All paths are case-sensitive

## Authentication

All endpoints except login and register require JWT authentication. Include the token in the Authorization header:

```
Authorization: Bearer <jwt_token>
```

## Endpoints

### Authentication

#### Register User

```http
POST /auth/register
```

Request Body:

```json
{
  "email": "john.doe@example.com",
  "password": "securePassword123",
  "fullName": "John Doe"
}
```

Response:

```json
{
  "status": 200,
  "message": "User registered successfully",
  "data": {
    "id": 1,
    "email": "john.doe@example.com",
    "fullName": "John Doe",
    "role": "CUSTOMER"
  }
}
```

#### Login

```http
POST /auth/login
```

Request Body:

```json
{
  "email": "john.doe@example.com",
  "password": "securePassword123"
}
```

Response:

```json
{
  "status": 200,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "user": {
      "id": 1,
      "email": "john.doe@example.com",
      "fullName": "John Doe",
      "role": "CUSTOMER"
    }
  }
}
```

### Plays

#### Create Play

```http
POST /plays
```

Request Body:

```json
{
  "title": "Hamlet",
  "description": "A tragedy by William Shakespeare"
}
```

Response:

```json
{
  "status": 200,
  "message": "Play created successfully",
  "data": {
    "id": 1,
    "title": "Hamlet",
    "description": "A tragedy by William Shakespeare"
  }
}
```

#### Get All Plays

```http
GET /plays
```

Response:

```json
{
  "status": 200,
  "message": "Plays retrieved successfully",
  "data": [
    {
      "id": 1,
      "title": "Hamlet",
      "description": "A tragedy by William Shakespeare"
    },
    {
      "id": 2,
      "title": "Romeo and Juliet",
      "description": "A romantic tragedy by William Shakespeare"
    }
  ]
}
```

#### Get Play by ID

```http
GET /plays/{id}
```

Response:

```json
{
  "status": 200,
  "message": "Play retrieved successfully",
  "data": {
    "id": 1,
    "title": "Hamlet",
    "description": "A tragedy by William Shakespeare"
  }
}
```

### Performances

#### Create Performance

```http
POST /performances
```

Request Body:

```json
{
  "playId": 1,
  "dateTime": "2024-04-15T19:00:00",
  "basePrice": 50.0
}
```

Response:

```json
{
  "status": 200,
  "message": "Performance created successfully",
  "data": {
    "id": 1,
    "playTitle": "Hamlet",
    "dateTime": "2024-04-15T19:00:00",
    "basePrice": 50.0,
    "availableSeats": 100
  }
}
```

#### Get All Performances

```http
GET /performances
```

Response:

```json
{
  "status": 200,
  "message": "Performances retrieved successfully",
  "data": [
    {
      "id": 1,
      "playTitle": "Hamlet",
      "dateTime": "2024-04-15T19:00:00",
      "basePrice": 50.0,
      "availableSeats": 100
    }
  ]
}
```

#### Get Performance by ID

```http
GET /performances/{id}
```

Response:

```json
{
  "status": 200,
  "message": "Performance retrieved successfully",
  "data": {
    "id": 1,
    "playTitle": "Hamlet",
    "dateTime": "2024-04-15T19:00:00",
    "basePrice": 50.0,
    "availableSeats": 100,
    "seatMap": [
      {
        "id": 1,
        "rowNumber": "A",
        "seatNumber": "1",
        "band": "A",
        "isBooked": false
      }
    ]
  }
}
```

### Bookings

#### Create Booking

```http
POST /bookings
```

Request Body:

```json
{
  "performanceId": 1,
  "seats": [
    {
      "seatId": 1,
      "discountType": "NONE"
    }
  ]
}
```

Response:

```json
{
  "status": 200,
  "message": "Booking created successfully",
  "data": {
    "id": 1,
    "performanceTitle": "Hamlet",
    "performanceDateTime": "2024-04-15T19:00:00",
    "seats": [
      {
        "seatLocation": "A1",
        "band": "A",
        "discountType": "NONE",
        "price": 50.0
      }
    ],
    "totalPrice": 50.0,
    "status": "PENDING",
    "bookingTime": "2024-03-20T10:00:00"
  }
}
```

#### Get User's Bookings

```http
GET /bookings/me
```

Response:

```json
{
  "status": 200,
  "message": "Bookings retrieved successfully",
  "data": [
    {
      "id": 1,
      "performanceTitle": "Hamlet",
      "performanceDateTime": "2024-04-15T19:00:00",
      "seats": [
        {
          "seatLocation": "A1",
          "band": "A",
          "discountType": "NONE",
          "price": 50.0
        }
      ],
      "totalPrice": 50.0,
      "status": "CONFIRMED",
      "bookingTime": "2024-03-20T10:00:00"
    }
  ]
}
```

#### Cancel Booking

```http
POST /bookings/{id}/cancel
```

Response:

```json
{
  "status": 200,
  "message": "Booking cancelled successfully",
  "data": {
    "id": 1,
    "performanceTitle": "Hamlet",
    "performanceDateTime": "2024-04-15T19:00:00",
    "seats": [
      {
        "seatLocation": "A1",
        "band": "A",
        "discountType": "NONE",
        "price": 50.0
      }
    ],
    "totalPrice": 50.0,
    "status": "CANCELLED",
    "bookingTime": "2024-03-20T10:00:00"
  }
}
```

### Payments

#### Process Payment

```http
POST /payments/process
```

Request Body:

```json
{
  "bookingId": 1,
  "cardNumber": "4111111111111111",
  "expiryDate": "12/25",
  "cvv": "123"
}
```

Response:

```json
{
  "status": 200,
  "message": "Payment processed successfully",
  "data": {
    "transactionId": "TXN123456",
    "bookingId": 1,
    "amount": 50.0,
    "status": "SUCCESS",
    "paymentTime": "2024-03-20T10:00:00"
  }
}
```

#### Refund Payment

```http
POST /payments/{bookingId}/refund
```

Response:

```json
{
  "status": 200,
  "message": "Payment refunded successfully",
  "data": {
    "transactionId": "TXN123456",
    "bookingId": 1,
    "amount": 50.0,
    "status": "REFUNDED",
    "paymentTime": "2024-03-20T10:00:00"
  }
}
```

### Reviews

#### Create Review

```http
POST /reviews
```

Request Body:

```json
{
  "playId": 1,
  "rating": 5,
  "comment": "Excellent performance!"
}
```

Response:

```json
{
  "status": 200,
  "message": "Review created successfully",
  "data": {
    "id": 1,
    "userFullName": "John Doe",
    "playTitle": "Hamlet",
    "rating": 5,
    "comment": "Excellent performance!",
    "createdAt": "2024-03-20T10:00:00",
    "status": "PENDING"
  }
}
```

#### Get Play Reviews

```http
GET /reviews/play/{playId}
```

Response:

```json
{
  "status": 200,
  "message": "Reviews retrieved successfully",
  "data": [
    {
      "id": 1,
      "userFullName": "John Doe",
      "playTitle": "Hamlet",
      "rating": 5,
      "comment": "Excellent performance!",
      "createdAt": "2024-03-20T10:00:00",
      "status": "APPROVED"
    }
  ]
}
```

#### Moderate Review

```http
PATCH /reviews/{id}/moderate
```

Query Parameters:

- `status`: APPROVED, REJECTED

Response:

```json
{
  "status": 200,
  "message": "Review moderated successfully",
  "data": {
    "id": 1,
    "userFullName": "John Doe",
    "playTitle": "Hamlet",
    "rating": 5,
    "comment": "Excellent performance!",
    "createdAt": "2024-03-20T10:00:00",
    "status": "APPROVED"
  }
}
```

### Theatre Packages

#### Get User's Package

```http
GET /packages/me
```

Response:

```json
{
  "status": 200,
  "message": "Package retrieved successfully",
  "data": {
    "id": 1,
    "playsBooked": 5,
    "freeTicketsEarned": 1,
    "isActive": true
  }
}
```

## Error Responses

### 400 Bad Request

```json
{
  "status": 400,
  "message": "Invalid request data",
  "errors": [
    {
      "field": "email",
      "message": "Invalid email format"
    }
  ]
}
```

### 401 Unauthorized

```json
{
  "status": 401,
  "message": "Unauthorized access"
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

### 500 Internal Server Error

```json
{
  "status": 500,
  "message": "Internal server error"
}
```
