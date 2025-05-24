-- Create database
CREATE DATABASE incident_db;

-- Connect to the database
\c incident_db;

-- Create user roles enum type
CREATE TYPE user_role AS ENUM ('CITIZEN', 'DISPATCHER', 'RESPONDER');

-- Create incident status enum type
CREATE TYPE incident_status AS ENUM ('REPORTED', 'ASSIGNED', 'RESOLVED');

-- Create incident type enum type
CREATE TYPE incident_type AS ENUM ('FIRE', 'MEDICAL', 'POLICE', 'OTHER');

-- Create users table
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    role user_role NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create incidents table
CREATE TABLE incidents (
    id SERIAL PRIMARY KEY,
    description TEXT NOT NULL,
    status incident_status NOT NULL DEFAULT 'REPORTED',
    incident_type incident_type NOT NULL DEFAULT 'OTHER',
    created_by INT NOT NULL REFERENCES users(id),
    assigned_by INT REFERENCES users(id),
    assigned_to INT REFERENCES users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for better query performance
/*CREATE INDEX idx_incidents_status ON incidents(status);
CREATE INDEX idx_users_role ON users(role);*/

-- Create function to update updated_at column
/* CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create triggers for automatic updated_at updates
CREATE TRIGGER update_users_updated_at
BEFORE UPDATE ON users
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_incidents_updated_at
BEFORE UPDATE ON incidents
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();*/