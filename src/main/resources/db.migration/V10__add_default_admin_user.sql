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
        '$2a$12$YZ3AvqMHayJFaVbdrUZNNOJpWnVWpGhQwvLCJBAQBt3uwAWLhak4u',
        'ADMIN',
        CURRENT_DATE,
        CURRENT_DATE,
        'admin',
        'admin'
    )
