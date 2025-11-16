
-- Bảng roles
CREATE TABLE roles (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR2(20) 

);  


-- Bảng bkap_account
CREATE TABLE bkap_account (
    account_id VARCHAR2(50) PRIMARY KEY,
    full_name NVARCHAR2(500) NOT NULL,
    password VARCHAR2(100) NOT NULL,
    gender NUMBER,
    phone VARCHAR2(20),
    email VARCHAR2(100),
    role VARCHAR2(20) CHECK (role IN ('ADMIN', 'STAFF', 'CUSTOMER'))
);


-- Bảng user_role
CREATE TABLE user_role (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    userId VARCHAR2(50) REFERENCES bkap_account(account_id),
    roleId NUMBER REFERENCES roles(id)
);  

-- Bảng bkap_city
CREATE TABLE bkap_city (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name NVARCHAR2(100),
    description NVARCHAR2(200),
    url_image VARCHAR2(1000)
);

-- Bảng bkap_TYPE_HOTEL
CREATE TABLE bkap_type_hotel (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name NVARCHAR2(100),
    description NVARCHAR2(200),
    url_image VARCHAR2(1000)
);

-- Bảng bkap_HOTEL
CREATE TABLE bkap_hotel (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name NVARCHAR2(100),
    address NVARCHAR2(200),
    phone VARCHAR2(20),
    description NVARCHAR2(1000),
    near_sea NUMBER(1) CHECK (near_sea IN (0, 1)),
    evaluate NUMBER CHECK (evaluate >= 0 AND evaluate <= 5),
    city_id NUMBER REFERENCES bkap_city(id),
    type_hotel_id NUMBER REFERENCES bkap_type_hotel(id),
    staff_id VARCHAR2(50) REFERENCES bkap_account(account_id)
);

-- Bảng ROOM
CREATE TABLE bkap_room (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name NVARCHAR2(100),
    area NUMBER,
    price NUMBER,
    description NVARCHAR2(1000),
    bed NUMBER,
    hotel_id NUMBER REFERENCES bkap_hotel(id)
);

-- Bảng danh mục tiện ích
CREATE TABLE bkap_amenity (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name NVARCHAR2(200) NOT NULL,
    description NVARCHAR2(500)
);

-- Bảng liên kết phòng - tiện ích (N-N)
CREATE TABLE bkap_room_amenity (
    room_id NUMBER REFERENCES bkap_room(id),
    amenity_id NUMBER REFERENCES bkap_amenity(id),
    PRIMARY KEY (room_id, amenity_id)
);


-- Bảng Booking
CREATE TABLE bkap_booking (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    account_id VARCHAR2(50) REFERENCES bkap_account(account_id),
    total_amount NUMBER,
    is_cancel NUMBER(1) DEFAULT 0 CHECK (is_cancel IN (0, 1)),  
    payment_status VARCHAR2(20) DEFAULT 'UNPAID'
        CHECK (payment_status IN ('UNPAID',
         'PENDING', 'PAID', 'FAILED', 'REFUNDED')),
    created_at DATE DEFAULT SYSDATE,
    update_at DATE DEFAULT SYSDATE,
    delete_flg NUMBER(1) DEFAULT 0 CHECK (delete_flg IN (0,1))
);

-- Bảng trung gian liên kết N-N giữa booking và room

CREATE TABLE bkap_booking_room (
    booking_id NUMBER REFERENCES bkap_booking(id),
    room_id NUMBER REFERENCES bkap_room(id),
    check_in_date DATE NOT NULL,
    check_out_date DATE NOT NULL,
    price NUMBER(12, 2) NOT NULL,   
    created_at DATE DEFAULT SYSDATE,
    update_at DATE DEFAULT SYSDATE,
    delete_flg NUMBER(1) DEFAULT 0 CHECK (delete_flg IN (0,1)),
    PRIMARY KEY (booking_id, room_id)
);

-- Thanh toán

