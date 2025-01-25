use car_rental_system;

-- ������� ��� �����������
CREATE TABLE Cars (
    Car_ID INT PRIMARY KEY IDENTITY(1,1),  /* ���������� ������������� ���������� */
    Model VARCHAR(100) NOT NULL,           /* ������ ���������� */
    License_Plate VARCHAR(20) NOT NULL,    /* ����� ���������� */
    Status VARCHAR(50) NOT NULL             /* ������ ���������� */
);

-- ������� ��� ��������
CREATE TABLE Clients (
    Client_ID INT PRIMARY KEY IDENTITY(1,1),  /* ���������� ������������� ������� */
    Name VARCHAR(100) NOT NULL,                /* ��� ������� */
    Passport_Data VARCHAR(20) NOT NULL         /* ���������� ������ ������� */
);

-- ������� ��� ������
CREATE TABLE Requests (
    Request_ID INT PRIMARY KEY IDENTITY(1,1),   /* ���������� ������������� ������ */
    Client_ID INT NOT NULL,                      /* ������������� ������� */
    Car_ID INT NOT NULL,                         /* ������������� ���������� */
    Rental_Period INT NOT NULL,                  /* ���� ������ */
    FOREIGN KEY (Client_ID) REFERENCES Clients(Client_ID), /* ����� � ��������� */
    FOREIGN KEY (Car_ID) REFERENCES Cars(Car_ID)          /* ����� � ������������ */
);

-- ������� ��� �������
CREATE TABLE Orders (
    Order_ID INT PRIMARY KEY IDENTITY(1,1),     /* ���������� ������������� ������ */
    Request_ID INT NOT NULL,                     /* ������������� ������ */
    Amount DECIMAL(10, 2) NOT NULL,             /* ����� ������ */
    FOREIGN KEY (Request_ID) REFERENCES Requests(Request_ID) /* ����� � �������� */
);