-- =========================================================
-- PostgreSQL schema for the Incidentra application database
-- This script creates the database, enums, tables, and (optionally) triggers.
-- =========================================================

-- 1. Create the main application database
CREATE DATABASE incident_db;

-- 2. Connect to the newly created database
\c incident_db;

-- 3. Define custom ENUM types for user roles, incident status, and incident type
-- ---------------------------------------------------------
-- user_role: Allowed roles for users in the system
CREATE TYPE user_role AS ENUM ('CITIZEN', 'DISPATCHER', 'RESPONDER');

-- incident_status: Allowed statuses for incidents
CREATE TYPE incident_status AS ENUM ('REPORTED', 'ASSIGNED', 'RESOLVED');

-- incident_type: Allowed types of incidents
CREATE TYPE incident_type AS ENUM ('FIRE', 'MEDICAL', 'POLICE', 'OTHER');

-- 4. Create the users table
-- ---------------------------------------------------------
-- Stores user accounts for all roles (citizen, dispatcher, responder)
CREATE TABLE users (
    id SERIAL PRIMARY KEY,                        -- Unique user ID (auto-increment)
    username VARCHAR(50) UNIQUE NOT NULL,         -- Unique username for login
    password VARCHAR(100) NOT NULL,               -- Hashed password
    email VARCHAR(100) UNIQUE NOT NULL,           -- Unique email address
    role user_role NOT NULL,                      -- Role of the user (enum)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Timestamp when user was created
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP  -- Timestamp when user was last updated
);

-- 5. Create the incidents table
-- ---------------------------------------------------------
-- Stores all reported, assigned, and resolved incidents
CREATE TABLE incidents (
    id SERIAL PRIMARY KEY,                        -- Unique incident ID (auto-increment)
    description TEXT NOT NULL,                    -- Description of the incident
    status incident_status NOT NULL DEFAULT 'REPORTED', -- Current status (enum)
    incident_type incident_type NOT NULL DEFAULT 'OTHER', -- Type of incident (enum)
    created_by INT NOT NULL REFERENCES users(id), -- User ID of the reporting citizen
    assigned_by INT REFERENCES users(id),         -- User ID of the dispatcher (nullable)
    assigned_to INT REFERENCES users(id),         -- User ID of the responder (nullable)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Timestamp when incident was created
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP  -- Timestamp when incident was last updated
);

-- 6. (Optional) Create indexes for faster queries on status and role
/*
CREATE INDEX idx_incidents_status ON incidents(status);
CREATE INDEX idx_users_role ON users(role);
*/

-- 7. (Optional) Create triggers to automatically update the updated_at column on row updates
/*
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_users_updated_at
BEFORE UPDATE ON users
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_incidents_updated_at
BEFORE UPDATE ON incidents
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();
*/

-- =========================================================
-- End of schema.sql
-- =========================================================