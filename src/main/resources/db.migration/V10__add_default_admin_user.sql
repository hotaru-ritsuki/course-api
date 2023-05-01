INSERT INTO course_management.users
(
     first_name,
     last_name,
     email,
     password,
     role,
     created_date,
     modified_date,
     created_by,
     modified_by
 )
VALUES (
        'Admin',
        'Phantom',
        'admin.phantom@courses.com',
        '$2a$12$icb9BIES3BgjXkHv1V2acu4YPcYJGNUVwjg2gZtyuSDVO4bQ/Flte',
        'ADMIN',
        CURRENT_DATE,
        CURRENT_DATE,
        'admin',
        'admin'
    )
