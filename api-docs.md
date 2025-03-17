# Theatre Booking System API Documentation

## Base URL

`http://localhost:8080`

## Authentication

The API uses JWT (JSON Web Token) for authentication. Include the JWT token in the Authorization header:

```
Authorization: Bearer <your_jwt_token>
```

## API Endpoints

### Authentication

#### Register User

- **URL**: `/api/auth/register`
- **Method**: `POST`
- **Auth Required**: No
- **Description**: Register a new user in the system
- **Request Body**:
  ```json
  {
    "email": "string",
    "password": "string",
    "fullName": "string",
    "phoneNumber": "string"
  }
  ```
- **Success Response**: 200 OK
  ```json
  {
    "status": 200,
    "message": "Success",
    "data": {
      "token": "string",
      "user": {
        "id": "number",
        "email": "string",
        "fullName": "string",
        "role": "string"
      }
    }
  }
  ```

#### Login

- **URL**: `/api/auth/login`
- **Method**: `POST`
- **Auth Required**: No
- **Description**: Authenticate user and get JWT token
- **Request Body**:
  ```json
  {
    "email": "string",
    "password": "string"
  }
  ```
- **Success Response**: 200 OK
  ```json
  {
    "status": 200,
    "message": "Success",
    "data": {
      "token": "string",
      "user": {
        "id": "number",
        "email": "string",
        "fullName": "string",
        "role": "string"
      }
    }
  }
  ```

### Bookings

#### Create Booking

- **URL**: `/api/bookings`
- **Method**: `POST`
- **Auth Required**: Yes
- **Roles**: CUSTOMER, STAFF, ADMIN
- **Description**: Create a new booking for a performance
- **Request Body**:
  ```json
  {
    "performanceId": "number",
    "seats": [
      {
        "seatId": "number",
        "discountType": "NONE|STUDENT|SENIOR|CHILD"
      }
    ]
  }
  ```
- **Success Response**: 200 OK
  ```json
  {
    "status": 200,
    "message": "Success",
    "data": {
      "id": "number",
      "performanceDetails": {
        "playTitle": "string",
        "dateTime": "string"
      },
      "bookedSeats": [
        {
          "seatLocation": "string",
          "price": "number",
          "discountType": "string"
        }
      ],
      "totalPrice": "number",
      "status": "string",
      "bookingTime": "string"
    }
  }
  ```

#### Get User's Bookings

- **URL**: `/api/bookings/my-bookings`
- **Method**: `GET`
- **Auth Required**: Yes
- **Roles**: CUSTOMER, STAFF, ADMIN
- **Description**: Get all bookings for the authenticated user
- **Success Response**: 200 OK
  ```json
  {
    "status": 200,
    "message": "Success",
    "data": [
      {
        "id": "number",
        "performanceDetails": {
          "playTitle": "string",
          "dateTime": "string"
        },
        "bookedSeats": [],
        "totalPrice": "number",
        "status": "string",
        "bookingTime": "string"
      }
    ]
  }
  ```

#### Cancel Booking

- **URL**: `/api/bookings/{bookingId}/cancel`
- **Method**: `POST`
- **Auth Required**: Yes
- **Roles**: CUSTOMER, STAFF, ADMIN
- **Description**: Cancel an existing booking
- **Path Parameters**: bookingId (number)
- **Success Response**: 200 OK
  ```json
  {
    "status": 200,
    "message": "Success",
    "data": {
      "id": "number",
      "status": "CANCELLED",
      "bookingTime": "string"
    }
  }
  ```

### Payments

#### Process Payment

- **URL**: `/api/payments/process`
- **Method**: `POST`
- **Auth Required**: Yes
- **Roles**: CUSTOMER, STAFF, ADMIN
- **Description**: Process payment for a booking
- **Request Body**:
  ```json
  {
    "bookingId": "number",
    "cardNumber": "string",
    "expiryDate": "string",
    "cvv": "string"
  }
  ```
- **Success Response**: 200 OK
  ```json
  {
    "status": 200,
    "message": "Success",
    "data": {
      "id": "number",
      "transactionId": "string",
      "amount": "number",
      "status": "COMPLETED",
      "paymentTime": "string"
    }
  }
  ```

#### Refund Payment

- **URL**: `/api/payments/{bookingId}/refund`
- **Method**: `POST`
- **Auth Required**: Yes
- **Roles**: STAFF, ADMIN
- **Description**: Refund payment for a booking
- **Path Parameters**: bookingId (number)
- **Success Response**: 200 OK
  ```json
  {
    "status": 200,
    "message": "Success",
    "data": {
      "id": "number",
      "transactionId": "string",
      "amount": "number",
      "status": "REFUNDED",
      "paymentTime": "string"
    }
  }
  ```

### Reviews

#### Create Review

- **URL**: `/api/reviews`
- **Method**: `POST`
- **Auth Required**: Yes
- **Roles**: CUSTOMER, STAFF, ADMIN
- **Description**: Create a review for a play
- **Request Body**:
  ```json
  {
    "playId": "number",
    "rating": "number (1-5)",
    "comment": "string"
  }
  ```
- **Success Response**: 200 OK
  ```json
  {
    "status": 200,
    "message": "Success",
    "data": {
      "id": "number",
      "userFullName": "string",
      "playTitle": "string",
      "rating": "number",
      "comment": "string",
      "createdAt": "string",
      "status": "PENDING"
    }
  }
  ```

#### Get Play Reviews

- **URL**: `/api/reviews/play/{playId}`
- **Method**: `GET`
- **Auth Required**: No
- **Description**: Get all approved reviews for a play
- **Path Parameters**: playId (number)
- **Success Response**: 200 OK
  ```json
  {
    "status": 200,
    "message": "Success",
    "data": [
      {
        "id": "number",
        "userFullName": "string",
        "playTitle": "string",
        "rating": "number",
        "comment": "string",
        "createdAt": "string",
        "status": "APPROVED"
      }
    ]
  }
  ```

#### Moderate Review

- **URL**: `/api/reviews/{reviewId}/moderate`
- **Method**: `PATCH`
- **Auth Required**: Yes
- **Roles**: STAFF, ADMIN
- **Description**: Moderate a review (approve/reject)
- **Path Parameters**: reviewId (number)
- **Query Parameters**: status (APPROVED|REJECTED)
- **Success Response**: 200 OK
  ```json
  {
    "status": 200,
    "message": "Success",
    "data": {
      "id": "number",
      "userFullName": "string",
      "playTitle": "string",
      "rating": "number",
      "comment": "string",
      "createdAt": "string",
      "status": "string"
    }
  }
  ```

## Error Responses

All endpoints can return the following error responses:

### 400 Bad Request

```json
{
  "status": 400,
  "message": "Error description",
  "data": null
}
```

### 401 Unauthorized

```json
{
  "status": 401,
  "message": "Unauthorized",
  "data": null
}
```

### 403 Forbidden

```json
{
  "status": 403,
  "message": "Access denied",
  "data": null
}
```

### 404 Not Found

```json
{
  "status": 404,
  "message": "Resource not found",
  "data": null
}
```

### 500 Internal Server Error

```json
{
  "status": 500,
  "message": "An unexpected error occurred",
  "data": null
}
```