CREATE TABLE bkap_payment (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    booking_id NUMBER REFERENCES bkap_booking(id),
    payment_method VARCHAR2(50) DEFAULT 'VNPAY',
    vnp_txn_ref VARCHAR2(100) UNIQUE,  -- Mã giao dịch gửi VNPAY
    vnp_amount NUMBER,                 -- Số tiền thanh toán
    vnp_bank_code VARCHAR2(50),
    vnp_order_info VARCHAR2(255),
    vnp_transaction_no VARCHAR2(100),  -- Mã giao dịch do VNPAY trả về
    vnp_response_code VARCHAR2(10),    -- Mã phản hồi
    vnp_transaction_status VARCHAR2(10),-- Trạng thái giao dịch
    payment_status VARCHAR2(20) CHECK (payment_status IN ('PENDING','SUCCESS','FAILED','REFUNDED')),
    create_at   DATE DEFAULT SYSDATE,
    delete_flg  NUMBER(1) DEFAULT 0 CHECK (delete_flg IN (0, 1))
);


-- Hoàn tiền
CREATE TABLE bkap_refund (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    payment_id NUMBER REFERENCES bkap_payment(id),
    refund_amount NUMBER NOT NULL,
    refund_reason NVARCHAR2(255),
    refund_status VARCHAR2(20) CHECK (refund_status IN ('REQUESTED','PROCESSING','SUCCESS','FAILED')),
    refund_date DATE DEFAULT SYSDATE,
    delete_flg  NUMBER(1) DEFAULT 0 CHECK (delete_flg IN (0, 1))
);


-----------------------------------------------------------
INSERT INTO bkap_account (account_id, full_name, password, gender, phone, email, role)
     VALUES ('thoant', N'Nguyễn Thị Thoa', '123456', 1, '0909000001', 'admin@example.com', 'ADMIN'); 
INSERT INTO bkap_account (account_id, full_name, password, gender, phone, email, role)
     VALUES ('user1', N'Nguyễn Phương Linh', '123456', 0, '0909000002', 'user1@example.com', 'CUSTOMER');
INSERT INTO bkap_account (account_id, full_name, password, gender, phone, email, role)
     VALUES ('staff1', N'Lê Hoàng Nam', '123456', 0, '0909000002', 'staff1@example.com', 'STAFF');   


Insert into BKAP_CITY (NAME,DESCRIPTION,URL_IMAGE) values ('Hà Nội','Thành phố thủ đô Việt Nam','/images/ThanhPho/HaNoi.jpg');
Insert into BKAP_CITY (NAME,DESCRIPTION,URL_IMAGE) values ('TP. Hồ Chí Minh','Thành phố lớn nhất Việt Nam','/images/ThanhPho/HoChiMinh.jpg');
Insert into BKAP_CITY (NAME,DESCRIPTION,URL_IMAGE) values ('Đà Nẵng','Thành Phố Đà Nẵng','/images/ThanhPho/DaNang.jpg');
Insert into BKAP_CITY (NAME,DESCRIPTION,URL_IMAGE) values ('Huế','Thành Phố Huế','/images/ThanhPho/Hue.jpg');
Insert into BKAP_CITY (NAME,DESCRIPTION,URL_IMAGE) value ('Phú Quốc','Thành Phố Phú Quốc','/images/ThanhPho/PhuQuoc.jpg');
Insert into BKAP_CITY (NAME,DESCRIPTION,URL_IMAGE) values ('Sapa','Thành phố Sapa','/images/ThanhPho/Sapa.jpg');


INSERT INTO bkap_type_hotel (name, description, url_image) VALUES (N'Khách sạn', N'Khách sạn', 'Content/Images/LoaiKhachSan/KhachSan.jpg');
   INSERT INTO bkap_type_hotel (name, description, url_image) VALUES (N'Biệt thự', N'Biệt thự', 'Content/Images/LoaiKhachSan/BietThu.jpg');
    INSERT INTO bkap_type_hotel (name, description, url_image) VALUES (N'Resort', N'Resort', 'Content/Images/LoaiKhachSan/Resort.jpg');
    INSERT INTO bkap_type_hotel (name, description, url_image) VALUES (N'Nhà khách', N'Nhà khách', 'Content/Images/LoaiKhachSan/NhaKhach.jpg');
    INSERT INTO bkap_type_hotel (name, description, url_image) VALUES (N'Nhà trọ', N'Nhà trọ', 'Content/Images/LoaiKhachSan/NhaTro.jpg');
    INSERT INTO bkap_type_hotel (name, description, url_image) VALUES (N'Thôn dã', N'Thôn dã', 'Content/Images/LoaiKhachSan/ThonDa.jpg');
    INSERT INTO bkap_type_hotel (name, description, url_image) VALUES (N'Nhà gỗ', N'Nhà gỗ', 'Content/Images/LoaiKhachSan/NhaGo.jpg');
    INSERT INTO bkap_type_hotel (name, description, url_image) VALUES (N'Căn hộ', N'Căn hộ', 'Content/Images/LoaiKhachSan/CanHo.jpg');
    INSERT INTO bkap_type_hotel (name, description, url_image) VALUES (N'Glamping', N'Glamping', 'Content/Images/LoaiKhachSan/Glamping.jpg');

