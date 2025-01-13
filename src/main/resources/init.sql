CREATE TABLE persons (
    id SERIAL PRIMARY KEY,               -- Auto-incremented primary key
    first_name VARCHAR(100) NOT NULL,    -- First name (required)
    middle_name VARCHAR(100),            -- Middle name (optional)
    last_name VARCHAR(100) NOT NULL,     -- Last name (required)
    full_name VARCHAR(300),              -- Full name (can be computed if needed)
    email VARCHAR(255) UNIQUE,           -- Email address (optional but unique)
    mobile VARCHAR(13) UNIQUE,           -- Mobile number (optional but unique)
    uid VARCHAR(16) UNIQUE,              -- Unique Identifier (e.g., Aadhaar)
    pan VARCHAR(10) UNIQUE,              -- PAN number (India-specific)
    uan VARCHAR(12) UNIQUE,              -- Universal Account Number (India-specific)
    passport VARCHAR(15) UNIQUE,         -- Passport number
    instagram VARCHAR(100),              -- Instagram handle
    twitter VARCHAR(100),                -- Twitter handle
    facebook VARCHAR(100),               -- Facebook username or ID
    snapchat VARCHAR(100),               -- Snapchat username
    github VARCHAR(100),                 -- GitHub username
    hackerrank VARCHAR(100),             -- HackerRank username
    hackernews VARCHAR(100),             -- HackerNews username
    created_at TIMESTAMP DEFAULT NOW(),  -- Timestamp for record creation
    updated_at TIMESTAMP DEFAULT NOW()   -- Timestamp for last update
);

INSERT INTO persons(first_name, last_name) VALUES ('Shubham', 'Varshney');
