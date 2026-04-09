# ☕ QLTraSua - Phần mềm Quản Lý Trà Sữa

Phần mềm quản lý quán trà sữa được xây dựng bằng **Java Swing** và **SQL Server**, phù hợp cho đồ án sinh viên với mục tiêu **dễ hiểu, dễ chạy, dễ chỉnh sửa và đủ thực tế để demo**.
<img width="537" height="433" alt="image" src="https://github.com/user-attachments/assets/9a84e122-b052-478f-aa82-f5a622675c4b" />

<img width="1612" height="968" alt="image" src="https://github.com/user-attachments/assets/4348d639-0f19-45fe-818a-ecc772456b07" />

---

## 📌 Giới thiệu

`QLTraSua` là project quản lý bán trà sữa trên máy tính, hỗ trợ các chức năng cơ bản như:

- Quản lý món trà sữa
- Quản lý size
- Quản lý topping
- Quản lý khách hàng
- Quản lý nhân viên
- Quản lý tài khoản
- Lập hóa đơn bán hàng
- Lưu chi tiết hóa đơn
- Thống kê doanh thu

Project được tổ chức theo mô hình nhiều tầng để dễ bảo trì và mở rộng.

---

## 🛠 Công nghệ sử dụng

- **Ngôn ngữ:** Java
- **Giao diện:** Java Swing
- **Cơ sở dữ liệu:** SQL Server
- **Kết nối database:** JDBC
- **IDE khuyến nghị:** NetBeans
- **Quản lý mã nguồn:** Git, GitHub



## 📂 Cấu trúc thư mục

```text
QLTraSua/
├─ src/
│  ├─ gui/          # Giao diện người dùng
│  ├─ dto/          # Data Transfer Object
│  ├─ dao/          # Data Access Object
│  ├─ bus/          # Business Logic
│  ├─ util/         # Tiện ích, kết nối DB, xử lý icon...
│  └─ main/         # Chạy chương trình
├─ icons/           # Hình ảnh, icon giao diện
├─ database.sql/    # Script cơ sở dữ liệu
├─ nbproject/       # Cấu hình NetBeans
├─ test/            # Test
├─ build/           # File build
└─ manifest.mf