INSERT INTO bkap_hotel (name, address, phone, description, near_sea, evaluate, city_id, type_hotel_id) VALUES
        ('The Light Hotel',
         N'79 Tran Cung, Quận Từ Liêm, Hà Nội',
          '0366918587', 
        N'Nằm ở quận Hoàn Kiếm, thuộc thành phố Hà Nội, The Light Hotel chỉ cách 300 m từ Nhà Thờ Lớn. Với các phòng nghỉ thanh lịch và hiện đại, khách sạn có hồ bơi ngoài trời cùng tầm nhìn ra vườn.',
         0, 5, 3,4);
INSERT INTO bkap_hotel (name, address, phone, description, near_sea, evaluate, city_id, type_hotel_id) VALUES
        ('Rising Dragon Palace Hotel', N'24 Hang Ga Street, Quận Ba Đình, Hà Nội', '0328758787',
        N'Rising Dragon Palace Hotel cách Hồ Hoàn Kiếm và Nhà hát múa rối nước chỉ vài dãy nhà. Khách sạn cung cấp chỗ đỗ xe cũng như Wi-Fi miễn phí và bữa sáng tự chọn miễn phí.', 0, 4, 3, 4);


INSERT INTO bkap_room (name, area, price, description, bed, hotel_id)
         VALUES (N'Phòng Tiêu chuẩn', 15, 350, N'Phòng này có một số trang thiết bị đơn giản.', 0, 1);         
INSERT INTO bkap_room (name, area, price, description, bed, hotel_id)
         VALUES (N'Phòng Trăng Mật', 15, 400, N'Phòng này có máy lạnh, minibar và được cách âm. Phòng này được trang trí bằng hoa và bánh ngọt.', 1, 1);         
INSERT INTO bkap_room (name, area, price, description, bed, hotel_id)
         VALUES (N'Phòng Cao Cấp', 20, 400, N'Phòng cách âm này có máy lạnh.', 1, 2);        
INSERT  INTO bkap_room (name, area, price, description, bed, hotel_id)
         VALUES (N'Phòng Gia Đình', 25, 500, N'Phòng gia đình này có quầy bar mini, truyền hình cáp và máy lạnh.', 1, 2);


INSERT INTO bkap_amenity (name, description) VALUES (N'Ti vi màn hình phẳng', N'Ti vi LED 42 inch với các kênh quốc tế và giải trí theo yêu cầu');
INSERT INTO bkap_amenity (name, description) VALUES (N'Tủ lạnh mini', N'Tủ lạnh nhỏ trong phòng, có sẵn nước lọc và đồ uống nhẹ');
INSERT INTO bkap_amenity (name, description) VALUES (N'Diều hòa nhiệt độ', N'Hệ thống điều hòa cá nhân, điều chỉnh nhiệt độ theo sở thích');
INSERT INTO bkap_amenity (name, description) VALUES (N'Ấm đun nước điện', N'Có sẵn ấm điện và trà, cà phê phục vụ trong phòng');
INSERT INTO bkap_amenity (name, description) VALUES (N'Két an toàn', N'Két để đồ cá nhân và tài sản quý, mã số cá nhân hóa');
INSERT INTO bkap_amenity (name, description) VALUES (N'Bàn làm việc', N'Bàn làm việc với đèn LED, ổ cắm điện và kết nối internet');
INSERT INTO bkap_amenity (name, description) VALUES (N'Bộ khăn tắm & áo choàng', N'Bao gồm khăn tắm, áo choàng và dép đi trong phòng');
INSERT INTO bkap_amenity (name, description) VALUES (N'Dịch vụ phòng 24/7', N'Đặt đồ ăn, đồ uống và nhu cầu khác trực tiếp đến phòng bất cứ lúc nào');
INSERT INTO bkap_amenity (name, description) VALUES (N'Điện thoại trong phòng', N'Điện thoại nội bộ và quốc tế, kết nối trực tiếp với lễ tân');
INSERT INTO bkap_amenity (name, description) VALUES (N'Wi-Fi trong phòng', N'Internet tốc độ cao, mật khẩu riêng cho từng phòng');

