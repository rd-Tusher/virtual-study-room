<p align="center">
  <img src="icon/icon.png" width="30" style="vertical-align: middle;" />
  <span style="font-size:2em; vertical-align: middle; font-weight: bold;">Virtual Study Room</span>
</p>

#

## 🏷️ Technologies Used

![GitHub repo size](https://img.shields.io/github/repo-size/rd-Tusher/virtual-study-room?style=flat)
![GitHub top language](https://img.shields.io/github/languages/top/rd-Tusher/virtual-study-room?style=flat)
![Swing](https://img.shields.io/badge/Frontend-Swing-blue?style=flat)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.6-blue)
![MongoDB](https://img.shields.io/badge/MongoDB-8.0.16-blue)

#
A desktop-based **Virtual Study Room** application designed to manage and participate in timed study sessions with a clean UI, session tracking, and backend integration.

This project is being developed as an academic initiative by **Tusher Debnath (rdTusher)** and **Fardin Hasan Adnan**, student  of **Mawlana Bhasani Science and Technology University**, under the supervision of **Professor Dr. Mahbuba Begum**. 

## 🚀 Features

* ⏱️ Session-based timer (days / hours / minutes / seconds)
* 🎨 Desktop UI (Java-based)
* 🔄 Real-time session state handling (active / ended)
* 🗄️ Backend integration using **Spring Boot**
* 📦 Database support (MongoDB )
* 🧩 Modular and clean architecture
* 🔐 Designed with scalability in mind

#

## 🛠️ Tech Stack

### Frontend

* Java - *Swing*

### Backend

* Java - *Spring Boot*
* REST APIs

### Database

* MongoDB 

### Tools & Environment

* Linux - *Kali*
* Maven
* Git & GitHub

#

## 📂 Project Structure

```
Virtual-Study-Room/
│── frontend/
│   ├── assets/
│   ├── bin/
│   ├── pom.xml/
│   ├── src/
│
│── backend/src/main/java/com/virtualstudyroom/backend
│   ├── BackendApplication.java
│   ├── Controller/
│   ├── Service/
│   ├── Model/
│   └── DTO/
│
│── LICENSE
│── README.md
```

##

## ⚙️ Setup & Installation

### Prerequisites

* Java 17+
* Maven
* MongoDB (running locally)
* Git

### Clone the Repository

```bash
git clone https://github.com/rd-Tusher/virtual-study-room.git
```

### Frontend Setup

```bash
cd ~/virtual-study-room/frontend
mvn clean install
mvn exec:java
```
### Backend Setup

```bash
cd ~/virtual-study-room/backend
mvn clean install
mvn spring-boot:run
```

##

## 🧪 Database Configuration

### MongoDB (default)

```properties
# application.properties
spring.data.mongodb.uri=mongodb://localhost:27017/virtualstudyroom
```

##

## 📸 Screenshots

> *will be added later...*

##

## 🧠 Design Concepts Used

* MVC Architecture
* DTO Pattern
* Observer Pattern (UI updates)
* Strategy Pattern (extensible components)
* Clean Code Principles


##

## 🤝 Contributing

Contributions are welcome!

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Open a Pull Request

##

## 📜 License

This project is licensed under the **MIT License**.  
See the [LICENSE](LICENSE) file for details.

##

### 👤 Author

* Name : **Tusher Debnath**
* GitHub: [https://github.com/rd-Tusher](https://github.com/rd-Tusher)

##

## ⭐ Support

If you like this project, consider giving it a ⭐ on GitHub!
