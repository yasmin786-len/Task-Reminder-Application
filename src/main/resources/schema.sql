CREATE TABLE tasks (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       description VARCHAR(500),
                       due_date TIMESTAMP NOT NULL,
                       completed BOOLEAN NOT NULL DEFAULT FALSE,
                       email VARCHAR(255) NOT NULL
);