-- 1️⃣ Booking 1 - Đã trả tiền (Phòng Trăng Mật)
INSERT INTO bkap_booking (account_id, total_amount, is_cancel, payment_status, created_at, update_at)
VALUES ('dqbao', 800, 0, 'PAID', TO_DATE('2025-06-15', 'YYYY-MM-DD'), TO_DATE('2025-06-17', 'YYYY-MM-DD'));

-- 2️⃣ Booking 2 - Đã trả tiền (Phòng Gia Đình)
INSERT INTO bkap_booking (account_id, total_amount, is_cancel, payment_status, created_at, update_at)
VALUES ('dqbao', 1000, 0, 'PAID', TO_DATE('2025-07-20', 'YYYY-MM-DD'), TO_DATE('2025-07-22', 'YYYY-MM-DD'));

-- 3️⃣ Booking 3 - Đã trả tiền (Phòng Cao Cấp)
INSERT INTO bkap_booking (account_id, total_amount, is_cancel, payment_status, created_at, update_at)
VALUES ('dqbao', 400, 0, 'PAID', TO_DATE('2025-08-01', 'YYYY-MM-DD'), TO_DATE('2025-08-03', 'YYYY-MM-DD'));

-- 4️⃣ Booking 4 - Chưa thanh toán (Phòng Tiêu chuẩn)
INSERT INTO bkap_booking (account_id, total_amount, is_cancel, payment_status, created_at, update_at)
VALUES ('dqbao', 350, 0, 'UNPAID', TO_DATE('2025-10-28', 'YYYY-MM-DD'), TO_DATE('2025-10-28', 'YYYY-MM-DD'));
 
 -- Booking 1: Phòng Trăng Mật
INSERT INTO bkap_booking_room (booking_id, room_id, check_in_date, check_out_date, price)
VALUES (1, 2, TO_DATE('2025-06-18', 'YYYY-MM-DD'), TO_DATE('2025-06-20', 'YYYY-MM-DD'), 400);

-- Booking 2: Phòng Gia Đình
INSERT INTO bkap_booking_room (booking_id, room_id, check_in_date, check_out_date, price)
VALUES (2, 4, TO_DATE('2025-07-25', 'YYYY-MM-DD'), TO_DATE('2025-07-27', 'YYYY-MM-DD'), 500);

-- Booking 3: Phòng Cao Cấp
INSERT INTO bkap_booking_room (booking_id, room_id, check_in_date, check_out_date, price)
VALUES (3, 3, TO_DATE('2025-08-05', 'YYYY-MM-DD'), TO_DATE('2025-08-06', 'YYYY-MM-DD'), 400);

-- Booking 4: Phòng Tiêu chuẩn (chưa trả)
INSERT INTO bkap_booking_room (booking_id, room_id, check_in_date, check_out_date, price)
VALUES (4, 1, TO_DATE('2025-11-05', 'YYYY-MM-DD'), TO_DATE('2025-11-06', 'YYYY-MM-DD'), 350);

-- Thanh toán 1: Thành công
INSERT INTO bkap_payment (booking_id, payment_method, vnp_txn_ref, vnp_amount, vnp_bank_code, 
                          vnp_order_info, vnp_transaction_no, vnp_response_code, 
                          vnp_transaction_status, payment_status, create_at)
VALUES (1, 'VNPAY', 'TXN001', 800, 'VCB', 'Thanh toán phòng Trăng Mật', 
        'TRANS001', '00', '00', 'SUCCESS', TO_DATE('2025-06-17', 'YYYY-MM-DD'));

-- Thanh toán 2: Thành công
INSERT INTO bkap_payment (booking_id, payment_method, vnp_txn_ref, vnp_amount, vnp_bank_code, 
                          vnp_order_info, vnp_transaction_no, vnp_response_code, 
                          vnp_transaction_status, payment_status, create_at)
VALUES (2, 'VNPAY', 'TXN002', 1000, 'ACB', 'Thanh toán phòng Gia Đình', 
        'TRANS002', '00', '00', 'SUCCESS', TO_DATE('2025-07-22', 'YYYY-MM-DD'));

-- Thanh toán 3: Thành công
INSERT INTO bkap_payment (booking_id, payment_method, vnp_txn_ref, vnp_amount, vnp_bank_code, 
                          vnp_order_info, vnp_transaction_no, vnp_response_code, 
                          vnp_transaction_status, payment_status, create_at)
