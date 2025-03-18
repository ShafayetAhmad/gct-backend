-- Create users table
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

-- Create plays table
CREATE TABLE plays (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT
);

-- Create performances table
CREATE TABLE performances (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    play_id BIGINT NOT NULL,
    date_time DATETIME NOT NULL,
    base_price DECIMAL(10,2) NOT NULL,
    available_seats INT NOT NULL,
    is_cancelled BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (play_id) REFERENCES plays(id)
);

-- Create seats table
CREATE TABLE seats (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    performance_id BIGINT NOT NULL,
    row_number INT NOT NULL,
    seat_number INT NOT NULL,
    band VARCHAR(1) NOT NULL,
    is_booked BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (performance_id) REFERENCES performances(id)
);

-- Create theatre_packages table
CREATE TABLE theatre_packages (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    plays_booked INT DEFAULT 0,
    free_tickets_earned INT DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Create bookings table
CREATE TABLE bookings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    performance_id BIGINT NOT NULL,
    total_price DECIMAL(10,2) NOT NULL,
    booking_time DATETIME NOT NULL,
    status VARCHAR(20) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (performance_id) REFERENCES performances(id)
);

-- Create booked_seats table
CREATE TABLE booked_seats (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    booking_id BIGINT NOT NULL,
    seat_id BIGINT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    discount_type VARCHAR(20),
    FOREIGN KEY (booking_id) REFERENCES bookings(id),
    FOREIGN KEY (seat_id) REFERENCES seats(id)
);

-- Create reviews table
CREATE TABLE reviews (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    play_id BIGINT NOT NULL,
    rating INT NOT NULL,
    comment TEXT,
    created_at DATETIME NOT NULL,
    status VARCHAR(20) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (play_id) REFERENCES plays(id)
);

-- Create payments table
CREATE TABLE payments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    booking_id BIGINT NOT NULL,
    transaction_id VARCHAR(255) NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    payment_time DATETIME NOT NULL,
    status VARCHAR(20) NOT NULL,
    FOREIGN KEY (booking_id) REFERENCES bookings(id)
); 