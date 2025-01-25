use car_rental_system;

-- Таблица для автомобилей
CREATE TABLE Cars (
    Car_ID INT PRIMARY KEY IDENTITY(1,1),  /* Уникальный идентификатор автомобиля */
    Model VARCHAR(100) NOT NULL,           /* Модель автомобиля */
    License_Plate VARCHAR(20) NOT NULL,    /* Номер автомобиля */
    Status VARCHAR(50) NOT NULL             /* Статус автомобиля */
);

-- Таблица для клиентов
CREATE TABLE Clients (
    Client_ID INT PRIMARY KEY IDENTITY(1,1),  /* Уникальный идентификатор клиента */
    Name VARCHAR(100) NOT NULL,                /* Имя клиента */
    Passport_Data VARCHAR(20) NOT NULL         /* Паспортные данные клиента */
);

-- Таблица для заявок
CREATE TABLE Requests (
    Request_ID INT PRIMARY KEY IDENTITY(1,1),   /* Уникальный идентификатор заявки */
    Client_ID INT NOT NULL,                      /* Идентификатор клиента */
    Car_ID INT NOT NULL,                         /* Идентификатор автомобиля */
    Rental_Period INT NOT NULL,                  /* Срок аренды */
    FOREIGN KEY (Client_ID) REFERENCES Clients(Client_ID), /* Связь с клиентами */
    FOREIGN KEY (Car_ID) REFERENCES Cars(Car_ID)          /* Связь с автомобилями */
);

-- Таблица для заказов
CREATE TABLE Orders (
    Order_ID INT PRIMARY KEY IDENTITY(1,1),     /* Уникальный идентификатор заказа */
    Request_ID INT NOT NULL,                     /* Идентификатор заявки */
    Amount DECIMAL(10, 2) NOT NULL,             /* Сумма заказа */
    FOREIGN KEY (Request_ID) REFERENCES Requests(Request_ID) /* Связь с заявками */
);