VALUES (3, 'VNPAY', 'TXN003', 400, 'TCB', 'Thanh toán phòng Cao Cấp', 
        'TRANS003', '00', '00', 'SUCCESS', TO_DATE('2025-08-03', 'YYYY-MM-DD'));

-- Thanh toán 4: Chưa thanh toán
INSERT INTO bkap_payment (booking_id, payment_method, vnp_txn_ref, vnp_amount, vnp_bank_code, 
                          vnp_order_info, vnp_transaction_no, vnp_response_code, 
                          vnp_transaction_status, payment_status, create_at)
VALUES (4, 'VNPAY', 'TXN004', 350, 'VCB', 'Chưa thanh toán phòng Tiêu chuẩn', 
        NULL, '99', '99', 'PENDING', TO_DATE('2025-10-28', 'YYYY-MM-DD'));

INSERT INTO bkap_refund (payment_id, refund_amount, refund_reason, refund_status, refund_date)
VALUES (3, 100, N'Khách trả phòng sớm 1 ngày', 'SUCCESS', TO_DATE('2025-08-07', 'YYYY-MM-DD'));

DROP TABLE news;


CREATE TABLE BKAP_NEWS (
    NEWS_ID NUMBER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    TITLE NVARCHAR2(255) NOT NULL,
    CONTENT NVARCHAR2(2000),
    IMAGE NVARCHAR2(255),
    CREATED_AT DATE DEFAULT SYSDATE,
    ACCOUNT_ID VARCHAR2(50),  -- tham chiếu đến BKAP_ACCOUNT
    CONSTRAINT FK_NEWS_ACCOUNT FOREIGN KEY (ACCOUNT_ID) REFERENCES BKAP_ACCOUNT(ACCOUNT_ID)
);
-- Admin đăng tin
INSERT INTO BKAP_NEWS (TITLE, CONTENT, IMAGE, ACCOUNT_ID, CREATED_AT) VALUES (
    N'Giảm giá 30% cho khách đặt phòng cuối tuần',
    N'Chương trình ưu đãi giảm giá 30% cho khách đặt phòng từ thứ 6 đến chủ nhật, áp dụng cho mọi loại phòng.',
    'images/sale_weekend.jpg',
    'thoant',
    SYSDATE - 1
);

-- Nhân viên đăng tin
INSERT INTO BKAP_NEWS (TITLE, CONTENT, IMAGE, ACCOUNT_ID, CREATED_AT) VALUES (
    N'Lễ hội ẩm thực Á – Âu tháng 11',
    N'Thưởng thức hơn 50 món ăn Á – Âu tại nhà hàng Golden Lotus từ ngày 5/11 đến 15/11.',
    'images/food_festival.jpg',
    'staff1',
    SYSDATE - 5
);

-- Admin khác đăng tin
INSERT INTO BKAP_NEWS (TITLE, CONTENT, IMAGE, ACCOUNT_ID, CREATED_AT) VALUES (
    N'Thông báo bảo trì hệ thống điện ngày 2/11',
    N'Hệ thống điện sẽ được bảo trì trong khung giờ 10h – 12h trưa ngày 2/11. Xin lỗi vì sự bất tiện.',
    'images/maintenance.jpg',
    'dqbao',
    SYSDATE - 3
);

-- User đăng tin (đánh giá / chia sẻ)
INSERT INTO BKAP_NEWS (TITLE, CONTENT, IMAGE, ACCOUNT_ID, CREATED_AT) VALUES (
    N'Trải nghiệm tuyệt vời tại khách sạn',
    N'Tôi và gia đình đã có kỳ nghỉ rất thoải mái, phòng ốc sạch sẽ, nhân viên thân thiện. Cảm ơn khách sạn!',
    'images/review_guest.jpg',
    'user1',
    SYSDATE - 2
);

-- Một tin mới
INSERT INTO BKAP_NEWS (TITLE, CONTENT, IMAGE, ACCOUNT_ID, CREATED_AT) VALUES (
    N'Khuyến mãi cho khách hàng thân thiết tháng 11',
    N'Giảm thêm 10% cho khách hàng hạng Vàng và Kim Cương trong chương trình Loyalty.',
    'images/loyalty.jpg',
    'thoant',
    SYSDATE - 4
);