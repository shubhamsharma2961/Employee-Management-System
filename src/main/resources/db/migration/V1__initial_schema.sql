CREATE TABLE departments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by UUID,
    updated_by UUID,
    is_deleted BOOLEAN DEFAULT FALSE,
    name VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE designations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by UUID,
    updated_by UUID,
    is_deleted BOOLEAN DEFAULT FALSE,
    name VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE qualification_types (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by UUID,
    updated_by UUID,
    is_deleted BOOLEAN DEFAULT FALSE,
    name VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE document_types (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by UUID,
    updated_by UUID,
    is_deleted BOOLEAN DEFAULT FALSE,
    name VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE company_details (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by UUID,
    updated_by UUID,
    is_deleted BOOLEAN DEFAULT FALSE,
    name VARCHAR(255) NOT NULL,
    address TEXT,
    contact_number VARCHAR(20),
    email VARCHAR(100),
    logo_url TEXT
);

CREATE TABLE roles (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by UUID,
    updated_by UUID,
    is_deleted BOOLEAN DEFAULT FALSE,
    name VARCHAR(50) UNIQUE NOT NULL,
    description TEXT
);

CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by UUID,
    updated_by UUID,
    is_deleted BOOLEAN DEFAULT FALSE,
    email VARCHAR(150) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    is_password_changed BOOLEAN DEFAULT FALSE
);


INSERT INTO company_details (name, address, contact_number, email)
VALUES ('Thakral One EMS', 'Kathmandu, Nepal', '9876543210', 'hr@thakralone.com');

INSERT INTO departments (name) VALUES ('IT'), ('HR'), ('Finance'), ('Operations'), ('Sales') 
ON CONFLICT (name) DO NOTHING;

INSERT INTO designations (name) VALUES ('Associate Engineer'), ('Senior Engineer'), ('Project Manager'), ('HR Manager'), ('Accountant') 
ON CONFLICT (name) DO NOTHING;

INSERT INTO qualification_types (name) VALUES ('Bachelor''s'), ('Master''s'), ('PhD'), ('Diploma'), ('Certification') 
ON CONFLICT (name) DO NOTHING;

INSERT INTO document_types (name) VALUES ('ID Card'), ('Degree Certificate'), ('Experience Letter'), ('Passport'), ('PAN Card') 
ON CONFLICT (name) DO NOTHING;

INSERT INTO roles (name, description) VALUES 
('HR_ADMIN', 'Full CRUD access to the entire system'),
('DEPARTMENT_HEAD', 'View access for Own department'),
('STAFF', 'Personal profile management only')
ON CONFLICT (name) DO NOTHING;

-- Password is BCrypt for "9876543210"
INSERT INTO users (email, password, is_password_changed)
VALUES ('admin@ems.com', '$2a$10$2jP.Q72O2EpcRFV4uHunpu/VlGF6s6OaQHXN0stjlpfF72TqTDiFe', FALSE)
ON CONFLICT (email) DO NOTHING;