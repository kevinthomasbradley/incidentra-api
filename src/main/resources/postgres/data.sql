-- Switch to the target database before inserting data
\c incident_db;

-- ---------------------------------------------------------
-- Insert sample users into the "users" table.
-- Passwords are bcrypt-hashed (use plain text only for demo/testing).
-- Roles must match the User.Role enum in the application (CITIZEN, DISPATCHER, RESPONDER).
-- Columns: username, password, email, role
-- ---------------------------------------------------------
INSERT INTO users (username, password, email, role) VALUES
    -- Regular citizen users
    ('john_doe', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGgaXNlK', 'john@example.com', 'CITIZEN'),
    ('alice_smith', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGgaXNlK', 'alice@example.com', 'CITIZEN'),
    ('bob_johnson', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGgaXNlK', 'bob@example.com', 'CITIZEN'),
    -- Dispatcher user
    ('dispatch_dave', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGgaXNlK', 'dave@example.com', 'DISPATCHER'),
    -- Responder user
    ('responder_rachel', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGgaXNlK', 'rachel@example.com', 'RESPONDER');

-- ---------------------------------------------------------
-- Insert sample incidents into the "incidents" table.
-- Columns: description, status, incident_type, created_by, assigned_by, assigned_to
--  - status: REPORTED, ASSIGNED, RESOLVED (must match Incident.Status enum)
--  - incident_type: FIRE, MEDICAL, POLICE, OTHER (must match IncidentType enum)
--  - created_by: user ID of the reporting citizen (foreign key to users)
--  - assigned_by: user ID of the dispatcher (nullable, foreign key to users)
--  - assigned_to: user ID of the responder (nullable, foreign key to users)
-- ---------------------------------------------------------
INSERT INTO incidents (description, status, incident_type, created_by, assigned_by, assigned_to) VALUES
    -- Incident reported but not yet assigned
    ('Pothole on Main Street', DEFAULT, 'OTHER', 1, NULL, NULL),
    -- Incident assigned to a responder
    ('Fallen tree blocking road', 'ASSIGNED', 'OTHER', 2, 3, 4),
    -- Incident resolved
    ('Power outage in downtown area', 'RESOLVED', 'OTHER', 5, 3, 4),
    -- Another reported incident
    ('Broken traffic light at 5th Ave', 'REPORTED', 'OTHER', 2, NULL, NULL),
    -- Another assigned incident
    ('Flooding in park area', 'ASSIGNED', 'OTHER', 1, 3, 4);

-- Notes:
-- - Ensure that referenced user IDs exist in the users table.
-- - The DEFAULT keyword for status will use the default value defined in the schema (typically 'REPORTED').
-- - This data is for development/demo purposes and should not be used in production.