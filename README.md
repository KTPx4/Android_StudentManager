# Android Student Manager

## Description
Android Student Manager is a mobile application developed using Android with Firebase Firestore for real-time student management. This app provides essential functionalities for managing students, accounts, and data import/export.

## Features
### User Management
- **User Login**: Authentication system for all users.
- **Profile Management**: Update user profile picture.
- **View User List**: Display all system users.
- **Add a New User**: Input name, age, phone number, and status (Normal/Locked).
- **Delete a User**: Remove user records.
- **Modify User Information**: Edit user details.
- **View Login History**: Track user login records.

### Student Management
- **View Student List**: Display all registered students.
- **Add a New Student**: Create new student entries.
- **Delete a Student**: Remove students from the system.
- **Update Student Information**: Modify student details.
- **Sort Students**: Sort based on multiple criteria.
- **Search Students**: Search students using various filters.
- **Student Details Page**: View complete student profiles.

### Certificate Management
- **View Certificate List**: Display all certificates associated with a student.
- **Add a Certificate**: Insert a new certificate.
- **Delete a Certificate**: Remove certificates.
- **Update Certificate Information**: Modify certificate details.

### Data Import/Export
- **Import Students**: Load student lists from external files.
- **Export Students**: Save student data in Excel/CSV format.
- **Import Certificates**: Load certificates from external files.
- **Export Certificates**: Save certificates in Excel/CSV format.

### Role-based Access Control
- **Admin**: Full access to all functionalities.
- **Manager**: Manage student-related data but cannot modify users.
- **Employee**: View-only access except for profile picture updates.

## Installation & Setup
### Prerequisites
- Android Studio
- Firebase Firestore Database
- Google Play Services

### Setup Steps
1. Clone the repository:
   ```sh
   git clone https://github.com/KTPx4/Android_StudentManager.git
   cd Android_StudentManager
   ```
2. Configure Firebase Firestore:
   - Create a Firebase project.
   - Add the `google-services.json` file to the `app` folder.
   - Enable Firestore Database in Firebase Console.
3. Open the project in Android Studio.
4. Build and run the application on an emulator or a real device.

## Test Account
```sh
# Admin Account:
Username: admin
Password: admin
```
